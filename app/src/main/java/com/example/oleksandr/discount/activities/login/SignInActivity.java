package com.example.oleksandr.discount.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.oleksandr.discount.R;
import com.example.oleksandr.discount.activities.UserProfileActivity;
import com.example.oleksandr.discount.db.User;
import com.example.oleksandr.discount.db.UserDatabase;
import com.example.oleksandr.discount.utils.MaskUtils;
import com.example.oleksandr.discount.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

import static com.example.oleksandr.discount.utils.Keys.NUMBER;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private EditText etPassword;
    private UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        db = UserDatabase.getPhoneDatabase(this);
        EditText textNumber = findViewById(R.id.et_number);
        textNumber.setText(getIntent().getStringExtra(NUMBER));

        MaskUtils maskUtils = new MaskUtils();
        maskUtils.blockEditText(textNumber);

        etPassword = findViewById(R.id.et_password);
        etPassword.setOnKeyListener(this);
        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // TODO: 24.03.2018 add Scroll
            }
        });


        findViewById(R.id.text_forgot_password).setOnClickListener(this);
        findViewById(R.id.text_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_forgot_password:
                Utils.showAlert(this, "Ваш пароль отправлен SMS сообщениямна номер +" + MaskUtils.getNumber());
                findViewById(R.id.image_facebook).setVisibility(View.VISIBLE);
                findViewById(R.id.text_facebook).setVisibility(View.VISIBLE);
                break;
            case R.id.text_confirm:
                pressBtnConfirm();
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

    private void pressBtnConfirm() {
        db.phoneDao().getByNumberRx(MaskUtils.getNumber())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (etPassword.getText().toString().equals(user.getPassword())) {
//                            Utils.showAlert(SignInActivity.this, "В розробці");
                            Intent intent = new Intent(SignInActivity.this, UserProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(NUMBER, MaskUtils.getNumber());
                            startActivity(intent);
                            finish();
                        } else {
                            Utils.showToast(SignInActivity.this, "Неверный пароль");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
