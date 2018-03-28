package com.example.oleksandr.discount.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class Utils {
    public static void showToast(@NonNull Context context, @NonNull String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showAlert (@NonNull Context context, @NonNull String message ){
        new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert)
                .setMessage(message)
                .setPositiveButton(context.getString(android.R.string.ok), (dialog, i) -> {
                })
                .create().show();
    }
}
