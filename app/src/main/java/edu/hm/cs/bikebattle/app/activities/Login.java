package edu.hm.cs.bikebattle.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import edu.hm.cs.bikebattle.app.R;

/**
 * Erstellt das Login Fenster, indem man sich über den Googleaccount anmeldet.
 * @author munichsven, darenegade
 */
public class Login extends BaseActivity {

  private static final String TAG = "SignInBikeBattle";


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

    //Lädt die Eigenschaften des login Button und fügt ihm einen Actionslistener hinzu.
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
   *Lässt den User seinen gewünschten Google Account auswählen.
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
