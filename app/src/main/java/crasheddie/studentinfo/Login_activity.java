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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

 public class Login_activity extends AppCompatActivity {
      Button Login_button;
      EditText usernameEt;
      static EditText passwordEt;
     private static final String IS_LOGIN = "IsLoggedIn";
     public static final String PREFS_NAME = "MyPrefsFile";
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         ActionBar ab =getSupportActionBar();
         ab.setDisplayShowHomeEnabled(true);
         ab.setIcon(R.mipmap.app_logo);
         super.onCreate(savedInstanceState);
         if(!this.isLoggedIn()){
             setContentView(R.layout.activity_login);
             usernameEt =(EditText)findViewById(R.id.user_name);
             passwordEt=(EditText)findViewById(R.id.pass_word);
         }
         else {
             if(!haveNetworkConnection())
             {
                 Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
             }
             Intent i = new Intent(Login_activity.this,homedraw_Activity.class);
             startActivity(i);
             finish();
         }

     }

     public boolean isLoggedIn(){
         SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
         return settings.getBoolean(IS_LOGIN, false);
     }
     public void loginbutton(View view)
     {
        String username =  usernameEt.getText().toString();
         String password = passwordEt.getText().toString();
         if(username.isEmpty()|password.isEmpty())
         {
             Toast.makeText(this,"User name or password cannot be empty",Toast.LENGTH_SHORT).show();
         }
         else
         {
             if(haveNetworkConnection())
             {
                 Login(username,password);
             }
             else
                 Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
         }


     }

     public void Login(final String username, final String password)
     {
         String type="login";
         class Backgroundworker extends AsyncTask<String,Void,String   > {
             AlertDialog alertDialog;
             private Dialog loadingDialog;

             @Override
             protected void onPreExecute() {
                 super.onPreExecute();
                 alertDialog = new AlertDialog.Builder(Login_activity.this).create();
                 loadingDialog = ProgressDialog.show(Login_activity.this,"Please Wait","Loading...");
             }

             @Override
             protected void onPostExecute(String result) {
                 super.onPostExecute(result);
                 alertDialog.setTitle("Login Info");
                 alertDialog.setCanceledOnTouchOutside(false);
                 loadingDialog.dismiss();
                 if(result.equals("success"))
                 {
                     loadingDialog.dismiss();
                     usernameEt.setText(null);
                     passwordEt.setText(null);
                     SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                     SharedPreferences.Editor editor = settings.edit();
                     editor.putBoolean(IS_LOGIN, true);
                     editor.putString("username",username);
                     editor.apply();
                     Intent i = new Intent(Login_activity.this,homedraw_Activity.class);
                     Toast.makeText(Login_activity.this, "logged IN", Toast.LENGTH_SHORT).show();
                     startActivity(i);
                     finish();
                 }
                 else
                 {
                     loadingDialog.dismiss();
                     passwordEt.setText(null);
                     alertDialog.setMessage("User Name And Password Are Not Matching!!");
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
                 String pass_word=params[1];
                     String login_url="http://studentinfo.ml/login.php";
                     try {

                         URL url= new URL(login_url);
                         HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                         httpURLConnection.setRequestMethod("POST");
                         httpURLConnection.setDoInput(true);
                         httpURLConnection.setDoOutput(true);
                         OutputStream outputStream = httpURLConnection.getOutputStream();
                         BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                         String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                                 +URLEncoder.encode("pass_word","UTF-8")+"="+URLEncoder.encode(pass_word,"UTF-8");
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
         LA.execute(username,password);

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
     private Boolean exit = false;
     @Override
     public void onBackPressed() {
         if (exit) {
             finish(); // finish activity
         } else {
             Toast.makeText(this, "Press Back again to Exit.",
                     Toast.LENGTH_SHORT).show();
             exit = true;
         }
     }

}
