package com.mtu.ceit.hhk.traget

class Utils {

    companion object {
        fun formateDate(it:Int):Pair<Int,Int> {
            val min = it%60
            val hour = it/60
            return Pair(hour,min)
        }
    }


}