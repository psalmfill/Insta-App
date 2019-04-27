package com.samfieldhawb.instaapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.samfieldhawb.instaapp.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText mNameField,mEmailField,mPasswordField;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mNameField = findViewById(R.id.name_field);
        mEmailField = findViewById(R.id.email_field);
        mPasswordField = findViewById(R.id.password_field);
        mButton = findViewById(R.id.register_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClicked(v);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void registerButtonClicked(View view){
        final String name = mNameField.getText().toString();
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        if(name.isEmpty()){
            mNameField.setError("Name cannot be empty");
            return;
        }
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailField.setError("Invalid Email");
            return;
        }
        if(password.isEmpty()){
            mEmailField.setError("Password cannot be empty");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDb = mReference.push().child(userId);
                    currentUserDb.child("Name").setValue(name);
                    currentUserDb.child("Image").setValue("default");
                    Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
            }
        });

    }
}
