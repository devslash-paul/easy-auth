package net.devslash.easyauth;

import net.devslash.easyauth.providers.ProfileProvider;

/**
 * Created by Paul on 12/02/2015.
 */
public interface AuthenticatedCallback {

    // This means you can now access the login info
    // With just using the AuthenticationProvider
    public void onLogin();

    public void onLogout();
}
