package org.techtown.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.techtown.workmanager.base.AppBaseActivity
import org.techtown.workmanager.base.BaseActivity
import org.techtown.workmanager.common.SharedPreferenceManager
import org.techtown.workmanager.home.HomeFragment
import org.techtown.workmanager.report.MonthlyReportFragment

class MainActivity : AppBaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private var TAG: String? = MainActivity::class.java.simpleName

    var transaction: FragmentTransaction? = null
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val homeFragment: HomeFragment = HomeFragment()
    private val reportFragment: MonthlyReportFragment = MonthlyReportFragment()


    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private var backKeyPressedTime: Long = 0

    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transaction = fragmentManager.beginTransaction()
        transaction!!.replace(R.id.main_frame, homeFragment).commitAllowingStateLoss()

        bottomNavigationView!!.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        when (item.getItemId()) {
            R.id.navigation_home -> {
                transaction.replace(R.id.main_frame, homeFragment).commitAllowingStateLoss()
                return true
            }
            R.id.navigation_report -> {
                transaction.replace(R.id.main_frame, reportFragment).commitAllowingStateLoss()
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        Log.e(TAG, "main fragment backStack: " + supportFragmentManager.backStackEntryCount)

        if (isMenuOpen()) {
            closeMenu()
            return
        }

        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.main_frame)
        if (fragment is HomeFragment) {
            closeApp()
        } else {
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.main_frame, homeFragment).commitAllowingStateLoss()
            bottomNavigationView!!.setSelectedItemId(R.id.navigation_home)
        }

    }

    private fun closeApp() {

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2초를 더해 현재 시간과 비교 후
        // 2초가 지났으면 Toast 출력
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG)
            toast.show()
            return
        }

        // 2초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish()
            toast.cancel()
            toast = Toast.makeText(this, "종료", Toast.LENGTH_LONG)
            toast.show()
        }
    }
}