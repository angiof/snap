package com.example.snap

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.snap.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.okhttp.internal.DiskLruCache

class AddFragment : Fragment() {
    private var RC_GALEERY = 18
    private lateinit var mBinder: FragmentAddBinding
    private val PATCH_SNAPSHOT = "snapshot"
    private lateinit var mStorage: StorageReference
    private lateinit var mDatabaseReference: DatabaseReference

    var mPhotoSelecteduri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinder = FragmentAddBinding.inflate(layoutInflater)

        return mBinder.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(mBinder) {
            btPost.setOnClickListener {
                postSnapShot()
            }
            btnSelect.setOnClickListener {
                openGaleery()
            }


        }
        mStorage = FirebaseStorage.getInstance().reference
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child("snapshots")

    }

    private fun openGaleery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RC_GALEERY)
        Toast.makeText(requireContext(), "premuto", Toast.LENGTH_SHORT).show()
    }

    private fun postSnapShot() {
        mBinder.progressBarAdder.visibility=View.VISIBLE
        val key=mDatabaseReference.push().key!!

        mBinder.progressBarAdder.visibility = View.VISIBLE
        //mStorage.child("snapshots").child("my_photo")
        val storageReference=mStorage.child("snapshots").child("my_photo")
        if (mPhotoSelecteduri != null) {

            storageReference.putFile(mPhotoSelecteduri!!).addOnProgressListener {
                val progress = (100 * it.bytesTransferred / it.totalByteCount).toDouble()
                mBinder.progressBarAdder.progress = progress.toInt()
                mBinder.tvMessage.text = "$progress%"
            }
                .addOnCompleteListener {
                    mBinder.progressBarAdder.visibility = View.VISIBLE
                }
                .addOnSuccessListener {
                    Snackbar.make(
                        mBinder.root, "pubblicata con sucesso cazzo! ci sono riuscio",
                        Snackbar.LENGTH_LONG)
                    it.storage.downloadUrl.addOnSuccessListener {
                        saveSnapshot(key,it.toString(),mBinder.eTitle.text.toString().trim())
                        mBinder.title.visibility=View.GONE
                        mBinder.tvMessage.text=getString(R.string.post_message_valid_title)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "errore contattare angio", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveSnapshot(key:String,uri:String,title:String) {
        val snapshot=SnapShotDataClass(title = title,photoUrl = uri)
        mDatabaseReference.child(key).setValue(snapshot)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_GALEERY) {
                mPhotoSelecteduri = data?.data
                mBinder.imagePhoto.setImageURI(mPhotoSelecteduri)

                mBinder.title.visibility = View.VISIBLE
                mBinder.tvMessage.text = getString(R.string.post_message_valid_title)
            }
        }
    }
}
