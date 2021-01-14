package com.example.sociologin.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

public class FacebookHelper {
    private final Collection<String> permissions = Arrays.asList("public_profile ", "email", "user_birthday", "user_location");
    public static CallbackManager callbackManager;
    private LoginManager loginManager;
    private Activity activity;
    private Fragment fragment;
    public static OnFbSignInListener fbSignInListener;

    /**
     * Interface to listen the Facebook login
     */
    public interface OnFbSignInListener {
        void OnFbSignInComplete(GraphResponse graphResponse,String error);
    }

    public FacebookHelper(Activity activity, OnFbSignInListener fbSignInListener) {
        this.activity = activity;
        this.fbSignInListener = fbSignInListener;
    }

    public FacebookHelper(Fragment fragment, OnFbSignInListener fbSignInListener) {
        this.fragment = fragment;
        this.fbSignInListener = fbSignInListener;
    }


    public void connect() {
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        if (activity != null)
            loginManager.logInWithReadPermissions(activity, permissions);
        else
            loginManager.logInWithReadPermissions(fragment, permissions);
        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (loginResult != null) {
                            callGraphAPI(loginResult.getAccessToken());
                        }
                    }

                    @Override
                    public void onCancel() {
                        fbSignInListener.OnFbSignInComplete(null, "User cancelled.");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        fbSignInListener.OnFbSignInComplete(null, exception.getMessage());
                    }
                });

    }

    private void callGraphAPI(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                (object, response) -> fbSignInListener.OnFbSignInComplete(response,null));
        Bundle parameters = new Bundle();
        //Explicitly we need to specify the fields to get values else some values will be null.
        parameters.putString("fields", "id,birthday,email,first_name,gender,last_name,link,location,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public static void diconnectFromFacebook(){
        if(AccessToken.getCurrentAccessToken()==null){
            return;
        }
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                LoginManager.getInstance().logOut();
            }
        });
    }

}