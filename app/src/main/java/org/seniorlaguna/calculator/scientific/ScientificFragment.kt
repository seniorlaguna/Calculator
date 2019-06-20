package org.seniorlaguna.calculator.scientific

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_scientific.*
import org.seniorlaguna.calculator.MainActivity
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.customviews.ExtendedViewPager
import org.seniorlaguna.calculator.utils.showInstruction

class ScientificFragment(private val mainActivity: MainActivity) : Fragment() {

    val displayFragment : DisplayFragment = DisplayFragment(mainActivity)
    val historyFragment : HistoryFragment = HistoryFragment(mainActivity)
    val calculator: ScientificCalculator = ScientificCalculator(mainActivity, this, displayFragment, historyFragment)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_scientific, container, false)
        initKeyboard(root, keyboard1)
        initViewPager(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // show warning
        showInstruction(mainActivity, fragment_viewpager, R.string.instructor_intro3)
    }

    override fun onResume() {
        super.onResume()
        fragment_root.requestFocus()
        calculator.showCurrentResult()

        fragment_viewpager.canSwipe = mainActivity.scientificViewModel.settings.history

        // fix because changing item of viewpager results in
        // null pointer exception masked as illegal state exception
        Handler().post({if (!mainActivity.scientificViewModel.settings.history) fragment_viewpager.currentItem = ExtendedViewPager.DISPLAY_TAB})
    }

    private fun initViewPager(root : View) {

        root.findViewById<ViewPager>(R.id.fragment_viewpager).adapter = object : FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

            override fun getCount(): Int {
                return 2
            }


            override fun getItem(position: Int): Fragment {
                return when (position) {
                    ExtendedViewPager.HISTORY_TAB -> historyFragment
                    else -> displayFragment
                }

            }

        }

        root.findViewById<ViewPager>(R.id.fragment_viewpager).addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    ExtendedViewPager.DISPLAY_TAB -> mainActivity.setToolbarTitle(R.string.scientific_calculator_toolbar_title_display)
                    ExtendedViewPager.HISTORY_TAB -> mainActivity.setToolbarTitle(R.string.scientific_calculator_toolbar_title_history)
                }
            }
        })

        root.findViewById<ViewPager>(R.id.fragment_viewpager).currentItem = ExtendedViewPager.DISPLAY_TAB
    }

    private fun initKeyboard(root : View, keyboard : ArrayList<ArrayList<out ArrayList<out Any?>>>) {

        val size = resources.displayMetrics.widthPixels / 51 * 10
        val margin = (resources.displayMetrics.widthPixels - (5 * size)) / 10
        val table = root.findViewById<TableLayout>(R.id.fragment_keyboard)
        table.removeAllViews()

        keyboard.forEach { row ->

            val tableRow = TableRow(table.context)

            row.forEach { column ->
                val btn = Button(table.context)
                styleButton(btn, column[0] as Int, size, margin)
                btn.setOnClickListener { calculator.onClick(column[0] as Int, column[1] as String?) }
                tableRow.addView(btn)
            }

            table.addView(tableRow, TableLayout.LayoutParams().apply { setMargins(0); updateMargins(0,0,0,0) })
        }

    }

    fun styleButton(btn : Button, textRes : Int, size : Int, margin : Int) {
        btn.text = getString(textRes)
        btn.textSize = 22f
        btn.setTextColor(ContextCompat.getColor(mainActivity, android.R.color.white))
        btn.setBackgroundColor(ContextCompat.getColor(mainActivity, when(textRes) {
            R.string.math_equals -> android.R.color.holo_blue_light
            R.string.math_clear -> R.color.deleteAllColor
            R.string.math_switch_page -> android.R.color.holo_orange_dark
            else -> android.R.color.holo_blue_dark
        }))
        btn.isAllCaps = false
        val params = TableRow.LayoutParams(size, size)
        params.updateMargins(margin, margin, margin, margin)
        btn.layoutParams = params
    }

    // public interface

    fun setKeyboard(keyboard : ArrayList<ArrayList<out ArrayList<out Any?>>>) = initKeyboard(fragment_root, keyboard)
}