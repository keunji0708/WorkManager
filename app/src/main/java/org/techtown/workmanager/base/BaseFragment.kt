package org.techtown.workmanager.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment


open class BaseFragment : Fragment() {
    private var TAG: String? = BaseFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
    }

}