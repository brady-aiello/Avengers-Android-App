package com.avengers.employeedirectory.ui

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import coil.ImageLoader
import coil.load
import com.google.android.material.button.MaterialButton
import com.avengers.employeedirectory.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EmployeeDetailFragment constructor(private val imageLoader: ImageLoader) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_employee_detail, container, false)

        if (viewModel.currentEmployee.value == null) {
            changeViewsVisibility(view, false)
        }
        viewModel.currentEmployee.observe(viewLifecycleOwner, Observer { employee ->
            if (employee != null) {
                changeViewsVisibility(view, true)
                view.findViewById<AppCompatImageView>(R.id.profile_pic_large)
                    .load(employee.photoUrlLarge, imageLoader) {
                        crossfade(200)
                        placeholder(R.drawable.ic_launcher_foreground)
                    }
                view.findViewById<AppCompatTextView>(R.id.employee_full_name).text =
                    "${employee.firstName} ${employee.lastName}"
                view.findViewById<AppCompatTextView>(R.id.team).text = employee.team
                view.findViewById<AppCompatTextView>(R.id.employee_type).text =
                        transformEmployeeType(employee.employeeType)
                view.findViewById<AppCompatTextView>(R.id.phone_number).text = employee.phoneNumber
                view.findViewById<AppCompatTextView>(R.id.email).text = employee.emailAddress
                view.findViewById<AppCompatTextView>(R.id.biography).text = employee.biography
                view.findViewById<MaterialButton>(R.id.give_kudos_button).setOnClickListener {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/*"

                    val packageManager = (activity as AppCompatActivity).packageManager
                    val resInfo = packageManager.queryIntentActivities(shareIntent, 0)
                    var linkedIn = false
                    resInfo.forEach {
                        val packageName = it.activityInfo.packageName
                        val activityName = it.activityInfo.name
                        if (activityName ==
                                "com.linkedin.android.publishing.sharing.SharingDeepLinkActivity") {
                            linkedIn = true
                            val intent = Intent()
                            intent.component = ComponentName(packageName, it.activityInfo.name)
                            intent.action = Intent.ACTION_SEND
                            shareIntent.type = "text/*"
                            intent.putExtra(Intent.EXTRA_TEXT,
                                    "@${employee.firstName} ${employee.lastName} has been " +
                                            "doing a fantastic job! Thanks for being on the team.")
                            startActivity(intent)
                        }
                    }
                    if (!linkedIn) {
                        Toast.makeText(activity, "LinkedIn is not installed",
                                Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                changeViewsVisibility(view, false)
            }
        })
        return view
    }

    private fun changeViewsVisibility(view: View, visible: Boolean) {
        view.findViewById<CardView>(R.id.profile_photo_card).visibility =
                if (visible) CardView.VISIBLE else CardView.GONE
        view.findViewById<AppCompatImageView>(R.id.profile_pic_large).visibility =
                if (visible) AppCompatImageView.VISIBLE else AppCompatImageView.GONE
        view.findViewById<AppCompatTextView>(R.id.employee_full_name).visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.findViewById<AppCompatTextView>(R.id.team).visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.findViewById<AppCompatTextView>(R.id.employee_type).visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.findViewById<AppCompatTextView>(R.id.phone_number).visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.findViewById<AppCompatTextView>(R.id.email).visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.findViewById<AppCompatTextView>(R.id.biography).visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.findViewById<MaterialButton>(R.id.give_kudos_button).visibility =
                if (visible) MaterialButton.VISIBLE else MaterialButton.GONE
    }

    private fun transformEmployeeType(employeeType: String): String {
        return when (employeeType) {
            "FULL_TIME" -> "full time employee"
            "PART_TIME" -> "part time employee"
            "CONTRACTOR" -> "contractor"
            else -> employeeType
        }
    }
}