package org.techtown.workmanager.dataservice

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.techtown.workmanager.base.BaseActivity


object VolleyService {
    private var TAG: String? = VolleyService::class.java.simpleName

    fun request_GET(context: Context?, url: String?, listener: VolleyResponseListener) {
        val stringRequest: StringRequest = object : StringRequest(Request.Method.GET, url,
            Response.Listener<String?> { response ->
                Log.e("request_GET", "response : $response")
                listener.onResponse(response)
            },
            Response.ErrorListener { error ->
                Log.e("request_GET", "error : $error")
                listener.onError(error.toString())
            }) {}

        // Access the RequestQueue through singleton class.
        VolleySingleton.getInstance(context!!).addToRequestQueue(stringRequest)
    }

    fun request_POST(activity: Activity?, url: String?, getParams: MutableMap<String, String>, listener: VolleyResponseListener) {

        (activity as BaseActivity).showProgressDialog()

        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String?> { response ->
                (activity as BaseActivity).hideProgressDialog()
                Log.e("request_POST", "response : $response")
                listener.onResponse(response)
            },
            Response.ErrorListener { error ->
                (activity as BaseActivity).hideProgressDialog()
                Log.e("request_POST", "error : $error")
                listener.onError(error.toString())
            })

        {
            private val map: MutableMap<String, String> = getParams

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                Log.e(TAG, map.toString())
                return map
            }
        }

        // Access the RequestQueue through singleton class.
        VolleySingleton.getInstance(activity).addToRequestQueue(stringRequest)
    }
}