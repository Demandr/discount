package com.example.oleksandr.discount.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.oleksandr.discount.DatePickerFragment;
import com.example.oleksandr.discount.R;
import com.example.oleksandr.discount.activities.login.LoginActivity;

import java.io.IOException;

import static com.example.oleksandr.discount.utils.Keys.DATE_PICKER;
import static com.example.oleksandr.discount.utils.Keys.NUMBER;

public class UserProfileActivity extends DrawerActivity
        implements  View.OnClickListener,
        DatePickerFragment.OnCompleteListener {

    private ImageView imUser;
    private TextView tvDate;

    private static final int PICK_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        TextView textIdNumber = findViewById(R.id.text_id_number);
        textIdNumber.setText("ID" + getIntent().getStringExtra(NUMBER));

        AutoCompleteTextView textRegion = findViewById(R.id.text_region);
        textRegion.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.region_array)));

        tvDate = findViewById(R.id.text_date);
        tvDate.setOnClickListener(this);
        imUser = findViewById(R.id.image_user);
        imUser.setOnClickListener(this);
        Spinner spinner = findViewById(R.id.text_sex);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                R.layout.spiner_item,
                getResources().getStringArray(R.array.sex_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        findViewById(R.id.text_exit_account).setOnClickListener(this);
        initDrawerToolbar("МОЙ ПРОФАЙЛ");
        createDrawer();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_user:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_PHOTO);
                break;
            case R.id.text_exit_account:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.text_date:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_PICKER);
                break;
        }
    }

    @Override
    public void onComplete(String time) {
        tvDate.setText(time);
    }
}
