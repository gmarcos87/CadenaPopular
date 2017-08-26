package ar.com.cadenapopular

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.content.Intent
import android.app.NotificationManager

import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.AccessToken
import com.facebook.FacebookException

import com.google.firebase.messaging.FirebaseMessaging

import kotlinx.android.synthetic.main.activity_main.*
import android.app.NotificationChannel
import android.net.Uri
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

class MainActivity : AppCompatActivity(),
        ConnectFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_about -> {
//                message.setText(R.string.title_about)
                changeFragment(AboutFragment.newInstance("1","2"))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_connect -> {
//                message.setText(R.string.title_connect)
                changeFragment(ConnectFragment.newInstance("1","2"))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction()
        if (cleanStack) {
            clearBackStack()
        }
        ft.setCustomAnimations(
                R.anim.abc_slide_in_bottom, R.anim.abc_shrink_fade_out_from_bottom, R.anim.abc_popup_enter, R.anim.abc_popup_exit)
        ft.replace(R.id.activity_base_content, f)
        ft.addToBackStack(null)
        ft.commit()
    }

    fun clearBackStack() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    /* implements *.OnFragmentInteractionListener as required*/
    override fun onFragmentInteraction(uri: Uri){
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            changeFragment(AboutFragment())
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        FirebaseMessaging.getInstance().subscribeToTopic("CFKArgentina~Facebook")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager!!.createNotificationChannel(NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW))
        }

        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!!.get(key)
                Log.d("Bla", "Key: $key Value: $value")
            }
        }
    }

}
