package org.techtown.workmanager.dataservice

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class VolleySingleton constructor(context: Context) {

    companion object {
        private val TAG = VolleySingleton::class.java.simpleName
        var mContext: Context? = null
        @Volatile
        private var mInstance: VolleySingleton? = null

        fun getInstance(context: Context) = mInstance
            ?: synchronized(this) {
                mInstance
                    ?: VolleySingleton(context).also {
                        mInstance = it
                    }
            }
    }

    init {
        mContext = context
    }

    val mRequestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        mRequestQueue.add(req)

    }

}
