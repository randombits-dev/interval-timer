package dev.randombits.intervaltimer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "active";
private const val ARG_PARAM2 = "rest"

class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var activeTime: Int? = 45
    private var restTime: Int? = 15
    private var mainActivity: MainActivity? = null;
    private var activeInput: EditText? = null;
    private var restInput: EditText? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        mainActivity = context as MainActivity;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            activeTime = it.getInt(ARG_PARAM1)
            restTime = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        view.
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false) as View;

//        if (savedInstanceState == null) {
//            view.findViewById<EditText>(R.id.activeTime)
//                .setText(savedInstanceState.getInt("settings.activeTime", 45).toString());
//        } else {
//            activePeriod = savedInstanceState.getLong("settings.activePeriod", 20000)
//            restPeriod = savedInstanceState.getLong("settings.restPeriod", 10000)
//            delay = savedInstanceState.getLong("settings.delay", 2000)
//        }

//        if (savedInstanceState != null) {
//            view.findViewById<EditText>(R.id.activeTime)
//                .setText(savedInstanceState.getInt("settings.activeTime", 45).toString());
//            view.findViewById<EditText>(R.id.restTime)
//                .setText(savedInstanceState.getInt("settings.restTime", 15).toString());
//        }

        activeInput = view.findViewById(R.id.activeTime)
        activeInput!!.setText(activeTime.toString())
        restInput = view.findViewById(R.id.restTime)
        restInput!!.setText(restTime.toString())

        view.findViewById<View>(R.id.activeTime_less).setOnClickListener {
            val activeTime = activeInput!!.text.toString();
            if (activeTime.isBlank()) {
                activeInput!!.setText("45");
            } else {
                if (Integer.parseInt(activeTime) >= 5) {
                    activeInput!!.setText((Integer.parseInt(activeTime) - 5).toString());
                }
            }

        }
        view.findViewById<View>(R.id.activeTime_more).setOnClickListener {
            val activeTime = activeInput!!.text.toString();
            if (activeTime.isBlank()) {
                activeInput!!.setText("45");
            } else {
                activeInput!!.setText((Integer.parseInt(activeTime) + 5).toString());
            }
        }

        view.findViewById<View>(R.id.restTime_less).setOnClickListener {
            val activeTime = restInput!!.text.toString();
            if (activeTime.isBlank()) {
                restInput!!.setText("45");
            } else {
                if (Integer.parseInt(activeTime) >= 5) {
                    restInput!!.setText((Integer.parseInt(activeTime) - 5).toString());
                }
            }

        }
        view.findViewById<View>(R.id.restTime_more).setOnClickListener {
            val activeTime = restInput!!.text.toString();
            if (activeTime.isBlank()) {
                restInput!!.setText("45");
            } else {
                restInput!!.setText((Integer.parseInt(activeTime) + 5).toString());
            }
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

    companion object {
        @JvmStatic
        fun newInstance(active: Int, rest: Int) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, active)
                    putInt(ARG_PARAM2, rest)
                }
            }
    }

    private fun startTimer() {
        val activeTime = requireView().findViewById<EditText>(R.id.activeTime).text.toString();
        val restTime = requireView().findViewById<EditText>(R.id.restTime).text.toString();
        mainActivity!!.startTimer(Integer.parseInt(activeTime), Integer.parseInt(restTime));
    }
}
