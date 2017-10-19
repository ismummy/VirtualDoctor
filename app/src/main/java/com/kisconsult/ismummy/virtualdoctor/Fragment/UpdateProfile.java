package com.kisconsult.ismummy.virtualdoctor.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kisconsult.ismummy.virtualdoctor.Activity.Patient;
import com.kisconsult.ismummy.virtualdoctor.App.EndPoints;
import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;
import com.kisconsult.ismummy.virtualdoctor.Helper.Image;
import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfile extends Fragment implements View.OnClickListener {

    private AutoCompleteTextView email, password, age, height, mobile;
    private TextInputLayout mail, pass, lAge, lHeight, phone;
    private CircleImageView image;
    private ImageButton camera;
    private Button register;
    private String profile;
    final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
    ProgressBar bar;

    public UpdateProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComp();
    }

    private void initComp() {
        profile = "";
        email = (AutoCompleteTextView) getActivity().findViewById(R.id.email);
        password = (AutoCompleteTextView) getActivity().findViewById(R.id.password);
        age = (AutoCompleteTextView) getActivity().findViewById(R.id.age);
        height = (AutoCompleteTextView) getActivity().findViewById(R.id.height);
        image = (CircleImageView) getActivity().findViewById(R.id.profile_pix);
        camera = (ImageButton) getActivity().findViewById(R.id.camera);
        register = (Button) getActivity().findViewById(R.id.register);
        mobile = (AutoCompleteTextView) getActivity().findViewById(R.id.phone);
        phone = (TextInputLayout) getActivity().findViewById(R.id.layout_phone);

        User user = MyApplication.getInstance().getPref().getUser();
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        age.setText(user.getAge() + "");
        height.setText(user.getHeight() + "");
        mobile.setText(user.getMobile());
        new Image(image, user.getImage());

        mail = (TextInputLayout) getActivity().findViewById(R.id.layout_email);
        pass = (TextInputLayout) getActivity().findViewById(R.id.layout_password);
        lAge = (TextInputLayout) getActivity().findViewById(R.id.layout_age);
        lHeight = (TextInputLayout) getActivity().findViewById(R.id.layout_height);

        email.addTextChangedListener(new MyTextWatcher(email));
        password.addTextChangedListener(new MyTextWatcher(password));
        age.addTextChangedListener(new MyTextWatcher(age));
        height.addTextChangedListener(new MyTextWatcher(height));
        mobile.addTextChangedListener(new MyTextWatcher(mobile));
        camera.setOnClickListener(this);
        register.setOnClickListener(this);

        bar = (ProgressBar) getActivity().findViewById(R.id.bar);
    }

    private void update() {
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
        if (!validatePhone()) {
            return;
        }

        final String mail, pass, h, a, phone;

        pass = password.getText().toString().trim().toLowerCase();
        mail = email.getText().toString().trim();
        h = height.getText().toString().trim();
        a = age.getText().toString().trim();
        phone = mobile.getText().toString().trim();

        bar.setVisibility(View.VISIBLE);
        String url = EndPoints.UPDATE_PROFILE.replace("_ID_", MyApplication.getInstance().getPref().getUser().getUsername());
        StringRequest strReq = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                bar.setVisibility(View.GONE);
                Log.e("Login", response);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        User user = MyApplication.getInstance().getPref().getUser();
                        user.setEmail(mail);
                        user.setAge(Integer.parseInt(a));
                        user.setPassword(pass);
                        user.setHeight(Double.parseDouble(h));
                        user.setMobile(phone);
                        MyApplication.getInstance().getPref().saveUser(user);
                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "" + obj.getString("message").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bar.setVisibility(View.GONE);
                Log.e("Login", "Error-> " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getActivity().getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", profile);
                params.put("email", mail);
                params.put("password", pass);
                params.put("phone", phone);
                params.put("age", a);
                params.put("height", h);

                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    private void camera() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Add Profile Photo!");
        alert.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(intent, 105);

                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"), 100);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        alert.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bitmap bm = null;
            switch (requestCode) {
                case 100:
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
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


    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            pass.setError(getActivity().getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            pass.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAge() {
        if (age.getText().toString().trim().isEmpty()) {
            lAge.setError(getActivity().getString(R.string.err_msg_age));
            requestFocus(age);
            return false;
        } else {
            lAge.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (mobile.getText().toString().trim().isEmpty()) {
            phone.setError(getActivity().getString(R.string.err_msg_mobile));
            requestFocus(mobile);
            return false;
        } else {
            phone.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateHeight() {
        if (height.getText().toString().trim().isEmpty()) {
            lHeight.setError(getActivity().getString(R.string.err_msg_height));
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
            mail.setError(getActivity().getString(R.string.err_msg_email));
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
                    update();
                    break;
            }
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
