package net.devslash.easyauth.providers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;

import java.io.Serializable;

/**
 * Created by paul on 27/01/15.
 */
public class FacebookProfileProvider implements ProfileProvider, Serializable {

    private static final String TAG = "FacebookProfileProvider";

    private Session mySession;
    private String fullName;
    private String firstName;
    private String email;
    private Bitmap profilePicture;
    private ProfileType profileType = ProfileType.FACEBOOK;

    /**
     * This is currently bad. Because when creating it you're also giving away the instance of it as
     * part of the callback. This should be transitioned to a static builder pattern. That makes a
     * ton more sense as that way there is no chance that the instance is accessed before being
     * available for use
     *
     * @param session
     * @param callback
     */
    public static void GenerateFacebookProvider(final Context ctx, Session session, final ProfileCallback callback) {

        /**
         * We have a facebook session so we can grab all
         * required details right from the start. We'll keep
         * a reference to it just so that we can perform timely updates
         * based on the session
         */

        final FacebookProfileProvider provider = new FacebookProfileProvider();
        provider.setSession(session);

        final int[] t = {0};

        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putString("height", "200");
        params.putString("type", "normal");
        params.putString("width", "200");

        Request.executeBatchAsync(Request.newMeRequest(session, new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser graphUser, Response response) {
                        if (graphUser == null) {
                            Log.e("Login", "Graph user was null after response");
                            Log.e("Login", response.getError().getErrorMessage());
                            callback.fail("Error when requesting profile");
                            return;
                        }

                        // graphUser now exists.
                        provider.setFullName(graphUser.getName());
                        provider.setFirstName(graphUser.getFirstName());
                        if (graphUser.getProperty("email") != null) {
                            provider.setEmail(graphUser.getProperty("email").toString());
                        }

                        t[0]++;
                        if (t[0] == 2) {
                            callback.onReady(provider);
                        }

                    }
                }),
                new Request(session,
                        "/me/picture",
                        params,
                        HttpMethod.GET,
                        new Request.Callback() {
                            @Override
                            public void onCompleted(Response response) {
                                try {
                                    String url = (String) response.getGraphObject()
                                            .getInnerJSONObject().getJSONObject("data").get("url");
                                    Picasso.with(ctx).load(url).into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            provider.setPicture(bitmap);
                                            incOrFinish();
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                            incOrFinish();
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                        }

                                        public void incOrFinish() {
                                            t[0]++;
                                            if (t[0] == 2) {
                                                callback.onReady(provider);
                                            }
                                        }
                                    });
                                } catch (JSONException e) {
                                    Log.e(TAG, e.toString());
                                }
                            }
                        }));
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Session getSession() {
        return mySession;
    }

    public void setSession(Session session) {
        this.mySession = session;
    }

    @Override
    public String getName() {
        return firstName;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }


    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    @Override
    public String getAccessToken() {
        return mySession.getAccessToken();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPicture(Bitmap picture) {
        this.profilePicture = picture;
    }

    @Override
    public ProfileType getProfileType() {
        return profileType;
    }
}
