package com.example.aimissionlite.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.ChipGroup

@BindingAdapter("android:getSelectedItem")
fun getSelectedChipGroupItem(chipGroup: ChipGroup, selectedId: MutableLiveData<Int>) {
    chipGroup.setOnCheckedChangeListener { group, _ ->
        val checkedChipId = group.checkedChipIds.first()
        selectedId.value = checkedChipId
    }
}