package com.example.tfg.Notificaciones;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tfg.R;
import com.example.tfg.Rutina.Rutina;

public class DailyNotificationWork extends Worker {
    private static final String CHANNEL_ID = "daily_notification_channel";

    public DailyNotificationWork(Context context, WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        // Obtén las preferencias compartidas
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean notificacionesActivas = preferences.getBoolean("notificaciones", false);

        // Si las notificaciones están activadas, envía una notificación diaria
        if (notificacionesActivas) {
            enviarNotificacionDiaria();
        }

        // Indica que el trabajo se completó con éxito
        return Result.success();
    }

    private void enviarNotificacionDiaria() {
        // Crea un canal de notificación si es necesario (Android Oreo o posterior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Daily Notification Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

        // Configura un intent para abrir una actividad cuando el usuario toque la notificación
        Intent intent = new Intent(getApplicationContext(), Rutina.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Crea la notificación
        Notification notificacion = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificacion = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("A entrenar")
                    .setContentText("Es hora de entrenar, vamos!")
                    .setSmallIcon(R.drawable.logotipo)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        }

        // Envía la notificación
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificacion);
    }
}
