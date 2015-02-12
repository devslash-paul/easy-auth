package net.devslash.easyauth.receivers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.devslash.easyauth.AuthenticationCallbacks;


/**
 * Created by Paul on 12/02/2015.
 * <p/>
 * LoginButton loginButton = (LoginButton) view.findViewById(R.id.authButton);
 * loginButton.setReadPermissions(Arrays.asList("email"));
 * SignInButton gSignIn = (SignInButton) view.findViewById(R.id.sign_in_button);
 * gSignIn.setOnClickListener((View.OnClickListener) getActivity());
 */
public class AuthenticationProvider {

    FacebookReceiver fReceiver;
    GoogleReceiver gReceiver;

    public AuthenticationProvider(Activity activity, AuthenticationCallbacks callback) {
        fReceiver = new FacebookReceiver(activity, callback);
        gReceiver = new GoogleReceiver(activity, callback);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fReceiver.onActivityResult(requestCode, resultCode, data);
        gReceiver.onActivityResult(requestCode, resultCode, data);
    }

    public void onCreate(Bundle savedInstanceState) {
        gReceiver.onCreate(savedInstanceState);
        fReceiver.onCreate(savedInstanceState);
    }

    public void onDestroy() {
        fReceiver.onDestroy();
    }

    public void onPause() {
        fReceiver.onDestroy();
    }

    public void onResume() {
        fReceiver.onResume();
        gReceiver.onResume();
    }

    public void onSaveInstanceState(Bundle outState) {
        fReceiver.onSaveInstanceState(outState);
    }

    public void onStop() {
        fReceiver.onStop();
    }

    public void doLogin(AvailableLogin loginType) {
        if (loginType == AvailableLogin.FACEBOOK) {

        } else if (loginType == AvailableLogin.GOOGLE) {
            gReceiver.doConnect();
        }
    }

    public static enum AvailableLogin {
        FACEBOOK,
        GOOGLE
    }
}
