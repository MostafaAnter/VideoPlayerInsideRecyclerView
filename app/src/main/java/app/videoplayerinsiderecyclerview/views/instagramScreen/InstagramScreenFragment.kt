package app.videoplayerinsiderecyclerview.views.instagramScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.videoplayerinsiderecyclerview.R
import app.videoplayerinsiderecyclerview.models.MediaObject
import app.videoplayerinsiderecyclerview.utils.PlayerViewAdapter
import app.videoplayerinsiderecyclerview.viewModels.MediaViewModel

/**
 * A simple [Fragment] subclass.
 */
class InstagramScreenFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: InstagramRecyclerAdapter? = null
    private val modelList: ArrayList<MediaObject> = ArrayList<MediaObject>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_instagram_screen, container, false)
        findViews(view)
        return view
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()

        // load data
        val model: MediaViewModel by viewModels()
        model.getMedia().observe(requireActivity(), Observer {
            mAdapter?.updateList(arrayListOf(*it.toTypedArray()))
        })
    }

    private fun findViews(view: View) {
        recyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView
    }

    private fun setAdapter() {
        mAdapter = InstagramRecyclerAdapter(requireActivity(), modelList)
        recyclerView!!.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter
        mAdapter!!.SetOnItemClickListener(object :
            InstagramRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(
                view: View?,
                position: Int,
                model: MediaObject?
            ) {

                //handle item click events here
                Toast.makeText(activity, "Hey " + model?.title, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        PlayerViewAdapter.releaseAllPlayers()
    }
}