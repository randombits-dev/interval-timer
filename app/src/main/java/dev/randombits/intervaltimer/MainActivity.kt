package dev.randombits.intervaltimer

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var alarmSound = 0
    private var endSound = 0;
    private var alarmStream = 0;
    private var alarmTime = 0L;

    private val attributes: AudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val soundPool: SoundPool = SoundPool.Builder()
        .setAudioAttributes(attributes)
        .setMaxStreams(1)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main);

        // the beeps play as media, so let the hardware keys control media volume
        // even between beeps, instead of the ringer volume on some phones
        volumeControlStream = AudioManager.STREAM_MUSIC;

        val prefs = getSharedPreferences(packageName, MODE_PRIVATE);
        val activeTime = prefs.getInt("activeTime", 45);
        val restTime = prefs.getInt("restTime", 15);
        val sets = prefs.getInt("sets", 0);

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainFrame, SettingsFragment.newInstance(activeTime, restTime, sets))
                .commit();
        }

        alarmSound = soundPool.load(this, R.raw.threesecbeep, 1);
        endSound = soundPool.load(this, R.raw.hit, 1);
    }

    override fun onStart() {
        super.onStart();
        val prefs = getSharedPreferences(packageName, MODE_PRIVATE);
        if (prefs.getBoolean("backToSettings", false)) {
            prefs.edit().putBoolean("backToSettings", false).apply();
            supportFragmentManager.popBackStack();
        }
    }

    fun startTimer(activeTime: Int, restTime: Int, sets: Int) {
        savePreferences(activeTime, restTime, sets);
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrame, TimerFragment.newInstance(activeTime, restTime, sets))
            .addToBackStack("start").commit()
    }

    fun backToSettings() {
        // the screens can't change while the app is in the background, where a workout can end,
        // so it returns to the settings when the app comes back. This is kept in the preferences,
        // as the activity may be recreated in the meantime and restore the timer screen.
        if (supportFragmentManager.isStateSaved)
            getSharedPreferences(packageName, MODE_PRIVATE).edit()
                .putBoolean("backToSettings", true).apply();
        else
            supportFragmentManager.popBackStack();
    }

    fun savePreferences(activeTime: Int, restTime: Int, sets: Int) {
        val prefs = getSharedPreferences(packageName, MODE_PRIVATE).edit();
        prefs.putInt("activeTime", activeTime);
        prefs.putInt("restTime", restTime);
        prefs.putInt("sets", sets);
        prefs.apply();
    }

    fun cancelAlarm() {
        // a resumed countdown beeps again
        alarmTime = 0;
        if (alarmStream == 0)
            return;

        soundPool.stop(alarmStream);
        alarmStream = 0;
    }

    fun soundAlarm() {
        // the timer asks for the beep on several ticks in a row
        val time = SystemClock.elapsedRealtime();
        if (alarmTime > 0 && time - alarmTime < 3000)
            return;

        // if it fails, the next tick tries again
        val stream = soundPool.play(alarmSound, 1f, 1f, 1, 0, 1f);
        if (stream == 0)
            return;

        alarmStream = stream;
        alarmTime = time;
    }

    fun soundEnd() {
        soundPool.play(endSound, 1f, 1f, 1, 2, 1f)
    }
}
