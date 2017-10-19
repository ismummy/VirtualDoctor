package com.kisconsult.ismummy.virtualdoctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kisconsult.ismummy.virtualdoctor.App.MyApplication;
import com.kisconsult.ismummy.virtualdoctor.Fragment.AddDoctor;
import com.kisconsult.ismummy.virtualdoctor.Fragment.Diagnosis;
import com.kisconsult.ismummy.virtualdoctor.Fragment.UpdateProfile;
import com.kisconsult.ismummy.virtualdoctor.Fragment.ViewUsers;
import com.kisconsult.ismummy.virtualdoctor.Helper.Image;
import com.kisconsult.ismummy.virtualdoctor.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_frame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //let set the initial content to diagnosis fragement
        Diagnosis frag = new Diagnosis();
        replaceFragment(frag);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.inflateMenu(R.menu.drawer_frame_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        CircleImageView imageView = (CircleImageView) header.findViewById(R.id.imageView);
        TextView fullname = (TextView) header.findViewById(R.id.fullname);
        TextView department = (TextView) header.findViewById(R.id.department);

        fullname.setText(MyApplication.getInstance().getPref().getUser().getFullname());
        department.setText(MyApplication.getInstance().getPref().getUser().getDepartment());
        new Image(imageView, MyApplication.getInstance().getPref().getUser().getImage());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //this method is used in add the menu to our page.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    //to respond to item click on the menu we use this menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            MyApplication.getInstance().getPref().clear();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //to respond to drawer item click we use this method
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Diagnosis fragment = new Diagnosis();
            replaceFragment(fragment);
            //ive replaced d content to diagnosis fragement.
        } else if (id == R.id.nav_add_doctor) {
            AddDoctor frag = new AddDoctor();
            replaceFragment(frag);
        } else if (id == R.id.nav_view_patient) {
            Bundle bundle = new Bundle();
            bundle.putString("category", "patient");

            //the bundle is used to set argument to send to d framgetnt.

            ViewUsers frag = new ViewUsers();
            frag.setArguments(bundle);

            replaceFragment(frag);
        } else if (id == R.id.nav_view_doctor) {
            Bundle bundle = new Bundle();
            bundle.putString("category", "doctor");

            //the bundle is used to set argument to send to d framgetnt.

            ViewUsers frag = new ViewUsers();
            frag.setArguments(bundle);

            replaceFragment(frag);
        } else if (id == R.id.nav_update_profile) {
            UpdateProfile frag = new UpdateProfile();
            replaceFragment(frag);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //all the four methods are generated auto. by the ide for choosing drawer layout as our root layout.

    private void replaceFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(fragment.getTag());
        transaction.replace(R.id.fragment_container, fragment).commit();

        //the first param, is what we wanna replace, whicj is d framelayout we include earlier
        //the secod param, is d fragement wwe wanna replace with.
    }
}
