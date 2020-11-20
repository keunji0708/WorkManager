package org.techtown.workmanager

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog


class BaseApplication : Application() {

    var progressDialog: AppCompatDialog? = null

    companion object {
        private var TAG: String? = BaseApplication::class.java.simpleName
        private var mInstance: BaseApplication? = null
        private var currentActivity: Activity? = null

        @Synchronized
        fun getInstance(): BaseApplication? {
            if (mInstance == null) {
                mInstance = BaseApplication()
            }

            return mInstance
        }

        fun getCurrentActivity(): Activity? {
            Log.d(TAG, "currentActivity : " + if (currentActivity != null) currentActivity!!::class.java.simpleName else "")

            return currentActivity
        }

        fun setCurrentActivity(currentActivity: Activity) {
            BaseApplication.currentActivity = currentActivity
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    override fun onTerminate() {
        super.onTerminate()
        mInstance = null
    }

    // 로딩화면 보여줌
    fun showProgressDialog(activity: Activity?) {
        if (activity == null || activity.isFinishing) {
            return
        }
        if (progressDialog != null && progressDialog!!.isShowing) {
            setProgressDialog("")
        }
        else {
            progressDialog = AppCompatDialog(activity)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setContentView(R.layout.dialog_custom_progress)
            progressDialog!!.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            progressDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.show()
        }

        val mLoadingImage: ImageView? = progressDialog!!.findViewById<View>(R.id.iv_progress_image) as ImageView?
        val anim = AnimationUtils.loadAnimation(this, R.anim.loading)
        mLoadingImage!!.animation = anim

    }

    // 로딩화면에 글씨 추가
    private fun setProgressDialog(message: String?) {
        if (progressDialog == null || !progressDialog!!.isShowing) {
            return
        }
        val tv_progress_message = progressDialog!!.findViewById<View>(R.id.tv_progress_message) as TextView?

        if (!TextUtils.isEmpty(message)) {
            tv_progress_message!!.text = message
        }
    }

    // 로딩화면 숨김
    fun hideProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

}