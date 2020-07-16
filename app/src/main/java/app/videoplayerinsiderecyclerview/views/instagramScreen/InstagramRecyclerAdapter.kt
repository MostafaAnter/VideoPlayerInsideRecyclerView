package app.videoplayerinsiderecyclerview.views.instagramScreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.videoplayerinsiderecyclerview.R
import app.videoplayerinsiderecyclerview.databinding.InstagramTimelineItemRecyclerBinding
import app.videoplayerinsiderecyclerview.models.MediaObject
import app.videoplayerinsiderecyclerview.utils.PlayerStateCallback
import app.videoplayerinsiderecyclerview.utils.PlayerViewAdapter
import com.google.android.exoplayer2.Player
import java.util.*

/**
 * A custom adapter to use with the RecyclerView widget.
 */
class InstagramRecyclerAdapter(
    private val mContext: Context,
    private var modelList: ArrayList<MediaObject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    PlayerStateCallback {
    private var mItemClickListener: OnItemClickListener? = null

    fun updateList(modelList: ArrayList<MediaObject>) {
        this.modelList = modelList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): VideoPlayerViewHolder {
        val binding: InstagramTimelineItemRecyclerBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context)
            , R.layout.instagram_timeline_item_recycler, viewGroup, false)
        return VideoPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        //Here you can fill your row view
        if (holder is VideoPlayerViewHolder) {
            val model = getItem(position)
            val genericViewHolder = holder

            // send data to view holder
            genericViewHolder.onBind(model)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        PlayerViewAdapter.releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    private fun getItem(position: Int): MediaObject {
        return modelList[position]
    }

    fun SetOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(
            view: View?,
            position: Int,
            model: MediaObject?
        )
    }

    inner class VideoPlayerViewHolder(private val binding: InstagramTimelineItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: MediaObject) {
            // handel on item click
            binding.root.setOnClickListener {
                mItemClickListener!!.onItemClick(
                    it,
                    adapterPosition,
                    model
                )
            }

            binding.apply {
                dataModel = model
                callback = this@InstagramRecyclerAdapter
                index = adapterPosition
                executePendingBindings()
            }
        }
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
    }

    override fun onVideoBuffering(player: Player) {
    }

    override fun onStartedPlaying(player: Player) {

    }

    override fun onFinishedPlaying(player: Player) {
    }
}