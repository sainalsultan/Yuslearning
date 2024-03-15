package com.yuscorp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.gsm.SmsMessage
import android.util.Log

class ReceiverSMS : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.extras
        val i = intent?.action
        Log.e("SMS", "onReceive: $i")

        val pdus = bundle!!.getString("pdus") as Array<Any>
        if (pdus != null) {
            for (pdu in pdus) {
                val smsMessage: SmsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                val sender: String = smsMessage.getOriginatingAddress()
                val messageBody: String = smsMessage.getMessageBody()

                Log.e("SMS", "onReceive: $sender / $messageBody")
                // Add your custom logic to handle the incoming SMS
                // For example, you can show a notification or perform some action
                // based on the SMS content or sender.
            }
        }
    }

}