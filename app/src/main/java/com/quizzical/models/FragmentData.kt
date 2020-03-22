package com.quizzical.models

import android.os.Bundle
import com.quizzical.enums.FragmentTypes

class FragmentData(
    val fragmentTypes: FragmentTypes,
    val bundle: Bundle,
    val addToBackStack: Boolean = false
)