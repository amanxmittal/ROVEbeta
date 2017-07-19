package com.me.sam.rove;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_experience:
               {
                    Experience first = new Experience();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, first).commit();
                }
                    return true;
                case R.id.navigation_explore:
                {
                    Explore second = new Explore();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, second).commit();
                }
                    return true;
                case R.id.navigation_utilities:
                {
                    Utilities third = new Utilities();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, third).commit();
                }
                    return true;
                case R.id.navigation_profile:
                {
                    Profile fourth = new Profile();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fourth).commit();
                }
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAuth = FirebaseAuth.getInstance();
        //b= (ImageButton) findViewById(R.id.logOut);
       /* b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"logOUT",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();

            }
        });*/


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Toast.makeText(MainActivity.this,"main to sign in",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this,LogInPage.class);
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

    @Override
    public void onStart() {

        mAuth.addAuthStateListener(mAuthListener);
        super.onStart();
    }

    @Override
    protected void onStop() {

        mAuth.removeAuthStateListener(mAuthListener);
        super.onStop();
    }
}
