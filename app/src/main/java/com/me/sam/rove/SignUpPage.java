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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {

    private Button b1,b2;
    private EditText e1,e2,e3,e4;
    private String uid;
    private final static  int RC_SIGN_IN = 3;

    private ProgressDialog mRegProgress;

    private GoogleApiClient mGoogleApiClient;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    //private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mAuth = FirebaseAuth.getInstance();


        b1 = (Button) findViewById(R.id.buttonRegisterAM);
        b2 = (Button) findViewById(R.id.buttonGoogle);
        e1 = (EditText) findViewById(R.id.editTextUserName);
        //e2 = (EditText) findViewById(R.id.editTextlastName);
        e3 = (EditText) findViewById(R.id.editTextEmailId);
        e4 = (EditText) findViewById(R.id.editTextPassword);
        mRegProgress = new ProgressDialog(SignUpPage.this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(SignUpPage.this,"Something is wrong", Toast.LENGTH_LONG).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //1: Create child in root object.
                //2: Assign value to Child object.

                /*HashMap<String,String> datamap = new HashMap<String, String>();
                datamap.put("First Name",e1.getText().toString().trim());
                //datamap.put("Last Name",e2.getText().toString().trim());
                datamap.put("Email ID",e3.getText().toString().trim());
                datamap.put("Password",e4.getText().toString().trim());

                mDatabase.push().setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpPage.this,"worked",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(SignUpPage.this,"worked",Toast.LENGTH_LONG).show();
                        }

                    }
                });*/


                /*mDatabase.child("First Name").setValue(e1.getText().toString().trim());
                //mDatabase.child("Last Name").setValue(e2.getText().toString().trim());
                mDatabase.child("Email ID").setValue(e3.getText().toString().trim());
                mDatabase.child("Password").setValue(e4.getText().toString().trim());*/



                // 3 : Retreving DATA

                /*mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        e1.setText(dataSnapshot.getValue().toString());


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

                String userName = e1.getText().toString().trim();
                //String lastName = e2.getText().toString().trim();
                String email = e3.getText().toString().trim();
                String password = e4.getText().toString().trim();

                if(!TextUtils.isEmpty(userName)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)){

                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait till your account is created");

                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    register_user(userName,email,password);

                }


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    //email and password

    private void register_user(final String userName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            mRegProgress.dismiss();

                            FirebaseUser user = mAuth.getCurrentUser();
                            uid = user.getUid();

                           mDatabase.child("Users").child(uid).child("UserName").setValue(userName);

                            Toast.makeText(SignUpPage.this,"user is registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpPage.this,MainActivity.class));
                            finish();
                        }else{

                            mRegProgress.hide();
                            Toast.makeText(SignUpPage.this,"error in registeration", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    //google

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
                Toast.makeText(SignUpPage.this,"Auth went wrong", Toast.LENGTH_LONG).show();
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
                    uid = user.getUid();
                    String userName;
                    String emailRetreive = user.getEmail();
                    userName = usernameExractor(emailRetreive);
                    mDatabase.child("Users").child(uid).child("UserName").setValue(userName);
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    Toast.makeText(SignUpPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }

                // ...
            }
        });
    }

    private String usernameExractor(String emailRetreive) {

        String ch="";
        int i;
        for (i=0;i<emailRetreive.length();i++){
            if(emailRetreive.charAt(i)!='@'){
                ch = ch + emailRetreive.charAt(i);

            }else
                break;
        }
        return  ch;
    }
}
