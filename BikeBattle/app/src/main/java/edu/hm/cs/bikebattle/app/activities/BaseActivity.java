package edu.hm.cs.bikebattle.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.google.android.gms.common.api.Status;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.data.BasicDataConnector;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.modell.User;

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

  private GoogleApiClient googleApiClient;
  private GoogleSignInOptions googleSignInOptions;

  private DataConnector dataConnector;

  private User principal;
  private Uri userPhoto;

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

    dataConnector = new BasicDataConnector(getApplicationContext(), googleApiClient);

  }


  /**
   * Reconnect to GoogleAPI.
   * Start Login Activity if reconnect fails.
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

          userPhoto = acct.getPhotoUrl();
          dataConnector.login(acct.getEmail(), new Consumer<User>() {
            @Override
            public void consume(User input) {
              principal = input;
              Log.d(TAG, principal.toString());
              refreshUserInfo();
            }

            @Override
            public void error(int error, Throwable exception) {
              Log.e(TAG, "LOGIN FAILURE");
            }
          });
        }
      }
    });
  }

  /**
   * Signs out the current signed-in user if any.
   * It also clears the account previously selected by the user and a future sign in attempt
   * will require the user pick an account again.
   */
  public void signOut(){
    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
      @Override
      public void onResult(@NonNull Status status) {

        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
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

  public User getPrincipal() {
    return principal;
  }

  public Uri getUserPhoto(){
    return userPhoto;
  }

  public DataConnector getDataConnector() {
    return dataConnector;
  }

  public  void refreshUserInfo(){

  }
}
