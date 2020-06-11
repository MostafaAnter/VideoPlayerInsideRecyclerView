package app.videoplayerinsiderecyclerview.views.tiktokScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import app.videoplayerinsiderecyclerview.R
import app.videoplayerinsiderecyclerview.models.MediaObject
import app.videoplayerinsiderecyclerview.utils.PlayerViewAdapter
import app.videoplayerinsiderecyclerview.utils.RecyclerViewScrollListener
import app.videoplayerinsiderecyclerview.viewModels.MediaViewModel


/**
 * A simple [Fragment] subclass.
 */
class TikTokScreenFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var mAdapter: TikTokRecyclerAdapter? = null
    private val modelList: ArrayList<MediaObject> = ArrayList<MediaObject>()

    // for handle scroll and get first visible item index
    private lateinit var scrollListener: RecyclerViewScrollListener


    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =
            inflater.inflate(R.layout.fragment_tik_tok_player, container, false)
        findViews(view)
        return view
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        mAdapter = TikTokRecyclerAdapter(requireActivity(), modelList)
        recyclerView!!.setHasFixedSize(true)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(getActivity())
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = mAdapter

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView!!)

        scrollListener = object : RecyclerViewScrollListener(){
            override fun onItemIsFirstVisibleItem(index: Int) {
                Log.d("visible item index", index.toString())
                // play just visible item
                PlayerViewAdapter.playIndexThenPausePreviousAndNextPlayers(index)
            }

        }
        recyclerView!!.addOnScrollListener(scrollListener)
        mAdapter!!.SetOnItemClickListener(object : TikTokRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, model: MediaObject?) {

            }
        })
    }

    override fun onPause() {
        super.onPause()
        PlayerViewAdapter.releaseAllPlayers()
    }
}