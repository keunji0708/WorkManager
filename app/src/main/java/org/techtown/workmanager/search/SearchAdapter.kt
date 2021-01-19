package org.techtown.workmanager.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import org.json.JSONArray
import org.techtown.workmanager.R

class SearchAdapter (val context: Context, val employeeList: JSONArray) : BaseAdapter() {
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        /* LayoutInflater는 item을 Adapter에서 사용할 View로 부풀려주는(inflate) 역할을 한다. */
        var convertView = view
        val holder : ViewHolder

        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_search, null)

            holder = ViewHolder()
            holder.emp_name = convertView.findViewById<TextView>(R.id.tv_name)
            holder.emp_depart = convertView.findViewById<TextView>(R.id.tv_depart)
            holder.emp_email = convertView.findViewById<TextView>(R.id.tv_email)
            holder.emp_phone = convertView.findViewById<TextView>(R.id.tv_phone)

            convertView.tag = holder

        } else {
            holder = view.tag as ViewHolder
            convertView = view
            /* 이미 만들어진 View가 있으므로, tag를 통해 불러와서 대체한다. */
        }

        val employee = employeeList.getJSONObject(position)

        holder.emp_id = employee.getString("emp_id")
        holder.emp_name?.text = employee.getString("emp_name")
        holder.emp_depart?.text = employee.getString("dep_name")
        holder.emp_email?.text = employee.getString("emp_email")
        holder.emp_phone?.text = employee.getString("emp_phone")
        /* holder와 실제 데이터를 연결한다. null일 수 있으므로 변수에 '?'을 붙여 safe call 한다. */

        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return employeeList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return employeeList.length()
    }

    private class ViewHolder {
        var emp_id = "0"
        var emp_name : TextView? = null
        var emp_depart: TextView? = null
        var emp_email: TextView? = null
        var emp_phone : TextView? = null
    }
}