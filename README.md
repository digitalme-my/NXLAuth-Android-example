# heimdall-android-sdk

NXLAuth for Android is a client SDK for communicating with OAuth 2.0 and OpenID Connect providers. It strives to directly map the requests and responses of those specifications, while following the idiomatic style of the implementation language. In addition to mapping the raw protocol flows, convenience methods are available to assist with common tasks like performing an action with fresh tokens.

The library follows the best practices set out in RFC 8252 - OAuth 2.0 for Native Apps, including using Custom Tabs for authorization requests. For this reason, WebView is explicitly not supported due to usability and security reasons.

The library also supports the PKCE extension to OAuth which was created to secure authorization codes in public clients when custom URI scheme redirects are used. The library is friendly to other extensions (standard or otherwise) with the ability to handle additional parameters in all protocol requests and responses.

A talk providing an overview of using the library for enterprise single sign-on (produced by Google) can be found here: Enterprise SSO with Chrome Custom Tabs.

## Download
Please contact your NXLAuth representative for latest SDK.

## Requirements
NXLAuth for Android supports Android API 16 (Jellybean) and above. Browsers which provide a custom tabs implementation are preferred by the library, but not required. Both Custom URI Schemes (all supported versions of Android) and App Links (Android M / API 23+) can be used with the library.

## Demo app
A demo app is contained in this repository. To try out the demo, just open the project in Android Studio and run 'app'.

## Installation
Copy the latest `nxlauth.aar` file to `<project folder>/nxlauth` folder.

In your app level `build.gradle`, insert this line of codes with your package name as value in `defaultConfig` block:

```
android {
  ...
  defaultConfig {
    ...
    manifestPlaceholders = [
      'appAuthRedirectScheme': 'com.example.sampleapp'
    ]
    ...
  }
  ...
}
```

And in `dependencies` block:
```
dependencies {
  ...
  implementation 'net.openid:appauth:0.7.0'
  implementation(name:'nxlauth', ext:'aar')
  ...
}
```

## Implementing the authorization code flow
First, an NXLAuth instance must be created and initialized with client id supplied.

```
import my.com.nexlife.nxlauth.AuthManager;
import my.com.nexlife.nxlauth.SDKMessages;
import my.com.nexlife.nxlauth.SDKScopes;

...

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    ...
    AuthManager mAuthManager = new AuthManager.Builder("client-id", getApplicationContext())
        .setTag("LOGCAT TAG")
        .setScope(SDKScopes.OPEN_ID, SDKScopes.OFFLINE)
        .build();
    ...
  }
```

## Getting access token
Given a successful authorization (build does not throw exception), a token request can be made and a token response can be retrieved via callback as per example below:

### Via lambda expression:
```
  private void getAccessToken() {
    /**
     * This callback needs to be implemented by tenant
     */
    this.mAuthManager.getAccessToken((accessToken, ex) -> {
      if (ex == null) {
        // Do stuffs with the access token retrieved
      } else {
        // Capture or log the exception
      }
    });
  }
```

### Via implementation of interface `AuthManager.NXLCallback`:
```
  private void getUserInfo() {
    /**
     * This callback needs to be implemented by tenant
     */
    this.mAuthManager.getAccessToken(new AuthManager.NXLCallback() {
      @Override
      public void onComplete(@NonNull String accessToken, @NonNull Exception ex) {
        if (ex == null) {
          // Do stuffs with the access token retrieved
        } else {
          // Capture or log the exception
        }
      }
    });
  }
```

## Getting user info
Given a successful authorization (build does not throw exception), a user info request can be made and a response can be retrieved via callback as per example below:

### Via lambda expression:
```
  private void getUserInfo() {
    /**
     * This callback needs to be implemented by tenant
     */
    this.mAuthManager.getUserInfo((response, ex) -> {
      if (ex == null) {
        // Do stuffs with the user info retrieved
      } else {
        // Capture or log the exception
      }
    });
  }
```

### Via implementation of interface `AuthManager.NXLCallback`:
```
  private void getAccessToken() {
    /**
     * This callback needs to be implemented by tenant
     */
    this.mAuthManager.geUserInfo(new AuthManager.NXLCallback() {
      @Override
      public void onComplete(@NonNull String response, @NonNull Exception ex) {
        if (ex == null) {
          // Do stuffs with the access token retrieved
        } else {
          // Capture or log the exception
        }
      }
    });
  }
```

### The `AuthManager.NXLCallback` interface
```
  public interface NXLCallback {
    void onComplete(@NonNull String result, @NonNull Exception ex);
  }
```

### List of `SDKScopes`
Please make sure you have the right scopes configured for your client.
Class import: `my.com.nexlife.nxlauth.SDKScopes`

|Syntax|Scope|
|------|-----|
|`SDKScopes.PROFILE`|Public profile|
|`SDKScopes.OPEN_ID`|Open ID profile|

## Clearing Shared Preferences
This method is to clear shared preferences for the SDK.
```clearSharedPreferences()```
