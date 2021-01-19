package com.jodimilans.matrimonial.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.jodimilans.matrimonial.ActivityConatiner.SignUp.LoginActivity;
import com.jodimilans.matrimonial.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyNotificationService extends FirebaseMessagingService {
    public static final String SET_Settings = "com.example.growfast_preferences";
    SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sharedPreferences = getSharedPreferences(SET_Settings, Context.MODE_PRIVATE);

        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }


    private void showNotification(String title, String message) {
        PendingIntent gotoActivity = PendingIntent.getActivity(this, 0, new Intent(this, LoginActivity.class), 0);
        Notification notify = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)
                .setContentIntent(gotoActivity)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notify);


    }


}