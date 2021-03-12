package dev.tsnanh.myvku.views.timetable

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.google.android.material.chip.ChipGroup

@InverseBindingMethods(InverseBindingMethod(type = ChipGroup::class, attribute = "android:checkedButton", method = "getCheckedChipId"))
class TimetableChipGroupBindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("android:checkedButton")
        fun setCheckedChip(view: ChipGroup?, id: Int) {
            if (id != view?.checkedChipId) {
                view?.check(id)
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["android:onCheckedChanged", "android:checkedButtonAttrChanged"], requireAll = false)
        fun setChipsListeners(view: ChipGroup?, listener: ChipGroup.OnCheckedChangeListener?,
            attrChange: InverseBindingListener?) {
            if (attrChange == null) {
                view?.setOnCheckedChangeListener(listener)
            } else {
                view?.setOnCheckedChangeListener { group, checkedId ->
                    listener?.onCheckedChanged(group, checkedId)
                    attrChange.onChange()
                }
            }
        }
    }
}