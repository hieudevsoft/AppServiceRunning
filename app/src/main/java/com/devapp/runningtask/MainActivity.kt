package com.devapp.runningtask

import android.app.AlertDialog
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private val number=1000000
    private lateinit var buttonStartService:Button
    private var intentService:Intent?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonStartService = findViewById(R.id.btn_start_service)
        buttonStartService.setOnClickListener {
            Log.d(TAG, "onCreate: Service initialize...")
            if(intentService!=null) stopService(intentService)
            intentService = Intent(this,RunningSumService::class.java)
            intentService!!.action = "ACTION_MAIN"
            intentService!!.putExtra("value",number)
            startService(intentService)
            Log.d(TAG, "onCreate: Start service...")
        }
    }

    override fun onDestroy() {
        intentService?.let { stopService(it) }
        super.onDestroy()
    }
    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(messageReceiver, IntentFilter("service_message"))
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val sum = intent.getDoubleExtra("sum", -1.0)
            showResultDialog(sum)
        }
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
        super.onPause()
    }
    private fun showResultDialog(sum: Double) {
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Running Task!")
            .setMessage("Sum of Task: $sum")
            .setCancelable(false)
            .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    if (dialog != null) {
                        dialog.dismiss()
                    }
                }
            })
        val dialog = dialogBuilder.create()
        dialog.show()

    }
}