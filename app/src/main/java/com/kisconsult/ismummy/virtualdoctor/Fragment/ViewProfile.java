package com.kisconsult.ismummy.virtualdoctor.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kisconsult.ismummy.virtualdoctor.Helper.Image;
import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfile extends Fragment {

    private User user;
    private TextView fname, uname, mobile, email, age, height, gender, bgroup, department;
    private CircleImageView image;

    public ViewProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        user = (User) bundle.getSerializable("data");
        initComp();
    }

    private void initComp() {
        fname = (TextView) getActivity().findViewById(R.id.fullname);
        uname = (TextView) getActivity().findViewById(R.id.username);
        mobile = (TextView) getActivity().findViewById(R.id.mobile);
        email = (TextView) getActivity().findViewById(R.id.email);
        age = (TextView) getActivity().findViewById(R.id.age);
        height = (TextView) getActivity().findViewById(R.id.height);
        gender = (TextView) getActivity().findViewById(R.id.gender);
        bgroup = (TextView) getActivity().findViewById(R.id.bgroup);
        department = (TextView) getActivity().findViewById(R.id.department);
        image = (CircleImageView) getActivity().findViewById(R.id.profile_pix);

        fname.setText(user.getFullname());
        uname.setText(user.getUsername());
        mobile.setText(user.getMobile());
        email.setText(user.getEmail());
        age.setText(user.getAge()+"");
        height.setText(user.getHeight() + "");
        gender.setText(user.getGender());
        bgroup.setText(user.getBgroup());
        department.setText(user.getDepartment());
        new Image(image, user.getImage());
    }
}
