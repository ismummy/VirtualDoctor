package com.kisconsult.ismummy.virtualdoctor.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
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
import com.kisconsult.ismummy.virtualdoctor.Helper.Date;
import com.kisconsult.ismummy.virtualdoctor.Helper.Image;
import com.kisconsult.ismummy.virtualdoctor.Model.*;
import com.kisconsult.ismummy.virtualdoctor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Diagnose extends Fragment implements View.OnClickListener {

    private com.kisconsult.ismummy.virtualdoctor.Model.Diagnosis diagnose;
    TextView fullname, mobile, date, time, complain, drug, response;
    CircleImageView image;
    AutoCompleteTextView dResponse;
    ImageButton send;
    ProgressBar bar;

    public Diagnose() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnose, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        diagnose = (com.kisconsult.ismummy.virtualdoctor.Model.Diagnosis) bundle.getSerializable("data");
        initComp();
    }

    private void initComp() {
        fullname = (TextView) getActivity().findViewById(R.id.fullname);
        mobile = (TextView) getActivity().findViewById(R.id.mobile);
        date = (TextView) getActivity().findViewById(R.id.date);
        time = (TextView) getActivity().findViewById(R.id.time);
        complain = (TextView) getActivity().findViewById(R.id.complain);
        drug = (TextView) getActivity().findViewById(R.id.drug);
        response = (TextView) getActivity().findViewById(R.id.response);
        image = (CircleImageView) getActivity().findViewById(R.id.profile_pix);
        dResponse = (AutoCompleteTextView) getActivity().findViewById(R.id.dResponse);
        send = (ImageButton) getActivity().findViewById(R.id.send);
        bar = (ProgressBar) getActivity().findViewById(R.id.bar);

        User user = diagnose.getUser();
        fullname.setText(user.getFullname());
        mobile.setText(user.getMobile());
        new Image(image, user.getImage());
        time.setText(new Date(diagnose.getTime()).getTimeAgo());
        date.setText(diagnose.getDate());
        complain.setText(diagnose.getComplain());
        drug.setText(diagnose.getDrug());
        response.setText(diagnose.getResponse());

        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!bar.isShown()) {
            if (dResponse.getText().toString().trim().isEmpty()) {
                Toast.makeText(getActivity(), "Response cannot be empty", Toast.LENGTH_LONG).show();
            } else {
                response.setText(dResponse.getText().toString());
                response();
            }
        }
    }

    private void response() {

        final String url = EndPoints.TREAT_COMPLAIN.replace("_ID_", diagnose.getId() + "");
        final String res, doctor;
        res = dResponse.getText().toString().trim();
        doctor = MyApplication.getInstance().getPref().getUser().getUsername();

        bar.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                bar.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Diagnosis()).commit();
                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getActivity(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("response", res);
                params.put("doctor", doctor);

                return params;
            }
        };
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
