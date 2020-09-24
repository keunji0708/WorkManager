package org.techtown.workmanager


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class LoginActivity  : AppCompatActivity() {
    val TAG = "MyRequestQueue"

    private var btn_login: Button? = null
    private var btn_register: Button? = null

    private var et_login_id: EditText? = null
    private var et_login_pw: EditText? = null

    var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        btn_login = findViewById(R.id.btn_login)
        btn_register = findViewById(R.id.btn_register)

        et_login_id = findViewById(R.id.et_login_id)
        et_login_pw = findViewById(R.id.et_login_pw)

        btn_login!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                login()
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
        Log.e("LOGINREQUEST", emp_id +", " + emp_pw)

        val responseListener: Response.Listener<String?> =
            object : Response.Listener<String?> {
                override fun onResponse(response: String?) {
                    try {
                        val jsonObject = JSONObject(response)
                        val success = jsonObject.getString("success")
                        Log.e("LOGINREQUEST", "response : " + response)
                        if (success != null && success == "true") {
                            Toast.makeText(applicationContext, "로그인 성공!", Toast.LENGTH_SHORT).show()

                            val user = User(
                                jsonObject.getInt("emp_id"),
                                jsonObject.getString("emp_name"),
                                jsonObject.getString("emp_phone"),
                                jsonObject.getString("dep_name")
                            )

                            // 사용자 정보 저장
                            SharedPrefManager.getInstance(applicationContext)!!.userLogin(user)

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("emp_id", emp_id)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "로그인 실패!", Toast.LENGTH_SHORT).show()
                            return
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        val errorListener: Response.ErrorListener =
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.e("LOGINREQUEST", "error : " + error)
                    Toast.makeText(applicationContext, "로그인 처리시 에러발생!", Toast.LENGTH_SHORT).show()
                    return
                }
            }

        // Volley 로 로그인 양식 웹전송
        val loginRequest = LoginRequest(emp_id, emp_pw, responseListener, errorListener)
        loginRequest.setShouldCache(false)
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(loginRequest)
    }



}