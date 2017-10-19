package com.kisconsult.ismummy.virtualdoctor.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kisconsult.ismummy.virtualdoctor.R;

public class Register extends AppCompatActivity {

    com.kisconsult.ismummy.virtualdoctor.Helper.Register register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_doctor);
        register = new com.kisconsult.ismummy.virtualdoctor.Helper.Register(this, "patient");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        register.onActivityResult(requestCode, resultCode, data);
    }
}
