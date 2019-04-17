package com.example.nitin.testapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;

    private TextView textViewSignin;
    private Button buttonRegister;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       if(firebaseAuth.getInstance().getCurrentUser() != null) {
            // user is already logged in
            //profile activity here
            finish();
            startActivity(new Intent(this, NavigationActivity.class));
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                (WindowManager.LayoutParams.FLAG_FULLSCREEN));

        setContentView(R.layout.activity_main);
        //   startActivity(new Intent(this, MapsActivity.class));
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }


    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // onClick of button perform this simplest code.
      /*  if (email.matches(emailPattern))
        {
            Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
        }
        */
        if(!email.matches(emailPattern))
        {
            editTextEmail.setError("Please enter a valid email address!");
        }

        if (password.isEmpty() || password.length() < 8) {
            editTextPassword.setError("Password cannot be less than 8 characters!");
        }
        else {
            editTextPassword.setError(null);
            //    startActivity(new Intent(RegistrationActivity.this,MainActivity.class));

            // if all the valodations are ok
            // we will first show a progress bar
            progressDialog.setMessage("Registering User.....");
            progressDialog.show();

            firebaseAuth.fetchProvidersForEmail(email)
                    .addOnCompleteListener(this, new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            boolean ischeck = !task.getResult().getProviders().isEmpty();

                            if(ischeck){
                                Toast.makeText(MainActivity.this, "Email already exist.", Toast.LENGTH_SHORT).show();
                            }
                            else{

                            }
                        }
                    });

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // user is successfully registered and logged in
                            // we will start the profile activity here
                            // right now lets display a toast only
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), NavigationActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this, "Could not registered successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        }
                    });
        }
    }


    @Override
    public void onClick(View view) {
        if(view == buttonRegister ){
            registerUser();
        }

        else if(view == textViewSignin){
            // will open login activity here
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
