package com.avengers.employeedirectory.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import coil.ImageLoader
import com.avengers.employeedirectory.R
import com.avengers.employeedirectory.util.DataState
import com.avengers.employeedirectory.util.EmployeesStateEvent
import com.avengers.employeedirectory.util.EmployeesStateEvent.GetEmployeesEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.employee_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.net.UnknownHostException

@AndroidEntryPoint
class EmployeeListFragment
constructor(private val imageLoader: ImageLoader,
            private val centerOnFaceTransformation: CenterOnFaceTransformation)
    : Fragment() {

    companion object {
        private const val TAG = "EmployeeListFragment"
    }

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var recyclerViewAdapter: EmployeeRecyclerViewAdapter
    //private lateinit var root: View
    private var isTablet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isTablet = requireContext().resources.getBoolean(R.bool.isTablet)
        return inflater.inflate(R.layout.employee_list_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //this.root = view
        setupRecyclerView(item_list)
        swipe_refresh.setOnRefreshListener {
            viewModel.setStateEvent(GetEmployeesEvent(forced = true))
        }
        //setExitToFullScreenTransition()
        //setReturnFromFullScreenTransition()
        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    swipe_refresh.isRefreshing = false
                    if (recyclerViewAdapter.employees != dataState.data) {
                        recyclerViewAdapter.employees = dataState.data
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                    item_list.visibility = RecyclerView.VISIBLE
                    empty_employees_response_text_view.visibility = AppCompatTextView.GONE
                    shrug_text_view.visibility = AppCompatTextView.GONE
                }
                is DataState.Error -> {
                    swipe_refresh.isRefreshing = false
                    item_list.visibility = RecyclerView.GONE
                    var errorDialogTitle = "Sorry, we couldn't process your request"
                    if (dataState.exception is UnknownHostException) {
                        errorDialogTitle = "Network error. Make sure you are connected to internet."
                    }
                    createAlertDialog(errorDialogTitle)
                    empty_employees_response_text_view.visibility = AppCompatTextView.GONE
                    shrug_text_view.visibility = AppCompatTextView.GONE
                }
                is DataState.Empty -> {
                    swipe_refresh.isRefreshing = false
                    item_list.visibility = RecyclerView.GONE
                    empty_employees_response_text_view.visibility = AppCompatTextView.VISIBLE
                    shrug_text_view.visibility = AppCompatTextView.VISIBLE
                }
                is DataState.Searching -> {
                    swipe_refresh.isRefreshing = false
                    item_list.visibility = RecyclerView.GONE
                    empty_employees_response_text_view.visibility = AppCompatTextView.GONE
                    shrug_text_view.visibility = AppCompatTextView.GONE
                }
                is DataState.Loading -> {
                    item_list.visibility = RecyclerView.GONE
                    empty_employees_response_text_view.visibility = AppCompatTextView.GONE
                    shrug_text_view.visibility = AppCompatTextView.GONE
                    swipe_refresh.isRefreshing = true
                }
            }
            viewModel.currentEmployee.observe(viewLifecycleOwner) { employee ->
                Log.d(TAG, "onViewCreated: $employee")
            }
        }

        viewModel.oneTimeNavigateEvent.observe(viewLifecycleOwner) { event ->
            val getEmployeeEvent = event.getContentIfNotHandled()
            if (getEmployeeEvent != null) {
                if (!isTablet) {
                    val action =
                            EmployeeListFragmentDirections.actionItemListFragmentToEmployeeDetailFragment()
                    view.findNavController().navigate(action)
                } else {
                    if (viewModel.currentEmployee.value == null) {
                        val navHostFragment = childFragmentManager.findFragmentById(R.id.detail_fragment_container)
                                as MasterDetailNavHostFragment

                        navHostFragment.navController.navigate(R.id.employeeDetailFragment)
                    }
                }
            }
        }

        viewModel.setStateEvent(GetEmployeesEvent())
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerViewAdapter = EmployeeRecyclerViewAdapter(imageLoader, viewModel = viewModel,
            centerOnFaceTransformation = centerOnFaceTransformation, onEmployeeClickListener = null)

        recyclerView.adapter = recyclerViewAdapter
        //postponeEnterTransition()
        recyclerView.viewTreeObserver.addOnPreDrawListener {
            //startPostponedEnterTransition()
            true
        }
    }

    @FlowPreview
    private fun createAlertDialog(errorTitle: String) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("Try again"
                ) { _, _ ->
                    viewModel.setStateEvent(GetEmployeesEvent())
                }
                setTitle(errorTitle)
            }
            builder.create()
        }
        alertDialog?.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        activity?.menuInflater?.inflate(R.menu.main_menu, menu)
    }

    @FlowPreview
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val searchView = item.actionView as SearchView
                searchView.queryHint = "Who are you looking for?"
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        if (!p0.isNullOrEmpty()) {
                            viewModel.setStateEvent(EmployeesStateEvent.FilterEmployeesByAnyEvent(p0))
                        }
                        else {
                            viewModel.setStateEvent(GetEmployeesEvent(forced = false))
                        }
                        return true
                    }
                })
            }
            R.id.action_sort -> {
                createAlertDialog(SortFilter.Sort)
            }
        }
        return true
    }

    @FlowPreview
    private fun createAlertDialog(type: SortFilter) {
        val items = arrayOf("Last Name", "First Name", "Team")
        val title = if (type is SortFilter.Sort) "Sort" else "Filter"
        AlertDialog.Builder(activity)
            .setTitle(title)
            .setSingleChoiceItems(items, 0, null)
            .setPositiveButton("Okay") { dialog, _ ->
                dialog.dismiss()
                val selectedPosition =
                    (dialog as AlertDialog).listView.checkedItemPosition
                when (selectedPosition) {
                     0 -> {
                         if (type is SortFilter.Sort) {
                             viewModel
                                 .setStateEvent(EmployeesStateEvent.GetEmployeesSortedByLastNameEvent)
                         }
                     }
                    1 -> {
                        if (type is SortFilter.Sort) {
                            viewModel
                                .setStateEvent(EmployeesStateEvent.GetEmployeesSortedByFirstNameEvent)
                        }
                    } else -> {
                        if (type is SortFilter.Sort) {
                            viewModel
                                .setStateEvent(EmployeesStateEvent.GetEmployeesSortedByTeamEvent)
                        }
                    }
                }
            }
            .show()
    }

    private fun setExitToFullScreenTransition() {
        exitTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.employee_list_exit_transition)
    }

    private fun setReturnFromFullScreenTransition() {
        reenterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.employee_list_return_transition)
    }
    sealed class SortFilter() {
        object Sort: SortFilter()
        object Filter: SortFilter()
    }
}