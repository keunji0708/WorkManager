package org.techtown.workmanager.common

import android.content.Context
import android.content.Intent
import org.techtown.workmanager.login.LoginActivity
import org.techtown.workmanager.User

class SharedPrefManager private constructor(context: Context) {

    // SharedPreference에 사용자 정보 저장
    fun userLogin(user: User) {
        val sharedPreferences = mContext!!.getSharedPreferences(SHARED_USER_INFO, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(USER_ID, user.emp_id)
        editor.putString(USER_NAME, user.emp_name)
        editor.putString(USER_POS, user.emp_pos)
        editor.putString(USER_PHONE, user.emp_phone)
        editor.putString(USER_DEPART, user.dep_name)
        editor.apply()
    }

    // 사용자가 로그인을 했는지 판단
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mContext!!.getSharedPreferences(
                SHARED_USER_INFO, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_NAME, null) != null
        }

    // 로그인한 사용자 정보 리턴
    val user: User
        get() {
            val sharedPreferences = mContext!!.getSharedPreferences(
                SHARED_USER_INFO, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getInt(USER_ID, -1),
                sharedPreferences.getString(USER_NAME, null)!!,
                sharedPreferences.getString(USER_POS, null)!!,
                sharedPreferences.getString(USER_PHONE, null)!!,
                sharedPreferences.getString(USER_DEPART, null)!!
            )
        }

    // SharedPreferences 사용자 정보 삭제하고 로그아웃
    fun logout() {
        val sharedPreferences = mContext!!.getSharedPreferences(
            SHARED_USER_INFO, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        mContext!!.startActivity(Intent(mContext, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    companion object {
        private const val SHARED_USER_INFO = "USER_INFO"
        private const val USER_ID = "USER_ID"
        private const val USER_NAME = "USER_NAME"
        private const val USER_POS = "USER_POS"
        private const val USER_PHONE = "USER_PHONE"
        private const val USER_DEPART = "USER_DEPART"

        private var mInstance: SharedPrefManager? = null
        private var mContext: Context? = null

        @Synchronized
        fun getInstance(context: Context): SharedPrefManager? {
            if (mInstance == null) {
                mInstance =
                    SharedPrefManager(context)
            }
            return mInstance
        }
    }

    init {
        mContext = context
    }
}
