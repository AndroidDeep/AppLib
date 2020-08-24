package com.self.lib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText @JvmOverloads constructor(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(mContext, attrs, defStyleAttr) {

    private var isShowPwd = false

    init {
        inputType = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
    }

    private fun setPasswordVisibility(showPwd: Boolean) {
        isShowPwd = showPwd
        inputType = if (isShowPwd) {
            // 可视密码输入
            EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            // 非可视密码状态
            EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
        }

        // 移动光标
        setSelection(text?.length ?: 0)
    }

    fun changePwdVisibility(): Boolean {
        setPasswordVisibility(!isShowPwd)
        return isShowPwd
    }
}