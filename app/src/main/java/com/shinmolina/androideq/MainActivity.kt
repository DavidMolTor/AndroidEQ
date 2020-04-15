package com.shinmolina.androideq

import android.content.*
import android.media.audiofx.Equalizer
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var receiver: AudioSessionReceiver? = null
    var intentFilter: IntentFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        receiver = AudioSessionReceiver()
        intentFilter = IntentFilter("com.journaldev.broadcastreceiver.SOME_ACTION")

        try {
            val mediaManager = this.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
            val controllers: List<MediaController> = mediaManager.getActiveSessions(null)

            for (controller in controllers) {
                Log.i("INFO", "Current audio session: ${controller.packageName}")
            }
        } catch (e: SecurityException) {
            Log.i("ERROR", "$e")
        }

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}

class AudioSessionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent!!.getIntExtra(Equalizer.EXTRA_AUDIO_SESSION, -1)
        val packageName = intent.getStringExtra(Equalizer.EXTRA_PACKAGE_NAME)

        Log.i("INFO", "Changed audio session: $id, $packageName")
    }
}