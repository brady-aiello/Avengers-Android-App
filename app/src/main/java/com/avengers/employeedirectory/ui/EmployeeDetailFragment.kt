package com.avengers.employeedirectory.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.load
import com.avengers.employeedirectory.R
import com.avengers.employeedirectory.async.DispatcherProvider
import com.avengers.employeedirectory.databinding.FragmentEmployeeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EmployeeDetailFragment constructor(private val imageLoader: ImageLoader) : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var coroutineDispatcherProvider: DispatcherProvider

    private var isTablet = false
    private lateinit var binding: FragmentEmployeeDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalCoilApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isTablet = requireContext().resources.getBoolean(R.bool.isTablet)
        val employee = viewModel.currentEmployee.value
        if (employee != null) {
            binding.employee = employee
            binding.employeeType = transformEmployeeType(employee.employeeType)
            binding.executePendingBindings()
            loadImage(binding.profilePicLarge)
        }
        if (!isTablet) {
            sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image)
        }
        binding.executePendingBindings()
    }

    @ExperimentalCoilApi
    private fun loadImage(imageView: ImageView) {
        imageView
            .load(binding.employee?.photoUrlLarge, imageLoader) {
                if (!isTablet) {
                    val key = viewModel.memCacheKey.value
                    if (key != null) {
                        placeholderMemoryCacheKey(key)
                    }
                }
                memoryCacheKey("large-${binding.employee?.photoUrlLarge}")
            }
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

@BindingAdapter("app:visibilityBy")
fun <T> nonNullVisibility(view: View, item: T?) {
    view.visibility = if (item != null) View.VISIBLE else View.GONE
}

@BindingAdapter("app:visibilityBy")
fun <T> transitionName(view: View, item: T?) {
    view.visibility = if (item != null) View.VISIBLE else View.GONE
}