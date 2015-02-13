# easy-auth

Easy auth provides a way to have Google and Facebook login without much set up at all!

# Installation

Easy auth can be installed using a gradle dependency


    dependencies {
        compile 'net.devslash:easy-auth:1.0.+'
    }
    
# Useage

EasyAuth takes most of the pain away from doing user authentication through Facebook/Google. It's still a fairly new tool and as such will still see active development. 

## Logging in
To log in you'll need to create an activity (see the same app) that contains the Google Plus login button as well as the Facebook sign in button.
Follow the guides found at Google and Facebook for how to set up your app for each of them. Once you've set up your signing configuration and Application Id's 
you'll be able to use easy auth.

If you ever need to grab authentication you'll use
 
        private AuthenticationProvider authenticationProvider;
        
The authentication provider provides an abstracted way for logging in with either Google or Facebook.


You will need to pass through lifecycle methods from your activity. These include

- onCreate(Bundle)
- onDestroy
- onPause
- onResume
- onSaveInstanceState
- onStop

As far as getting someone logged in with Facebook, if the button exists then your login will be done automatically.
In any future activities Easy Auth will auto-login and allow you to access the users profile. For Google+ you'll need 
to hook the onClick method of the SignInButton to call 'authenticationProvider.doLogin(AvailableLogin.GOOGLE)'. 

## Using Easy auth to get the profile
Once you've logged in easy auth will provide the profiles to you as a callback. This callback interface
is required as one of the constructors. The ability to register extra callbacks will come in an upcoming version.

The profile will be returned to you as an instance of ProfileProvider. All of the details will be available as a getter.

EasyAuth does currently not allow you to grab extra details apart from what is available in the ProfileProvider. You may either fork the project or wait
for updates that allow you to dynamically request details. 

### Note for Facebook. 
To work with EasyAuth you must initialise the login button with the following code:

    LoginButton loginButton = (LoginButton) findViewById(R.id.authButton);
    loginButton.setReadPermissions(Arrays.asList("email"));
    
    

 
