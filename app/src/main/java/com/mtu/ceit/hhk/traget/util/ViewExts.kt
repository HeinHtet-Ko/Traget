package com.mtu.ceit.hhk.traget.util

import android.os.Build
import android.text.Editable
import android.widget.EditText
import android.widget.TimePicker
import androidx.appcompat.widget.SearchView
import com.google.android.material.timepicker.MaterialTimePicker
import com.mtu.ceit.hhk.traget.BuildConfig


inline fun SearchView.onQueryChanged(crossinline lambda:(String) -> Unit) {

    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(p0: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(query: String?): Boolean {
            lambda.invoke(query.orEmpty())
            return true
        }

    })

}

inline fun EditText.setext(str:String?){
    this.text = Editable.Factory().newEditable(str?:"")
}

inline fun TimePicker.getTime():Pair<Int,Int>{

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

    }
    return Pair(1,1)
}