package org.techtown.workmanager.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import org.techtown.workmanager.R


class ReportAdapter internal constructor(jsonArray: JSONArray?) :
    RecyclerView.Adapter<ReportAdapter.ViewHolder>() {
    private var mJsonArray: JSONArray? = null

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var iv_report_check: ImageView = itemView.findViewById(R.id.iv_report_check)
        var tv_report_title: TextView = itemView.findViewById(R.id.tv_report_title)
        var tv_report_content: TextView = itemView.findViewById(R.id.tv_report_content)

    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val context: Context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_recyclerview_report, parent, false)

        return ViewHolder(view)

    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (mJsonArray != null && mJsonArray!!.length() >= position) {

            val report = mJsonArray!!.getJSONObject(position)
            holder.tv_report_title.text = report.getString("project_name")
            holder.tv_report_content.text = report.getString("report_msg")

            if (report.optString("report_check") == "1"){
                holder.iv_report_check.setImageResource(R.drawable.icon_report_check)
            }

        }
    }


    override fun getItemCount(): Int {
        return if (mJsonArray == null){
            0
        } else
            mJsonArray!!.length()
    }

    fun addItem(item : JSONObject?) {
        mJsonArray!!.put(item)
    }

    fun setJsonArray(jsonArray: JSONArray) {
        mJsonArray = JSONArray()
        mJsonArray = jsonArray
    }

    init {
        mJsonArray = jsonArray
    }
}