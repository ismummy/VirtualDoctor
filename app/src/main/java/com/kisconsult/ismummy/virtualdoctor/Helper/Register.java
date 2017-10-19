package com.kisconsult.ismummy.virtualdoctor.Helper;

import android.app.Activity;
import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kisconsult.ismummy.virtualdoctor.Activity.Admin;
import com.kisconsult.ismummy.virtualdoctor.Activity.Patient;
import com.kisconsult.ismummy.virtualdoctor.App.EndPoints;
import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;
import com.kisconsult.ismummy.virtualdoctor.Fragment.ViewUsers;
import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ISMUMMY on 6/14/2016.
 */
public class Register implements View.OnClickListener {
    private AppCompatActivity context;
    private String category;
    private AutoCompleteTextView fullname, email, username, password, age, height, mobile;
    private TextInputLayout fname, mail, uname, pass, lAge, lHeight, phone;
    private Spinner gender, department, bgroup;
    private CircleImageView image;
    private ImageButton camera;
    private Button register;
    private String profile;
    final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
    ProgressBar bar;


    public Register(AppCompatActivity context, String category) {
        this.context = context;
        this.category = category;
        initComp();
    }

    private void initComp() {
        fullname = (AutoCompleteTextView) context.findViewById(R.id.fullname);
        email = (AutoCompleteTextView) context.findViewById(R.id.email);
        username = (AutoCompleteTextView) context.findViewById(R.id.username);
        password = (AutoCompleteTextView) context.findViewById(R.id.password);
        age = (AutoCompleteTextView) context.findViewById(R.id.age);
        height = (AutoCompleteTextView) context.findViewById(R.id.height);
        gender = (Spinner) context.findViewById(R.id.gender);
        department = (Spinner) context.findViewById(R.id.department);
        bgroup = (Spinner) context.findViewById(R.id.bgroup);
        image = (CircleImageView) context.findViewById(R.id.profile_pix);
        camera = (ImageButton) context.findViewById(R.id.camera);
        register = (Button) context.findViewById(R.id.register);
        mobile = (AutoCompleteTextView) context.findViewById(R.id.phone);
        phone = (TextInputLayout) context.findViewById(R.id.layout_phone);

        fname = (TextInputLayout) context.findViewById(R.id.layout_fullname);
        mail = (TextInputLayout) context.findViewById(R.id.layout_email);
        uname = (TextInputLayout) context.findViewById(R.id.layout_username);
        pass = (TextInputLayout) context.findViewById(R.id.layout_password);
        lAge = (TextInputLayout) context.findViewById(R.id.layout_age);
        lHeight = (TextInputLayout) context.findViewById(R.id.layout_height);

        fullname.addTextChangedListener(new MyTextWatcher(fullname));
        email.addTextChangedListener(new MyTextWatcher(email));
        username.addTextChangedListener(new MyTextWatcher(username));
        password.addTextChangedListener(new MyTextWatcher(password));
        age.addTextChangedListener(new MyTextWatcher(age));
        height.addTextChangedListener(new MyTextWatcher(height));
        mobile.addTextChangedListener(new MyTextWatcher(mobile));

        if (category.equalsIgnoreCase("patient"))
            department.setVisibility(View.GONE);

        //get default image and decode it
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile);
        profile = getImage(bm);

        camera.setOnClickListener(this);
        register.setOnClickListener(this);

        bar = (ProgressBar) context.findViewById(R.id.bar);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (!bar.isShown()) {
            switch (id) {
                case R.id.camera:
                    camera();
                    break;
                case R.id.register:
                    register();
                    break;
            }
        }
    }

    private void register() {
        if (!validateFullname()) {
            return;
        }
        if (!validateUsername()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validateHeight()) {
            return;
        }
        if (!validateAge()) {
            return;
        }
        if (!validateBGroup()) {
            return;
        }
        if (!validateGender()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        if (!category.equalsIgnoreCase("patient")) {
            if (!validateDepartment()) {
                return;
            }
        }

        final String fname, uname, mail, pass, h, a, sex, blood, dept, phone;

        fname = fullname.getText().toString().trim();
        uname = username.getText().toString().trim().toLowerCase();
        pass = password.getText().toString().trim().toLowerCase();
        mail = email.getText().toString().trim();
        h = height.getText().toString().trim();
        a = age.getText().toString().trim();
        sex = gender.getSelectedItem().toString();
        blood = bgroup.getSelectedItem().toString();
        phone = mobile.getText().toString().trim();
        dept = category.equalsIgnoreCase("patient") ? "" : department.getSelectedItem().toString();

        bar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.USER_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                bar.setVisibility(View.GONE);
                Log.e("Login", response);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {

                        User user = new User();
                        user.setFullname(fname);
                        user.setMobile(phone);
                        user.setImage("images/" + uname + ".JPEG");
                        user.setUsername(uname);
                        user.setAge(Integer.parseInt(a));
                        user.setHeight(Double.parseDouble(h));
                        user.setEmail(mail);
                        user.setCategory(category);
                        user.setBgroup(blood);
                        user.setPassword(pass);
                        user.setGender(sex);
                        user.setDepartment(dept);

                        if (category.equalsIgnoreCase("patient")) {
                            MyApplication.getInstance().getPref().saveUser(user);
                            Intent intent = new Intent(context, Patient.class);
                            context.startActivity(intent);
                            context.finish();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("category", "doctor");
                            ViewUsers frag = new ViewUsers();
                            frag.setArguments(bundle);
                            context.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, frag).commit();
                        }
                    } else {
                        // login error - simply toast the message
                        Toast.makeText(context.getApplicationContext(), "" + obj.getString("message").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(context.getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bar.setVisibility(View.GONE);
                Log.e("Login", "Error-> " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(context.getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", fname);
                params.put("image", profile);
                params.put("username", uname);
                params.put("email", mail);
                params.put("password", pass);
                params.put("phone", phone);
                params.put("department", dept);
                params.put("age", a);
                params.put("height", h);
                params.put("gender", sex);
                params.put("bgroup", blood);
                params.put("category", category);

                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    private void camera() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Add Profile Photo!");
        alert.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    context.startActivityForResult(intent, 105);

                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    context.startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"), 100);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == context.RESULT_OK) {
            Bitmap bm = null;
            switch (requestCode) {
                case 100:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = context.getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    bm = BitmapFactory.decodeFile(picturePath);
                    break;
                case 105:
                    bm = (Bitmap) data.getExtras().get("data");
                    break;
            }
            image.setImageBitmap(bm);
            profile = getImage(bm);
        }
    }

    private String getImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private boolean validateFullname() {
        if (fullname.getText().toString().trim().isEmpty()) {
            fname.setError(context.getString(R.string.err_msg_fullname));
            requestFocus(fullname);
            return false;
        } else {
            fname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateUsername() {
        if (username.getText().toString().trim().isEmpty()) {
            uname.setError(context.getString(R.string.err_msg_username));
            requestFocus(username);
            return false;
        } else {
            uname.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            pass.setError(context.getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            pass.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDepartment() {
        if (department.getSelectedItemPosition() <= 0) {
            Toast.makeText(context, "Select your department", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateGender() {
        if (gender.getSelectedItemPosition() <= 0) {
            Toast.makeText(context, "Select your Gender", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateBGroup() {
        if (bgroup.getSelectedItemPosition() <= 0) {
            Toast.makeText(context, "Select your Blood Group", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateAge() {
        if (age.getText().toString().trim().isEmpty()) {
            lAge.setError(context.getString(R.string.err_msg_age));
            requestFocus(age);
            return false;
        } else {
            lAge.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (mobile.getText().toString().trim().isEmpty()) {
            phone.setError(context.getString(R.string.err_msg_mobile));
            requestFocus(mobile);
            return false;
        } else {
            phone.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateHeight() {
        if (height.getText().toString().trim().isEmpty()) {
            lHeight.setError(context.getString(R.string.err_msg_height));
            requestFocus(height);
            return false;
        } else {
            lHeight.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = this.email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            mail.setError(context.getString(R.string.err_msg_email));
            requestFocus(this.email);
            return false;
        } else {
            mail.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements android.text.TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
            this.view = view;
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
                case R.id.fullname:
                    validateFullname();
                    break;
                case R.id.username:
                    validateUsername();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.age:
                    validateAge();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
                case R.id.height:
                    validateHeight();
                    break;
                case R.id.phone:
                    validatePhone();
                    break;
            }
        }
    }
}
