package com.example.oleksandr.discount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oleksandr.discount.R;
import com.example.oleksandr.discount.db.User;
import com.example.oleksandr.discount.db.UserDatabase;
import com.example.oleksandr.discount.utils.MaskUtils;
import com.example.oleksandr.discount.utils.Utils;

import java.util.Random;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.oleksandr.discount.utils.Keys.NUMBER;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{


    private EditText etPassword;
    private EditText etNumber;
    private TextView tvPassword;
    private TextView tvConfirm;
    private UserDatabase db;
    private int passwordSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendPasswordSms();
        setContentView(R.layout.activity_sign_up);
        etNumber = findViewById(R.id.et_number);
        MaskUtils maskUtils = new MaskUtils();
        maskUtils.blockEditText(etNumber);
        etNumber.setText(getIntent().getStringExtra(NUMBER));
        db = UserDatabase.getPhoneDatabase(this);

        etPassword = findViewById(R.id.et_password);
        tvConfirm = findViewById(R.id.text_confirm);
        tvPassword = findViewById(R.id.text_password);

        findViewById(R.id.image_back).setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        etPassword.setOnKeyListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_confirm:
                pressBtnConfirm();
                break;
            case R.id.image_back:
                finish();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN)
            return false;
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            pressBtnConfirm();
            return true;
        }
        return false;
    }

    private void sendPasswordSms(){
        Random r = new Random();
        passwordSms = r.nextInt(10000 - 1000 + 1) + 1000;
        Utils.ShowNotification(this, "" + passwordSms);
    }

    private void pressBtnConfirm() {
        if (tvConfirm.getText().toString().equals(getString(R.string.sign_up_confirm))) {
            if (etPassword.getText().toString().equals(passwordSms + "")  ) {
                Utils.cancelNotification(this);
                showHideComponents();
            } else {
                etPassword.setText("");
                Utils.showAlert(this, "Пароль из SMS введен не правельно");
            }
        } else {
            if (etPassword.getText().length() < 6) {
                Utils.showToast(this, "Пароль слишком короткий");
            } else {

                Completable.fromAction(() -> db.phoneDao()
                        .insert(new User(MaskUtils.getNumber(), etPassword.getText().toString())))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(NUMBER, MaskUtils.getNumber());
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }
                        });
            }
        }


    }

    private void showHideComponents() {
        etPassword.setText("");
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        tvPassword.setText(getString(R.string.sign_up_create_password));
        tvPassword.setMinLines(2);
        tvConfirm.setText(getString(R.string.sign_up_ok));
        findViewById(R.id.image_back).setVisibility(View.GONE);
    }



}
