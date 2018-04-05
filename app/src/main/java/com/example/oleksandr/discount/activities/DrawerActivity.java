package com.example.oleksandr.discount.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.oleksandr.discount.R;
import com.example.oleksandr.discount.utils.Utils;

public abstract class DrawerActivity extends AppCompatActivity  {
    protected Context context;
    private View.OnClickListener navClickListener;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private View.OnClickListener createNavClickListener(){
        return v -> {
            Class<?> nextActivityClass = null;
            switch (v.getId()){
                case R.id.nav_company:
                    break;
                case R.id.nav_news:
                    break;
                case R.id.nav_bonus:
                    break;
                case R.id.nav_chat:
                    break;
                case R.id.nav_contacts:
                    break;
                case R.id.nav_discount:
                    break;
                case R.id.nav_history_operation:
                    break;
                case R.id.nav_voucher:
                    break;
                case R.id.tv_qr_code:
                    break;
            }
            if (nextActivityClass != null){
                startActivity(new Intent(context, nextActivityClass));
            }else {
                Utils.showToast(context, "В розробці");
            }
        };
    }

    protected void createDrawer(){
//        loadDrawerData();
        navClickListener = createNavClickListener();
        initNavigationViews();
//        updateDrawerHeader();
    }

    protected void initDrawerToolbar(String title) {



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(title.toUpperCase());
    }

    private void initNavigationViews(){
        navigationView = findViewById(R.id.nav_view);

        navigationView.findViewById(R.id.nav_company).setOnClickListener(navClickListener);
        navigationView.findViewById(R.id.nav_news).setOnClickListener(navClickListener);
        navigationView.findViewById(R.id.nav_bonus).setOnClickListener(navClickListener);
        navigationView.findViewById(R.id.nav_chat).setOnClickListener(navClickListener);
        navigationView.findViewById(R.id.nav_contacts).setOnClickListener(navClickListener);
        navigationView.findViewById(R.id.nav_discount).setOnClickListener(navClickListener);
        navigationView.findViewById(R.id.nav_history_operation).setOnClickListener(navClickListener);
        navigationView.findViewById(R.id.nav_voucher).setOnClickListener(navClickListener);
        navigationView.findViewById(R.id.tv_qr_code).setOnClickListener(navClickListener);
    }
}
