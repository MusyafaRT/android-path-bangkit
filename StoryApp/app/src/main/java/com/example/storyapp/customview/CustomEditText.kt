package com.example.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText : AppCompatEditText {
    private var inputListener: InputListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    fun setInputListener(listener: InputListener) {
        inputListener = listener
    }

    interface InputListener {
        fun onInputChanged(isValid: Boolean)
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                text: CharSequence?,
                start: Int,
                lengthBefore: Int,
                lengthAfter: Int
            ) {
                val input = text.toString()
                when (inputType) {
                    EMAIL_EDIT_TEXT -> {
                        if (!input.matches(emailRegex.toRegex())) {
                            setError("Format email yang digunakan salah", null)
                        } else {
                            error = null
                        }
                    }

                    PASSWORD_EDIT_TEXT -> {
                        if (input.length <= 8) {
                            setError("Password tidak boleh kurang dari 8 karakter", null)
                        } else {
                            error = null
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val input = text.toString()
                when (inputType) {
                    EMAIL_EDIT_TEXT -> {
                        if (!input.matches(emailRegex.toRegex())) {
                            setError("Format email yang digunakan salah", null)
                        } else {
                            error = null
                        }
                    }

                    PASSWORD_EDIT_TEXT -> {
                        if (input.length < 8) {
                            setError("Password tidak boleh kurang dari 8 karakter", null)
                        } else {
                            error = null
                        }
                    }
                }
            }

        })
    }


    companion object {
        const val emailRegex =
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
        const val EMAIL_EDIT_TEXT = 0x00000021
        const val PASSWORD_EDIT_TEXT = 0x00000081
    }
}