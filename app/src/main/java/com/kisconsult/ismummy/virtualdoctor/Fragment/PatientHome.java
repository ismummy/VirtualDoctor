package com.kisconsult.ismummy.virtualdoctor.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kisconsult.ismummy.virtualdoctor.Adapter.DiagnosisAdapter;
import com.kisconsult.ismummy.virtualdoctor.Adapter.PatientHomeAdapter;
import com.kisconsult.ismummy.virtualdoctor.App.EndPoints;
import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;
import com.kisconsult.ismummy.virtualdoctor.Model.*;
import com.kisconsult.ismummy.virtualdoctor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    PatientHomeAdapter adapter;
    ArrayList<com.kisconsult.ismummy.virtualdoctor.Model.Diagnosis> arrayList;
    SwipeRefreshLayout refresh;
    ListView listView;
    FloatingActionButton fab;

    public PatientHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnosis, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComp();
    }

    private void initComp() {
        arrayList = new ArrayList<>();
        adapter = new PatientHomeAdapter(getActivity(), arrayList);

        listView = (ListView) getActivity().findViewById(R.id.list);
        refresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh);

        listView.setAdapter(adapter);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
                getDiagnose();
            }
        });
        listView.setOnItemClickListener(this);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SendDiagnose()).commit();
            }
        });
    }

    @Override
    public void onRefresh() {
        getDiagnose();
    }

    private void getDiagnose() {
        final String url = EndPoints.GET_USER_COMPLAIN.replace("_ID_", MyApplication.getInstance().getPref().getUser().getUsername());
        refresh.setRefreshing(true);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                refresh.setRefreshing(false);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {

                        JSONArray array = obj.getJSONArray("diagnose");
                        arrayList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject diagnose = (JSONObject) array.get(i);
                            com.kisconsult.ismummy.virtualdoctor.Model.Diagnosis diagnosis = new com.kisconsult.ismummy.virtualdoctor.Model.Diagnosis();
                            diagnosis.setDate(diagnose.getString("date"));
                            diagnosis.setComplain(diagnose.getString("complain"));
                            diagnosis.setTime(diagnose.getString("time"));
                            diagnosis.setDrug(diagnose.getString("drug"));
                            diagnosis.setId(diagnose.getInt("id"));
                            diagnosis.setResponse(diagnose.getString("response"));

                            if (!diagnose.getString("response").equalsIgnoreCase("")) {
                                JSONObject users = diagnose.getJSONObject("doctor");
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

                                diagnosis.setUser(user);
                            }

                            arrayList.add(diagnosis);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getActivity(), "" + obj.getString("message").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refresh.setRefreshing(false);
                Log.e("Login", "Error-> " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", arrayList.get(position));
        ViewDiagnose frag = new ViewDiagnose();
        frag.setArguments(bundle);
        frag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(frag.getTag()).replace(R.id.fragment_container, frag).commit();
    }
}
