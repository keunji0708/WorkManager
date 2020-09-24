package org.techtown.workmanager

import android.content.Context
import android.content.Intent

class SharedPrefManager private constructor(context: Context) {

    // SharedPreference에 사용자 정보 저장
    fun userLogin(user: User) {
        val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(USER_ID, user.id)
        editor.putString(USER_NAME, user.name)
        editor.putString(USER_EMAIL, user.phone)
        editor.putString(USER_DEPART, user.depart)
        editor.apply()
    }

    // 사용자가 로그인을 했는지 판단
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(USER_NAME, null) != null
        }

    // 로그인한 사용자 정보 리턴
    val user: User
        get() {
            val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getInt(USER_ID, -1),
                sharedPreferences.getString(USER_NAME, null)!!,
                sharedPreferences.getString(USER_EMAIL, null)!!,
                sharedPreferences.getString(USER_DEPART, null)!!
            )
        }

    // SharedPreferences 사용자 정보 삭제하고 로그아웃
    fun logout() {
        val sharedPreferences = mCtx!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        mCtx!!.startActivity(Intent(mCtx, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

        /** 서비스인 경우
         * Intent intent = new Intent(context, 새로생성할액티비티.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        try {
             pendingIntent.send();
        } catch(CanceledException e) {
             e.printStackTrace();
        }
        출처: https://croute.me/275 [식탁 위의 프로그래머]
         */
    }

    companion object {
        //the constants
        private const val SHARED_PREF_NAME = "USER_INFO"
        private const val USER_ID = "USER_ID"
        private const val USER_NAME = "USER_NAME"
        private const val USER_EMAIL = "USER_EMAIL"
        private const val USER_DEPART = "USER_DEPART"

        private var mInstance: SharedPrefManager? = null
        private var mCtx: Context? = null

        @Synchronized
        fun getInstance(context: Context): SharedPrefManager? {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance
        }
    }

    init {
        mCtx = context
    }
}
