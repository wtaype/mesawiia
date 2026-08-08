package com.mesawii.core.kidev

import com.mesawii.core.kicss.*


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

fun wicopy(context: Context, text: String, messenger: WiMessenger? = null, msg: String = "Copiado") {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("MesaWii", text)
    clipboard.setPrimaryClip(clip)
    
    if (messenger != null) {
        messenger.Mensaje(msg, WiMsgType.Success)
    } else {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}

