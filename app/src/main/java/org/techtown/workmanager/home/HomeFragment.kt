package org.techtown.workmanager.home

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONArray
import org.json.JSONException
import org.techtown.workmanager.R
import org.techtown.workmanager.common.Constant
import org.techtown.workmanager.dataservice.VolleyResponseListener
import org.techtown.workmanager.dataservice.VolleyService
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    private var TAG: String? = HomeFragment::class.java.simpleName

    var adapter: NoticeAdapter? = null
    var noticeArray: JSONArray = JSONArray()

    private var mTimer: Timer? = null
    var time1 = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val timerTask = MainTimerTask()
        mTimer = Timer()
        mTimer!!.schedule(timerTask, 500, 1000)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = Constant.user
        tv_home_emp_name.text = user!!.emp_name + " " + user.emp_pos
        tv_home_emp_depart.text = user.dep_name
        tv_home_emp_phone.text = user.emp_phone

        tv_home_yeolmae!!.text = Constant.yeolmae_num.toString()
        tv_home_seed!!.text = Constant.seed_num.toString()

        //getEmpYeolmae()

        btn_home_출근.setOnClickListener {
            tv_home_출근.text = "출근 - $time1"
            btn_home_출근.isEnabled = false
        }
        btn_home_퇴근.setOnClickListener {
            tv_home_퇴근.text = "퇴근 - $time1"
            btn_home_퇴근.isEnabled = false
        }

        btn_home_send_yeolmae.setOnClickListener{

        }

        recycler_notice.layoutManager = LinearLayoutManager(requireContext())
        adapter = NoticeAdapter(noticeArray)
        recycler_notice!!.adapter = adapter
        getNoticeData()
    }

    val mHandler: Handler = Handler()

    inner class MainTimerTask : TimerTask() {
        override fun run() {
            mHandler.post(mUpdateTimeTask)
        }
    }

    private val mUpdateTimeTask = Runnable {
        val currentDate = Calendar.getInstance().time
        val date_text = SimpleDateFormat("yyyy년 MM월 dd일 (EE)", Locale.KOREA).format(currentDate)

        val timeFormat = SimpleDateFormat("HH:mm:ss/aa", Locale.KOREA)
        val currentTime: String = timeFormat.format(Date())
        val array: Array<String> = currentTime.split("/".toRegex()).toTypedArray()

        time1 = array[0]
        val ampm = array[1]

        tv_home_time.text = time1.substring(0, 5) + " " + ampm
        tv_home_date.text = date_text
    }

    private fun getNoticeData() {
        val params: MutableMap<String, String> = HashMap()
        val URL_POST = Constant.server_url + "/notice/getRecentNotice.php"

        VolleyService.request_POST_Progress(activity, URL_POST, params,
            object : VolleyResponseListener {
                override fun onResponse(response: String?) {
                    try {
                        adapter = NoticeAdapter(JSONArray(response))
                        recycler_notice!!.adapter = adapter
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                override fun onError(message: String?) {
                    Toast.makeText(requireContext(), "에러 발생!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDestroy() {
        mTimer!!.cancel()
        super.onDestroy()
    }

    override fun onPause() {
        mTimer!!.cancel()
        super.onPause()
    }

    override fun onResume() {
        val timerTask = MainTimerTask()
        mTimer!!.schedule(timerTask, 500, 3000)

        super.onResume()
    }

}