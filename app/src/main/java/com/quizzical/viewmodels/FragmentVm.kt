package com.quizzical.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.quizzical.activities.MainActivity
import com.quizzical.models.FragmentData

class FragmentVm : ViewModel() {

    companion object {
        val TAG = FragmentVm::class.java.simpleName

        fun get(activity: MainActivity) =
            ViewModelProviders.of(activity).get(FragmentVm::class.java)
    }

    val currentFragment = MutableLiveData<FragmentData>()

}