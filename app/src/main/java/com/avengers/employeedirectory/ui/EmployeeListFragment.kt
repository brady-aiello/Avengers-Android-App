package com.avengers.employeedirectory.ui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import coil.ImageLoader
import coil.metadata
import com.avengers.employeedirectory.R
import com.avengers.employeedirectory.databinding.EmployeeListFragmentBinding
import com.avengers.employeedirectory.databinding.EmployeeViewholderBinding
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.util.DataState
import com.avengers.employeedirectory.util.EmployeesStateEvent
import com.avengers.employeedirectory.util.EmployeesStateEvent.GetEmployeesEvent
import com.commit451.coiltransformations.facedetection.CenterOnFaceTransformation
import dagger.hilt.android.AndroidEntryPoint
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
    private var isTablet: Boolean = false
    private lateinit var binding: EmployeeListFragmentBinding

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isTablet = requireContext().resources.getBoolean(R.bool.isTablet)
        postponeEnterTransition()

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.employee_list_fragment, container, false)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.shared_image)
        return binding.root;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @ExperimentalCoroutinesApi
    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerViewAdapter = EmployeeRecyclerViewAdapter(imageLoader, viewModel = viewModel,
            centerOnFaceTransformation = centerOnFaceTransformation, onEmployeeClickListener = {
                    binding, employee -> navigate(binding, employee)
            })

        recyclerView.adapter = recyclerViewAdapter
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.itemList.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
        setupRecyclerView(binding.itemList)


        binding.swipeRefresh.setOnRefreshListener {
            viewModel.setStateEvent(GetEmployeesEvent(forced = true))
        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    if (recyclerViewAdapter.employees != dataState.data) {
                        recyclerViewAdapter.employees = dataState.data
                        recyclerViewAdapter.notifyDataSetChanged()
                    }
                    binding.itemList.visibility = RecyclerView.VISIBLE
                    binding.emptyEmployeesResponseTextView.visibility = AppCompatTextView.GONE
                    binding.shrugTextView.visibility = AppCompatTextView.GONE
                }
                is DataState.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.itemList.visibility = RecyclerView.GONE
                    var errorDialogTitle = "Sorry, we couldn't process your request"
                    if (dataState.exception is UnknownHostException) {
                        errorDialogTitle = "Network error. Make sure you are connected to internet."
                    }
                    createAlertDialog(errorDialogTitle)
                    binding.emptyEmployeesResponseTextView.visibility = AppCompatTextView.GONE
                    binding.shrugTextView.visibility = AppCompatTextView.GONE
                }
                is DataState.Empty -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.itemList.visibility = RecyclerView.GONE
                    binding.emptyEmployeesResponseTextView.visibility = AppCompatTextView.VISIBLE
                    binding.shrugTextView.visibility = AppCompatTextView.VISIBLE
                }
                is DataState.Searching -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.itemList.visibility = RecyclerView.GONE
                    binding.emptyEmployeesResponseTextView.visibility = AppCompatTextView.GONE
                    binding.shrugTextView.visibility = AppCompatTextView.GONE
                }
                is DataState.Loading -> {
                    binding.itemList.visibility = RecyclerView.GONE
                    binding.emptyEmployeesResponseTextView.visibility = AppCompatTextView.GONE
                    binding.shrugTextView.visibility = AppCompatTextView.GONE
                    binding.swipeRefresh.isRefreshing = true
                }
            }
            viewModel.currentEmployee.observe(viewLifecycleOwner) { employee ->
                Log.d(TAG, "onViewCreated: $employee")
            }
        }

        viewModel.setStateEvent(GetEmployeesEvent())
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun navigate(binding: EmployeeViewholderBinding, employee: Employee) {
        val key = binding.profilePicSmall.metadata?.memoryCacheKey
        viewModel.memCacheKey.value = key
        viewModel.currentEmployee.value = employee

        if (!isTablet) {
            val extras =
                FragmentNavigatorExtras(binding.profilePicSmall to "pic-${employee.uuid}",
                    binding.employeeViewHolderCard to "card-${employee.uuid}")
            val action =
                EmployeeListFragmentDirections.actionItemListFragmentToEmployeeDetailFragment()

            findNavController().navigate(action, extras)
        } else {

                val navHostFragment = childFragmentManager.findFragmentById(R.id.detail_fragment_container)
                        as MasterDetailNavHostFragment
                navHostFragment.navController.navigate(R.id.employeeDetailFragment)
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

    sealed class SortFilter() {
        object Sort: SortFilter()
        object Filter: SortFilter()
    }
}