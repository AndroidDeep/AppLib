package com.self.lib.extension

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import splitties.systemservices.inputMethodManager

/**
 *
 * @author MBP
 * @date 2020/7/13
 */

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
fun EditText.avoidStatsWithZero() {
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

