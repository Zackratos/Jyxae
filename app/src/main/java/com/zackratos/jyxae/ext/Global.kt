package com.zackratos.jyxae.ext

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import android.widget.Toast
import com.zackratos.jyxae.App

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/5  9:07 PM
 * @Describe :
 */
val context: Context
    get() = App.application.applicationContext

val display: Display?
    get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.display
        }
        return (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    }

val displayMetrics: DisplayMetrics
    get() = context.resources.displayMetrics

val screenWidth: Int
    get() {
        val dm = displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display?.getRealMetrics(dm)
        } else {
            display?.getMetrics(dm)
        }
        return dm.widthPixels
    }

fun toast(content: String) {
    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
}