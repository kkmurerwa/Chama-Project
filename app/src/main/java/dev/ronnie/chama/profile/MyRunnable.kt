package dev.ronnie.chama.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Base64

open class MyRunnable(var handler: Handler, val imageString: String) : Runnable {

    override fun run() {
        val decodedByte = Base64.decode(imageString, 0)
        val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        val message = Message.obtain()
        val bundle = Bundle()
        bundle.putParcelable("bitmap", bitmap)
        message.data = bundle
        handler.sendMessage(message)
    }
}

