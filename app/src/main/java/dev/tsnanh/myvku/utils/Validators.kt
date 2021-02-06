package dev.tsnanh.myvku.utils

import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputEditText

fun String.isThreadTitleValidLength(): Boolean = this.trim().length > 10

fun String.isReplyContentIsValidLength(): Boolean = this.trim().length > 25

fun TextInputEditText.validate(messageError: String, validator: (String) -> Boolean) {
    this.doAfterTextChanged {
        this.error = if (validator(it.toString())) null else messageError
    }
}

