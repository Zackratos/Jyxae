package com.zackratos.jyxae.ext

import android.Manifest
import androidx.fragment.app.Fragment
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.PermissionBuilder

/**
 * @Author   : zhangwenchao
 * @Date     : 2021/7/5  7:51 PM
 * @Describe :
 */

fun Fragment.request(vararg permissions: String, block: () -> Unit) {
    PermissionX.init(this)
        .permissions(*permissions)
        .requestWithFilter(block)
}

fun Fragment.requestExternalStorage(block: () -> Unit) {
    request(Manifest.permission.WRITE_EXTERNAL_STORAGE, block = block)
}

fun PermissionBuilder.requestWithFilter(block: () -> Unit) {
    request { allGranted, _, _ ->
        if (allGranted) block()
    }
}