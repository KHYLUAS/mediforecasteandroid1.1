package com.example.mediforecast;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "MedicineReminderChannel";
    private static Ringtone ringtone;
    private static Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = null;
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MedicineReminder::WakeLock");
            wakeLock.acquire(3000); // Keep CPU awake for 3 seconds
        }

        String medicineName = intent.getStringExtra("medicine_name");
        String startDateString = intent.getStringExtra("start_date");
        String endDateString = intent.getStringExtra("end_date");
        String selectedDays = intent.getStringExtra("selected_days");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);
            Date currentDate = Calendar.getInstance().getTime();

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            String currentDay = dayFormat.format(currentDate);

            if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
                List<String> dayList = Arrays.asList(selectedDays.split(", "));
                if (dayList.contains(currentDay)) {
                    startSoundAndVibration(context);
                    sendNotification(context, medicineName);

                    // Reschedule for the next day if within the range
                    Calendar alarmTime = Calendar.getInstance();
                    alarmTime.setTime(currentDate);
                    rescheduleAlarm(context, medicineName, startDateString, endDateString, selectedDays, alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
    }

    private void sendNotification(Context context, String medicineName) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Medicine Reminder Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        int notificationId = (int) System.currentTimeMillis();

        Intent stopIntent = new Intent(context, StopAlarmReceiver.class);
        stopIntent.putExtra("notification_id", notificationId);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.finalelogo)
                .setContentTitle("Time for Your Medicine")
                .setContentText("Remember to take " + medicineName + " for best results. Take it now to stay on schedule!")
                .setContentInfo(medicineName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_stop, "I took my medicine", stopPendingIntent);

        notificationManager.notify(notificationId, builder.build());
    }

    private void startSoundAndVibration(Context context) {
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.pills_sound_effect);
        ringtone = RingtoneManager.getRingtone(context, soundUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ringtone.setLooping(true);
        }
        ringtone.play();

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));
            } else {
                vibrator.vibrate(pattern, 0);
            }
        }

        new Handler().postDelayed(() -> stopSoundAndVibration(), 180000); // Stop after 3 minutes
    }

    @SuppressLint("ScheduleExactAlarm")
    private void rescheduleAlarm(Context context, String medicineName, String startDate, String endDate, String selectedDays, int hour, int minute) {
        Calendar nextDay = Calendar.getInstance();
        nextDay.add(Calendar.DAY_OF_YEAR, 1);
        nextDay.set(Calendar.HOUR_OF_DAY, hour);
        nextDay.set(Calendar.MINUTE, minute);
        nextDay.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("medicine_name", medicineName);
        intent.putExtra("start_date", startDate);
        intent.putExtra("end_date", endDate);
        intent.putExtra("selected_days", selectedDays);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextDay.getTimeInMillis(),
                    pendingIntent
            );
        }
    }

    public static void stopSoundAndVibration() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}
