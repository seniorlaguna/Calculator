package org.seniorlaguna.calculator.tool.scientific

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import org.seniorlaguna.calculator.GlobalViewModel
import org.seniorlaguna.calculator.R
import org.seniorlaguna.calculator.customviews.ExtendedViewPager
import org.seniorlaguna.calculator.databinding.FragmentScientificBinding
import org.seniorlaguna.calculator.utils.showInstruction

open class ScientificFragment : Fragment() {

    companion object {
        const val TOOL_ID = 2
    }

    val displayFragment : DisplayFragment by lazy (::DisplayFragment)
    val historyFragment : HistoryFragment by lazy (::HistoryFragment)

    // view models
    protected lateinit var globalViewModel: GlobalViewModel
    protected lateinit var toolViewModel: ScientificViewModel

    // view binding
    private var _binding : FragmentScientificBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // get view models
        globalViewModel =  ViewModelProvider(requireActivity())[GlobalViewModel::class.java]
        toolViewModel = ViewModelProvider(this)[ScientificViewModel::class.java]

        _binding = FragmentScientificBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init view pager
        initViewPager(view)

        // show instructions
        showInstruction(requireActivity(), binding.fragmentViewpager, R.string.instructor_intro1)


    }

    override fun onResume() {
        super.onResume()
        binding.fragmentRoot.requestFocus()

        // set view pager swipeability
        binding.fragmentViewpager.canSwipe = toolViewModel.settings.history

        // set current view pager tab
        if (!toolViewModel.settings.history) {
            toolViewModel.viewPagerTab.value = ExtendedViewPager.DISPLAY_TAB
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewPager(root : View) {

        // find and init view pager
        root.findViewById<ViewPager>(R.id.fragment_viewpager).apply {

            // define different tabs
            adapter = object : FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

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

            // add page change listener
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    // propagate tab to view model
                    toolViewModel.viewPagerTab.value = position

                    // adopt toolbar title to tab
                    when (position) {
                        ExtendedViewPager.DISPLAY_TAB -> globalViewModel.toolbarTitle.value = (requireContext().getString(R.string.basic_calculator_toolbar_title_display))
                        ExtendedViewPager.HISTORY_TAB -> globalViewModel.toolbarTitle.value = (requireContext().getString(R.string.basic_calculator_toolbar_title_history))
                    }
                }
            })

            // set initial tab
            currentItem = ExtendedViewPager.DISPLAY_TAB

            // register observer
            toolViewModel.viewPagerTab.observe(this@ScientificFragment, Observer {
                currentItem = it
            })
        }

    }

}