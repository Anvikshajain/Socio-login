package com.example.sociologin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sociologin.helper.FacebookHelper;
import com.example.sociologin.helper.GoogleSignInHelper;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserProfile extends AppCompatActivity {

    TextView loggiout;
    TextView uname,uemail,pname;
    ImageView profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        loggiout=findViewById(R.id.button_logout);
        uname=findViewById(R.id.username);
        pname=findViewById(R.id.aname);
        uemail=findViewById(R.id.emailaddress);
        profilepic=findViewById(R.id.user_pic);

        uname.setText(Profile.getUsername());
        uemail.setText(Profile.getUseremail());
        pname.setText(Profile.getUsername());

        if(Profile.getId()==1){
            profilepic.setImageResource(R.drawable.ic_facebook);
        }else if(Profile.getId()==2){
            profilepic.setImageResource(R.drawable.ic_google);
        }

        loggiout.setOnClickListener(v -> {
            int id=Profile.getId();
            if(id==1){
                FacebookHelper.diconnectFromFacebook();
                startActivity(new Intent(UserProfile.this,LoginActivity.class));
                finish();
            }
            else if(id==2){
                GoogleSignInHelper.signOut();
                startActivity(new Intent(UserProfile.this,LoginActivity.class));
                finish();
            }
        });
    }
}