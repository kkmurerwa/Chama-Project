package dev.ronnie.chama.imageUtils

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import dev.ronnie.chama.Constants.Companion.CAMERA_PERMISSION
import dev.ronnie.chama.Constants.Companion.CAMERA_PIC_REQUEST
import dev.ronnie.chama.Constants.Companion.STORAGE_PERMISSION
import dev.ronnie.chama.Constants.Companion.STORAGE_REQUEST

import dev.ronnie.chama.R
import kotlinx.android.synthetic.main.activity_choose_image.view.*


class ChooseImageFragment : DialogFragment() {

    private var uriProfileImage: Uri? = null


    interface OnInputListener {
        fun sendInput(bytes: ByteArray)
    }


    companion object {
        lateinit var progressBar: ProgressBar
        var mOnInputListener: OnInputListener? = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mOnInputListener = activity as OnInputListener
        } catch (e: ClassCastException) {

            e.printStackTrace()

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_choose_image, container, false)

        //  mOnInputListener = activity as OnInputListener

        view.textViewPhoto.setOnClickListener {
            openCamera()
        }

        view.textViewChoose.setOnClickListener {
            openGallery()
        }

        progressBar = view.progress_bar_Dialog

        return view
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Profile Image"),
                STORAGE_REQUEST
            )
        } else {
            requestStoragePermission()
        }
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val cameraIntent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
        } else {
            requestCameraPermission()
        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {

            AlertDialog.Builder(activity!!)
                .setTitle("Permission needed")
                .setMessage("This permission is needed inorder to update Profile Picture")
                .setPositiveButton("Ok") { _, _ ->
                    ActivityCompat.requestPermissions(
                        activity!!,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION
                    )
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ -> dialog.dismiss() }
                .create().show()

        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION
            )
        }
    }

    private fun requestCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                Manifest.permission.CAMERA
            )
        ) {

            AlertDialog.Builder(activity!!)
                .setTitle("Permission needed")
                .setMessage("This permission is needed inorder to capture profile picture")
                .setPositiveButton("Ok") { _, _ ->
                    ActivityCompat.requestPermissions(
                        activity!!,
                        arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION
                    )
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ -> dialog.dismiss() }
                .create().show()

        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(activity, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(activity, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            uriProfileImage = data.data

            val backgroundImageResize = BackgroundImageResize(
                this.requireActivity(),
                this@ChooseImageFragment
            )

            backgroundImageResize.execute(uriProfileImage)


        } else if (requestCode == STORAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            uriProfileImage = data.data

            val backgroundImageResize = BackgroundImageResize(
                this.requireActivity(),
                this@ChooseImageFragment
            )

            backgroundImageResize.execute(uriProfileImage)


        }
    }


}
