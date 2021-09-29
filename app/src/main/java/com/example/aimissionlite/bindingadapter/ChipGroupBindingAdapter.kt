package com.example.aimissionlite.bindingadapter

import androidx.core.view.get
import androidx.core.view.size
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

@BindingAdapter("android:setChipGroupItem")
fun ChipGroup.setSelectedChipGroupItem(selectedId: MutableLiveData<Int>?) {
    if (selectedId?.value == null) {
        return
    }

    val newValue = selectedId.value ?: return

    if (newValue > this.size) {
        return
    }

    this[newValue].isSelected = true
}