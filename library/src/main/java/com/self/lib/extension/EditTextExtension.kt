package com.self.lib.extension

import android.text.InputFilter
import android.text.Spanned
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import splitties.systemservices.inputMethodManager
import java.util.regex.Pattern

fun EditText.setClearIconView(icon: ImageView) {
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            icon.isGone = text.isNullOrEmpty()
        }
    }

    doOnTextChanged { text, _, _, _ ->
        if (isFocused)
            icon.isGone = text.isNullOrEmpty()
    }

    icon.setOnClickListener {
        text = null
    }
}

fun EditText.showSoftBoard() {
    requestFocus()
    postDelayed({ inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_FORCED) }, 300L)
}

/**
 * 过滤掉输入数字时以0开头且不为小数的情况，自动删除前面的0
 * @receiver EditText
 */
fun EditText.avoidStartsWithZero() {
    doOnTextChanged { text, _, _, _ ->
        text?.toString()?.let {
            if (it.length > 1 && it.startsWith("0") && !it.startsWith("0.")) {
                this.text = it.substring(1).toEditable()
                setSelection(this.length())
                return@doOnTextChanged
            }
        }
    }
}

fun EditText.setMaxDecimalDigits(digits:Int){
    filters = filters.toMutableList().run {
        add(DecimalDigitsInputFilter(digits))
        toTypedArray()
    }
}

private class DecimalDigitsInputFilter(digitsAfterZero: Int) : InputFilter {

    private var mPattern = Pattern.compile("[0-9]{0,10}+((\\.[0-9]{0,$digitsAfterZero})?)|(\\.)?")

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val matcher = mPattern.matcher(dest?.subSequence(0, dstart).toString() + source?.subSequence(start, end).toString() + dest?.subSequence(dend, dest.length).toString())
        return if (!matcher.matches())
            ""
        else
            null
    }
}

