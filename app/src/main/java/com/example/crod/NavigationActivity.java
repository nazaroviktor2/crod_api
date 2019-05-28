package com.example.crod;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.View;


import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.firebase.auth.FirebaseAuth;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;



public class NavigationActivity extends AppCompatActivity {
    SharedPreferences nPref;
    SharedPreferences snPref;
    Drawer drawer;
    String Name_Surname;
    FirebaseAuth user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadText();
        Log.e("Test",Name_Surname);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity)this;
        if (activity != null) {
            setSupportActionBar(toolbar);

        }
        IProfile profile = new ProfileDrawerItem()
        .withName(Name_Surname)

                .withIcon(R.drawable.account_icon);


        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header3)
                .addProfiles(profile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_container, new AccountFragment()).commit();
                        drawer.closeDrawer();
                        return false;
                    }
                })
                .withOnlyMainProfileImageVisible(false)
              .withSelectionListEnabled(false)
                .build();


        drawer = new DrawerBuilder(this)

               .withRootView(R.id.drawer_container)
               .withToolbar(toolbar)
               .withDisplayBelowStatusBar(false)
               .withActionBarDrawerToggleAnimated(true)
               .withAccountHeader(header)
               .addDrawerItems(
                       new PrimaryDrawerItem().withName(getString(R.string.menu_news)).withIcon(R.drawable.home_icon),
                       new PrimaryDrawerItem().withName(getString(R.string.menu_tour)).withIcon(R.drawable.assignment),
                       new PrimaryDrawerItem().withName(getString(R.string.How_was_your_day)).withIcon(R.drawable.assessmetn_icon),

                       new SecondaryDrawerItem().withName(getString(R.string.exit)).withIcon(R.drawable.exit_icon)


               )

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    SelectFragment(position);
                        return false;
                    }
                })
               .withSavedInstance(savedInstanceState)
                .build();
        SelectFragment(1);
    }



    void loadText() {
        nPref = getSharedPreferences(getString(R.string.sp_name),MODE_PRIVATE);
        snPref = getSharedPreferences(getString(R.string.sp_surname),MODE_PRIVATE);
        String name = nPref.getString(getString(R.string.sp_name), "");
        String surname = snPref.getString(getString(R.string.sp_surname), "");
         Name_Surname = name+" "+surname;
        Log.e("TestL",Name_Surname);
    }
    @Override
    public void onBackPressed() {
        if (drawer!=null&&drawer.isDrawerOpen()){
            drawer.closeDrawer();
        }else
            super.onBackPressed();
    }
    void SelectFragment(int position){
        Log.e("SetsPosition","pos= "+position);
        Fragment fragment = null;
        switch (position){
            case 1:
                fragment = new NewsFragment();
                break;
            case 2:
                fragment = new TourFragment();
                break;
            case 3:
                fragment = new AssessentFragment();
                break;
            case 4:
                user = FirebaseAuth.getInstance();
                user.signOut();
                user = null;
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        if(fragment!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container,fragment).commit();
        }

        drawer.closeDrawer();
    }
}

