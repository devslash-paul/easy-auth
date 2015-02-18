package net.devslash.easyauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.devslash.easyauth.authflows.FacebookAuthenticator;
import net.devslash.easyauth.authflows.GoogleAuthenticator;
import net.devslash.easyauth.providers.ProfileProvider;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by Paul on 12/02/2015.
 * This class will perform all required authentication.
 * <p/>
 * This will then allow the base activity to not worry and grab the authenticated details whenver
 * required
 * <p/>
 * The class will look in the preferences to see which login is being used. No login attempt will be
 * made if there are no preferences set
 * <p/>
 * To use this class your activity needs to provide an interface that will allow for the fragment to
 * request the activity as an AuthenticatedActivity. Then you're able to call registerForProfile to
 * get a callback when a profile is received.
 * <p/>
 * You NEED to call this on
 * - onActivityResult
 * - onPause
 * - onStop
 * - onSaveInstanceState
 * - onCreate
 * - onStop
 * - onDestroy
 */
public class AuthenticationProvider implements AuthenticationCallbacks {

    FacebookAuthenticator fReceiver;
    GoogleAuthenticator gReceiver;
    private Set<AuthenticationCallbacks> authCallbacksList = new HashSet<>();
    private ProfileProvider mCurrentProfile = null;

    public AuthenticationProvider(Activity activity) {
        fReceiver = new FacebookAuthenticator(activity);
        fReceiver.registerAuthenticatedCallback(this);
        gReceiver = new GoogleAuthenticator(activity);
        gReceiver.registerAuthenticationCallback(this);
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

    public void doLogout(boolean invalidateTokens) {
        fReceiver.doLogout(invalidateTokens);
        gReceiver.doLogout(invalidateTokens);
    }

    /**
     * The default case means that the tokens will not
     * be invalidated at all.
     */
    public void doLogout() {
        doLogout(false);
    }

    public void registerForAuthenticationCallbacks(AuthenticationCallbacks callback,
                                                   boolean callIfLoggedOut) {
        authCallbacksList.add(callback);
        //If we have an authenticated context then call it back
        // straight away
        if (mCurrentProfile != null) {
            callback.onLogin(mCurrentProfile);
        } else if (callIfLoggedOut) {
            callback.onLogout();
        }
    }

    @Override
    public void onLogin(ProfileProvider profileProvider) {
        mCurrentProfile = profileProvider;
        for (AuthenticationCallbacks callbacks : authCallbacksList) {
            callbacks.onLogin(profileProvider);
        }
    }

    @Override
    public void onLogout() {
        mCurrentProfile = null;
        for (AuthenticationCallbacks callbacks : authCallbacksList) {
            callbacks.onLogout();
        }
    }

    public ProfileProvider getCurrentProfile() {
        return mCurrentProfile;
    }

    public static enum AvailableLogin {
        FACEBOOK,
        GOOGLE
    }
}
