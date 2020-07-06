package org.seniorlaguna.calculator.myfunctions;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView;
import org.seniorlaguna.calculator.Function
import org.seniorlaguna.calculator.R

class FunctionAdapter(private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<FunctionAdapter.FunctionViewHolder>() {

    private var functions = ArrayList<Function>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.function_item, parent, false)
        view.setOnClickListener(onClickListener)

        return FunctionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return functions.size
    }

    override fun onBindViewHolder(holder: FunctionViewHolder, position: Int) {
        holder.bind(functions[position])
    }

    fun setData(functions : ArrayList<Function>) {
        this.functions.apply {
            clear()
            addAll(functions)
        }
        notifyDataSetChanged()
    }

    inner class FunctionViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

        fun bind(function : Function) {
            view.findViewById<TextView>(R.id.functions_item_title).text = function.title
            view.findViewById<TextView>(R.id.functions_item_expression).text = function.expression
        }

    }
}
