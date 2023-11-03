package dev.randombits.lite.intervaltimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "defaultValue"

/**
 * A simple [Fragment] subclass.
 * Use the [NumberControl.newInstance] factory method to
 * create an instance of this fragment.
 */
class NumberControl(initialValue: Int) : Fragment() {
    // TODO: Rename and change types of parameters
//    private var defaultValue: Int? = null
    var numberInput: EditText? = null
    var initialValue: Int = initialValue

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            defaultValue = it.getInt(ARG_PARAM1)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_number_control, container, false)

        numberInput = view.findViewById(R.id.numberControl)
        numberInput!!.setText(this.initialValue.toString())

        view.findViewById<View>(R.id.numberControl_less).setOnClickListener {
            val currentVal = numberInput!!.text.toString();
            if (currentVal.isBlank()) {
                numberInput!!.setText("45");
            } else {
                if (Integer.parseInt(currentVal) >= 5) {
                    numberInput!!.setText((Integer.parseInt(currentVal) - 5).toString());
                }
            }

        }
        view.findViewById<View>(R.id.numberControl_more).setOnClickListener {
            val activeTime = numberInput!!.text.toString();
            if (activeTime.isBlank()) {
                numberInput!!.setText("45");
            } else {
                numberInput!!.setText((Integer.parseInt(activeTime) + 5).toString());
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(defaultValue: Int) =
            NumberControl(defaultValue)
    }
}
