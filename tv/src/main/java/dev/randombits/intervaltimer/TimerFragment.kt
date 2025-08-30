package dev.randombits.intervaltimer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "active";
private const val ARG_PARAM2 = "rest"

class TimerFragment : Fragment() {
    private var activeTime: Int? = null
    private var restTime: Int? = null
    private var mainActivity: MainActivity? = null;
    private var timer: HiitTimer? = null;
    private var paused: Boolean = false;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        mainActivity = context as MainActivity;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            activeTime = it.getInt(dev.randombits.intervaltimer.ARG_PARAM1)
            restTime = it.getInt(dev.randombits.intervaltimer.ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity!!.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        mainActivity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        pauseTimer();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false);

        val resultTextView: TextView = view.findViewById(R.id.timeLeft);

        val statusTextView: TextView = view.findViewById(R.id.status);
        val setCountView: TextView = view.findViewById(R.id.setCount);

        timer = object : HiitTimer(activeTime!!, restTime!!) {
            override fun onUpdate(millisRemaining: Long) {
                resultTextView.text = ((millisRemaining + 999) / 1000).toString()

                if (millisRemaining > 2950 && millisRemaining < 3050) {
                    mainActivity!!.soundAlarm();
                }
            }

            override fun onStatusChange(status: TimerStatus, set: Int) {
                if (status === TimerStatus.ACTIVE) {
                    statusTextView.setTextColor(resources.getColor(R.color.activeBg));
                    statusTextView.text = getString(R.string.activeTime_label).uppercase();
                } else if (status === TimerStatus.REST) {
                    statusTextView.setTextColor(resources.getColor(R.color.restBg));
                    statusTextView.text = getString(R.string.restTime_label).uppercase();
                } else {
                    statusTextView.setTextColor(resources.getColor(R.color.text));
                    statusTextView.text = getString(R.string.prepareTime_label).uppercase();
                }

                if (set > 0) {
                    setCountView.text = getString(R.string.setCount, set);
                }
            }
        };
        timer!!.start();

        view.isFocusableInTouchMode = true

        view.requestFocus();
        view.setOnKeyListener { v, keyCode, event ->
            print("keycode: $keyCode")
            if (event.action != android.view.KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false;
            }
            if (keyCode == 23) {
                paused = !paused;
                if (paused) {
                    pauseTimer();
                } else {
                    resumeTimer()
                }
            }
            false;
        }

        return view;
    }

    private fun pauseTimer() {
        timer!!.pause();
        mainActivity!!.cancelAlarm();
    }

    private fun resumeTimer() {
        timer!!.resume()
    }

    companion object {
        @JvmStatic
        fun newInstance(active: Int, rest: Int) =
            TimerFragment().apply {
                arguments = Bundle().apply {
                    putInt(dev.randombits.intervaltimer.ARG_PARAM1, active)
                    putInt(dev.randombits.intervaltimer.ARG_PARAM2, rest)
                }
            }
    }
}
