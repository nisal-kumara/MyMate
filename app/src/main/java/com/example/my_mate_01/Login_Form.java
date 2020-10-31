package com.example.my_mate_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Form extends AppCompatActivity {

    EditText txtEmail1, txtpass1;
    Button btnLogin;
    TextView txtVsignup, txtvforgetP;

    ProgressDialog proD;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__form);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        //In the onCreate() method, initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        txtEmail1 = findViewById(R.id.txtEmail1);
        txtpass1 = findViewById(R.id.txtPass1);
        btnLogin = findViewById(R.id.btnLogin);
        txtVsignup = findViewById(R.id.txtVSignup);
        txtvforgetP = findViewById(R.id.txtvForgetP);



        //Login button event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inserting data
                String email1 = txtEmail1.getText().toString();
                String pass1 = txtpass1.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
                    //invalid email format validation
                    txtEmail1.setError("Invalid Email");
                    txtpass1.setFocusable(true);
                }
                else {
                    loginUser(email1, pass1);
                }

            }
        });
        //Don't have an account textview event
        txtVsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Form.this,Registration_Form.class));
                finish();
            }
        });

        //forget password textview event
        txtvforgetP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });

        //progress dialog
        proD = new ProgressDialog(this, R.style.DialogColor);
    }

    private void forgetPassword() {
        //Alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        //set linear layout
        LinearLayout linearL = new LinearLayout(this);

        final EditText rmail = new EditText(this);
        rmail.setHint("Type your Email here!");
        rmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        //setting min width of editview
        rmail.setMinEms(16);

        linearL.addView(rmail);
        linearL.setPadding(10,10,10,10);

        builder.setView(linearL);

        //recover button
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            String email = rmail.getText().toString().trim();
            startRecovery(email);
            }
        });
        //cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //show dialog
        builder.create().show();
    }

    private void startRecovery(String email) {
        //show progress dialog
        proD.setMessage("Sending email... ");
        proD.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                proD.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(Login_Form.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Login_Form.this, "Failed to send mail", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                proD.dismiss();
                Toast.makeText(Login_Form.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loginUser(String email1, String pass1) {
        //show progress dialog
        proD.setMessage("Logging In... ");
        proD.show();
        mAuth.signInWithEmailAndPassword(email1, pass1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            proD.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            // user will redirect to the home
                            startActivity(new Intent(Login_Form.this, Dashbord.class));
                            finish();
                        } else {
                            proD.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login_Form.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                proD.dismiss();
                //show error message
                Toast.makeText(Login_Form.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
