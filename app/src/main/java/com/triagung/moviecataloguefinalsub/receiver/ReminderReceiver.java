package com.triagung.moviecataloguefinalsub.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.triagung.moviecataloguefinalsub.BuildConfig;
import com.triagung.moviecataloguefinalsub.R;
import com.triagung.moviecataloguefinalsub.activity.DetailActivity;
import com.triagung.moviecataloguefinalsub.activity.MainActivity;
import com.triagung.moviecataloguefinalsub.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.triagung.moviecataloguefinalsub.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class ReminderReceiver extends BroadcastReceiver {
    public static final String TYPE_DAILY_REMINDER = "Daily Reminder";
    public static final String TYPE_RELEASE_TODAY_REMINDER = "Release Today Reminder";
    public static final String EXTRA_TYPE = "extra_type";

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    private final int ID_DAILY_REMINDER = 100;
    private final int ID_RELEASE_TODAY_REMINDER = 101;

    private final String TIME_FORMAT = "HH:mm";

    @Override
    public void onReceive(final Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);

        if (type.equals(TYPE_RELEASE_TODAY_REMINDER)) {
            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://api.themoviedb.org/3/discover/movie?api_key="+ API_KEY +"&primary_release_date.gte="+ dateNow() +"&primary_release_date.lte="+ dateNow();

            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String result = new String(responseBody);
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray results = responseObject.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject movie = results.getJSONObject(i);
                            Movie listMovie = new Movie(movie, false);
                            if (!listMovie.getPoster().equals("null")) {
                                showReleaseTodayReminderNotification(context, listMovie);
                            }
                        }

                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", "Get data failed!");
                }
            });

        } else {
            showDailyReminderNotification(context);
        }
    }

    public void setDailyReminder(Context context, String type, String time) {
        if (isTimeInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void showDailyReminderNotification(Context context) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "DailyReminder channel";

        int ID_DAILY_REMINDER = 100;

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(context.getString(R.string.daily_reminder))
                .setContentText(context.getString(R.string.message_daily_reminder))
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        if (notificationManager != null) notificationManager.notify(ID_DAILY_REMINDER, notification);
    }

    public void setReleaseTodayReminder(Context context, String type, String time) {
        if (isTimeInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_TYPE, type);

        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_TODAY_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    private void showReleaseTodayReminderNotification(Context context, Movie movie) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "ReleaseTodayReminder channel";

        Intent intent = new Intent(context, DetailActivity.class);
        Uri uri = Uri.parse(CONTENT_URI_MOVIE + "/" + movie.getId());
        intent.setData(uri);
        intent.putExtra(DetailActivity.EXTRA_TYPE, "movie");
        intent.putExtra(DetailActivity.EXTRA_ID, movie.getId());

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(context.getString(R.string.message_release_today_reminder))
                .setContentText(movie.getTitle() + context.getString(R.string.has_release_today))
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        if (notificationManager != null) notificationManager.notify(movie.getId(), notification);
    }

    private boolean isTimeInvalid(String time, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            dateFormat.setLenient(false);
            dateFormat.parse(time);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private String dateNow() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void cancelReminder(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_REMINDER) ? ID_DAILY_REMINDER : ID_RELEASE_TODAY_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }
}
