package net.devslash.easyauth.providers;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by paul on 27/01/15.
 */
public interface ProfileProvider extends Serializable {

    public String getName();

    public boolean isLoggedIn();

    public boolean signOut();

    public String getEmail();

    public Bitmap getProfilePicture();

    public String getAccessToken();

}
