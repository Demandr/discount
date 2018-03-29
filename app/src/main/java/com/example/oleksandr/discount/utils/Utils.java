package com.example.oleksandr.discount.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class Utils {
    public static void showToast(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showAlert(@NonNull Context context, @NonNull String message) {
        new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert)
                .setMessage(message)
                .setPositiveButton(context.getString(android.R.string.ok), (dialog, i) -> {
                })
                .create().show();
    }

    public static void ShowNotification(@NonNull Context context, @NonNull String message) {
        NotificationCompat.Builder notification_builder;
        NotificationManager notification_manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "1";
            CharSequence name = "SMS";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            notification_manager.createNotificationChannel(mChannel);
            notification_builder = new NotificationCompat.Builder(context, chanel_id);
        } else {
            notification_builder = new NotificationCompat.Builder(context);
        }
        notification_builder.setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Discount SMS")
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);

        notification_manager.notify(1, notification_builder.build());
    }

    public static void cancelNotification(@NonNull Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
