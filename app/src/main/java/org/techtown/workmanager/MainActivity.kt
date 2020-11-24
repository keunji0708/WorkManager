package org.techtown.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import org.techtown.workmanager.base.AppBaseActivity
import org.techtown.workmanager.base.BaseActivity
import org.techtown.workmanager.common.SharedPreferenceManager

class MainActivity : AppBaseActivity() {

    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private var backKeyPressedTime: Long = 0

    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


    }



    override fun onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

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