package net.devslash.easyauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import net.devslash.easyauth.receivers.LoginReceiver;


/**
 * Created by paul on 27/01/15.
 */
public class LoginActivity extends FragmentActivity implements AuthenticatedCallback {

    private LoginReceiver loginReceiver;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginReceiver.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialise the G+ API
        // This will let me grab all the info.. But how do i deal with this on application start?
        loginReceiver = new LoginReceiver(this, this);
        loginReceiver.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginReceiver.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginReceiver.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginReceiver.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        loginReceiver.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginReceiver.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginReceiver.onStop();
    }


    public void onClick(View v) {
        loginReceiver.doLogin(LoginReceiver.AvailableLogin.GOOGLE);
        loginReceiver.doLogin(LoginReceiver.AvailableLogin.FACEBOOK);
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void onLogout() {

    }
}
