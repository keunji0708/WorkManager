package org.techtown.workmanager.dataservice

interface VolleyResponseListener {
    fun onResponse(response: String?)
    fun onError(message: String?)
}