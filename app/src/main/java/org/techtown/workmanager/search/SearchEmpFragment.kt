package org.techtown.ocean.search

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.techtown.workmanager.R
import org.techtown.workmanager.common.Constant
import org.techtown.workmanager.dataservice.VolleyResponseListener
import org.techtown.workmanager.dataservice.VolleyService
import org.techtown.workmanager.search.SearchAdapter
import java.util.HashMap


class SearchEmpFragment : DialogFragment() {
    private val TAG: String? = SearchEmpFragment::class.java.simpleName

    lateinit var spinner: Spinner
    lateinit var rb_name: RadioButton
    lateinit var rb_depart: RadioButton
    lateinit var radioGroup: RadioGroup

    private var searchListView: ListView? = null
    private var mSearchAdapter: SearchAdapter? = null
    var mOriginalData: JSONArray? = null

    lateinit var et_searchName: EditText
    var btn_search: Button? = null
    var btn_cancel: ImageButton? = null
    var data : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater.inflate(R.layout.fragment_search, container, false)

        val extra = arguments

        if (extra != null) {
            data = extra.getString("activityType")
        }

        initView(root)
        return root
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            dialog!!.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        }
    }

    private fun initView(root: View) {
        val ll_search_title: LinearLayout = root.findViewById(R.id.ll_search_title)

        if (data == "main") {
            ll_search_title.visibility = View.GONE
        } else {
            ll_search_title.visibility = View.VISIBLE
        }

        btn_search = root.findViewById(R.id.btn_search)
        btn_cancel = root.findViewById(R.id.btn_cancel)

        btn_search!!.setOnClickListener {
            hideKeyboard()
            searchEmployee(1)
        }

        btn_cancel!!.setOnClickListener {
            dismiss()
        }

        et_searchName = root.findViewById(R.id.et_searchName)
        searchListView = root.findViewById<View>(R.id.lv_search_result) as ListView

        searchListView!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as JSONObject
                Log.e(TAG, "SelectedItem ${selectedItem}")
                val emp_name = selectedItem.getString("emp_name")
                val emp_id = selectedItem.getString("emp_id")

                if (data == "sendYeolmae") {
                    val intent = Intent()
                    intent.putExtra("emp_name", emp_name)
                    intent.putExtra("emp_id", emp_id)
                    sendResult(intent, 9999)
                } else if (data == "dailyReport") {
                    this.mListener!!.onSearchEmpComplete(emp_name, emp_id)
                    dismiss()
                }

                dismiss()
            }

        spinner = root.findViewById(R.id.spinner_searchDepart)

        ArrayAdapter.createFromResource(
            requireContext(), R.array.depart_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.prompt = "부서를 선택하세요."

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                searchEmployee(0)
            }

        }

        //라디오 버튼 설정
        rb_name = root.findViewById(R.id.rb_name)
        rb_depart = root.findViewById(R.id.rb_depart)

        //라디오 그룹 설정
        radioGroup = root.findViewById(R.id.rg_search_cond)
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener)

    }

    //라디오 그룹 클릭 리스너
    var radioGroupButtonChangeListener =
        RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.rb_depart) {
                et_searchName.visibility = View.GONE
                spinner.visibility = View.VISIBLE
                btn_search!!.visibility = View.GONE
            } else if (i == R.id.rb_name) {
                et_searchName.visibility = View.VISIBLE
                spinner.visibility = View.GONE
                btn_search!!.visibility = View.VISIBLE
            }
        }


    fun setData(accountArray: JSONArray) {
        if(isAdded){
            mSearchAdapter = SearchAdapter(this.requireActivity(), accountArray)
            searchListView!!.adapter = mSearchAdapter
        }

    }

    private fun searchEmployee(type: Int) {
        val params: MutableMap<String, String> = HashMap()

        if (type == 0){
            params["search_type"] = "depart"
            params["search_word"] = spinner.getSelectedItem().toString()
        } else if (type == 1){
            params["search_type"] = "name"
            params["search_word"] = et_searchName.text.toString()
        }

        val url = Constant.server_url + "/search/searchEmployee.php"

        VolleyService.request_POST(activity, url, params,
            object : VolleyResponseListener {
                override fun onResponse(response: String?) {
                    try {
                        val jsonArray = JSONArray(response)
                        setData(jsonArray)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                override fun onError(message: String?) {
                    Toast.makeText(requireContext(), "에러 발생!", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    interface OnCompleteListener {
        fun onSearchEmpComplete(name: String?, id:String?)
    }

    private var mListener: OnCompleteListener? = null

    // make sure the Activity implemented it
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)

        try {
            mListener = activity as OnCompleteListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$activity must implement OnCompleteListener")
        }
    }

    companion object {
        fun getInstance(): SearchEmpFragment {
            return SearchEmpFragment()
        }
    }


    private fun sendResult(intent: Intent, REQUEST_CODE: Int) {
        targetFragment!!.onActivityResult(targetRequestCode, REQUEST_CODE, intent)
    }

}
