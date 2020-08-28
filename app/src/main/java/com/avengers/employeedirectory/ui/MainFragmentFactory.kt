package com.avengers.employeedirectory.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import coil.ImageLoader
import javax.inject.Inject

class MainFragmentFactory @Inject constructor(
    val imageLoader: ImageLoader): FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            EmployeeListFragment::class.java.name -> {
                EmployeeListFragment(imageLoader)
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