package ar.com.cadenapopular

/* based on
https://raw.githubusercontent.com/firebase/quickstart-android/master/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/MyFirebaseMessagingService.java
*/

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod

import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import org.json.JSONObject


class CPFirebaseMessagingService : FirebaseMessagingService() {
    private val CFKID: String = "115689108495633"

    private fun checkShareStatus(user: String, postId: String): Boolean {
        val postHistory = getSharedPreferences("post_history", Context.MODE_PRIVATE)
        return postHistory.getBoolean(user +"_"+ postId, false)
    }

    private fun setSharedStatus(user: String, postId: String, shared: Boolean = true){
        val postHistory = getSharedPreferences("post_history", Context.MODE_PRIVATE)
        val editor = postHistory.edit()
        editor.putBoolean(user +"_"+ postId, shared)
        editor.apply();
    }

    private fun sharePost(user: String=CFKID, postId: String){
        if (checkShareStatus(user, postId) == false) {
            setSharedStatus(user, postId, false)
            val params = Bundle()
            params.putString("message", "#CadenaPopular")
            params.putString("link", "https://www.facebook.com/${user}/posts/${postId}")
            val response = GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/feed",
                    params,
                    HttpMethod.POST
            ).executeAndWait()
            if (response.error == null) {
                setSharedStatus(user, postId)
                Log.d(TAG, "Shared post ${postId} from ${user}")
            }
            else{
                Log.d(TAG, "Could not share post. Error: ${response.error.toString()}")
            }
        }
        else{
            Log.d(TAG, "Post ${postId} from ${user} was already shared")
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Log.d(TAG, "From: " + remoteMessage!!.from)

        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            if (/* Check if data needs to be processed by long running job */ false) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow(remoteMessage.data)
            }

        }

        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification.body)
        }
//        sendNotification("some notification message")
    }

    private fun scheduleJob() {
        val dispatcher = FirebaseJobDispatcher(GooglePlayDriver(this))
        val myJob = dispatcher.newJobBuilder()
                .setService(CPFirebaseJobService::class.java)
                .setTag("my-job-tag")
                .build()
        dispatcher.schedule(myJob)
    }

    private fun handleNow(data: Map<String, String>) {
        val jObjData = JSONObject(data)
        val payload = JSONObject(jObjData.getString("payload"))
        val user = payload.getString("user")
        val postId = payload.getString("postId")
        sharePost(postId = postId, user = user)
    }

    /**
     * Create and show a simple notification containing the received FCM message.

     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification24dp)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setChannelId(getString(R.string.default_notification_channel_id))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {
        private val TAG = "CPFirebaseMsgService"
    }
}
