package ke.co.yegon.geos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class fck extends AppCompatActivity {
    Button btnStart;
    EditText varMsg;
//    varPhoneNo
    Button btn;
    String sNum;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnStart = (Button) findViewById(R.id.idbtnStart);
        varMsg = (EditText) findViewById(R.id.idTxtMsg);
//        varPhoneNo = (EditText) findViewById(R.id.idTxtPhoneNo);
        btn = (Button) findViewById(R.id.idbtnCall);
        btn = (Button) findViewById(R.id.idbtn);
    }

    public void btnClick(View v) {
        Intent i = new Intent(Intent.ACTION_CALL);
        sNum = "0703909189";
        if(!sNum.trim().isEmpty()){
            i.setData(Uri.parse("tel:"+sNum));
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Please grant the  permission to call",Toast.LENGTH_SHORT).show();
            requestPermission();
        }
        else {
            startActivity(i);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);
    }

    //Method to Start the Service
    public void sendSms(View v) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Name of Method for Calling Message
            MyMessage();
        } else {
            //TODO
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

    }

    public void MyMessage(){
        String myNumber = "0726471755";
//        varPhoneNo.getText().toString().trim()
        String myMsg = varMsg.getText().toString().trim();
//
        //Begin Check for PhoneNumber
        if(myNumber==null || myNumber.equals("") || myMsg==null  || myMsg.equals("") ){
            Toast.makeText(this,"Field Cant be Empty",Toast.LENGTH_SHORT).show();
        }else{
            if(TextUtils.isDigitsOnly(myNumber)){
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(myNumber, null, "["+myMsg +"]"+ "Hello there. I would like to talk to you about something I discovered in your app", null, null);
                Toast.makeText(this,"Sending message...",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Please Enter Integer Only",Toast.LENGTH_SHORT).show();
            }

        }
        //End Check for PhoneNumber
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
            {
                if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Name of Method for Calling Message
                    MyMessage();
                }else{
                    Toast.makeText(this,"You dont have required permission to make the Action",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void emailClick(View v){
        String myMsg = varMsg.getText().toString().trim();
        //Begin Check for PhoneNumber
        if(myMsg==null  || myMsg.equals("") ){
            Toast.makeText(this,"Field Cant be Empty",Toast.LENGTH_SHORT).show();
        }else{
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("email"));
        String[] s={"support@yegon.co.ke"};
//       {abc@gmail.com","xyz@gmail.com}
        i.putExtra(Intent.EXTRA_EMAIL,s);
        i.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
        i.putExtra(Intent.EXTRA_TEXT,"[" + myMsg +"]"+"Hello there. I would like to talk to you about something I discovered in your app");
        i.setType("message/rfc822");
        Intent chooser = Intent.createChooser(i,"Launch Email");
        startActivity(chooser);
        }
    }

}
