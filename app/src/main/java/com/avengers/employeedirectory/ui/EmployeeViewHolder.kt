package com.avengers.employeedirectory.ui

import androidx.recyclerview.widget.RecyclerView
import com.avengers.employeedirectory.databinding.EmployeeViewholderBinding
import com.avengers.employeedirectory.models.Employee

class EmployeeViewHolder(val binding: EmployeeViewholderBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(employee: Employee) {
        binding.employee = employee
        binding.executePendingBindings()
    }
}