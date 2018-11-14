package my.com.nexlife.nxlauth.ssosampleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import my.com.nexlife.nxlauth.AuthManager;
import my.com.nexlife.nxlauth.SDKMessages;
import my.com.nexlife.nxlauth.SDKScopes;

public class MainActivity extends AppCompatActivity {
  private AuthManager mAuthManager;
  protected String mTag = "MAINAPP";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button loginButton = findViewById(R.id.loginButton);
    Button accessTokenButton = findViewById(R.id.getAccessTokenButton);
    Button userInfoButton = findViewById(R.id.getUserInfoButton);
    Button clearLogButton = findViewById(R.id.clearLogButton);
    Button clearSharedPrefsButton = findViewById(R.id.clearSharedPrefsButton);
    TextView logTextView = findViewById(R.id.logTextView);

    loginButton.setOnClickListener((view) -> authenticate());
    accessTokenButton.setOnClickListener((view) -> getAccessToken());
    userInfoButton.setOnClickListener((view) -> getUserInfo());
    clearLogButton.setOnClickListener((view) -> clearLog());
    clearSharedPrefsButton.setOnClickListener((view) -> clearSharedPrefs());
    logTextView.setMovementMethod(new ScrollingMovementMethod());
    logTextView.setTextIsSelectable(true);

    mAuthManager = new AuthManager.Builder("0cc5ff74-9131-4b66-84e1-099c7390a910", getApplicationContext())
        .setTag("HEIMDALLSSO")
        .setScope(SDKScopes.OPEN_ID, SDKScopes.OFFLINE)
        .build();
  }

  private void clearSharedPrefs() {
    this.mAuthManager.clearSharedPreferences();
  }

  private void clearLog() {
    TextView logTextView = findViewById(R.id.logTextView);
    logTextView.setText("");
  }

  private void getUserInfo() {
    /**
     * This callback needs to be implemented by tenant
     */
    TextView logTextView = findViewById(R.id.logTextView);

    this.mAuthManager.getUserInfo((response, ex) -> {
      if (ex == null) {
        Log.d(this.mTag, response);
        logTextView.append("Response:\n" + response + "\n");
      } else {
        Log.d(this.mTag, ex.toString());
        logTextView.append("Exception: " + ex.toString() + "\n");
      }
    });
  }

  private void getAccessToken() {
    /**
     * This callback needs to be implemented by tenant
     */
    TextView logTextView = findViewById(R.id.logTextView);

    this.mAuthManager.getAccessToken((accessToken, ex) -> {
      if (ex == null) {
        Log.d(mTag, "Access Token: " + accessToken);
        logTextView.append("Access Token: " + accessToken + "\n");
      } else {
        Log.d(mTag, "Exception: " + ex.toString());
        logTextView.append("Exception: " + ex.toString() + "\n");
      }
    });
  }

  /**
   * This part needs to be implemented by tenant
   */
  private void authenticate() {
    Intent authIntent = this.mAuthManager.startAuthentication();
    startActivityForResult(authIntent, SDKMessages.RC_AUTH);
  }

  /**
   * This part needs to be implemented by tenant
   */
  @Override
  protected void onActivityResult(int requestCode, int responseCode, Intent data) {
    TextView logTextView = findViewById(R.id.logTextView);

    if (requestCode == SDKMessages.RC_AUTH) {
      boolean status = this.mAuthManager.performTokenRequestSuccessful(data);
      Log.d(mTag, "Status: " + status);
    }
  }
}
