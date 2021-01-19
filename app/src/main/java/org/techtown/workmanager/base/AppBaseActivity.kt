package org.techtown.workmanager.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import org.techtown.workmanager.R
import org.techtown.workmanager.common.Constant
import org.techtown.workmanager.common.SharedPreferenceManager
import org.techtown.workmanager.login.LoginActivity
import org.techtown.workmanager.login.User


open class AppBaseActivity : BaseActivity(){
    private var TAG: String? = AppBaseActivity::class.java.simpleName

    var bottomNavigationView: BottomNavigationView? = null
    var sideNavigationView: NavigationView? = null
    private var mDrawerLayout: DrawerLayout? = null
    var tv_toolbar_title: TextView? = null
    var btn_logout: ImageButton? = null
    var btn_menu: ImageButton? = null

    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    override fun setContentView(layoutResID: Int) {
        val baseView : DrawerLayout = layoutInflater.inflate(R.layout.activity_base, null) as DrawerLayout
        val activityContainer : FrameLayout = baseView.findViewById(R.id.activity_content)
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(baseView)

        setUserInfo()

        mDrawerLayout = findViewById<DrawerLayout>(R.id.fullView)
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        /**
         * LOCK_MODE_LOCKED_CLOSED - 드로어의 swipe 기능을 비활성화, 드로어를 닫음. swipe 모션 사용 불가.
         * LOCK_MODE_LOCKED_OPEN - 드로어의 swipe 기능을 비활성화, 드로어를 오픈합니다. swipe 모션 사용 불가.
         * LOCK_MODE_UNDEFINED - 드로어의 설정된 상태들 초기화.
         * LOCK_MODE_UNLOCKED - LOCK_MODE_LOCKED_CLOSED, LOCK_MODE_LOCKED_OPEN로 비활성화된 swipe 기능 활성화.
         * 출처: https://android-blog.dev/49 [Log.d]
         */
        mDrawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        intiSideMenuView()
        configureToolbar()
    }

    private fun intiSideMenuView(){
        sideNavigationView = findViewById<NavigationView>(R.id.side_nav)

        val header: View = sideNavigationView!!.getHeaderView(0)

        val side_emp_id = header.findViewById<TextView>(R.id.side_emp_id)
        val side_emp_part = header.findViewById<TextView>(R.id.side_emp_part)

        side_emp_id.text = user?.emp_name ?: ""
        side_emp_part.text = user?.dep_name ?: ""

        sideNavigationView!!.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                //menuItem.setChecked(true)
                val id: Int = menuItem.getItemId()

                if (id == R.id.side_열매선물) {
                    Toast.makeText(applicationContext, "열매선물", Toast.LENGTH_SHORT).show()
                } else if (id == R.id.side_설정) {
                    Toast.makeText(applicationContext, "환경설정", Toast.LENGTH_SHORT).show()
                }

                mDrawerLayout!!.closeDrawers()
                return true
            }
        })
    }

    private fun configureToolbar() {
        val toolbar : LinearLayout = findViewById(R.id.toolbar)
        tv_toolbar_title = findViewById(R.id.tv_toolbar_title)
        btn_logout = findViewById(R.id.btn_logout)
        btn_menu = findViewById(R.id.btn_menu)

        if (useToolbar()) {
            tv_toolbar_title!!.text = getString(R.string.app_name)
            btn_logout!!.setOnClickListener {
                logout()
            }
            btn_menu!!.setOnClickListener {
                mDrawerLayout!!.openDrawer(GravityCompat.END)
            }

        } else {
            toolbar.visibility = View.GONE
        }

    }

    //툴바를 사용할지 말지 정함
    open fun useToolbar() : Boolean {
        return true
    }

    //툴바 타이틀 설정
    open fun getTitleToolBar() : String {
        return getString(R.string.app_name)
    }

    private fun setUserInfo() {
        Constant.login = SharedPreferenceManager.getInstance(this)!!.isLoggedIn
        // 사용자가 로그인을 했는지 판단
        if (!Constant.login) {
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            user = SharedPreferenceManager.getInstance(this)!!.user
            Constant.user = user
        }

    }

    private fun logout(){
        Log.e(TAG, "-----logout-------->")
        finish()
        SharedPreferenceManager.getInstance(applicationContext)!!.logout()
    }

    fun isMenuOpen(): Boolean{
        return mDrawerLayout!!.isDrawerOpen(GravityCompat.END)
    }

    fun closeMenu() {
        mDrawerLayout!!.closeDrawer(GravityCompat.END)
    }

}