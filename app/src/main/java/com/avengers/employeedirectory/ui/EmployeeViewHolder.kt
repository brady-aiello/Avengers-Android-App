package com.avengers.employeedirectory.ui

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.avengers.employeedirectory.R

class EmployeeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var employeePhoto: AppCompatImageView = itemView.findViewById(R.id.profile_pic_small)
    var employeeName: AppCompatTextView = itemView.findViewById(R.id.employee_full_name)
    var employeeTeam: AppCompatTextView = itemView.findViewById(R.id.team)
}