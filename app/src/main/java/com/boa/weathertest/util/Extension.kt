package com.boa.weathertest.util

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boa.weathertest.R
import com.boa.weathertest.base.OnSelectItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.ref.WeakReference

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Fragment?.receiveSafeString(key: String): String = try {
    this?.requireArguments()?.getString(key, "") ?: ""
} catch (e: Exception) {
    ""
}

fun RecyclerView?.build(context: WeakReference<Context>) {
    val layoutManager = LinearLayoutManager(context.get())
    layoutManager.orientation = LinearLayoutManager.VERTICAL
    this?.hasFixedSize()
    this?.layoutManager = layoutManager
    this?.addItemDecoration(
        DividerItemDecoration(
            context.get(),
            layoutManager.orientation
        )
    )
    this?.itemAnimator = object : DefaultItemAnimator() {
        override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
            dispatchAddFinished(holder)
            dispatchAddStarting(holder)
            return false
        }
    }
}

fun AppCompatSpinner.build(
    listener: OnSelectItem<String>? = null,
    context: WeakReference<Context>,
    selectedItem: String = "",
    list: List<String> = listOf()
) {
    val adapter = context.get()?.let { list.toArrayAdapter(it) }
    this.adapter = adapter

    if (selectedItem.isNotEmpty()) {
        val selectedPosition = adapter?.getPosition(selectedItem)

        if (selectedPosition != null) {
            this.setSelection(selectedPosition)
        }
    }

    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
            listener?.onSelectItem(list[position])
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }
}

@Suppress("DEPRECATION")
fun Context.toast(message: String) {
    Handler().post {
        val toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}

fun TextInputLayout?.getIntValue(): Int = try {
    Integer.valueOf(this?.editText?.text?.toString() ?: "") ?: 0
} catch (e: Exception) {
    0
}

fun List<String>?.toArrayAdapter(context: Context): ArrayAdapter<String> {
    val typeAdapter = ArrayAdapter(
        context,
        android.R.layout.simple_spinner_item,
        this ?: listOf()
    )
    typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    return typeAdapter
}

fun TextInputLayout?.getText() = this?.editText?.text.toString()

fun TextInputLayout?.checkLength(context: WeakReference<Context>): Boolean {
    return if (this.getText().isNotEmpty()) {
        this?.isErrorEnabled = false
        true
    } else {
        this?.isErrorEnabled = true
        this?.error = context.get()?.getString(R.string.required)
        false
    }
}

fun TextInputLayout?.addWatcherLength(context: WeakReference<Context>) {
    val txtInput = this
    this?.editText?.addTextChangedListener(object :
        CustomTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            txtInput.checkLength(context)
        }
    })
}

@Suppress("SetJavaScriptEnabled", "DEPRECATION")
fun WebSettings?.build() {
    this?.javaScriptEnabled = true
    this?.setRenderPriority(WebSettings.RenderPriority.HIGH)
    this?.loadWithOverviewMode = true
    this?.useWideViewPort = true
    this?.setSupportZoom(false)
    this?.builtInZoomControls = false
    this?.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    this?.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
}