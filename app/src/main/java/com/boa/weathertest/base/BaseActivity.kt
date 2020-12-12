@file:Suppress("DEPRECATION")

package com.boa.weathertest.base

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.boa.weathertest.R

/**
 * Base Fragment for using in Model-View-ViewModel architecture. Must be specified ViewState and ViewModel classes.
 */
abstract class BaseActivity<VS, VM : BaseViewModel<VS>> : AppCompatActivity() {
    private lateinit var viewModel: VM
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = initViewModel()
        viewModel.resourceViewStatus.observe(this, viewStatusObserver)
        setContentView(getLayoutResource())
    }

    private val viewStatusObserver = Observer<VS> {
        onViewStatusUpdated(it)
    }

    fun showLoading() {
        hideLoading()
        progressDialog = ProgressDialog(this)
        progressDialog?.setMessage(getString(R.string.loading))
        progressDialog?.show()
    }

    private fun hideLoading() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    abstract fun initViewModel(): VM
    abstract fun getLayoutResource(): Int
    abstract fun onViewStatusUpdated(viewStatus: VS)
}
