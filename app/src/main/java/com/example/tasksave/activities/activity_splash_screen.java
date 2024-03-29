package com.example.tasksave.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import com.example.tasksave.servicesreceiver.AlarmReceiver;
import com.example.tasksave.conexaoBD.Conexao;
import com.example.tasksave.R;

import java.util.Timer;
import java.util.TimerTask;

public class activity_splash_screen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1000;
    private Conexao con;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

// Agendar o BroadcastReceiver para ser chamado a cada minuto (ajuste conforme necessário)

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                CharSequence name = getString(R.string.channel_name);
                String description = getString(R.string.channel_description);
                int importance = NotificationManager.IMPORTANCE_MAX;

                @SuppressLint("WrongConstant")
                NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
                channel.setDescription(description);

                long[] pattern = {0, 1000, 500, 1000};
                channel.setVibrationPattern(pattern);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(activity_splash_screen.this, activity_fingerprint.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    }