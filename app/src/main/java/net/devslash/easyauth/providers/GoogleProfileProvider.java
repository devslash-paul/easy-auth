
package net.devslash.easyauth.providers;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.Serializable;

/**
 * Created by paul on 27/01/15.
 */
public class GoogleProfileProvider implements ProfileProvider, Serializable {
    private static final String TAG = "GoogleProfileProvider";
    private String fullName;
    private String email;
    private String firstName;


    public static void GenerateGoogleProfile(GoogleApiClient app, ProfileCallback profileCallback) {

        GoogleProfileProvider provider = new GoogleProfileProvider();


        Person profile = Plus.PeopleApi.getCurrentPerson(app);
        String email = Plus.AccountApi.getAccountName(app);

        if (profile == null) {
            Log.e(TAG, "Unable to fetch the profile");
            return; //TODO: Maybe shouldn't fail like this?
        }

        provider.setFullName(profile.getDisplayName());
        provider.setEmail(email);
        provider.setFirstName(profile.getName().getGivenName());

        profileCallback.onReady(provider);
    }

    @Override
    public String getName() {
        return fullName;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public boolean signOut() {

        return false;
    }

    @Override
    public String getEmail() {
        return "G+: " + email;
    }

    @Override
    public Bitmap getProfilePicture() {
        return null;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

}
