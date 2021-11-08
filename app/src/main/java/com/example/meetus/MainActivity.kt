package com.example.meetus


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.meetus.Fragment.ChatFragment
import com.example.meetus.Fragment.MoreFragment
import com.example.meetus.Fragment.PeopleFragment
import com.example.meetus.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val mChatFragment = ChatFragment()
    private val mPeopleFragment = PeopleFragment()
    private val mMoreFragment =MoreFragment()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //فتح التطبيق على شاشة المحادثة
        setFragment(mChatFragment)
        //حجز مكان للنص فى toolbar مثل (chat-people-more)
        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.title=""
        //إظهار ال status bar فى الشاشة
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        //التنقل بين الثلاث صفحات(chat-people-more)
        binding.navView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.chat_icon -> { setFragment(mChatFragment) }
                R.id.people_icon -> { setFragment(mPeopleFragment) }
                R.id.more_icon -> { setFragment(mMoreFragment) }
            }
            true
        }
    }


    private fun setFragment(fragment: Fragment) {
        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.nav_host_fragment_activity_main, fragment)
        fr.commit()
    }
}