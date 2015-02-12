package net.devslash.easyauth.receivers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import net.devslash.easyauth.AuthenticatedCallback;

/**
 * Created by Paul on 12/02/2015.
 */
public class FacebookReceiver {

    private final String TAG = "FacebookReceiver";
    private final Activity mActivity;
    private final AuthenticatedCallback mAuthCallbacks;
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
            mAuthCallbacks.onLogin();
        } else if (sessionState.isClosed()) {
            Log.i(TAG, "Logged out...");
            mAuthCallbacks.onLogout();
        }
    }


    /* Below this line is fairly boiler plate stuff */

    public FacebookReceiver(Activity activity, AuthenticatedCallback authenticated) {
        uiHelper = new UiLifecycleHelper(activity, callback);
        mActivity = activity;
        mAuthCallbacks = authenticated;
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
}
