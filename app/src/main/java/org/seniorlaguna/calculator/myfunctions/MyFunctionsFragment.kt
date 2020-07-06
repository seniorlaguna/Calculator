package org.seniorlaguna.calculator.example

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_functions.*
import org.seniorlaguna.calculator.Function
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.myfunctions.FunctionAdapter

class MyFunctionsFragment : Fragment(), View.OnClickListener {

    companion object {
        const val TOOL_ID = 4
    }

    private val functionAdapter = FunctionAdapter(this)

    // view models
    protected lateinit var globalViewModel: GlobalViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // get view models
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        return inflater.inflate(R.layout.fragment_my_functions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        my_functions_fab.setOnClickListener(this)

        my_functions_recyclerview.layoutManager = LinearLayoutManager(this.context)
        my_functions_recyclerview.adapter = functionAdapter
        my_functions_recyclerview.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        globalViewModel.database.getAllFunctionsLive().observe(requireActivity(),
            Observer<List<Function>> { t -> if (t != null) functionAdapter.setData(t as ArrayList<Function>) })

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.my_functions_fab -> editFunction(Function("", ""), true)
            else -> editFunction(Function(
                v.findViewById<TextView>(R.id.functions_item_title).text.toString(),
                v.findViewById<TextView>(R.id.functions_item_expression).text.toString()),
                false
            )
        }
    }

    private fun editFunction(function: Function, newFunction: Boolean) {

        val view = layoutInflater.inflate(R.layout.function_item_edit, null)

        AlertDialog.Builder(requireContext()).apply {
            setView(view)

            val title = view.findViewById<EditText>(R.id.functions_item_title_edit)!!
            val expression = view.findViewById<EditText>(R.id.functions_item_expression_edit)!!

            title.text.clear()
            title.text.append(function.title)

            expression.text.clear()
            expression.text.append(function.expression)

            setNegativeButton("Delete") { _, _ ->

                if (!newFunction) {
                    globalViewModel.database.deleteFunction(
                        Function(
                            title.text.toString(),
                            expression.text.toString()
                        ))
                }

            }
            setPositiveButton(R.string.app_rating_request_yes) { _, _ ->

                if (title.text.toString().isNotEmpty() and expression.text.toString().isNotEmpty()) {

                    Function(
                        title.text.toString(),
                        expression.text.toString()
                    ).let {

                        when (newFunction) {
                            true -> {
                                try {
                                    globalViewModel.database.insertFunction(it)
                                } catch (e : SQLiteConstraintException) {
                                    Toast.makeText(requireContext(), "Duplicate of function ${title.text}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else -> globalViewModel.database.updateFunction(it)
                        }

                    }
                }
            }
            show()
        }
    }

}