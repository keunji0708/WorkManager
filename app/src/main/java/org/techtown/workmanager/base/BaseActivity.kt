package org.techtown.workmanager.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import org.techtown.workmanager.BaseApplication
import java.text.SimpleDateFormat
import java.util.*


open class BaseActivity : AppCompatActivity() {
    private var TAG: String? = BaseActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStart() {
        Log.v(TAG, "onStart")
        super.onStart()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    fun showProgressDialog() {
        Log.d(this::class.simpleName, "showProgressDialog")
        BaseApplication.getInstance()!!.showProgressDialog(this)
    }

    fun hideProgressDialog() {
        Log.d(this::class.simpleName, "hideProgressDialog")
        BaseApplication.getInstance()!!.hideProgressDialog()
    }


    fun getTime(type : String): String{
        val today = Calendar.getInstance().time
        var result = ""
        when (type) {
            "dateTime" -> {
                result  = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(today)
            }
            "yyyymm" -> {
                result  = SimpleDateFormat("yyyy-MM", Locale.KOREA).format(today)
            }
            "date" -> {
                result  = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(today)
            }
        }

        return result
    }

    fun keyboardHide(){
        try {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            Log.d(TAG, "keyboardHide error : " + e.message)
        }
    }

}