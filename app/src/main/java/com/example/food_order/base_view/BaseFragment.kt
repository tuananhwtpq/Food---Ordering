package com.example.food_order.base_view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.food_order.utils.extension.AppLogger
import com.example.food_order.utils.extension.handleBackPressed
import com.example.food_order.utils.extension.navigateUp
import com.example.food_order.utils.feat.Constants
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    companion object {
        const val TAG = Constants.TAG
    }

    private var _binding: VB? = null
    val binding get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB


    //Giống setcontentView trong activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    //Giống hàm onCreate trong activity
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeData()
        initListener()
        handleOnBackPressed()
    }

    private fun handleOnBackPressed() {
        requireActivity().handleBackPressed {
            onBack()
        }
    }

    open fun onBack() {
//        super.onDestroyView()
//        _binding = null
        navigateUp()
    }

    open fun setupView() {}
    open fun observeData() {}
    open fun initListener() {}


    fun launchCoroutine(
        dispatcher: CoroutineContext = EmptyCoroutineContext,
        action: suspend () -> Unit
    ) {
        if (view != null && isAdded) {
            viewLifecycleOwner.lifecycleScope.launch(dispatcher + coroutineExceptionHandler) {
                action()
            }
        }
    }

    fun launchCoroutineIO(action: suspend () -> Unit) {
        launchCoroutine(Dispatchers.IO) {
            action()
        }
    }

    fun launchCoroutineMain(action: suspend () -> Unit) {
        launchCoroutine(Dispatchers.Main) {
            action()
        }
    }

    fun delayToAction(delayTime: Long = 200L, action: () -> Unit) {
        launchCoroutine(Dispatchers.IO) {
            delay(delayTime)
            launchCoroutine(Dispatchers.Main) {
                action()
            }

        }
    }

    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }

    open fun handleError(throwable: Throwable) {
        val errorMessage = throwable.message ?: "Unknown Error"
        logError(errorMessage)
        throwable.printStackTrace()
    }


    fun logError(msg: String) =
//        AppLogger.e(TAG, "Fragment: ${this.javaClass.simpleName}")
        Log.e("Fragment: ${this.javaClass.simpleName}", "Error: $msg")

}