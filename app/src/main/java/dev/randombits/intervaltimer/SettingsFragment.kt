package dev.randombits.intervaltimer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "active";
private const val ARG_PARAM2 = "rest";

class SettingsFragment : Fragment() {
    private var defaultActiveTime: Int = 45;
    private var defaultRestTime: Int = 15;
    private var mainActivity: MainActivity? = null;
    private var activeInput: EditText? = null;
    private var restInput: EditText? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        mainActivity = context as MainActivity;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        arguments?.let {
            defaultActiveTime = it.getInt(ARG_PARAM1);
            defaultRestTime = it.getInt(ARG_PARAM2);
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_settings, container, false) as View;
        setupFields(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private fun setupFields(view: View) {
        activeInput = view.findViewById(R.id.activeTime);
        activeInput!!.setText(defaultActiveTime.toString());
        restInput = view.findViewById(R.id.restTime);
        restInput!!.setText(defaultRestTime.toString());

        view.findViewById<View>(R.id.activeTime_less).setOnClickListener {
            changeActiveValue(activeInput!!, defaultActiveTime, -5);
        }

        view.findViewById<View>(R.id.restTime_less).setOnClickListener {
            changeRestValue(restInput!!, defaultRestTime, -5);
        }

        view.findViewById<View>(R.id.activeTime_more).setOnClickListener {
            changeActiveValue(activeInput!!, defaultActiveTime, 5);
        }

        view.findViewById<View>(R.id.restTime_more).setOnClickListener {
            changeRestValue(restInput!!, defaultRestTime, 5);
        }

        view.findViewById<View>(R.id.beginBtn).setOnClickListener { startTimer(); };
    }

    @SuppressLint("SetTextI18n")
    private fun changeRestValue(editText: EditText, defaultValue: Int, change: Int) {
        val currentValue = editText.text.toString().toIntOrNull() ?: defaultValue;
        val newValue = (currentValue + change).coerceAtLeast(0);
        editText.setText(newValue.toString());
    }

    @SuppressLint("SetTextI18n")
    private fun changeActiveValue(editText: EditText, defaultValue: Int, change: Int) {
        val currentValue = editText.text.toString().toIntOrNull() ?: defaultValue;
        val newValue = (currentValue + change).coerceAtLeast(5);
        editText.setText(newValue.toString());
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (activeInput == null || restInput == null) {
            return;
        }
        outState.putInt(
            "settings.activeTime",
            Integer.parseInt(activeInput!!.text.toString())
        );

        outState.putInt(
            "settings.restTime",
            Integer.parseInt(restInput!!.text.toString())
        );

        super.onSaveInstanceState(outState);
    }

    override fun onDestroy() {
        super.onDestroy();

        if (activeInput == null || restInput == null) {
            return;
        }
        mainActivity!!.savePreferences(
            Integer.parseInt(activeInput!!.text.toString()),
            Integer.parseInt(restInput!!.text.toString())
        );
    }

    companion object {
        @JvmStatic
        fun newInstance(active: Int, rest: Int) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, active);
                    putInt(ARG_PARAM2, rest);
                }
            };
    }

    private fun startTimer() {
        var activeTime = Integer.parseInt(requireView().findViewById<EditText>(R.id.activeTime).text.toString());
        var restTime = Integer.parseInt(requireView().findViewById<EditText>(R.id.restTime).text.toString());
        if (activeTime < 5)
            activeTime = 5;
        if (restTime < 0)
            restTime = 0;
        mainActivity!!.startTimer(activeTime, restTime);
    }
}
