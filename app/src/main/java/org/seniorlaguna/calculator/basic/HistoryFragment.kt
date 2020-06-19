package org.seniorlaguna.calculator.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_basic_history.*
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.customviews.ExtendedViewPager

open class HistoryFragment : Fragment(), View.OnClickListener, View.OnLongClickListener {

    protected open val calculationType = Calculation.TYPE_BASIC

    private val calculationAdapter =
        CalculationAdapter(this, this)

    // view models
    protected lateinit var globalViewModel: GlobalViewModel
    protected lateinit var toolViewModel: BasicViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // get view models
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]
        toolViewModel = ViewModelProviders.of(requireParentFragment())[BasicViewModel::class.java]

        return inflater.inflate(R.layout.fragment_basic_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        history.layoutManager = LinearLayoutManager(this.context)
        history.adapter = calculationAdapter
        history.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        globalViewModel.database.getAllCalculations(calculationType).observe(requireActivity(),
            Observer<List<Calculation>> { t -> if (t != null) calculationAdapter.setData(t as ArrayList<Calculation>) })

    }

    override fun onClick(v: View?) {
        toolViewModel.viewPagerTab.value = ExtendedViewPager.DISPLAY_TAB
        toolViewModel.insert(v?.findViewById<TextView>(R.id.history_item_calculation)?.text.toString(), true)
    }

    override fun onLongClick(v: View?): Boolean {

        if (v == null) return true

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(R.string.basic_calculator_history_dialog_title)
        builder.setItems(R.array.basic_calculator_history_dialog_options) { _, which ->

            val target = Calculation(
                v.id,
                v.findViewById<TextView>(R.id.history_item_title).text.toString(),
                v.findViewById<TextView>(R.id.history_item_calculation).text.toString(),
                calculationType
            )

            when (which) {
                0 -> {
                    toolViewModel.insert(target.calculation)
                    toolViewModel.viewPagerTab.value = ExtendedViewPager.DISPLAY_TAB
                }
                1 -> createEditDialog(R.string.basic_calculator_history_dialog_rename, target, true)
                2 -> createEditDialog(R.string.basic_calculator_history_dialog_modify, target, false)
                3 -> globalViewModel.database.deleteCalculation(target)
            }
        }
        builder.show()

        return true
    }

    private fun createEditDialog(titleRes : Int, calculation: Calculation, renameDialog : Boolean) {

        val input = EditText(requireActivity())
        input.text.insert(0, if (renameDialog) calculation.title else calculation.calculation)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(titleRes)
        builder.setView(input)
        builder.setPositiveButton(R.string.basic_calculator_history_dialog_yes) { _, _ ->

            if (renameDialog) {
                calculation.title = input.text.toString()
            } else {
                calculation.calculation = input.text.toString()
            }

            globalViewModel.database.updateCalculation(calculation)
        }
        builder.setNegativeButton(R.string.basic_calculator_history_dialog_no, null)
        builder.show()
    }

}