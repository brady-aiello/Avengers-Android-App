package com.avengers.employeedirectory.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.avengers.employeedirectory.R
import com.avengers.employeedirectory.models.Employee
import com.avengers.employeedirectory.util.EmployeesStateEvent
import kotlinx.coroutines.FlowPreview

class EmployeeRecyclerViewAdapter (
    private val imageLoader: ImageLoader, var employees: List<Employee> = listOf(),
    val viewModel: MainViewModel):
    RecyclerView.Adapter<EmployeeViewHolder>() {
    var isTablet: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_viewholder, parent, false)
        isTablet = parent.context.resources.getBoolean(R.bool.isTablet)
        return EmployeeViewHolder(view)
    }

    override fun getItemCount(): Int = employees.size

    @FlowPreview
    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]
        holder.employeePhoto.load(employee.photoUrlSmall, imageLoader) {
            memoryCacheKey(employee.photoUrlSmall)
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
        }
        holder.employeeName.text = "${employee.firstName} ${employee.lastName}"
        holder.employeeTeam.text = employee.team
        holder.itemView.setOnClickListener {

            viewModel.setStateEvent(EmployeesStateEvent.GetEmployeeDetailEvent(employees[position], isTablet))
        }
    }
}