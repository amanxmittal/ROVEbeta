package com.me.sam.rove;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogInPage extends AppCompatActivity {

    private EditText et1,et2;
    private Button b1,b2;
    Button b3;
    private ProgressDialog mLoginProgress;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private final static  int RC_SIGN_IN = 2;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        et1 = (EditText) findViewById(R.id.editTextEmaildAM);
        et2 = (EditText) findViewById(R.id.editTextPasswordAM);
        b1 = (Button) findViewById(R.id.buttonForgotPasswordAM);
        b2 = (Button) findViewById(R.id.buttonSignInAM);
        b3 = (Button) findViewById(R.id.buttonGoogleSignInAM);

        mAuth = FirebaseAuth.getInstance();

        mLoginProgress = new ProgressDialog(this);



        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LogInPage.this,"Something is wrong", Toast.LENGTH_LONG).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et1.getText().toString().trim();
                String password = et2.getText().toString().trim();
                if(!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){
                    mLoginProgress.setTitle("Logging in");
                    mLoginProgress.setMessage("Please wait till we check your credentials...");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(email,password);

                }else{
                    Toast.makeText(LogInPage.this,"All fields are to filled", Toast.LENGTH_LONG).show();
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed out
                    Toast.makeText(LogInPage.this,"main to sign in", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LogInPage.this,MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };









    }
    ///google login
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(LogInPage.this,"Auth went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            } else {
                             // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LogInPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                             }

                            // ...
                     }
        });
    }


    /// email and password login

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    mLoginProgress.dismiss();

                    Intent i = new Intent(LogInPage.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    mLoginProgress.hide();
                    Toast.makeText(LogInPage.this,"error", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    public void signUpIntend (View v)
    {
        Intent i = new Intent(LogInPage.this,SignUpPage.class);
        startActivity(i);

    }
    public void skip (View v)
    {
        Intent i = new Intent(LogInPage.this,MainActivity.class);
        startActivity(i);

    }

}
