@file:Suppress("DEPRECATION")

package com.boa.weathertest.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.boa.weathertest.R

/**
 * Base Fragment for using in Model-View-ViewModel architecture. Must be specified ViewState and ViewModel classes.
 */
abstract class BaseFragment<VS, VM : BaseViewModel<VS>> : Fragment() {
    lateinit var viewModel: VM
    private var progressDialog: ProgressDialog? = null
    var baseBinding: BaseFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseBinding = BaseFragmentBinding.inflate(inflater, container, false)
        //val view = inflater.inflate(getLayoutResource(), container, false)
        viewModel = initViewModel()
        viewModel.resourceViewStatus.observe(viewLifecycleOwner, viewStatusObserver)
        return baseBinding?.root as View? //view
    }

    private val viewStatusObserver = Observer<VS> {
        onViewStatusUpdated(it)
    }

    fun showLoading() {
        hideLoading()
        progressDialog = ProgressDialog(requireActivity())
        progressDialog?.setMessage(getString(R.string.loading))
        progressDialog?.show()
    }

    fun hideLoading() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    abstract fun initViewModel(): VM
    abstract fun getLayoutResource(): Int
    abstract fun onViewStatusUpdated(viewStatus: VS)
}
