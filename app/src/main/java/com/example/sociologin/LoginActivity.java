package com.example.sociologin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.example.sociologin.helper.FacebookHelper;
import com.example.sociologin.helper.GoogleSignInHelper;
import com.example.sociologin.utils.KeyHashGenerator;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity
        implements FacebookHelper.OnFbSignInListener, GoogleSignInHelper.OnGoogleSignInListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    //--------------------------------Facebook login--------------------------------------//
    private FacebookHelper fbConnectHelper;
    private Button fbSignInButton;
    //----------------------------------Google +Sign in-----------------------------------//
    //Google plus sign-in button
    private GoogleSignInHelper googleSignInHelper;
    private Button gSignInButton;

    private ProgressBar progressBar;
    private boolean isFbLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // Facebook SDK Initialization
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progress_bar);


        //--------------------------------Facebook login--------------------------------------//
        KeyHashGenerator.generateKey(this);
        fbConnectHelper = new FacebookHelper(this, this);
        fbSignInButton = findViewById(R.id.fb_sign_in_button);
        fbSignInButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            fbConnectHelper.connect();
            isFbLogin = true;
        });


        //----------------------------------Google +Sign in-----------------------------------//
        googleSignInHelper = new GoogleSignInHelper(this, this);
        googleSignInHelper.connect();
        gSignInButton = findViewById(R.id.main_g_sign_in_button);
        gSignInButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            googleSignInHelper.signIn();
            isFbLogin = false;
        });

    }

    @Override protected void onStart() {
        super.onStart();
        googleSignInHelper.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleSignInHelper.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);
        if (isFbLogin) {
            progressBar.setVisibility(View.VISIBLE);
            isFbLogin = false;
        }
    }

    @Override
    public void OnFbSignInComplete(GraphResponse graphResponse, String error) {
        Profile.setId(1);
        progressBar.setVisibility(View.GONE);
        if (error == null) {
            try {
                JSONObject jsonObject = graphResponse.getJSONObject();
                Profile.setUsername(jsonObject.getString("name"));
                Profile.setUseremail(jsonObject.getString("email"));
                /*String id = jsonObject.getString("id");
                String profileImg = "http://graph.facebook.com/" + id + "/picture?type=large";*/
                startActivity(new Intent(LoginActivity.this,UserProfile.class));
                finish();

            } catch (JSONException e) {
                Log.i(TAG, e.getMessage());
            }
        }
    }

    @Override public void OnGSignInSuccess(GoogleSignInAccount googleSignInAccount) {
        Profile.setId(2);
        progressBar.setVisibility(View.GONE);
        if (googleSignInAccount != null) {
            Profile.setUsername(googleSignInAccount.getGivenName());
            Profile.setUseremail(googleSignInAccount.getEmail());
            startActivity(new Intent(LoginActivity.this,UserProfile.class));
            finish();
        }
    }

    @Override public void OnGSignInError(String error) {
        Log.e(TAG, error);
    }

}