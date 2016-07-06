package edu.hm.cs.bikebattle.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.squareup.picasso.Picasso;
import edu.hm.cs.bikebattle.app.R;

/**
 * Creats a Login Activity, and uses the user account from google.
 * @author munichsven, darenegade
 */
public class Login extends BaseActivity {
  /** Tag from the current Acktivity*/
  private static final String TAG = "SignInBikeBattle";

  /** Port*/
  public static final int RC_SIGN_IN = 9001;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initCompoents();
  }

  /**
   * Initialized all important variables.
   */
  private void initCompoents() {

    ImageView splashScreen = (ImageView) findViewById(R.id.logo);
    // Loads the background picture for the Login View into the imageview.
    Picasso
        .with(getApplicationContext())
        .load(R.drawable.splash_screen_login)
        .fit()
        .centerCrop()
        .into(splashScreen);

    SignInButton login = (SignInButton) findViewById(R.id.sign_in_button);
    login.setScopes(getGoogleSignInOptions().getScopeArray());
    login.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        signIn();
      }
    });
  }

  /**
   * The User can select his google profil.
   */
  public void signIn() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  protected void onStart() {
    super.onStart();
    getGoogleApiClient().connect();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      handleSignInResult(result);
    }
  }

  /**
   * Checks the login result, if true the user gets a Toast Dialog which shows
   * a "Sucessfull Login" Information. If flase the gets an error.
   * Popups informiert.
   * @param result - Login result.
   */
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

      Log.d(TAG, "Name:" + userName);
      Log.d(TAG, "Mail:" + userMail);
      Log.d(TAG, "Token:" + userToken);

      Toast toast = Toast.makeText(getApplicationContext(), "Anmeldung erfolgreich",
          Toast.LENGTH_LONG);
      toast.show();

      Intent intent = new Intent(getApplicationContext(), MainActivity.class);
      startActivity(intent);

    } else {
      Log.d(TAG, "Fehler beim Login: " + result.getStatus().toString());
      Toast toast = Toast.makeText(getApplicationContext(), "Anmeldung fehlgeschlagen",
          Toast.LENGTH_LONG);
      toast.show();
    }
  }
}
