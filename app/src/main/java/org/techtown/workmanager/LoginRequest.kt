package org.techtown.workmanager

import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Response

import com.android.volley.toolbox.StringRequest


class LoginRequest(
    emp_id: String,
    emp_pw: String,
    listener: Response.Listener<String?>?,
    errorListener: Response.ErrorListener?
) :
    StringRequest(
        Method.POST,
        URL,
        listener,
        errorListener
    ) {
    private val map: MutableMap<String, String>

    @Throws(AuthFailureError::class)
    override fun getParams(): Map<String, String> {
        Log.e("LOGINREQUEST", map.toString())
        return map
    }

    companion object {
        private const val URL = "http://192.168.43.219/forest_pt/login.php"
    }

    init {
        map = HashMap()
        map["emp_id"] = emp_id
        map["emp_pw"] = emp_pw
    }
}
