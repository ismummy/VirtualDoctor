package com.kisconsult.ismummy.virtualdoctor.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;
import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        delay();
    }

    private void delay() {
        //the timer will wait for 2000milisec. before perfomin it task
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //to move to the next activity whic is d admin page for now
                User user = MyApplication.getInstance().getPref().getUser();
                if (user == null) {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                } else if (user.getCategory().equalsIgnoreCase("doctor")) {
                    Intent intent = new Intent(SplashScreen.this, Admin.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashScreen.this, Patient.class);
                    startActivity(intent);
                }

                //we need to kill dis activity to disable user from navigating back to dis activity on back button press
                SplashScreen.this.finish();
            }
        }, 2000);
    }
}
