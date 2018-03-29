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

public class SignUpActivity extends AppCompatActivity {

    private EditText mEditTextPassword;
    private EditText textNumber;
    private TextView mTextPassword;
    private TextView mTextConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Random r = new Random();
        int passwordSms = r.nextInt(10000 - 1000 + 1) + 1000;
        Utils.ShowNotification(this, "" + passwordSms);
        setContentView(R.layout.activity_sign_up);
        textNumber = findViewById(R.id.etext_number);
        MaskUtils maskUtils = new MaskUtils();
        maskUtils.blockEditText(textNumber);
        textNumber.setText(getIntent().getStringExtra(LoginActivity.EXTRA_NUMBER));

        UserDatabase db = UserDatabase.getPhoneDatabase(this);

        mEditTextPassword = findViewById(R.id.etext_password);
        mTextConfirm = findViewById(R.id.text_confirm);
        mTextPassword = findViewById(R.id.text_password);

        findViewById(R.id.image_back).setOnClickListener(v -> finish());
        mEditTextPassword.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return false;
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                pressBtnConfirm(db, MaskUtils.getNumber(), passwordSms);
                return true;
            }
            return false;
        });
        mTextConfirm.setOnClickListener(v -> {
            pressBtnConfirm(db, MaskUtils.getNumber(), passwordSms);
        });
    }

    private void pressBtnConfirm(UserDatabase db, String number, int sms) {
        if (mTextConfirm.getText().toString().equals(getString(R.string.sign_up_confirm))) {
            if (Integer.parseInt(mEditTextPassword.getText().toString()) == sms) {
                Utils.cancelNotification(this);
                showHideComponents();
            } else {
                mEditTextPassword.setText("");
                Utils.showAlert(this, "Пароль из SMS введен не правельно");
            }
        } else {
            if (mEditTextPassword.getText().length() < 6) {
                Utils.showToast(this, "Пароль слишком короткий");
            } else {

                Completable.fromAction(() -> db.phoneDao()
                        .insert(new User(number, mEditTextPassword.getText().toString())))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(LoginActivity.EXTRA_NUMBER, MaskUtils.getNumber());
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
        mEditTextPassword.setText("");
        mEditTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mTextPassword.setText(getString(R.string.sign_up_create_password));
        mTextPassword.setMinLines(2);
        mTextConfirm.setText(getString(R.string.sign_up_ok));
        findViewById(R.id.image_back).setVisibility(View.GONE);
    }


}
