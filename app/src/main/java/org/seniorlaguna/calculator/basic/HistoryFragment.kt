package org.seniorlaguna.calculator.basic

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_basic.*
import kotlinx.android.synthetic.main.fragment_basic_history.*
import org.seniorlaguna.calculator.MainActivity
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.basic.db.Calculation
import org.seniorlaguna.calculator.basic.db.CalculationAdapter
import org.seniorlaguna.calculator.customviews.ExtendedViewPager

class HistoryFragment(private val mainActivity: MainActivity) : Fragment(), View.OnClickListener, View.OnLongClickListener {

    private val calculationAdapter = CalculationAdapter(mainActivity)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        history.layoutManager = LinearLayoutManager(this.context)
        history.adapter = calculationAdapter
        history.addItemDecoration(DividerItemDecoration(mainActivity, DividerItemDecoration.VERTICAL))

        mainActivity.basicViewModel.getAll().observe(mainActivity, object : Observer<List<Calculation>> {
            override fun onChanged(t: List<Calculation>?) {
                if (t != null) calculationAdapter.setData(t as ArrayList<Calculation>)

            }
        })

    }

    override fun onClick(v: View?) {
        mainActivity.basicFragment.fragment_viewpager.currentItem = ExtendedViewPager.DISPLAY_TAB
        mainActivity.basicFragment.calculator.replace(v?.findViewById<TextView>(R.id.history_item_calculation)?.text.toString())
    }

    override fun onLongClick(v: View?): Boolean {

        if (v == null) return true

        val builder = AlertDialog.Builder(mainActivity)
        builder.setTitle(R.string.basic_calculator_history_dialog_title)
        builder.setItems(R.array.basic_calculator_history_dialog_options, object : DialogInterface.OnClickListener {

            override fun onClick(dialog: DialogInterface?, which: Int) {

                val target = Calculation(v.id,
                    v.findViewById<TextView>(R.id.history_item_title).text.toString(),
                    v.findViewById<TextView>(R.id.history_item_calculation).text.toString())


                when (which) {
                    0 -> {
                        mainActivity.basicFragment.calculator.insert(target.calculation)
                        mainActivity.basicFragment.fragment_viewpager.currentItem = ExtendedViewPager.DISPLAY_TAB
                    }
                    1 -> createEditDialog(R.string.basic_calculator_history_dialog_rename, target, true)
                    2 -> createEditDialog(R.string.basic_calculator_history_dialog_modify, target, false)
                    3 -> mainActivity.basicViewModel.delete(target)
                }
            }
        })
        builder.show()

        return true
    }

    private fun createEditDialog(titleRes : Int, calculation: Calculation, renameDialog : Boolean) {

        val input = EditText(mainActivity)
        input.text.insert(0, if (renameDialog) calculation.title else calculation.calculation)

        val builder = AlertDialog.Builder(mainActivity)
        builder.setTitle(titleRes)
        builder.setView(input)
        builder.setPositiveButton(R.string.basic_calculator_history_dialog_yes, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {

                if (renameDialog) {
                    calculation.title = input.text.toString()
                }
                else {
                    calculation.calculation = input.text.toString()
                }

                mainActivity.basicViewModel.updateCalculation(calculation)
            }
        })
        builder.setNegativeButton(R.string.basic_calculator_history_dialog_no, null)
        builder.show()
    }

}