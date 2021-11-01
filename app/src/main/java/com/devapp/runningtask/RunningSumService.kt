package com.devapp.runningtask

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class RunningSumService : LifecycleService() {
    val TAG = "RunningSumService"
    private val action = "ACTION_MAIN"
    override fun onCreate() {
        Log.d(TAG, "onCreate: Service created...")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: Service is Running....")
        intent?.let {
            if (it.action == action) {
                val value = it.getIntExtra("value", 1000000)
                lifecycleScope.launch(Dispatchers.IO) {
                    var sum = 0.0
                    for (i in 1..value) {
                        sum += i
                    }
                    Log.d(TAG, "onStartCommand: Done task...")
                    Log.d(TAG, "onStartCommand: $sum")
                    delay(5000)
                    withContext(Dispatchers.Main) {
                        sendMessage(sum)
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun sendMessage(value:Double) {
        val intent = Intent("service_message")
        intent.putExtra("sum", value)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
    override fun onDestroy() {
        Log.d(TAG, "onDestroy: Service is destroyed")
        super.onDestroy()
    }
}