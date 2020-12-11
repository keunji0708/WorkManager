package org.techtown.workmanager.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import org.techtown.workmanager.R


class NoticeRecyclerAdapter internal constructor(noticeArray: JSONArray) :
    RecyclerView.Adapter<NoticeRecyclerAdapter.ViewHolder>() {
    private var mJsonArray: JSONArray? = null

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    inner class ViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var btn_report_check: ImageButton = itemView.findViewById(R.id.iv_notice_arrow)
        var tv_notice_num: TextView = itemView.findViewById(R.id.tv_notice_num)
        var tv_notice_title: TextView = itemView.findViewById(R.id.tv_notice_title)
        var tv_notice_content: TextView = itemView.findViewById(R.id.tv_notice_content)
        var tv_notice_name: TextView = itemView.findViewById(R.id.tv_notice_name)
        var tv_notice_date: TextView = itemView.findViewById(R.id.tv_notice_date)

    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_recyclerview_notice, parent, false)
        return ViewHolder(view)
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (mJsonArray != null && mJsonArray!!.length() >= position) {
            val notice = mJsonArray!!.getJSONObject(position)
            holder.tv_notice_num.text = notice.getString("notice_id")
            holder.tv_notice_title.text = notice.getString("notice_title")
            holder.tv_notice_content.text = notice.getString("notice_content")
            holder.tv_notice_name.text = notice.getString("notice_writer")
            holder.tv_notice_date.text = notice.getString("notice_date")

            holder.btn_report_check.setOnClickListener {
                if(holder.tv_notice_content.visibility == View.GONE){
                    holder.tv_notice_content.visibility = View.VISIBLE
                    holder.btn_report_check.setImageResource(R.drawable.icon_arrow_up)
                } else  if(holder.tv_notice_content.visibility == View.VISIBLE){
                    holder.tv_notice_content.visibility = View.GONE
                    holder.btn_report_check.setImageResource(R.drawable.icon_arrow_down)
                }
            }
        }
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    override fun getItemCount(): Int {
        return if (mJsonArray == null){
            0
        } else
            mJsonArray!!.length()
    }

    // 리스트에 item을 추가
    fun addItem(item : JSONObject?) {
        mJsonArray!!.put(item)
    }

    fun setJsonArray(jsonArray: JSONArray) {
        mJsonArray = JSONArray()

        mJsonArray = jsonArray
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    init {
        mJsonArray = noticeArray
    }


}