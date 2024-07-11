package com.boa.weathertest.ui.help

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.findNavController
import com.boa.weathertest.R
import com.boa.weathertest.base.BaseFragment
import com.boa.weathertest.databinding.HelpFragmentBinding
import com.boa.weathertest.util.build
import com.boa.weathertest.util.toast
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File

class HelpFragment : BaseFragment<HelpViewStatus, HelpViewModel>() {
    private var binding: HelpFragmentBinding? = null
    override fun initViewModel(): HelpViewModel = getViewModel()

    override fun getLayoutResource(): Int = R.layout.help_fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HelpFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        viewModel?.initialize()
        binding?.helpWebView?.settings.build()
        binding?.helpWebView?.webViewClient = MyWebClient()
        binding?.helpWebView?.setOnKeyListener(OnKeyListener { _, keyCode, event ->
            try {
                if (event.action != KeyEvent.ACTION_DOWN) {
                    return@OnKeyListener true
                }

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed()
                    return@OnKeyListener true
                }
            } catch (_: Exception) {
                //Nothing
            }
            false
        })
        binding?.helpBackButton?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onViewStatusUpdated(viewStatus: HelpViewStatus) {
        if (viewStatus.url.isNotEmpty()) {
            binding?.helpWebView?.loadUrl(viewStatus.url)
        }
    }

    private fun onBackPressed() {
        requireActivity().findNavController(R.id.helpFragmentRoot)
            .navigate(R.id.navigation_action_help_to_home)
    }

    @Suppress("DEPRECATION")
    override fun onDestroy() {
        super.onDestroy()
        binding?.helpWebView?.clearHistory()
        binding?.helpWebView?.clearView()
        binding?.helpWebView?.removeAllViews()
        binding?.helpWebView?.destroy()
    }

    private inner class MyWebClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            hideLoading()
            super.onPageFinished(view, url)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            view.apply {
                File(requireActivity().cacheDir, "org.chromium.android_webview").let {
                    if (error?.errorCode == -2 && (!it.exists() || (it.listFiles()?.size
                            ?: 0) < 5)
                    ) {
                        requireActivity().toast(getString(R.string.no_internet))
                    }
                }
            }

            super.onReceivedError(view, request, error)
        }
    }
}