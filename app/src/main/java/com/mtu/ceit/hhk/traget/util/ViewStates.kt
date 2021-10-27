package com.mtu.ceit.hhk.traget.util

import com.mtu.ceit.hhk.traget.data.model.Maintenance

sealed class MAIN_EVENT{
    object SHOW_DIALOG:MAIN_EVENT()
    object HIDE_DIALOG:MAIN_EVENT()
}

sealed class DIALOG_EVENT{
    data class SHOW_ERROR(val message:String):DIALOG_EVENT()
    data class BIND_EDITTEXT<T>(val data:T):DIALOG_EVENT()
}