package com.example.oleksandr.discount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oleksandr.discount.R;
import com.example.oleksandr.discount.db.User;
import com.example.oleksandr.discount.db.UserDatabase;
import com.example.oleksandr.discount.utils.MaskUtils;
import com.example.oleksandr.discount.utils.Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_NUMBER = "NUMBER";

    private EditText mTextNumber;
    private TextView mTextNext;
    private TextView mTextLicence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeNoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserDatabase db = UserDatabase.getPhoneDatabase(this);

        mTextNumber = findViewById(R.id.etext_number);
        mTextNext = findViewById(R.id.text_next);
        mTextLicence = findViewById(R.id.text_licence);
        setTextLicence(getString(R.string.login_text_licence));

        MaskUtils maskUtils = new MaskUtils();
        maskUtils.setNumberMask(mTextNumber);

        mTextNumber.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return false;
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                pressBtnNext(db, MaskUtils.getNumber());
                return true;
            }
            return false;
        });

        mTextNext.setOnClickListener(v -> {
            pressBtnNext(db, MaskUtils.getNumber());
        });


    }

    private void pressBtnNext(UserDatabase db, String number) {
        if (number.length() < 12) {
            Utils.showToast(this, "Номер слишком короткий");
        } else {
            db.phoneDao().getByNumberRx(MaskUtils.getNumber())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableMaybeObserver<User>() {
                        @Override
                        public void onSuccess(User user) {
                            startNextActivity(SignInActivity.class);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            startNextActivity(SignUpActivity.class);
                        }
                    });
        }
    }

    private void startNextActivity(Class mClass) {
        Intent intent = new Intent(this, mClass);
        String number = mTextNumber.getText().toString();
        intent.putExtra(EXTRA_NUMBER, number);
        startActivity(intent);
    }

    private void setTextLicence(String textLicence) {
        SpannableString ss = new SpannableString(textLicence);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // TODO: 23.03.2018 add Licence
                Utils.showToast(LoginActivity.this, "Правила в розроці");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, 49, 71, 0);
        mTextLicence.setText(ss);
        mTextLicence.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
