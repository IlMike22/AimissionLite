package com.example.aimissionlite.bindingadapter

import android.view.View
import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.example.aimissionlite.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

@BindingAdapter("android:getCheckedItem")
fun getCheckedItem(chipGroup: ChipGroup, selectedId: MutableLiveData<Int>) {
    chipGroup.setOnCheckedChangeListener { group, checkedId ->
        val checkedChipId = group.checkedChipIds.first()
        val selectedChip = group.findViewById<Chip>(checkedChipId)
        println("!!! hello from binding adapter. checked group id is ${group.id}")
        selectedId.value = checkedChipId
    }
}