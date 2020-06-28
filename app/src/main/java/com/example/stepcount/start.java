package com.example.stepcount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

public class start extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SignInButton sign = findViewById(R.id.sign_in_button);
        //Button con=(Button)findViewById(R.id.con);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        final GoogleSignInClient mGoogleSignInClient;
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivity(signInIntent);
                detail();
            }
        });


    }
    @Override
    public void onStart(){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }
    public void detail(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            SharedPreferences sharedPreferences = getSharedPreferences("mydata",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("name",personName);
            editor.putString("email",personEmail);
            editor.putString("id",personId);

            Intent i=new Intent(start.this,MainActivity.class);
            i.putExtra("name",personName);
            i.putExtra("namegiven",personGivenName);
            i.putExtra("namefamily",personFamilyName);
            i.putExtra("email",personEmail);
            i.putExtra("id",personId);
            startActivity(i);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account!=null)
        {
            detail();
            //Intent i=new Intent(start.this,MainActivity.class);
            //startActivity(i);
        }
        else{
            Toast.makeText(this, "You haven't logged in with google", Toast.LENGTH_SHORT).show();
        }
    }
}
