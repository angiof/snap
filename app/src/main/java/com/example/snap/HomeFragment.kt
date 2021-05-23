package com.example.snap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.snap.databinding.FragmentHomeBinding
import com.example.snap.databinding.ItemSnapshotBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {
    //var firebase adapter che contiene l'ob e il viewHolder
    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<SnapShotDataClass, SnapShotHolder>
    //binding
    lateinit var binding: ItemSnapshotBinding
    //binding fragment
    lateinit var mBinding :FragmentHomeBinding
    //layout
    lateinit var mlayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ///querry db +creazione del file stradamento dove andare a cercare
        val querry = FirebaseDatabase.getInstance().reference.child(getString(R.string.patch_strdamento_snap))
        //opione collegamento del ob alla qurry creata nella riga 41
        val option = FirebaseRecyclerOptions.Builder<SnapShotDataClass>().setQuery(querry, SnapShotDataClass::class.java).build()

        //creao adapter
        mFirebaseAdapter = object : FirebaseRecyclerAdapter<SnapShotDataClass, SnapShotHolder>(option) {
                private lateinit var mContext: android.content.Context
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapShotHolder {
                    mContext=parent.context
                    val view1=LayoutInflater.from(mContext).inflate(R.layout.item_snapshot,parent,false)

                    return SnapShotHolder(view1)

                }

                override fun onBindViewHolder(holder: SnapShotHolder, position: Int, model: SnapShotDataClass) {
                    val snapShot=getItem(position)
                    with(holder){
                        setListener(snapShot)
                        binding.tvTitle.text=snapShot.title

                        Log.d("k"," og è$binding.tvTitle.text.toString()")
                        Glide.with(mContext).load(snapShot.photoUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .into(binding.imgPhoto)
                    }
                }

            override fun onDataChanged() {
                super.onDataChanged()

                mBinding.progressBar.visibility=View.GONE

            }


            override fun onError(error: DatabaseError) {
                super.onError(error)
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }




        }
        mlayoutManager= LinearLayoutManager(requireContext())
        mBinding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager=mlayoutManager
            adapter=mFirebaseAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    inner class SnapShotHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemSnapshotBinding.bind(view)

        fun setListener(snapshotData: SnapShotDataClass) {


            }
        }
    }






