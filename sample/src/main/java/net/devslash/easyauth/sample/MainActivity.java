package net.devslash.easyauth.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.LoginButton;
import com.google.android.gms.common.SignInButton;

import net.devslash.easyauth.AuthenticationCallbacks;
import net.devslash.easyauth.providers.ProfileProvider;
import net.devslash.easyauth.receivers.AuthenticationProvider;

import java.util.Arrays;


public class MainActivity extends Activity implements AuthenticationCallbacks{

    private AuthenticationProvider authenticationProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authenticationProvider.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginButton loginButton = (LoginButton) findViewById(R.id.authButton);
        loginButton.setReadPermissions(Arrays.asList("email"));

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationProvider.doLogin(AuthenticationProvider.AvailableLogin.GOOGLE);
            }
        });

        // Initialise the G+ API
        // This will let me grab all the info.. But how do i deal with this on application start?
        authenticationProvider = new AuthenticationProvider(this);
        authenticationProvider.registerForAuthenticationCallbacks(this, false);
        authenticationProvider.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        authenticationProvider.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        authenticationProvider.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        authenticationProvider.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        authenticationProvider.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        authenticationProvider.onStop();
    }


    public void onClick(View v) {
        authenticationProvider.doLogin(AuthenticationProvider.AvailableLogin.GOOGLE);
        authenticationProvider.doLogin(AuthenticationProvider.AvailableLogin.FACEBOOK);
    }

    @Override
    public void onLogin(ProfileProvider provider) {
        ((TextView) findViewById(R.id.tv_email_val)).setText(provider.getEmail());
        ((TextView) findViewById(R.id.tv_profile_name_val)).setText(provider.getName());
        ((TextView) findViewById(R.id.tv_identifier_val)).setText(provider.getAccessToken());
        ((ImageView) findViewById(R.id.iv_profile_picture)).setImageBitmap(provider.getProfilePicture());
    }

    @Override
    public void onLogout() {
        ((TextView) findViewById(R.id.tv_email_val)).setText("");
        ((TextView) findViewById(R.id.tv_profile_name_val)).setText("");
        ((TextView) findViewById(R.id.tv_identifier_val)).setText("");
        ((ImageView) findViewById(R.id.iv_profile_picture)).setImageBitmap(null);
    }
}
