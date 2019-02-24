package org.seniorlaguna.calculator

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

class HistoryFragment() : Fragment() {

    companion object {
        lateinit var listView : ListView
    }

    var activity : MainActivity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        listView = view.findViewById(R.id.list_history)
        listView.adapter = activity?.mResultHistory
        listView.setOnItemClickListener(activity)
        listView.setOnItemLongClickListener(activity)
        return view
    }

    fun setMainActivity(pActivity: MainActivity) {
        activity = pActivity
    }

}