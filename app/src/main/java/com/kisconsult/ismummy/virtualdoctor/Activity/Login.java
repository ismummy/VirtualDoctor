package com.kisconsult.ismummy.virtualdoctor.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kisconsult.ismummy.virtualdoctor.App.EndPoints;
import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;
import com.kisconsult.ismummy.virtualdoctor.Fragment.ViewUsers;
import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private AutoCompleteTextView username, password;
    private TextInputLayout uname, pass;
    private Button login;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComp();
    }

    private void initComp() {
        bar = (ProgressBar) findViewById(R.id.bar);
        register = (TextView) findViewById(R.id.register);
        username = (AutoCompleteTextView) findViewById(R.id.username);
        password = (AutoCompleteTextView) findViewById(R.id.password);
        uname = (TextInputLayout) findViewById(R.id.layout_username);
        pass = (TextInputLayout) findViewById(R.id.layout_password);
        login = (Button) findViewById(R.id.login);

        username.addTextChangedListener(new MyTextWatcher(username));
        password.addTextChangedListener(new MyTextWatcher(password));

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void register() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    private void login() {
        if (!validateUsername()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }

        final String uname, pass;
        uname = username.getText().toString().trim().toLowerCase();
        pass = password.getText().toString().trim().toLowerCase();

        bar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.USER_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                bar.setVisibility(View.GONE);
                Log.e("Login", response);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {

                        JSONObject users = obj.getJSONObject("user");

                        User user = new User();
                        user.setFullname(users.getString("fullname"));
                        user.setMobile(users.getString("phone"));
                        user.setImage(users.getString("image"));
                        user.setUsername(users.getString("username"));
                        user.setAge(users.getInt("age"));
                        user.setHeight(users.getDouble("height"));
                        user.setEmail(users.getString("email"));
                        user.setCategory(users.getString("category"));
                        user.setBgroup(users.getString("bgroup"));
                        user.setPassword(users.getString("password"));
                        user.setGender(users.getString("gender"));
                        user.setDepartment(users.getString("department"));

                        MyApplication.getInstance().getPref().saveUser(user);
                        if (user.getCategory().equalsIgnoreCase("patient")) {
                            Intent intent = new Intent(Login.this, Patient.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(Login.this, Admin.class);
                            startActivity(intent);
                        }
                        Login.this.finish();
                    } else {
                        // login error - simply toast the message
                        Toast.makeText(Login.this, "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(Login.this, "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bar.setVisibility(View.GONE);
                Log.e("Login", "Error-> " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(Login.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", uname);
                params.put("password", pass);

                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private boolean validateUsername() {
        if (username.getText().toString().trim().isEmpty()) {
            uname.setError(getString(R.string.err_msg_username));
            requestFocus(username);
            return false;
        } else {
            uname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            pass.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            pass.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (!bar.isShown()) {
            switch (id) {
                case R.id.register:
                    register();
                    break;
                case R.id.login:
                    login();
                    break;
            }
        }
    }

    private class MyTextWatcher implements TextWatcher {
        View view;

        public MyTextWatcher(View v) {
            view = v;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (view.getId()) {
                case R.id.username:
                    validateUsername();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }
}
