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
import com.google.android.gms.common.api.Status;

import edu.hm.cs.bikebattle.app.BikeBattleApplication;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.data.CachingDataConnector;
import edu.hm.cs.bikebattle.app.data.Consumer;
import edu.hm.cs.bikebattle.app.data.DataConnector;
import edu.hm.cs.bikebattle.app.modell.User;

/**
 * Ist the Basic Activity which were used by all other Activitys
 * Organization: HM FK07.
 * Project: BikeBattle, edu.hm.cs.bikebattle.app.activities
 *
 * @author Rene Zarwel
 *         Date: 09.05.16
 *         OS: MacOS 10.11
 *         Java-Version: 1.8
 *         System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public abstract class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
  /** Tag from the current Activity*/
  private static final String TAG = "BaseActivity";

  /** User from Google Account*/
  private GoogleApiClient googleApiClient;

  /** Google login*/
  private GoogleSignInOptions googleSignInOptions;

  /** Current Connector to the backend*/
  private DataConnector dataConnector;

  /** Current User*/
  private User principal;

  /** Photo from current User*/
  private String userPhoto;

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

    dataConnector = new CachingDataConnector(
        getApplicationContext(),
        googleApiClient,
        ((BikeBattleApplication) getApplication()).getUserCache(),
        ((BikeBattleApplication) getApplication()).getDriveCache(),
        ((BikeBattleApplication) getApplication()).getRouteCache());

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

        if (acct == null) {
          Intent intent = new Intent(getApplicationContext(), Login.class);
          startActivity(intent);
        } else {
          Log.d(TAG, "Login successful!!!....again");
          Log.d(TAG, "Name:" + acct.getDisplayName());
          Log.d(TAG, "Mail:" + acct.getEmail());
          Log.d(TAG, "Token:" + acct.getIdToken());

          dataConnector.login(acct.getEmail(), new Consumer<User>() {
            @Override
            public void consume(User input) {
              principal = input;
              userPhoto = input.getFotoUri();
              Log.d(TAG, principal.toString());
              refreshUserInfo();
            }

            @Override
            public void error(int error, Throwable exception) {
              Log.e(TAG, "LOGIN FAILURE with: " + exception.getMessage());
              exception.printStackTrace();
            }
          }, true);
        }
      }
    });
  }

  /**
   * Signs out the current signed-in user if any.
   * It also clears the account previously selected by the user and a future sign in attempt
   * will require the user pick an account again.
   */
  public void signOut() {
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

  /**
   * Returns the GoogleApiClient.
   * @return -  ApiClient.
   */
  public GoogleApiClient getGoogleApiClient() {
    return googleApiClient;
  }

  /**
   * Return the GoogleSignInOptions.
   * @return - GoogleSignInOptions.
   */
  public GoogleSignInOptions getGoogleSignInOptions() {
    return googleSignInOptions;
  }

  /**
   * Return the current User.
   * @return - User.
   */
  public User getPrincipal() {
    return principal;
  }

  /**
   * Return the Uri which shows the user photo.
   * @return - URI
   */
  public String getUserPhoto() {
    return userPhoto;
  }

  /**
   * Returns the current DataConnenctor;
   * @return - dataConncetor
   */
  public DataConnector getDataConnector() {
    return dataConnector;
  }

  /**
   * Refreshes all Information from the user.
   */
  public void refreshUserInfo() {

  }
}
