package com.abdul.parkhapa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdul.parkhapa.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class SignupActivity extends AppCompatActivity {
    EditText editName , editEmail, editPassword , editPassword2;
    ProgressDialog progressDialog;
    TextView txtHaveAnAccount;
    Button btn_signup;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ArrayList<String> mtitle;


    String userName , userEmail;

    TextView str;
    static int i = 1;
    ProgressBar pb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initializeViews();
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(getApplicationContext() ,LoginActivity.class));
            }
        });

        mtitle = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.password)));
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (editPassword.getText().toString().length() == 0) {
                    editPassword.setError("Enter your password..!");
                } else {
                    checkPasswordStrength();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                userName = editName.getText().toString().trim();
                userEmail = editEmail.getText().toString().trim();
                String password = editPassword.getText().toString();
                String password2 = editPassword2.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    progressDialog.dismiss();
                    editName.setError("Enter Name !");
                    return;
                }
                if (TextUtils.isEmpty(userEmail )) {
                    progressDialog.dismiss();
                    editEmail.setError("Invalid email address ! ");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    progressDialog.dismiss();
                    editPassword.setError("Enter password ");
                    return;
                }
                if (!password2.equals(password2) ) {
                    progressDialog.dismiss();
                    editPassword2.setError("Password mismatch");
                    return;
                }
                createAccount(userEmail, password);
            }
        });
    }

    private void initializeViews() {
        editName = (EditText)findViewById(R.id.input_name);
        editEmail = (EditText)findViewById(R.id.input_email);
        editPassword = (EditText)findViewById(R.id.input_password);
        editPassword2 = (EditText)findViewById(R.id.input_password2);
        txtHaveAnAccount = (TextView)findViewById(R.id.link_login);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        pb = (ProgressBar)findViewById(R.id.progressBar);
    }

    private  void createAccount (final String email , String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                         String   userPassword= mtitle.get(2);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity( new Intent( getApplicationContext() , MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }




    void  checkPasswordStrength (){
        String temp = editPassword.getText().toString();
        System.out.println(i + " current password is : " + temp);
        i = i + 1;

        int length = 0, uppercase = 0, lowercase = 0, digits = 0, symbols = 0, bonus = 0, requirements = 0;

        int lettersonly = 0, numbersonly = 0, cuc = 0, clc = 0;

        length = temp.length();
        for (int i = 0; i < temp.length(); i++) {
            if (Character.isUpperCase(temp.charAt(i)))
                uppercase++;
            else if (Character.isLowerCase(temp.charAt(i)))
                lowercase++;
            else if (Character.isDigit(temp.charAt(i)))
                digits++;

            symbols = length - uppercase - lowercase - digits;

        }

        for (int j = 1; j < temp.length() - 1; j++) {

            if (Character.isDigit(temp.charAt(j)))
                bonus++;

        }

        for (int k = 0; k < temp.length(); k++) {

            if (Character.isUpperCase(temp.charAt(k))) {
                k++;

                if (k < temp.length()) {

                    if (Character.isUpperCase(temp.charAt(k))) {

                        cuc++;
                        k--;

                    }

                }

            }

        }

        for (int l = 0; l < temp.length(); l++) {

            if (Character.isLowerCase(temp.charAt(l))) {
                l++;

                if (l < temp.length()) {

                    if (Character.isLowerCase(temp.charAt(l))) {

                        clc++;
                        l--;

                    }

                }

            }

        }

        System.out.println("length" + length);
        System.out.println("uppercase" + uppercase);
        System.out.println("lowercase" + lowercase);
        System.out.println("digits" + digits);
        System.out.println("symbols" + symbols);
        System.out.println("bonus" + bonus);
        System.out.println("cuc" + cuc);
        System.out.println("clc" + clc);

        if (length > 7) {
            requirements++;
        }

        if (uppercase > 0) {
            requirements++;
        }

        if (lowercase > 0) {
            requirements++;
        }

        if (digits > 0) {
            requirements++;
        }

        if (symbols > 0) {
            requirements++;
        }

        if (bonus > 0) {
            requirements++;
        }

        if (digits == 0 && symbols == 0) {
            lettersonly = 1;
        }

        if (lowercase == 0 && uppercase == 0 && symbols == 0) {
            numbersonly = 1;
        }

        int Total = (length * 4) + ((length - uppercase) * 2)
                + ((length - lowercase) * 2) + (digits * 4) + (symbols * 6)
                + (bonus * 2) + (requirements * 2) - (lettersonly * length*2)
                - (numbersonly * length*3) - (cuc * 2) - (clc * 2);

        System.out.println("Total" + Total);

        if(Total<30){
            pb.setProgress(Total-15);
        }

        else if (Total>=40 && Total <50)
        {
            pb.setProgress(Total-20);
        }

        else if (Total>=56 && Total <70)
        {
            pb.setProgress(Total-25);
        }

        else if (Total>=76)
        {
            pb.setProgress(Total-30);
        }
        else{
            pb.setProgress(Total-20);
        }

    }

}

