package net.devslash.easyauth.providers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by paul on 27/01/15.
 */
public class GoogleProfileProvider implements ProfileProvider, Serializable {
    private static final String TAG = "GoogleProfileProvider";
    private static String authToken;
    private String fullName;
    private String email;
    private String firstName;
    private Bitmap profilePicture;


    public static void GenerateGoogleProfile(final Context ctx, GoogleApiClient app, final ProfileCallback profileCallback) {

        final GoogleProfileProvider provider = new GoogleProfileProvider();


        Person profile = Plus.PeopleApi.getCurrentPerson(app);
        final String email = Plus.AccountApi.getAccountName(app);

        if (profile == null) {
            Log.e(TAG, "Unable to fetch the profile");
            return; //TODO: Maybe shouldn't fail like this?
        }

        final int[] t = {0};

        new Thread(new Runnable() {
            public void run() {
                try {
                    authToken =
                        GoogleAuthUtil.getToken(
                            ctx,
                            email,
                            "oauth2:server:client_id:478536351589-49e8btdvr8366pjlcp0d7pfv89mourfr.apps.googleusercontent.com");
                } catch (IOException | GoogleAuthException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        provider.setFullName(profile.getDisplayName());
        provider.setEmail(email);
        provider.setFirstName(profile.getName().getGivenName());

        String profileImageURL = profile.getImage().getUrl();
        Picasso.with(ctx).load(profileImageURL).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                provider.setProfilePicture(bitmap);
                profileCallback.onReady(provider);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                profileCallback.onReady(provider);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


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
        return profilePicture;
    }

    @Override
    public String getAccessToken() {
        return authToken;
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

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }
}
