package com.example.oleksandr.discount.utils;

import android.widget.EditText;

import com.redmadrobot.inputmask.MaskedTextChangedListener;

public class MaskUtils {
    private static String number;

    public void setNumberMask(EditText textNumber) {

        MaskedTextChangedListener listener = new MaskedTextChangedListener(
                "+ ({380}) [00] [000] [00] [00]", true, textNumber, null, (b, s) -> number = s
        );
        textNumber.addTextChangedListener(listener);
        textNumber.setOnFocusChangeListener(listener);
        textNumber.setHint(listener.placeholder());
    }

    public void blockEditText(EditText editText){
        editText.setFocusable(false);
        editText.setLongClickable(false);
        editText.setCursorVisible(false);
        editText.setClickable(false);
    }

    public static String getNumber() {
        if (number == null) return "";
        return number;
    }
}
