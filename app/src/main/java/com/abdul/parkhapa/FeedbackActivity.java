package com.abdul.parkhapa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.abdul.parkhapa.mail.GmailSender;


import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {
   // MaterialSpinner spinner;
    EditText message;
    Button btn_send;
    ProgressDialog progressDialog;
    String  text , body;
    final Context context = this;

    List<String> listOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_send = (Button) findViewById(R.id.btn_send);
        message = (EditText) findViewById(R.id.message);


        progressDialog = new ProgressDialog(FeedbackActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending feedback ...");
//
//        spinner = (MaterialSpinner) findViewById(R.id.spinner);
//        assert spinner != null;
//        listOptions = new ArrayList<>();
//        listOptions.add("Give us feedback");
//        listOptions.add("Report a problem");
//        spinner.setItems(listOptions);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = null;

//               switch (spinner.getSelectedIndex()) {
//                   case 0:
//                       subject=listOptions.get(0);
//                       break;
//                   case 1:
//                       subject=listOptions.get(1);
//                       break;
//               }
//
//                String feedback = message.getText().toString().trim();
//                if ( !feedback.isEmpty()) {
//                    sendEmail(feedback, subject);
//                }else {
//                    Toast.makeText(getApplicationContext(),
//                            "Feedback is null  ",Toast.LENGTH_LONG).show();
//                }
            }
        });


    }


     void  sendEmail (String feeback , final String subject){
         progressDialog.show();
             body =  feeback ;
             new Thread(new Runnable() {
                 public void run() {
                     try {
                         GmailSender sender = new GmailSender(
                                 "abduljama829@gmail.com",
                                 "witness12");
                         sender.sendMail(subject, body, "abduljama829@gmail.com",
                                 "jama.abdirahman@strathmore.edu");
                        // Toast.makeText(getApplicationContext() , "Feedback Sent ", Toast.LENGTH_LONG).show();
                         progressDialog.dismiss();
                         startActivity( new Intent( getApplicationContext() , MainActivity.class));

                     } catch (Exception e) {
                         Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                     }
                 }

             }).start();
     }


    void  onEmailSent (){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Feedback Sent ! ");
        alertDialogBuilder
                .setMessage(" Thank you for your feedback  we really do appreciate hearing from you ")
                .setCancelable(false)
                .setPositiveButton("Okay",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                       startActivity( new Intent( getApplicationContext() ,
                               MainActivity.class));
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId()){
            case android.R.id.home:
                Intent i= new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;



        }
        return super.onOptionsItemSelected(item);
    }


}
