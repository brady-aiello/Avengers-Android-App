package com.avengers.employeedirectory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionInflater
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.load
import coil.memory.MemoryCache
import com.avengers.employeedirectory.R
import com.avengers.employeedirectory.async.DispatcherProvider
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_employee_detail.*
import kotlinx.android.synthetic.main.fragment_employee_detail.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class EmployeeDetailFragment constructor(private val imageLoader: ImageLoader) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    @Inject
    lateinit var coroutineDispatcherProvider: DispatcherProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_employee_detail, container, false)
        changeViewsVisibility(view, false)
        return view
    }

    @ExperimentalCoilApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setSharedElementTransitionOnEnter()

        viewModel.currentEmployee.observe(viewLifecycleOwner, Observer { employee ->
            if (employee != null) {
                changeViewsVisibility(view, true)
                //postponeEnterTransition()
                requireActivity().lifecycleScope.launch(coroutineDispatcherProvider.io()) {
                    profile_pic_large
                        .load(employee.photoUrlLarge, imageLoader) {
                            if (imageLoader.memoryCache[MemoryCache.Key(employee.photoUrlLarge)] == null) {
                                crossfade(1000)
                                placeholder(R.drawable.ic_launcher_foreground)
                                memoryCacheKey(employee.photoUrlLarge)
                            }
                        }.await()
                    //startPostponedEnterTransition()
                }
                employee_full_name.text =
                        "${employee.firstName} ${employee.lastName}"
                team.text = employee.team
                employee_type.text = transformEmployeeType(employee.employeeType)
                phone_number.text = employee.phoneNumber
                email.text = employee.emailAddress
                biography.text = "\"${employee.biography}\""
                give_kudos_button.visibility = MaterialButton.GONE
            } else {
                changeViewsVisibility(view, false)
            }
        })
    }

    private fun changeViewsVisibility(view: View, visible: Boolean) {
        view.profile_photo_card.visibility =
                if (visible) CardView.VISIBLE else CardView.GONE
        view.profile_pic_large.visibility =
                if (visible) AppCompatImageView.VISIBLE else AppCompatImageView.GONE
        view.employee_full_name.visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.team.visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.employee_type.visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.phone_number.visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.email.visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.biography.visibility =
                if (visible) AppCompatTextView.VISIBLE else AppCompatTextView.GONE
        view.give_kudos_button.visibility =
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

    private fun setSharedElementTransitionOnEnter() {
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.shared_element_transition)
    }
}