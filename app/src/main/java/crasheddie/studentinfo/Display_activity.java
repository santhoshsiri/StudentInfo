package crasheddie.studentinfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;


public class Display_activity extends AppCompatActivity {
    String usn;
    TextView nameTv;
    TextView branchTv;
    TextView semTv;
    TextView addressTv;
    TextView parentTv;
    TextView studentTv;
    TextView usnTv;
    TextView yearTv;
    TextView mailTv;
    TextView statusTv;
    TextView firstTv;
    TextView secondTv;
    TextView thirdTv;
    TextView fourthTv;
    TextView fifthTv;
    TextView sixthTv;
    TextView seventhTv;
    TextView eighthTv;
    TextView backsTv;
    TextView skillsTv;

    String[] j;
    ImageView img;
    Bitmap bitmap;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            j =(String[]) b.get("details");
        }
        usnTv=(TextView)findViewById(R.id.usn);
        nameTv=(TextView)findViewById(R.id.name);
        branchTv=(TextView)findViewById(R.id.branch);
        semTv=(TextView)findViewById(R.id.sem);
        addressTv=(TextView)findViewById(R.id.address);
        parentTv=(TextView)findViewById(R.id.parent_no);
        studentTv=(TextView)findViewById(R.id.student_no);
        mailTv=(TextView)findViewById(R.id.mail);
        yearTv=(TextView)findViewById(R.id.year);
        statusTv=(TextView)findViewById(R.id.status);
        firstTv=(TextView)findViewById(R.id.first);
        secondTv=(TextView)findViewById(R.id.second);
        thirdTv=(TextView)findViewById(R.id.third);
        fourthTv=(TextView)findViewById(R.id.fourth);
        fifthTv=(TextView)findViewById(R.id.fifth);
        sixthTv=(TextView)findViewById(R.id.sixth);
        seventhTv=(TextView)findViewById(R.id.seventh);
        eighthTv=(TextView)findViewById(R.id.eighth);
        backsTv=(TextView)findViewById(R.id.backs);
        skillsTv=(TextView)findViewById(R.id.skills);
        img = (ImageView)findViewById(R.id.imageView2);

        new LoadImage().execute("http://studentinfo.ml/images/"+j[9]);
        usnTv.setText(j[19]);
        nameTv.setText(j[0]);
        branchTv.setText(j[1]);
        semTv.setText(j[2]);
        addressTv.setText(j[3]);
        parentTv.setText(j[5]);
        studentTv.setText(j[4]);
        yearTv.setText(j[6]);
        statusTv.setText(j[7]);
        mailTv.setText(j[8]);
        firstTv.setText(j[10]);
        secondTv.setText(j[11]);
        thirdTv.setText(j[12]);
        fourthTv.setText(j[13]);
        fifthTv.setText(j[14]);
        sixthTv.setText(j[15]);
        seventhTv.setText(j[16]);
        eighthTv.setText(j[17]);
        backsTv.setText(j[18]);
        skillsTv.setText("java");


    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            img.setImageResource(R.drawable.loding);

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                img.setImageBitmap(image);


            }else{
                img.setImageResource(R.drawable.back_end);
                Toast.makeText(Display_activity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parentmsg(View view)
    {
        String call =j[5];
        Calendar c = Calendar.getInstance();
        String sDate = c.get(Calendar.DAY_OF_MONTH) + "-"
                + c.get(Calendar.MONTH)
                + "-" + c.get(Calendar.YEAR)
                + " at " + c.get(Calendar.HOUR_OF_DAY)
                + ":" + c.get(Calendar.MINUTE);
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address",call);
        smsIntent.putExtra("sms_body","The student "+j[0]+" is not attended the class on date "+sDate+" This is your kind information");
        startActivity(smsIntent);

    }

    public void parentcall(View view)
    {
        String call =  j[5];
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+Uri.encode(call.trim())));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);

    }

}
