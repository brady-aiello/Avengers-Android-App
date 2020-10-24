package com.avengers.employeedirectory.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import coil.ImageLoader
import com.commit451.coiltransformations.facedetection.CenterOnFaceTransformation
import javax.inject.Inject

class MainFragmentFactory @Inject constructor(
    private val imageLoader: ImageLoader,
    private val centerOnFaceTransformation: CenterOnFaceTransformation): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            EmployeeListFragment::class.java.name -> {
                EmployeeListFragment(imageLoader, centerOnFaceTransformation)
            }
            EmployeeDetailFragment::class.java.name -> {
                EmployeeDetailFragment(imageLoader)
            }
            else -> {
                super.instantiate(classLoader, className)
            }
        }
    }
}