package com.boa.weathertest.ui.help

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.findNavController
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.util.*
import kotlinx.android.synthetic.main.help_fragment.*
import kotlinx.android.synthetic.main.view_header.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File

class HelpFragment : BaseFragment<HelpViewStatus, HelpViewModel>() {
    override fun initViewModel(): HelpViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.help_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        viewModel.initialize()
        helpWebView?.settings.build()
        helpWebView?.webViewClient = MyWebClient()
        helpWebView?.setOnKeyListener(OnKeyListener { _, keyCode, event ->
            try {
                if (event.action != KeyEvent.ACTION_DOWN) {
                    return@OnKeyListener true
                }

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed()
                    return@OnKeyListener true
                }
            } catch (e: Exception) {
            }
            false
        })
        helpBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onViewStatusUpdated(viewStatus: HelpViewStatus) {
        if (viewStatus.url.isNotEmpty()) {
            helpWebView?.loadUrl(viewStatus.url)
        }
    }

    private fun onBackPressed() {
        requireActivity().findNavController(R.id.helpFragmentRoot)
            .navigate(R.id.navigation_action_help_to_home)
    }

    @Suppress("DEPRECATION")
    override fun onDestroy() {
        super.onDestroy()
        helpWebView?.clearHistory()
        helpWebView?.clearView()
        helpWebView?.removeAllViews()
        helpWebView?.destroy()
    }

    private inner class MyWebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            hideLoading()
            super.onPageFinished(view, url)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                view.apply {
                    File(requireActivity().cacheDir, "org.chromium.android_webview").let {
                        if (error?.errorCode == -2 && (!it.exists() || it.listFiles()?.size ?: 0 < 5)) {
                            requireActivity().toast(getString(R.string.no_internet))
                        }
                    }
                }
            } else {
                requireActivity().toast(getString(R.string.no_internet))
            }

            super.onReceivedError(view, request, error)
        }
    }
}