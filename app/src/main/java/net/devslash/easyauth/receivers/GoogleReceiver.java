package net.devslash.easyauth.receivers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import net.devslash.easyauth.AuthenticationCallbacks;
import net.devslash.easyauth.providers.GoogleProfileProvider;
import net.devslash.easyauth.providers.ProfileCallback;
import net.devslash.easyauth.providers.ProfileProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Paul on 12/02/2015.
 */
public class GoogleReceiver implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "GoogleReceiver";
    private final Activity mActivity;
    private final Set<AuthenticationCallbacks> mCallback = new HashSet<>();
    
    private ConnectionResult gConnectionResult;
    private GoogleApiClient googleApiClient;

    private boolean gIntentInProgress = false;
    private boolean gSignInClicked = false;

    private ProgressDialog googleProgressDialog;
    private Set<AuthenticationCallbacks> mAuthCallbacks = new HashSet<>();

    public GoogleReceiver(Activity activity) {
        mActivity = activity;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "User has signed in with Google Plus Succesfully");
        Log.d(TAG, "Google API connection status: " + googleApiClient.isConnected());

        if (googleProgressDialog != null &&
                googleProgressDialog.isShowing()) {
            googleProgressDialog.dismiss();
        }

        GoogleProfileProvider.GenerateGoogleProfile(googleApiClient, new ProfileCallback() {
            @Override
            public void onReady(ProfileProvider instance) {
                onLogin(instance);
            }

            @Override
            public void fail(String s) {

            }
        });

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

    /**
     * Find out what this does
     *
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {
        onLogout();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Problem getting a connection");
        Log.e(TAG, connectionResult.toString());

        if (googleProgressDialog != null &&
                googleProgressDialog.isShowing()) {
            googleProgressDialog.dismiss();
        }

        gConnectionResult = connectionResult;

        if (!gIntentInProgress) {

            if (gSignInClicked) {
                resolveSignInError(gConnectionResult);
            }
        }
    }

    private void resolveSignInError(ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                gIntentInProgress = true;
                result.startResolutionForResult(mActivity, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was cancelled before it was sent
                // attempt to connect again to get an updated result
                gIntentInProgress = false;
                googleApiClient.connect();
            }


        }
    }

    public void onCreate(Bundle savedInstanceState) {
        googleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    public void onResume() {
        googleApiClient.connect();
    }

    public void doConnect() {
        gSignInClicked = true;
        Log.d(TAG, "Starting google sign in");
        googleProgressDialog = new ProgressDialog(mActivity);
        googleProgressDialog.setMessage("Logging in...");
        googleProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        googleProgressDialog.show();

        resolveSignInError(gConnectionResult);
    }

    public void registerAuthenticationCallback(AuthenticationCallbacks authenticationCallbacks) {
        mAuthCallbacks.add(authenticationCallbacks);
    }
}
