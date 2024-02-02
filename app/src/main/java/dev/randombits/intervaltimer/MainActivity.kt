package dev.randombits.intervaltimer

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private var alarmSound = 0
    private var playingSound = 0;

    val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()
    val soundPool = SoundPool.Builder()
        .setAudioAttributes(attributes)
        .setMaxStreams(1)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main);


        val prefs = getSharedPreferences(packageName, MODE_PRIVATE);
        val activeTime = prefs.getInt("activeTime", 45);
        val restTime = prefs.getInt("restTime", 15);

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrame, SettingsFragment.newInstance(activeTime, restTime))
                .commit();
        }
        alarmSound = soundPool.load(this, R.raw.threesecbeep, 1);
    }

    fun startTimer(activeTime: Int, restTime: Int) {
        savePreferences(activeTime, restTime);
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrame, TimerFragment.newInstance(activeTime, restTime))
            .addToBackStack("start").commit()
    }

    fun backToSettings() {
        supportFragmentManager.popBackStack();
    }

    fun savePreferences(activeTime: Int, restTime: Int) {
        val prefs = getSharedPreferences(packageName, MODE_PRIVATE).edit();
        prefs.putInt("activeTime", activeTime);
        prefs.putInt("restTime", restTime);
        prefs.apply();
    }

    fun cancelAlarm() {
        if (playingSound == 0) {
            return;
        }
        soundPool.stop(playingSound);
        playingSound = 0;
    }

    fun soundAlarm() {
        if (playingSound > 0) {
            return;
        }
        playingSound = soundPool.play(alarmSound, 1f, 1f, 1, 0, 1f);

        Handler(Looper.getMainLooper()).postDelayed(
            { playingSound = 0 },
            3000
        )
    }
}
