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

import static com.example.oleksandr.discount.utils.Keys.NUMBER;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    private EditText etNumber;
    private TextView tvLicence;
    private UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeNoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = UserDatabase.getPhoneDatabase(this);

        etNumber = findViewById(R.id.et_number);
        TextView textNext = findViewById(R.id.text_next);
        tvLicence = findViewById(R.id.text_licence);
        setTextLicence(getString(R.string.login_text_licence));

        MaskUtils maskUtils = new MaskUtils();
        maskUtils.setNumberMask(etNumber);

        etNumber.setOnKeyListener(this);
        textNext.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_next:
                pressBtnNext();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN)
            return false;
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            pressBtnNext();
            return true;
        }
        return false;
    }

    private void pressBtnNext() {
        if (MaskUtils.getNumber().length() < 12) {
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
        String number = etNumber.getText().toString();
        intent.putExtra(NUMBER, number);
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
        tvLicence.setText(ss);
        tvLicence.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
