package net.devslash.easyauth.providers;

/**
 * Created by paul on 29/01/15.
 */
public interface ProfileCallback {
    public void onReady(ProfileProvider instance);

    void fail(String s);
}
