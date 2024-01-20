package dev.randombits.intervaltimer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {
    private var mainActivity: MainActivity? = null;
    private var activeInput: TextView? = null;
    private var restInput: TextView? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        mainActivity = context as MainActivity;
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val settings = mainActivity!!.getSettings();

        val view = inflater.inflate(R.layout.fragment_settings, container, false) as View;

        activeInput = view.findViewById(R.id.activeTime) as TextView;
        activeInput!!.text = settings.first.toString();
        restInput = view.findViewById(R.id.restTime)
        restInput!!.text = settings.second.toString();

        activeInput!!.setOnFocusChangeListener { v, hasFocus ->
            view.findViewById<View>(R.id.activeTime_less).isVisible = hasFocus;
            view.findViewById<View>(R.id.activeTime_more).isVisible = hasFocus;
        }

        restInput!!.setOnFocusChangeListener { v, hasFocus ->
            view.findViewById<View>(R.id.restTime_less).isVisible = hasFocus;
            view.findViewById<View>(R.id.restTime_more).isVisible = hasFocus;
        }

        activeInput!!.setOnKeyListener { v, keyCode, event ->
            if (event.action != android.view.KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false;
            }
            if (keyCode == 21) {
                val activeTime = activeInput!!.text.toString();
                if (activeTime.isBlank()) {
                    activeInput!!.text = "45";
                } else if (Integer.parseInt(activeTime) > 5) {
                    activeInput!!.text = (Integer.parseInt(activeTime) - 5).toString();
                }
            } else if (keyCode == 22) {
                val activeTime = activeInput!!.text.toString();
                if (activeTime.isBlank()) {
                    activeInput!!.text = "45";
                } else {
                    activeInput!!.text = (Integer.parseInt(activeTime) + 5).toString();
                }
            }
            false;
        }

        restInput!!.setOnKeyListener { v, keyCode, event ->
            if (event.action != android.view.KeyEvent.ACTION_DOWN) {
                return@setOnKeyListener false;
            }
            System.out.println("keycode: " + keyCode);
            if (keyCode == 21) {
                val activeTime = restInput!!.text.toString();
                if (activeTime.isBlank()) {
                    restInput!!.text = "45";
                } else {
                    if (Integer.parseInt(activeTime) > 5) {
                        restInput!!.text = (Integer.parseInt(activeTime) - 5).toString();
                    }
                }
            } else if (keyCode == 22) {
                val activeTime = restInput!!.text.toString();
                if (activeTime.isBlank()) {
                    restInput!!.text = "45";
                } else {
                    restInput!!.text = (Integer.parseInt(activeTime) + 5).toString();
                }
            }
            false;
        }

        view.findViewById<View>(R.id.beginBtn).setOnClickListener { startTimer() }
        return view;
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(
            "settings.activeTime",
            Integer.parseInt(activeInput!!.text.toString())
        )
        outState.putInt(
            "settings.restTime",
            Integer.parseInt(restInput!!.text.toString())
        )
        super.onSaveInstanceState(outState);
    }

    override fun onDestroy() {
        super.onDestroy();
        mainActivity!!.savePreferences(
            Integer.parseInt(activeInput!!.text.toString()),
            Integer.parseInt(restInput!!.text.toString())
        );
    }

    private fun startTimer() {
        mainActivity!!.savePreferences(
            Integer.parseInt(activeInput!!.text.toString()),
            Integer.parseInt(restInput!!.text.toString())
        );
        val activeTime = requireView().findViewById<TextView>(R.id.activeTime).text.toString();
        val restTime = requireView().findViewById<TextView>(R.id.restTime).text.toString();
        mainActivity!!.startTimer(Integer.parseInt(activeTime), Integer.parseInt(restTime));
    }
}
