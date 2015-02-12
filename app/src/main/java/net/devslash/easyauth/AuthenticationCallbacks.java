package net.devslash.easyauth;


import net.devslash.easyauth.providers.ProfileProvider;

/**
 * Created by Paul on 1/02/2015.
 */
public interface AuthenticationCallbacks {

    public void onLogin(ProfileProvider profileProvider);

    public void onLogout();

}
