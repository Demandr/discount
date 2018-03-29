package com.example.oleksandr.discount.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.oleksandr.discount.R;
import com.example.oleksandr.discount.db.User;
import com.example.oleksandr.discount.db.UserDatabase;
import com.example.oleksandr.discount.utils.MaskUtils;
import com.example.oleksandr.discount.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {

    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        UserDatabase db = UserDatabase.getPhoneDatabase(this);
        EditText textNumber = findViewById(R.id.etext_number);
        textNumber.setText(getIntent().getStringExtra(LoginActivity.EXTRA_NUMBER));

        MaskUtils maskUtils = new MaskUtils();
        maskUtils.blockEditText(textNumber);

        mPassword = findViewById(R.id.etext_password);
        mPassword.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return false;
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                pressBtnConfirm(db, MaskUtils.getNumber());
                return true;
            }
            return false;
        });
        mPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // TODO: 24.03.2018 add Scroll
            }
        });


        findViewById(R.id.text_forgot_password).setOnClickListener(v -> {
            Utils.showAlert(this, "Ваш пароль отправлен SMS сообщениямна номер +" + MaskUtils.getNumber());
            findViewById(R.id.image_facebook).setVisibility(View.VISIBLE);
            findViewById(R.id.text_facebook).setVisibility(View.VISIBLE);
        });

        findViewById(R.id.text_confirm).setOnClickListener(v -> {
            pressBtnConfirm(db, MaskUtils.getNumber());
        });
    }

    private void pressBtnConfirm(UserDatabase db, String number) {
        db.phoneDao().getByNumberRx(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (mPassword.getText().toString().equals(user.getPassword())) {
                            Utils.showAlert(SignInActivity.this, "В розробці");
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
