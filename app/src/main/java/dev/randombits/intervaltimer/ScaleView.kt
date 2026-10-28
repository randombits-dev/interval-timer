package dev.randombits.intervaltimer

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import kotlin.math.roundToInt

// the views are scaled directly, as a higher density doesn't take effect on devices that override
// the app's DPI, like some e-ink phones
fun scaleView(view: View, factor: Float) {
    fun Int.scaled() = (this * factor).roundToInt();

    if (view is TextView)
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, view.textSize * factor);
    view.setPaddingRelative(
        view.paddingStart.scaled(), view.paddingTop.scaled(),
        view.paddingEnd.scaled(), view.paddingBottom.scaled()
    );

    val params = view.layoutParams;
    if (params != null) {
        // sizes that follow the content or the parent adjust by themselves
        if (params.width > 0)
            params.width = params.width.scaled();
        if (params.height > 0)
            params.height = params.height.scaled();
        if (params is ViewGroup.MarginLayoutParams) {
            val relative = params.isMarginRelative;
            val start = params.marginStart;
            val end = params.marginEnd;
            params.setMargins(
                params.leftMargin.scaled(), params.topMargin.scaled(),
                params.rightMargin.scaled(), params.bottomMargin.scaled()
            );
            if (relative) {
                params.marginStart = start.scaled();
                params.marginEnd = end.scaled();
            }
        }
        view.layoutParams = params;
    }

    if (view is ViewGroup)
        for (child in view.children) scaleView(child, factor);
}
