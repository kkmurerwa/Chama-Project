package dev.ronnie.chama.imageUtils
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import kotlin.math.roundToInt

class RotateBitmap {

    private var mContext: Context? = null

    @Throws(IOException::class)
    fun handleSamplingAndRotationBitmap(context: Context, selectedImage: Uri): Bitmap? {
        mContext = context
        val MAX_HEIGHT = 1024
        val MAX_WIDTH = 1024

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var imageStream = context.contentResolver.openInputStream(selectedImage)
        BitmapFactory.decodeStream(imageStream, null, options)
        imageStream!!.close()

        options.inSampleSize =
            calculateInSampleSize(
                options,
                MAX_WIDTH,
                MAX_HEIGHT
            )

        options.inJustDecodeBounds = false
        imageStream = context.contentResolver.openInputStream(selectedImage)
        var img = BitmapFactory.decodeStream(imageStream, null, options)

        img = rotateImageIfRequired(img, selectedImage)
        return img
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap?, selectedImage: Uri): Bitmap? {

        val input = mContext!!.contentResolver.openInputStream(selectedImage)
        val ei: ExifInterface
        try {
            ei = ExifInterface(input!!)

            val orientation =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                    img,
                    90
                )
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                    img,
                    180
                )
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                    img,
                    270
                )
                else -> img
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, "rotateImageIfRequired: Could not read file." + e.message)
        }

        return img
    }

    companion object {

        private const val TAG = "RotateBit map"

        private fun rotateImage(img: Bitmap?, degree: Int): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(degree.toFloat())
            val rotatedImg = Bitmap.createBitmap(img!!, 0, 0, img.width, img.height, matrix, true)
            img.recycle()
            return rotatedImg
        }


        private fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int, reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {

                val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
                val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()

                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio


                val totalPixels = (width * height).toFloat()

                val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

                while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                    inSampleSize++
                }
            }
            return inSampleSize
        }
    }
}