package com.kisconsult.ismummy.virtualdoctor.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kisconsult.ismummy.virtualdoctor.Helper.Date;
import com.kisconsult.ismummy.virtualdoctor.Helper.Image;
import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewDiagnose extends Fragment {
    private com.kisconsult.ismummy.virtualdoctor.Model.Diagnosis diagnose;
    TextView fullname, mobile, date, complain, drug, response;
    CircleImageView image;
    LinearLayout layout;

    public ViewDiagnose() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_diagnose, container, false);
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
        complain = (TextView) getActivity().findViewById(R.id.complain);
        drug = (TextView) getActivity().findViewById(R.id.drug);
        response = (TextView) getActivity().findViewById(R.id.response);
        image = (CircleImageView) getActivity().findViewById(R.id.profile_pix);

        layout = (LinearLayout) getActivity().findViewById(R.id.doctor);

        if (diagnose.getResponse().equalsIgnoreCase("")) {
            layout.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "No Response Yet", Toast.LENGTH_LONG).show();
        } else {
            layout.setVisibility(View.VISIBLE);
            User user = diagnose.getUser();
            fullname.setText(user.getFullname());
            mobile.setText(user.getMobile());
            new Image(image, user.getImage());
        }
        date.setText(diagnose.getDate());
        complain.setText(diagnose.getComplain());
        drug.setText(diagnose.getDrug());
        response.setText(diagnose.getResponse());
    }
}
