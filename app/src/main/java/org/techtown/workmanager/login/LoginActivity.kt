package org.techtown.workmanager.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import org.techtown.workmanager.MainActivity
import org.techtown.workmanager.R
import org.techtown.workmanager.base.BaseActivity
import org.techtown.workmanager.common.Constant
import org.techtown.workmanager.common.SharedPreferenceManager
import org.techtown.workmanager.dataservice.VolleyResponseListener
import org.techtown.workmanager.dataservice.VolleyService


class LoginActivity : BaseActivity() {
    private var TAG: String? = LoginActivity::class.java.simpleName

    private var btn_login: Button? = null
    private var btn_register: Button? = null

    private var et_login_id: EditText? = null
    private var et_login_pw: EditText? = null

    var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        btn_login = findViewById(R.id.btn_login)
        btn_register = findViewById(R.id.btn_register)

        et_login_id = findViewById(R.id.et_login_id)
        et_login_pw = findViewById(R.id.et_login_pw)

        btn_login!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                login()
//                startMainActivity()
//                finish()
            }
        })

        // RequestQueue 객체생성
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(applicationContext)
        }
    }

    private fun login() {
        val emp_id: String = et_login_id?.text.toString()
        val emp_pw: String = et_login_pw?.text.toString()

        val params: MutableMap<String, String> = HashMap()
        params["emp_id"] = emp_id
        params["emp_pw"] =  emp_pw

        val URL_POST = Constant.server_url + "/login/login.php"

        VolleyService.request_POST(this, URL_POST, params,
            object : VolleyResponseListener {
                override fun onResponse(response: String?) {
                    try {
                        val jsonObject = JSONObject(response)
                        val success = jsonObject.getString("success")

                        if (success == "true") {
                            val userObject = jsonObject.getString("user")
                            val gson = Gson()
                            val user: User = gson.fromJson(userObject, User::class.java)
                            SharedPreferenceManager.getInstance(applicationContext)!!.saveUserInfo(user)

                            Constant.yeolmae_num = jsonObject.getInt("yeolmae")
                            Constant.seed_num = jsonObject.getInt("seed")

                            Toast.makeText(applicationContext, "로그인 성공!", Toast.LENGTH_SHORT).show()
                            startMainActivity()
                        } else {
                            Toast.makeText(applicationContext, "로그인 실패!", Toast.LENGTH_SHORT).show()
                            return
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(applicationContext, "로그인 실패!", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                }

                override fun onError(message: String?) {
                    Toast.makeText(applicationContext, "로그인 처리시 에러발생!", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun startMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}