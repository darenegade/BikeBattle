package edu.hm.cs.bikebattle.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import edu.hm.cs.bikebattle.app.R;

/**
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.activities
 * Author(s): Rene Zarwel
 * Date: 09.05.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = "BaseActivity";

  public static final int RC_SIGN_IN = 9001;

  private GoogleApiClient googleApiClient;
  private GoogleSignInOptions googleSignInOptions;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(getString(R.string.server_client_id))
        .build();

    googleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this, this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
        .build();
  }

  /**
   *Lässt den User seinen gewünschten Google Account auswählen.
   */
  public void reconnect() {

    Auth.GoogleSignInApi.silentSignIn(getGoogleApiClient()).setResultCallback(new ResultCallback<GoogleSignInResult>() {
      @Override
      public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
        GoogleSignInAccount acct = googleSignInResult.getSignInAccount();

        if(acct == null){
          Intent intent = new Intent(getApplicationContext(), Login.class);
          startActivity(intent);
        }else {
          Log.d(TAG, "Login successful!!!....again");
          Log.d(TAG, "Name:" + acct.getDisplayName());
          Log.d(TAG, "Mail:" + acct.getEmail());
          Log.d(TAG, "Token:" + acct.getIdToken());
        }
      }
    });
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

  public GoogleApiClient getGoogleApiClient() {
    return googleApiClient;
  }

  public GoogleSignInOptions getGoogleSignInOptions() {
    return googleSignInOptions;
  }
}
