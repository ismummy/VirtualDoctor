package com.kisconsult.ismummy.virtualdoctor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;
import com.kisconsult.ismummy.virtualdoctor.Helper.Date;
import com.kisconsult.ismummy.virtualdoctor.Helper.Image;
import com.kisconsult.ismummy.virtualdoctor.Model.Diagnosis;
import com.kisconsult.ismummy.virtualdoctor.R;
import com.kisconsult.ismummy.virtualdoctor.App.Config;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ISMUMMY on 6/9/2016.
 */
public class PatientHomeAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<Diagnosis> data;

    public PatientHomeAdapter(Context context, ArrayList<Diagnosis> data) {
        super(context, Config.DIAGNOSIS_RESOURCES);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(Config.DIAGNOSIS_RESOURCES, parent, false);
        TextView fullname, mobile, date;
        CircleImageView imageView;

        fullname = (TextView) row.findViewById(R.id.fullname);
        mobile = (TextView) row.findViewById(R.id.mobile);
        date = (TextView) row.findViewById(R.id.date);
        imageView = (CircleImageView) row.findViewById(R.id.profile_pix);

        fullname.setText(data.get(position).getComplain());
        mobile.setText(data.get(position).getDate());
        date.setText(new Date(data.get(position).getTime()).getTimeAgo());
        new Image(imageView, MyApplication.getInstance().getPref().getUser().getImage());

        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
