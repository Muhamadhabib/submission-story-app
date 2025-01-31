package com.demuha.submission01.component

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomTextInput: TextInputEditText {
    enum class ValidationType {
        EMAIL,
        PASSWORD,
        TEXT
    }
    private var validationType: ValidationType = ValidationType.TEXT

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        getInputTypes()
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validate(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validate(text: String) {
        val parent = parent.parent as? TextInputLayout
        parent?.error = when (validationType) {
            ValidationType.EMAIL -> validateEmail(text)
            ValidationType.PASSWORD -> validatePassword(text)
            ValidationType.TEXT -> validateText(text)
        }
    }

    private fun getInputTypes() {
        val inputTypeValue = inputType
        validationType = when (inputTypeValue) {
            (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) -> ValidationType.EMAIL
            (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) -> ValidationType.PASSWORD
            else -> ValidationType.TEXT
        }
    }

    private fun validateEmail(email: String) = when {
        email.isEmpty() -> "Email tidak boleh kosong"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Format email kurang tepat"
        else -> null
    }
    private fun validatePassword(password: String) = when {
        password.isEmpty() -> "Password tidak boleh kosong"
        password.length < 8 -> "Password minimal 8 karakter"
        else -> null
    }
    private fun validateText(text: String) = when {
        text.isEmpty() -> "Field tidak boleh kosong"
        else -> null
    }

    fun isValid(): Boolean {
        val text = text.toString()
        return when (validationType) {
            ValidationType.EMAIL -> text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(text).matches()
            ValidationType.PASSWORD ->text.isNotEmpty() && text.length >= 8
            ValidationType.TEXT -> text.isNotEmpty()
        }
    }
}