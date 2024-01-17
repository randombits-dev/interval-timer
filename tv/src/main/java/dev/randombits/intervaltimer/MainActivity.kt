package dev.randombits.intervaltimer

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class MainActivity : AppCompatActivity() {
    private var alarmSound = 0
    private var settingsFrag: Fragment? = null;
    private var timerFrag: Fragment? = null;
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

        val ft = supportFragmentManager.beginTransaction();
        settingsFrag = SettingsFragment.newInstance(activeTime, restTime);
        ft.add(R.id.mainFrame, settingsFrag as Fragment);
        ft.commit();
        alarmSound = soundPool.load(this, R.raw.threesecbeep, 1);
    }

    fun startTimer(activeTime: Int, restTime: Int) {
        savePreferences(activeTime, restTime);
        val ft = supportFragmentManager.beginTransaction()

        timerFrag = TimerFragment.newInstance(activeTime, restTime);
        ft.replace(R.id.mainFrame, timerFrag as Fragment);
        ft.addToBackStack("start");
//        ft.hide(settingsFrag as Fragment);
//
//        ft.add(R.id.mainFrame, timerFrag as Fragment)

        ft.commit()

    }

    fun backToSettings() {
        val ft = supportFragmentManager.beginTransaction()
        if (timerFrag != null) {
            ft.remove(timerFrag as Fragment);
        }

        ft.show(settingsFrag as Fragment)

        ft.commit()
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
