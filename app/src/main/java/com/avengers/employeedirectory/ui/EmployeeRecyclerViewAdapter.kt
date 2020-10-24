package com.avengers.employeedirectory.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import com.avengers.employeedirectory.R
import com.avengers.employeedirectory.databinding.EmployeeViewholderBinding
import com.avengers.employeedirectory.models.Employee
import com.commit451.coiltransformations.facedetection.CenterOnFaceTransformation
import kotlinx.coroutines.FlowPreview

class EmployeeRecyclerViewAdapter(
    private val imageLoader: ImageLoader,
    var employees: List<Employee> = listOf(),
    val viewModel: MainViewModel,
    private val centerOnFaceTransformation: CenterOnFaceTransformation,
    private val onEmployeeClickListener: ((binding: EmployeeViewholderBinding, employee: Employee) -> Unit)?
):
    RecyclerView.Adapter<EmployeeViewHolder>() {

    private var isTablet: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = EmployeeViewholderBinding
            .inflate(LayoutInflater.from(parent.context),
         parent, false)
        isTablet = parent.context.resources.getBoolean(R.bool.isTablet)
        return EmployeeViewHolder(binding)
    }

    override fun getItemCount(): Int = employees.size

    @FlowPreview
    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]
        holder.bind(employee)

        holder.binding.profilePicSmall.load(employee.photoUrlSmall, imageLoader) {
            memoryCacheKey("small-${employee.photoUrlSmall}")
            transformations(centerOnFaceTransformation, RoundedCornersTransformation(8F))
        }

        holder.itemView.setOnClickListener {
            onEmployeeClickListener?.let { it(holder.binding, employee) }
        }
    }
}