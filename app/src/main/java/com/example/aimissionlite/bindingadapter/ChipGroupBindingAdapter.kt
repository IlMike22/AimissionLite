package com.example.aimissionlite.bindingadapter

import androidx.core.view.get
import androidx.databinding.BindingAdapter
import com.example.aimissionlite.data.ChipSelectionException
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.MutableStateFlow

@BindingAdapter("android:getSelectedItem") //todo really needed this adapter?
fun getSelectedChipGroupItem(chipGroup: ChipGroup, selectedId: MutableStateFlow<Int?>) {
    chipGroup.setOnCheckedChangeListener { group, _ ->

        if (group.checkedChipIds.isEmpty()) {
            return@setOnCheckedChangeListener
        }

        val checkedChipId = group.checkedChipIds.first()
        selectedId.value = checkedChipId
    }
}

@BindingAdapter("android:setChipGroupItem")
fun ChipGroup.setSelectedChipGroupItem(selectedId: MutableStateFlow<Int?>) {
    val selectedChipId = selectedId.value ?: return

    if (selectedChipId >= this.childCount || selectedChipId <= -1) {
        return
    }

    try {
        val chip = this[selectedChipId]
        if (chip is Chip) {
            chip.isChecked = true
        }

    } catch (exception: Exception) {
        throw ChipSelectionException(exception.message)
    }
}