package edu.hm.cs.bikebattle.app.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import edu.hm.cs.bikebattle.app.BuildConfig;
import edu.hm.cs.bikebattle.app.R;
import edu.hm.cs.bikebattle.app.api.rest.ClientFactory;
import retrofit2.Response;

import java.io.IOException;

/**
 * Erstellt das Login Fenster, indem man sich über den Googleaccount anmeldet.
 * @author munichsven, darenegade
 */
public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = "SignInBikeBattle";
  private static final int RC_SIGN_IN = 9001;
  private GoogleApiClient googleApiClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initCompoents();
  }

  /**
   * Initialisiert die verschiedenen Komponenten, welche für das activity_login benötigt werden.
   */
  private void initCompoents() {

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(getString(R.string.server_client_id))
        .build();

    googleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this, this)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();

    //Lädt die Eigenschaften des login Button und fügt ihm einen Actionslistener hinzu.
    SignInButton login = (SignInButton) findViewById(R.id.sign_in_button);
    login.setScopes(gso.getScopeArray());
    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        signIn();
      }
    });
  }

  /**
   *Lässt den User seinen gewünschten Google Account auswählen.
   */
  private void signIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.d(TAG, "onConnectionFailed:" + connectionResult);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    googleApiClient.connect();
  }

  /**
   * Überprüft das Login Result wenn das Result == true ist,
   * werden die benötigten Informationen herrausgefiltert zudem erscheint
   * ein Pop-up Fenster mit erfolgreichem Login.
   * Wenn das result Falsch ist, wird der Benutzer mithilfe eines Fehler -
   * Popups informiert.
   * @param result - Login Ergebnis.
   */
  private void handleSignInResult(GoogleSignInResult result) {

    String userName;
    String userMail;
    String userToken;
    //Wenn das Result True ist
    if (result.isSuccess()) {
      Log.d(TAG, "Login successful!!!");
      GoogleSignInAccount acct = result.getSignInAccount();
      userName = acct.getDisplayName();
      userMail = acct.getEmail();
      userToken = acct.getIdToken();

      Log.d(TAG, "Name:" + userName);
      Log.d(TAG, "Mail:" + userMail);
      Log.d(TAG, "Token:" + userToken);

      Toast toast = Toast.makeText(getApplicationContext(), "Anmeldung erfolgreich",
          Toast.LENGTH_LONG);
      toast.show();

      new RetrieveTokenTask().execute(userMail);
    } else {
      Log.d(TAG, "Fehler beim Login: " + result.getStatus().toString());
      Toast toast = Toast.makeText(getApplicationContext(), "Anmeldung fehlgeschlagen",
          Toast.LENGTH_LONG);
      toast.show();
    }
  }

  private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
      String accountName = params[0];
      String scopes = "oauth2:profile email";
      String token = null;
      try {
        token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
      } catch (IOException e) {
        Log.e(TAG, e.getMessage());
      } catch (UserRecoverableAuthException e) {
        startActivityForResult(e.getIntent(), RC_SIGN_IN);
      } catch (GoogleAuthException e) {
        Log.e(TAG, e.getMessage());
      }
      return token;
    }

    @Override
    protected void onPostExecute(String s) {
      super.onPostExecute(s);
      Log.d(TAG, "Token retrieved: " + s);
      try {
        Response response = ClientFactory.getUserClient(s).findAll().execute();
        if(BuildConfig.DEBUG && 200 == response.code()){
         throw new AssertionError(response.code());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
