package org.techtown.workmanager.report

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import org.json.JSONArray
import org.json.JSONException
import org.techtown.workmanager.R
import org.techtown.workmanager.common.Constant
import org.techtown.workmanager.common.decorators.EventDecorator
import org.techtown.workmanager.common.decorators.OneDayDecorator
import org.techtown.workmanager.common.decorators.SaturdayDecorator
import org.techtown.workmanager.common.decorators.SundayDecorator
import org.techtown.workmanager.dataservice.VolleyResponseListener
import org.techtown.workmanager.dataservice.VolleyService
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class MonthlyReportFragment : Fragment() {
    private var TAG: String? = MonthlyReportFragment::class.java.simpleName

    lateinit var recyclerView: RecyclerView
    var reportAdapter: ReportAdapter? = null

    lateinit var materialCalendarView: MaterialCalendarView
    private val oneDayDecorator: OneDayDecorator = OneDayDecorator() // 오늘 날짜
    var monthlyDates: ArrayList<String> = ArrayList()
    private var selectedDay = ""
    private var doubleclick = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_calendar_report, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tv_selected_day: TextView = view.findViewById(R.id.tv_selected_day)
        val btn_add_report: ImageButton = view.findViewById(R.id.btn_add_report)

        materialCalendarView = view.findViewById(R.id.calendarView)

        materialCalendarView.state().edit()
            .isCacheCalendarPositionEnabled(false)
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2019, 0, 1)) // 달력의 시작
            .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        materialCalendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator(),
            oneDayDecorator
        )

        initRecycler(view)

        val date = Calendar.getInstance().time
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        selectedDay = formatter.format(date)
        doubleclick = selectedDay

        materialCalendarView.setSelectedDate(date)
        tv_selected_day.text = selectedDay

        materialCalendarView.setOnMonthChangedListener(object : OnMonthChangedListener {
            override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay) {
               // Log.e(TAG, "selectedDay : ${date.date}")
                selectedDay = formatter.format(date.date)
                materialCalendarView.setSelectedDate(date.date)
                tv_selected_day.text = selectedDay
                doubleclick = selectedDay

                getMonthReportData()
                getDailyReportData()
            }
        })

        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
           // Log.e(TAG, "selectedDay : $selectedDay")
            selectedDay = formatter.format(date.date)
            tv_selected_day.text = selectedDay
            getDailyReportData()
        }

        getMonthReportData()
        getDailyReportData()

        btn_add_report.setOnClickListener {
//            val intent = Intent(activity, DailyReportActivity::class.java)
//            intent.putExtra("date", selectedDay)
//            startActivity(intent)
        }

    }

    // 리사이클러뷰에 LinearLayoutManager 객체 지정.
    private fun initRecycler(root: View?) {
        recyclerView = root!!.findViewById(R.id.recycler_home)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getMonthReportData() {
        val params: MutableMap<String, String> = HashMap()

        params["emp_id"] = Constant.user!!.emp_id.toString()
        params["report_month"] = selectedDay.substring(0, selectedDay.length - 3)

        val URL_POST = Constant.server_url + "/report/getMonthlyReport.php"

        VolleyService.request_POST_Progress(activity, URL_POST, params,
            object : VolleyResponseListener {
                override fun onResponse(response: String?) {
                    try {
                        val jsonArray = JSONArray(response)
                        monthlyDates = ArrayList()

                        for (i in 0 until jsonArray.length()) {
                            val item = jsonArray.getJSONObject(i)
                            monthlyDates.add(item.getString("report_date"))
                        }

                        val dateList: Array<String> = monthlyDates.toArray(arrayOfNulls<String>(monthlyDates.size))
                        ApiSimulator(dateList).executeOnExecutor(Executors.newSingleThreadExecutor())

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                override fun onError(message: String?) {
                    Toast.makeText(requireContext(), "getMonthReportData 에러 발생!", Toast.LENGTH_SHORT).show()
                }
            })
    }



//    fun getMonthReportData() {
//        val responseListener: Response.Listener<String?> =
//            object : Response.Listener<String?> {
//                override fun onResponse(response: String?) {
//                    try {
//                        Log.e(TAG, "getMonthlyDates : " + response)
//                        val jsonArray = JSONArray(response)
//                        monthlyDates = ArrayList()
//
//                        for (i in 0 until jsonArray.length()) {
//                            val item = jsonArray.getJSONObject(i)
//                            //Log.e(TAG, "getMonthlyDates item: " + item.getString("report_date"))
//                            monthlyDates.add(item.getString("report_date"))
//                        }
//
//                        val dateList: Array<String> = monthlyDates.toArray(arrayOfNulls<String>(monthlyDates.size))
//                        ApiSimulator(dateList).executeOnExecutor(Executors.newSingleThreadExecutor())
//
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        val errorListener: Response.ErrorListener =
//            object : Response.ErrorListener {
//                override fun onErrorResponse(error: VolleyError?) {
//                    Log.e(TAG, "error : " + error)
//                    Toast.makeText(requireContext(), "에러발생!", Toast.LENGTH_SHORT).show()
//                    return
//                }
//            }
//
//        val report_month: String = selectedDay.substring(0, selectedDay.length - 3)
//        val reportMonthRequest = ReportMonthRequest(Constant.userID, report_month, responseListener, errorListener)
//        reportMonthRequest.setShouldCache(false)
//        MySingleton.getInstance(requireContext()).addToRequestQueue(reportMonthRequest)
//    }

    // 리사이클러뷰에 표시할 데이터 리스트 생성.
    private fun getDailyReportData() {
        val params: MutableMap<String, String> = HashMap()

        params["emp_id"] = Constant.user!!.emp_id.toString()
        params["emp_date"] = selectedDay

        val URL_POST = Constant.server_url + "/report/getDailyReport.php"

        VolleyService.request_POST(activity, URL_POST, params,
            object : VolleyResponseListener {
                override fun onResponse(response: String?) {
                    try {
                        val jsonArray = JSONArray(response)
                        initAdapter(jsonArray)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                override fun onError(message: String?) {
                    Toast.makeText(requireContext(), "getDailyReportData 에러 발생!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
    fun initAdapter(jsonArray: JSONArray?) {
        if (isAdded) {
            reportAdapter = ReportAdapter(jsonArray)
            recyclerView.adapter = reportAdapter
        }

    }

    @SuppressLint("StaticFieldLeak")
    private inner class ApiSimulator internal constructor(var date_Result: Array<String>) :
        AsyncTask<Void?, Void?, List<CalendarDay>>() {
        override fun doInBackground(vararg p0: Void?): List<CalendarDay>? {
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val calendar = Calendar.getInstance()
            val dates = ArrayList<CalendarDay>()

            /*특정날짜 달력에 점 표시. 월은 0이 1월 년,일은 그대로*/
            for (i in date_Result.indices) {
                val dateTime = date_Result[i].split("-".toRegex()).toTypedArray()

                val year = dateTime[0].toInt()
                val month = dateTime[1].toInt()
                val day = dateTime[2].toInt()

                calendar[year, month - 1] = day
                val date = CalendarDay.from(calendar)
                dates.add(date)
            }

            return dates
        }

        override fun onPostExecute(@NonNull calendarDays: List<CalendarDay>) {
            super.onPostExecute(calendarDays)

            if (isAdded && activity != null) {
                if (activity!!.isFinishing) {
                    return
                }
                Log.e(TAG,  "test calendarDays : $calendarDays")
                materialCalendarView.addDecorator(EventDecorator(Color.MAGENTA, calendarDays, activity))
            }

        }

    }
}