package com.example.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

class MyBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent != null && context != null) {
            if (intent.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                val msg = Telephony.Sms.Intents.getMessagesFromIntent(intent)
//                for(smsMessage in msg){
//                    Log.i("msg", smsMessage.displayMessageBody)
//                }
                val newIntent = Intent(context, MainActivity::class.java)
                newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                newIntent.putExtra("msgSender", msg[0].originatingAddress)
                newIntent.putExtra("msgBody", msg[0].messageBody)
                context.startActivity(newIntent)
            }
        }
    }
}