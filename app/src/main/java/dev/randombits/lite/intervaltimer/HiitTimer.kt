package dev.randombits.lite.intervaltimer

import android.os.CountDownTimer

enum class TimerStatus {
    START,
    ACTIVE,
    REST
}

abstract class HiitTimer(val activeTime: Int, val restTime: Int) {

    private var set: Int = 0;
    private var status: TimerStatus = TimerStatus.START;
    private var timer: CountDownTimer? = null;
    private var remainingTime: Long = 0;

    public abstract fun onUpdate(millisRemaining: Long);
    public abstract fun onStatusChange(status: TimerStatus, set: Int);

    public fun start() {
        status = TimerStatus.START;
        onStatusChange(status, set);
        timer = object : CountDownTimer(3 * 1000, 50) {
            override fun onTick(millisRemaining: Long) {
                onUpdate(millisRemaining);
            }

            override fun onFinish() {
                startActive();
            }
        }.start()
    }


    public fun pause() {
        timer?.cancel();
    }

    public fun resume() {
        when (status) {
            TimerStatus.START -> start();
            TimerStatus.ACTIVE -> startActive();
            TimerStatus.REST -> start();
        }
    }

    public fun stop() {
        timer?.cancel();
        timer = null;
    }

    private fun startActive() {
        var time = remainingTime;
        if (remainingTime == 0L) {
            time = activeTime.toLong() * 1000;
            status = TimerStatus.ACTIVE;
            set++;
            onStatusChange(status, set);
        }
        timer = object : CountDownTimer(time, 50) {
            override fun onTick(millisRemaining: Long) {
                remainingTime = millisRemaining;
                onUpdate(millisRemaining);
            }

            override fun onFinish() {
                remainingTime = 0;
                startRest();
            }
        }.start()
    }

    private fun startRest() {
        status = TimerStatus.REST;
        onStatusChange(status, set);
        timer = object : CountDownTimer(restTime.toLong() * 1000, 50) {
            override fun onTick(millisRemaining: Long) {
                onUpdate(millisRemaining);
            }

            override fun onFinish() {
                startActive();
            }
        }.start()
    }
}
