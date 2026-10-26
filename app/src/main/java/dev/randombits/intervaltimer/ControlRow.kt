package dev.randombits.intervaltimer

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.children
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

// the buttons must leave 16dp at both screen edges and a 30dp gap
private const val SPACING_DP = 2 * 16 + 30;

class ControlRow(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val density = resources.displayMetrics.density;
        val widthLeft = MeasureSpec.getSize(widthMeasureSpec) - SPACING_DP * density;
        // bigger ones don't look right next to the countdown on tablets
        var size = min(widthLeft / 2, 200 * density);
        // a short window, as in split screen, leaves less height below the countdown
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED)
            size = min(size, MeasureSpec.getSize(heightMeasureSpec).toFloat());
        // rather cut off than too small to press
        val px = max(size, 64 * density).roundToInt();

        // setLayoutParams() would ask for another layout pass
        for (child in children) {
            child.layoutParams.height = px;
            child.layoutParams.width = px;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
