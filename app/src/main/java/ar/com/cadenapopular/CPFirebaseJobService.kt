package ar.com.cadenapopular

/* based on
  https://raw.githubusercontent.com/firebase/quickstart-android/master/messaging/app/src/main/java/com/google/firebase/quickstart/fcm/MyJobService.java
*/

import android.util.Log

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService


class CPFirebaseJobService : JobService() {

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        Log.d(TAG, "Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        return false
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }

    companion object {
        private val TAG = "CPJobService"
    }

}
