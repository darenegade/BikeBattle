package edu.hm.cs.bikebattle.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.Scope;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "SignInBikeBattle";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initCompoents();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in .....");
    }

    private void initCompoents(){
      GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton login = (SignInButton) findViewById(R.id.sign_in_button);
        login.setScopes(gso.getScopeArray());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progressDialog.show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        String userName;
        String userMail;
        String userToken;
        if (result.isSuccess()) {
            Log.d(TAG, "Login successful!!!");
            GoogleSignInAccount acct = result.getSignInAccount();
            userName = acct.getDisplayName();
            userMail = acct.getEmail();
            userToken = acct.getIdToken();
            
            Toast toast = Toast.makeText(getApplicationContext(), "Erfolgreich angemeldet", Toast.LENGTH_LONG);
            toast.show();
        } else {
            Log.d(TAG, "Fehler beim Login: " + result.getStatus().toString());
        }
    }
}
