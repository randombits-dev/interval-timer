package dev.randombits.intervaltimer

import android.os.CountDownTimer

enum class TimerStatus {
    PREPARE,
    ACTIVE,
    REST
}

abstract class HiitTimer(private val activeTime: Int, val restTime: Int, private val maxSets: Int = Int.MAX_VALUE) {
    private var set: Int = 0;
    private var status: TimerStatus = TimerStatus.PREPARE;
    private var timer: CountDownTimer? = null;
    private var remainingTime: Long = 0;
    private var isRunning = false;

    abstract fun onUpdate(millisRemaining: Long);
    abstract fun onStatusChange(status: TimerStatus, set: Int);
    abstract fun onComplete();

    fun start() {
        isRunning = true;
        status = TimerStatus.PREPARE;
        onStatusChange(status, set);

        timer = object : CountDownTimer(5 * 1000, 50) {
            override fun onTick(millisRemaining: Long) {
                if (isRunning)
                    onUpdate(millisRemaining);
                else
                    this.cancel();
            }

            override fun onFinish() {
                if (isRunning)
                    startActive();
                else
                    this.cancel();
            }
        }.start()
    }

    fun pause() {
        isRunning = false;
        timer?.cancel();
    }

    fun resume() {
        isRunning = true;

        when (status) {
            TimerStatus.PREPARE -> start();
            TimerStatus.ACTIVE -> startActive();
            TimerStatus.REST -> start();
        }
    }

    fun stop() {
        isRunning = false;
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
                if (isRunning)
                    onUpdate(millisRemaining);
                else
                    this.cancel();
            }

            override fun onFinish() {
                remainingTime = 0;
                if (isRunning)
                    if (set >= maxSets) {
                        stop();
                        onComplete();
                    } else {
                        startRest();
                    }
                else
                    this.cancel();
            }
        }.start();
    }

    private fun startRest() {
        status = TimerStatus.REST;
        onStatusChange(status, set);

        timer = object : CountDownTimer(restTime.toLong() * 1000, 50) {
            override fun onTick(millisRemaining: Long) {
                if (isRunning)
                    onUpdate(millisRemaining);
                else
                    this.cancel();
            }

            override fun onFinish() {
                if (isRunning)
                    startActive();
                else
                    this.cancel();
            }
        }.start();
    }
}
