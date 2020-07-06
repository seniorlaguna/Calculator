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
import kotlinx.android.synthetic.main.fragment_my_constants.*
import org.seniorlaguna.calculator.Constant
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.myconstants.ConstantAdapter

class MyConstantsFragment : Fragment(), View.OnClickListener {

    companion object {
        const val TOOL_ID = 3
    }

    private val constantAdapter = ConstantAdapter(this)

    // view models
    protected lateinit var globalViewModel: GlobalViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // get view models
        globalViewModel = ViewModelProviders.of(this)[GlobalViewModel::class.java]

        return inflater.inflate(R.layout.fragment_my_constants, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        my_constants_fab.setOnClickListener(this)

        my_constants_recyclerview.layoutManager = LinearLayoutManager(this.context)
        my_constants_recyclerview.adapter = constantAdapter
        my_constants_recyclerview.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        globalViewModel.database.getAllConstantsLive().observe(requireActivity(),
            Observer<List<Constant>> { t -> if (t != null) constantAdapter.setData(t as ArrayList<Constant>) })

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.my_constants_fab -> editConstant(Constant("", ""), true)
            else -> editConstant(Constant(
                v.findViewById<TextView>(R.id.constants_item_title).text.toString(),
                v.findViewById<TextView>(R.id.constants_item_value).text.toString()),
                false
            )
        }
    }

    private fun editConstant(constant: Constant, newConstant : Boolean) {

        val view = layoutInflater.inflate(R.layout.constant_item_edit, null)

        AlertDialog.Builder(requireContext()).apply {
            setView(view)

            val title = view.findViewById<EditText>(R.id.constants_item_title_edit)!!
            val value = view.findViewById<EditText>(R.id.constants_item_value_edit)!!

            title.text.clear()
            title.text.append(constant.title)

            value.text.clear()
            value.text.append(constant.value)

            setNegativeButton(if (newConstant) getString(R.string.cancel) else getString(R.string.delete)) { _, _ ->

                if (!newConstant) {
                    globalViewModel.database.deleteConstant(
                        Constant(
                            title.text.toString(),
                            value.text.toString()
                        )
                    )
                }

            }
            setPositiveButton(R.string.app_rating_request_yes) { _, _ ->

                if (title.text.toString().isNotEmpty() and value.text.toString().isNotEmpty()) {

                    Constant(
                        title.text.toString(),
                        value.text.toString()
                    ).let {

                        when (newConstant) {
                            true -> {
                                try {
                                    globalViewModel.database.insertConstant(it)
                                } catch (e : SQLiteConstraintException) {
                                    Toast.makeText(requireContext(), "Duplicate of constant ${title.text}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else -> globalViewModel.database.updateConstant(it)
                        }

                    }
                }
            }
            show()
        }
    }

}