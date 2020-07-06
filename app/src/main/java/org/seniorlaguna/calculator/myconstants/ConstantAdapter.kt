package org.seniorlaguna.calculator.myconstants;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView;
import org.seniorlaguna.calculator.Constant
import org.seniorlaguna.calculator.R

class ConstantAdapter(private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<ConstantAdapter.ConstantViewHolder>() {

    private var constants = ArrayList<Constant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.constant_item, parent, false)
        view.setOnClickListener(onClickListener)

        return ConstantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return constants.size
    }

    override fun onBindViewHolder(holder: ConstantViewHolder, position: Int) {
        holder.bind(constants[position])
    }

    fun setData(constants : ArrayList<Constant>) {
        this.constants.apply {
            clear()
            addAll(constants)
        }
        notifyDataSetChanged()
    }

    inner class ConstantViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

        fun bind(constant : Constant) {
            view.findViewById<TextView>(R.id.constants_item_title).text = constant.title
            view.findViewById<TextView>(R.id.constants_item_value).text = constant.value
        }

    }
}
