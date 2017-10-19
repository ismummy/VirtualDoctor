package com.kisconsult.ismummy.virtualdoctor.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kisconsult.ismummy.virtualdoctor.Adapter.UserAdapter;
import com.kisconsult.ismummy.virtualdoctor.App.EndPoints;
import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;
import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUsers extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    ArrayList<User> arrayList;
    UserAdapter adapter;
    private ListView listView;
    private SwipeRefreshLayout refresh;
    private String category;

    public ViewUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_users, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initComp();
    }

    private void initComp() {
        Bundle bundle = getArguments();
        category = bundle.getString("category");

        arrayList = new ArrayList<>();
        adapter = new UserAdapter(getActivity(), arrayList);

        listView = (ListView) getActivity().findViewById(R.id.list);
        refresh = (SwipeRefreshLayout) getActivity().findViewById(R.id.refresh);

        listView.setAdapter(adapter);
        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
                getUser();
            }
        });
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        getUser();
    }

    private void getUser() {
        String url = EndPoints.GET_USERS.replace("_ID_", category);


        refresh.setRefreshing(true);
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                refresh.setRefreshing(false);
                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {

                        JSONArray array = obj.getJSONArray("user");
                        arrayList.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject users = (JSONObject) array.get(i);
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
                            arrayList.add(user);
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
        ViewProfile frag = new ViewProfile();
        frag.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(frag.getTag()).replace(R.id.fragment_container, frag).commit();
    }
}
