package com.kisconsult.ismummy.virtualdoctor.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendDiagnose extends Fragment implements View.OnClickListener {

    private AutoCompleteTextView complain, date, drug;
    private Spinner dept;
    Button send;
    TextInputLayout comp, dat, drg;
    private ProgressBar bar;

    public SendDiagnose() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_diagnose, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComp();
    }

    private void initComp() {
        complain = (AutoCompleteTextView) getActivity().findViewById(R.id.complain);
        date = (AutoCompleteTextView) getActivity().findViewById(R.id.date);
        drug = (AutoCompleteTextView) getActivity().findViewById(R.id.drug);
        dept = (Spinner) getActivity().findViewById(R.id.department);
        send = (Button) getActivity().findViewById(R.id.send);
        comp = (TextInputLayout) getActivity().findViewById(R.id.layout_complain);
        dat = (TextInputLayout) getActivity().findViewById(R.id.layout_date);
        drg = (TextInputLayout) getActivity().findViewById(R.id.layout_drug);
        bar = (ProgressBar) getActivity().findViewById(R.id.bar);

        complain.addTextChangedListener(new MyTextWatcher(complain));
        date.addTextChangedListener(new MyTextWatcher(date));
        drug.addTextChangedListener(new MyTextWatcher(drug));

        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (!bar.isShown()) {
            switch (id) {
                case R.id.send:
                    send();
                    break;
            }
        }
    }

    private boolean validateDepartment() {
        if (dept.getSelectedItemPosition() <= 0) {
            Toast.makeText(getActivity(), "Select Specialization", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateComplain() {
        if (complain.getText().toString().trim().isEmpty()) {
            comp.setError(getString(R.string.err_msg_complain));
            requestFocus(complain);
            return false;
        } else {
            comp.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDate() {
        if (date.getText().toString().trim().isEmpty()) {
            dat.setError(getString(R.string.err_msg_date));
            requestFocus(date);
            return false;
        } else {
            dat.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDrug() {
        if (drug.getText().toString().trim().isEmpty()) {
            drg.setError(getString(R.string.err_msg_drug));
            requestFocus(drug);
            return false;
        } else {
            drg.setErrorEnabled(false);
        }

        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                case R.id.complain:
                    validateComplain();
                    break;
                case R.id.date:
                    validateDate();
                    break;
                case R.id.drug:
                    validateDrug();
                    break;
            }
        }
    }

    private void send() {
        if (!validateDepartment()) {
            return;
        }
        if (!validateComplain()) {
            return;
        }
        if (!validateDrug()) {
            return;
        }
        if (!validateDate()) {
            return;
        }

        final String dat, comp, drg, dept, uname;

        dat = date.getText().toString().trim();
        comp = complain.getText().toString().trim();
        drg = drug.getText().toString().trim();
        dept = this.dept.getSelectedItem().toString();
        uname = MyApplication.getInstance().getPref().getUser().getUsername();

        bar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.ADD_COMPLAIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                bar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {

                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PatientHome()).commit();
                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                bar.setVisibility(View.GONE);
                Log.e("Login", "Error-> " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", uname);
                params.put("complain", comp);
                params.put("drug", drg);
                params.put("date", dat);
                params.put("department", dept);
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);

    }
}
