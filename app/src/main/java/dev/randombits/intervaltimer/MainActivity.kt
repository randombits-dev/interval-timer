package dev.randombits.intervaltimer

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var alarmSound = 0
    private var endSound = 0;
    private var playingSound = 0;

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

    fun startTimer(activeTime: Int, restTime: Int, sets: Int) {
        savePreferences(activeTime, restTime, sets);
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrame, TimerFragment.newInstance(activeTime, restTime, sets))
            .addToBackStack("start").commit()
    }

    fun backToSettings() {
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
        if (playingSound == 0)
            return;

        soundPool.stop(playingSound);
        playingSound = 0;
    }

    fun soundAlarm() {
        if (playingSound > 0)
            return;

        playingSound = soundPool.play(alarmSound, 1f, 1f, 1, 0, 1f);
        Handler(Looper.getMainLooper()).postDelayed({ playingSound = 0 }, 3000);
    }

    fun soundEnd() {
        soundPool.play(endSound, 1f, 1f, 1, 2, 1f)
    }
}
