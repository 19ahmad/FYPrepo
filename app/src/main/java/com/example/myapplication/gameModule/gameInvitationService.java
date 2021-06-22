package com.example.myapplication.gameModule;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class gameInvitationService extends FirebaseMessagingService {
    private String body,title;
    public gameInvitationService(){}



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        body = remoteMessage.getData().get("Body");
        title = remoteMessage.getData().get("Title");
       generateNotification(body,title);
    }

    public void generateNotification(String body, String title) {

        Intent intent1 = new Intent(getApplicationContext(), guess_topic_of_discussion.class);
        intent1.putExtra("Accept",true);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(getApplicationContext(),0,intent1,PendingIntent.FLAG_ONE_SHOT);

        Intent intent2 = new Intent(getApplicationContext(), main_Menu.class);
        intent1.putExtra("Decline",true);
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(getApplicationContext(),1,intent2,PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).
                setSmallIcon(R.drawable.notification_bell)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .addAction(R.drawable.ic_launcher_foreground,"Yes",pendingIntent1)
                .addAction(R.drawable.ic_launcher_foreground,"No",pendingIntent2);

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notificationBuilder.build());




    }
}
