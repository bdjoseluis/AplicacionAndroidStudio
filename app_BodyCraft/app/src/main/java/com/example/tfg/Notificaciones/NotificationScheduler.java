package com.example.tfg.Notificaciones;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Data;


import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    private static final String TAG = "NotificationScheduler";
    private static final String NOTIFICATION_WORK_TAG = "daily_notification_work";

    /**
     * Programa una notificación diaria a una hora específica utilizando WorkManager.
     *
     * @param context El contexto de la aplicación.
     */
    public static void scheduleDailyNotification(Context context) {
        // Lee el intervalo de notificaciones de las preferencias
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int intervaloNotificaciones = Integer.parseInt(preferences.getString("intervalo_notificacion", "1440"));

        // Configura el tiempo para que la notificación se ejecute periódicamente
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(DailyNotificationWork.class, intervaloNotificaciones, TimeUnit.MINUTES)
                .addTag(NOTIFICATION_WORK_TAG)
                .build();

        // Programa la notificación periódica
        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
    }

    /**
     * Cancela la notificación diaria programada.
     *
     * @param context El contexto de la aplicación.
     */
    public static void cancelDailyNotification(Context context) {
        // Cancelar la notificación diaria utilizando el tag que se agregó al WorkRequest
        WorkManager.getInstance(context).cancelAllWorkByTag(NOTIFICATION_WORK_TAG);
    }
}
