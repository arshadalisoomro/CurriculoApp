package barreto.alessandro.curriculo.service;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import barreto.alessandro.curriculo.R;
import barreto.alessandro.curriculo.view.activity.ChatActivity;

/**
 * Created by Alessandro on 10/07/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (isAppIsInBackground(this)){
            enviarNotificacao();
        }else{
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("nova_mensagem"));
        }

    }

    private void enviarNotificacao(){
        final int id = 6565;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setTicker(getResources().getString(R.string.nova_mensagem))
                .setSmallIcon( R.mipmap.ic_launcher )
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.mensagem_respondida))
                .setAutoCancel(true);

        Intent it = new Intent(this, ChatActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);

        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, builder.build());

    }


    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
