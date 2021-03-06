package org.seniorlaguna.calculator.tool.basic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.seniorlaguna.calculator.Calculation
import org.seniorlaguna.calculator.R

class CalculationAdapter(val onClickListener: View.OnClickListener, val onLongClickListener: View.OnLongClickListener) : RecyclerView.Adapter<CalculationAdapter.CalculationViewHolder>() {

    private var calculationList = ArrayList<Calculation>()

    override fun onBindViewHolder(holder: CalculationViewHolder, position: Int) {
        holder.bind(calculationList[position])
    }

    override fun getItemCount(): Int {
        return calculationList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calculator_history_item, parent, false)

        view.setOnClickListener(onClickListener)
        view.setOnLongClickListener(onLongClickListener)

        return CalculationViewHolder(view)
    }

    fun setData(calculations : ArrayList<Calculation>) {
        calculationList.apply {
            clear()
            addAll(calculations)
        }
        notifyDataSetChanged()
    }

    inner class CalculationViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

        val titleText = view.findViewById<TextView>(R.id.history_item_title)
        val calculationText = view.findViewById<TextView>(R.id.history_item_calculation)

        fun bind(calculation: Calculation) {
            view.id = calculation.id
            titleText.text = calculation.title
            calculationText.text = calculation.calculation
        }
    }
}