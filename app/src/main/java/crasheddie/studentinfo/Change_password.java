package crasheddie.studentinfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Change_password extends AppCompatActivity {
//class change_password
    static EditText curpasswordcp;
    static EditText newpasswordcp;
    static EditText confpasswordcp;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        curpasswordcp =(EditText)findViewById(R.id.currentpass);
        newpasswordcp=(EditText)findViewById(R.id.newpass);
        confpasswordcp=(EditText)findViewById(R.id.confpass);
    }
    public void changebutton(View view)
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String username = settings.getString("username","no username");
        String password = curpasswordcp.getText().toString();
        String newpass = newpasswordcp.getText().toString();
        String confpass = confpasswordcp.getText().toString();

        if(username.isEmpty()|password.isEmpty()|newpass.isEmpty()|confpass.isEmpty())
        {
            Toast.makeText(this,"User password cannot be empty",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(haveNetworkConnection())
            {
                change(username,password,newpass,confpass);
            }
            else
                Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
        }


    }
    public void change(final String username, final String password,final String newpass,final String confpass)
    {
        String type="change";
        class Backgroundworker extends AsyncTask<String,Void,String   > {
            AlertDialog alertDialog;
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                alertDialog = new AlertDialog.Builder(Change_password.this).create();
                loadingDialog = ProgressDialog.show(Change_password.this,"Please Wait","Loading...");
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                alertDialog.setTitle("Changing Password");
                alertDialog.setCanceledOnTouchOutside(false);
                loadingDialog.dismiss();
                if(result.equals(" successfull\t\t\t\t\t"))
                {
                    loadingDialog.dismiss();
                    curpasswordcp.setText(null);
                    newpasswordcp.setText(null);
                    confpasswordcp.setText(null);
                    Toast.makeText(Change_password.this, "Password Changed", Toast.LENGTH_SHORT).show();
                    finish();

                }
                else
                {
                    loadingDialog.dismiss();
                    alertDialog.setMessage("User Password Are Not Matching!!");
                    alertDialog.setCanceledOnTouchOutside(true);
                    alertDialog.show();
                }

            }
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            protected String doInBackground(String... params) {
                String user_name=params[0];
                String cur_pass_word=params[1];
                String new_pass_word=params[2];
                String conf_pass_word=params[3];
                String login_url="http://studentinfo.ml/change_password.php";
                try {

                    URL url= new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                    String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                            +URLEncoder.encode("cur_pass_word","UTF-8")+"="+URLEncoder.encode(cur_pass_word,"UTF-8")+"&"
                            +URLEncoder.encode("new_pass_word","UTF-8")+"="+URLEncoder.encode(new_pass_word,"UTF-8")+"&"
                            +URLEncoder.encode("conf_pass_word","UTF-8")+"="+URLEncoder.encode(conf_pass_word,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream= httpURLConnection.getInputStream();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1" ));
                    String result="";
                    String line="";
                    while ((line = bufferedReader.readLine())!=null)
                    {
                        result=line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


        }
        Backgroundworker LA = new Backgroundworker();
        LA.execute(username,password,newpass,confpass);

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
