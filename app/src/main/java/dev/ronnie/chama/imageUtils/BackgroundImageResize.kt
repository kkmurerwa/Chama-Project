package  dev.ronnie.chama.imageUtils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.View
import dev.ronnie.chama.imageUtils.ChooseImageFragment.Companion.progressBar
import java.io.ByteArrayOutputStream
import java.io.IOException


class BackgroundImageResize(ctx: Context, private val chooseImageActivity: ChooseImageFragment?) :
    AsyncTask<Uri, Int, ByteArray>() {

    private var mBitmap: Bitmap? = null
    private var mUploadBytes: ByteArray? = null
    private val context: Context = ctx.applicationContext


    override fun onPreExecute() {
        super.onPreExecute()

        if (chooseImageActivity != null) {
            progressBar.visibility = View.VISIBLE
        }

    }

    override fun doInBackground(vararg params: Uri): ByteArray {


        try {
            val rotateBitmap = RotateBitmap()
            mBitmap = rotateBitmap.handleSamplingAndRotationBitmap(context, params[0])
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return getBytesFromBitmap(
            mBitmap!!,
            70
        )
    }

    override fun onPostExecute(bytes: ByteArray) {
        super.onPostExecute(bytes)
        mUploadBytes = bytes

        progressBar.visibility = View.INVISIBLE
        chooseImageActivity!!.dialog!!.dismiss()
        ChooseImageFragment.mOnInputListener?.sendInput(mUploadBytes!!)
        Log.d("BR", "THESE ${mUploadBytes.toString()}")


    }

    companion object {

        fun getBytesFromBitmap(bitmap: Bitmap, quality: Int): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            return stream.toByteArray()
        }
    }


}