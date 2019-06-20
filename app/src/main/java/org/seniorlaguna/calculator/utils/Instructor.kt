package org.seniorlaguna.calculator.utils

import android.app.Activity
import android.view.View
import org.seniorlaguna.calculator.R
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape

fun showInstruction(activity: Activity, target : View, stringRes : Int) {
    MaterialShowcaseView.Builder(activity)
        .setShape(RectangleShape(0, 0))
        .setTarget(target)
        .setDismissText(R.string.instructor_got_it)
        .setContentText(stringRes)
        .setDelay(500)
        .singleUse("$stringRes")
        .show()
}

