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

import androidx.annotation.OptIn;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import java.text.ParseException;
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
        int intervalHours = intent.getIntExtra("interval_hours", 24);

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
//                    rescheduleAlarm(context, medicineName, startDateString, endDateString, selectedDays, alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE));
                    rescheduleAlarm(context, medicineName, startDateString, endDateString, selectedDays);
                }
                if (intervalHours > 0) {
                    rescheduleAlarmWithInterval(context, medicineName, startDateString, endDateString, selectedDays, intervalHours);
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

//    @SuppressLint("ScheduleExactAlarm")
//    private void rescheduleAlarm(Context context, String medicineName, String startDate, String endDate, String selectedDays, int hour, int minute) {
//        Calendar nextDay = Calendar.getInstance();
//        nextDay.add(Calendar.DAY_OF_YEAR, 1);
//        nextDay.set(Calendar.HOUR_OF_DAY, hour);
//        nextDay.set(Calendar.MINUTE, minute);
//        nextDay.set(Calendar.SECOND, 0);
//
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.putExtra("medicine_name", medicineName);
//        intent.putExtra("start_date", startDate);
//        intent.putExtra("end_date", endDate);
//        intent.putExtra("selected_days", selectedDays);
//
//        int requestCode = (medicineName + hour + minute).hashCode();
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    nextDay.getTimeInMillis(),
//                    pendingIntent
//            );
//        }
//    }
@SuppressLint("ScheduleExactAlarm")
private void rescheduleAlarm(Context context, String medicineName, String startDate, String endDate, String selectedDays) {
    // Create a calendar instance for the next alarm time
    Calendar nextAlarm = Calendar.getInstance();
    nextAlarm.setTimeInMillis(System.currentTimeMillis());

    // Get the current day of the week and the list of selected days
    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    String currentDay = dayFormat.format(nextAlarm.getTime());
    List<String> dayList = Arrays.asList(selectedDays.split(", "));

    if (dayList.contains(currentDay)) {
        nextAlarm.add(Calendar.DAY_OF_YEAR, 1); // Move to the next day if it's in the list
    }

    // Set up the intent with extra data
    Intent intent = new Intent(context, AlarmReceiver.class);
    intent.putExtra("medicine_name", medicineName);
    intent.putExtra("start_date", startDate);
    intent.putExtra("end_date", endDate);
    intent.putExtra("selected_days", selectedDays);

    // Generate a unique request code
    int requestCode = (medicineName + nextAlarm.get(Calendar.HOUR_OF_DAY) + nextAlarm.get(Calendar.MINUTE)).hashCode();
    PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

    // Set the alarm using AlarmManager
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (alarmManager != null) {
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                nextAlarm.getTimeInMillis(),
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
//    working but nnot consistent time
    @SuppressLint("ScheduleExactAlarm")
    private void rescheduleAlarmWithInterval(Context context, String medicineName, String startDate, String endDate, String selectedDays, int intervalHours) {
        // Set up the next alarm time after adding the interval
        Calendar nextAlarm = Calendar.getInstance();
        nextAlarm.setTimeInMillis(System.currentTimeMillis());
        nextAlarm.add(Calendar.HOUR_OF_DAY, intervalHours); // Reschedule after the interval

        // Create the intent for the AlarmReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("medicine_name", medicineName);
        intent.putExtra("start_date", startDate);
        intent.putExtra("end_date", endDate);
        intent.putExtra("selected_days", selectedDays);
        intent.putExtra("interval_hours", intervalHours); // Pass interval_hours in the intent

        // Generate a unique request code using the medicine name and next alarm time
        int requestCode = (medicineName + nextAlarm.get(Calendar.HOUR_OF_DAY) + nextAlarm.get(Calendar.MINUTE)).hashCode();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set up the alarm using AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextAlarm.getTimeInMillis(),
                    pendingIntent
            );
        }
    }
//@OptIn(markerClass = UnstableApi.class)
//@SuppressLint("ScheduleExactAlarm")
//    private void rescheduleAlarmWithInterval(Context context, String medicineName, String startDate, String endDate, String selectedDays, int intervalHours) {
//        // Set up the next alarm time after adding the interval
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
//        Calendar nextAlarm = Calendar.getInstance();
//
//        try {
//            nextAlarm.setTime(dateFormat.parse(startDate));
//            nextAlarm.set(Calendar.HOUR_OF_DAY, 0); // Start at midnight of the start day
//            nextAlarm.set(Calendar.MINUTE, 0);
//            nextAlarm.set(Calendar.SECOND, 0);
//        } catch (ParseException e) {
//            Log.e("MedicineReminder", "Unparseable date format. Expected format is d/M/yyyy.");
//            return;
//        }
//
//        nextAlarm.add(Calendar.HOUR_OF_DAY, intervalHours); // Add the interval to the start date
//
//        // Create the intent for the AlarmReceiver
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.putExtra("medicine_name", medicineName);
//        intent.putExtra("start_date", startDate);
//        intent.putExtra("end_date", endDate);
//        intent.putExtra("selected_days", selectedDays);
//        intent.putExtra("interval_hours", intervalHours);
//
//        // Generate a unique request code using the medicine name and next alarm time
//        int requestCode = (medicineName + nextAlarm.get(Calendar.HOUR_OF_DAY) + nextAlarm.get(Calendar.MINUTE)).hashCode();
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        // Set up the alarm using AlarmManager
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.setExactAndAllowWhileIdle(
//                    AlarmManager.RTC_WAKEUP,
//                    nextAlarm.getTimeInMillis(),
//                    pendingIntent
//            );
//        }
//    }

//@SuppressLint("ScheduleExactAlarm")
//private void rescheduleAlarm(Context context, String medicineName, String startDate, String endDate, String selectedDays, int hour, int minute) {
//    try {
//        // Parse start and end dates
//        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
//        Date startDateObj = dateFormat.parse(startDate);
//        Date endDateObj = dateFormat.parse(endDate);
//
//        // Get the current date
//        Calendar currentDate = Calendar.getInstance();
//        Date currentDateObj = currentDate.getTime();
//
//        // If the current date is after the end date, don't reschedule
//        if (currentDateObj.compareTo(endDateObj) > 0) {
//            return;
//        }
//
//        // Get the list of selected days
//        List<String> dayList = Arrays.asList(selectedDays.split(", "));
//
//        // Find the next occurrence of one of the selected days
//        Calendar nextAlarm = findNextAlarmDay(currentDate, dayList, hour, minute);
//        if (nextAlarm != null && !nextAlarm.getTime().after(endDateObj)) {
//            Intent intent = new Intent(context, AlarmReceiver.class);
//            intent.putExtra("medicine_name", medicineName);
//            intent.putExtra("start_date", startDate);
//            intent.putExtra("end_date", endDate);
//            intent.putExtra("selected_days", selectedDays);
//
//            int requestCode = (medicineName + nextAlarm.get(Calendar.HOUR_OF_DAY) + nextAlarm.get(Calendar.MINUTE)).hashCode();
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                    context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            if (alarmManager != null) {
//                alarmManager.setExactAndAllowWhileIdle(
//                        AlarmManager.RTC_WAKEUP,
//                        nextAlarm.getTimeInMillis(),
//                        pendingIntent
//                );
//            }
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}
//
//    /**
//     * Find the next occurrence of one of the selected days, starting from the current date.
//     */
//    private Calendar findNextAlarmDay(Calendar currentDate, List<String> dayList, int hour, int minute) {
//        Calendar nextAlarm = (Calendar) currentDate.clone();
//        String currentDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(currentDate.getTime());
//
//        // If today is the selected day, set the alarm for today at the specified hour and minute
//        if (dayList.contains(currentDay)) {
//            nextAlarm.set(Calendar.HOUR_OF_DAY, hour);
//            nextAlarm.set(Calendar.MINUTE, minute);
//            nextAlarm.set(Calendar.SECOND, 0);
//
//            // If the time for today has already passed, set it for the next occurrence
//            if (nextAlarm.before(currentDate)) {
//                nextAlarm.add(Calendar.DAY_OF_YEAR, 1);
//            }
//        } else {
//            // Find the next available day
//            for (int i = 1; i <= 7; i++) { // Check up to the next 7 days
//                nextAlarm.add(Calendar.DAY_OF_YEAR, 1);
//                String nextDay = new SimpleDateFormat("EEEE", Locale.getDefault()).format(nextAlarm.getTime());
//                if (dayList.contains(nextDay)) {
//                    nextAlarm.set(Calendar.HOUR_OF_DAY, hour);
//                    nextAlarm.set(Calendar.MINUTE, minute);
//                    nextAlarm.set(Calendar.SECOND, 0);
//                    break;
//                }
//            }
//        }
//        return nextAlarm;
//    }



}
