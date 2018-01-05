package com.kola.mytodo;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.kola.mytodo.Fragment.AddFragment;
import com.kola.mytodo.Fragment.CompletedFragment;
import com.kola.mytodo.Fragment.DeleteFragment;
import com.kola.mytodo.Fragment.TodoFragment;
import com.kola.mytodo.other.CircleTransform;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String name, email;
    boolean hasParent;
    TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppDatabase appDatabase = Room.databaseBuilder(this, AppDatabase.class, TaskDb.DATABASE).build();
        taskDao = appDatabase.taskDao();

        Bundle b = getIntent().getExtras();

        name = b.getString("name");
        email= b.getString("email");

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.activity_drawer_frame, new AddFragment());
                fragmentTransaction.commit();

                toggleFab(0);

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DrawerActivity.this, "dgsd", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView profile_image = navHeader.findViewById(R.id.profile_imv);

        File imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + "myToDo/", email+".jpg");

        if (imageFile.exists()){

            Glide.with(this).load(imageFile)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_image);

        }
        else{
            Glide.with(this).load(R.drawable.ic_person_white_48dp)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile_image);
        }

        TextView name_tv = navHeader.findViewById(R.id.name_tv);
        name_tv.setText(name);
        TextView email_tv = navHeader.findViewById(R.id.email_tv);
        email_tv.setText(email);

        setDefaultFragment();

    }

    private void setDefaultFragment() {

        hasParent = true;
       showFragment(new TodoFragment());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            this.finish();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.delete){

            deleteAll();

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteAll() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                taskDao.deleteAll("");

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                setDefaultFragment();

                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.Fragment fragment = null;

        if (id == R.id.nav_mytodo) {
            fragment = new TodoFragment();

        } else if (id == R.id.nav_completed_task) {

            fragment = new CompletedFragment();

        } else if (id == R.id.nav_deleted_task) {

            fragment = new DeleteFragment();

        } else if (id == R.id.nav_share) {

            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_log_out) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(getApplicationContext(), AuthActivity.class));
        }

        if (fragment != null) showFragment(fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(android.support.v4.app.Fragment fragment) {

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.activity_drawer_frame, fragment);
        fragmentTransaction.commit();

        toggleFab(1);

    }

    void toggleFab(int i) {

//        CoordinatorLayout cl = findViewById(R.id.cl);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        if (!hasParent && i == 1) {
//            cl.addView(fab);
//            hasParent = true;
//        }
//        if (hasParent && i == 0){
//            cl.removeView(fab);
//            hasParent = false;
//        }
    }

}
