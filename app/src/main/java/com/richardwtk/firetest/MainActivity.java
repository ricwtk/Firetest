package com.richardwtk.firetest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                updateUI(firebaseAuth.getCurrentUser());
            }
        });

        final Button log_in_button = findViewById(R.id.loginbtn);
        log_in_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getResources().getResourceEntryName(v.getId()));
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
            }
        });

        findViewById(R.id.signupbtn).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                signUp();
            }
        });

        findViewById(R.id.loginbtn).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                logIn();
            }
        });

        findViewById(R.id.log_out_button).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                logOut();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        final TextView displaytext = findViewById(R.id.textdisplay);
        if (user != null) displaytext.setText(user.getEmail());
        else displaytext.setText(R.string.defaulttextdisplay);
    }

    private void signUp() {
        final String emailtext = ((EditText) findViewById(R.id.emailtext)).getText().toString();
        final String passwordtext = ((EditText) findViewById(R.id.passwdtext)).getText().toString();

        if (emailtext.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.no_email_text, Toast.LENGTH_SHORT).show();
        } else if (passwordtext.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.no_password_text, Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(emailtext, passwordtext)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(MainActivity.this.getLocalClassName(), "createUserWithEmail:success");
                        //                        FirebaseUser user = mAuth.getCurrentUser();
                        //                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(MainActivity.this.getLocalClassName(), "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        //                        updateUI(null);
                    }

                    // ...
                    }
                });
        }
    }

    private void logIn() {
        final String emailtext = ((EditText) findViewById(R.id.emailtext)).getText().toString();
        final String passwordtext = ((EditText) findViewById(R.id.passwdtext)).getText().toString();

        if (emailtext.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.no_email_text, Toast.LENGTH_SHORT).show();
        } else if (passwordtext.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.no_password_text, Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(emailtext, passwordtext)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(MainActivity.this.getLocalClassName(), "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(MainActivity.this.getLocalClassName(), "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
        }
    }

    private void logOut() {
        mAuth.signOut();
    }
}
