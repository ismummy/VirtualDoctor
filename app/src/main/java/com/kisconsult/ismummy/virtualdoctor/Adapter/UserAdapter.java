package com.kisconsult.ismummy.virtualdoctor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kisconsult.ismummy.virtualdoctor.Model.User;
import com.kisconsult.ismummy.virtualdoctor.R;
import com.kisconsult.ismummy.virtualdoctor.App.Config;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ISMUMMY on 6/10/2016.
 */
public class UserAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<User> data;

    public UserAdapter(Context context, ArrayList<User> data) {
        super(context, Config.USER_RESOURCES);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(Config.USER_RESOURCES, parent, false);
        TextView fullname, username, mobile;
        CircleImageView imageView;

        fullname = (TextView) row.findViewById(R.id.fullname);
        username = (TextView) row.findViewById(R.id.username);
        mobile = (TextView) row.findViewById(R.id.mobile);
        imageView = (CircleImageView) row.findViewById(R.id.profile_pix);

        fullname.setText(data.get(position).getFullname());
        username.setText(data.get(position).getUsername());
        mobile.setText(data.get(position).getMobile());

        return row;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
