package net.devslash.easyauth.receivers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import net.devslash.easyauth.AuthenticationCallbacks;
import net.devslash.easyauth.providers.FacebookProfileProvider;
import net.devslash.easyauth.providers.ProfileCallback;
import net.devslash.easyauth.providers.ProfileProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Paul on 12/02/2015.
 */
public class FacebookReceiver {

    private final String TAG = "FacebookReceiver";
    private final Activity mActivity;
    private final Set<AuthenticationCallbacks> mAuthCallbacks = new HashSet<>();
    
    private UiLifecycleHelper uiHelper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState sessionState, Exception e) {
            onSessionStateChange(session, sessionState, e);
        }
    };

    private void onSessionStateChange(final Session session, SessionState sessionState, Exception exception) {
        Log.i(TAG, "Logged about to come up");
        if (sessionState.isOpened()) {
            Log.i(TAG, "Logged in...");

            FacebookProfileProvider.GenerateFacebookProvider(mActivity, session, new ProfileCallback() {
                @Override
                public void onReady(ProfileProvider instance) {
                    onLogin(instance);
                }

                @Override
                public void fail(String s) {
                    onLogout();
                }
            });
        } else if (sessionState.isClosed()) {
            Log.i(TAG, "Logged out...");
            onLogout();
        }
    }

    private void onLogin(ProfileProvider instance) {
        for (AuthenticationCallbacks callbacks : mAuthCallbacks) {
            callbacks.onLogin(instance);
        }
    }
    
    private void onLogout() {
        for (AuthenticationCallbacks callbacks : mAuthCallbacks) {
            callbacks.onLogout();
        }
    }
    


    /* Below this line is fairly boiler plate stuff */

    public FacebookReceiver(Activity activity) {
        uiHelper = new UiLifecycleHelper(activity, callback);
        mActivity = activity;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uiHelper.onActivityResult(requestCode, resultCode, data);

    }

    public void onCreate(Bundle savedInstanceState) {
        uiHelper.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(mActivity);
    }

    public void onDestroy() {
        uiHelper.onDestroy();
    }

    public void onPause() {
        uiHelper.onPause();
        AppEventsLogger.deactivateApp(mActivity);
    }

    public void onResume() {
        uiHelper.onResume();

        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed())) {
            onSessionStateChange(session, session.getState(), null);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        uiHelper.onSaveInstanceState(outState);
    }

    public void onStop() {
        uiHelper.onStop();
    }

    public void registerAuthenticatedCallback(AuthenticationCallbacks authenticationProvider) {
        // If it's not been added then add it
        mAuthCallbacks.add(authenticationProvider);
    }

    public void doLogout(boolean invalidateTokens) {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            if (invalidateTokens) {
                
                session.closeAndClearTokenInformation();
            } else {
                session.close();
            }
            onLogout();
        }
        
    }
}
