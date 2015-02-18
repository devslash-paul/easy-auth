package net.devslash.easyauth.providers;

/**
 * Created by Paul on 18/02/2015.
 */
public enum ProfileType {
    GOOGLE("Google+"),
    FACEBOOK("Facebook");


    private final String name;

    ProfileType(String name) {
        this.name = name;
    }
}
