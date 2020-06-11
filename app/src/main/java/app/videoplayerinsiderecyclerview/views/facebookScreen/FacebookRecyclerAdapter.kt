package app.videoplayerinsiderecyclerview.views.facebookScreen

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.videoplayerinsiderecyclerview.R
import app.videoplayerinsiderecyclerview.databinding.FacebookTimelineItemRecyclerListBinding
import app.videoplayerinsiderecyclerview.models.MediaObject
import app.videoplayerinsiderecyclerview.utils.PlayerStateCallback
import com.google.android.exoplayer2.Player
import java.util.*

/**
 * A custom adapter create by Mostafa Anter.
 */
class FacebookRecyclerAdapter(
    private val mContext: Context,
    private var modelList: ArrayList<MediaObject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PlayerStateCallback {

    private var mItemClickListener: OnItemClickListener? =
        null

    fun updateList(modelList: ArrayList<MediaObject>) {
        this.modelList = modelList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): VideoPlayerViewHolder {
        val binding: FacebookTimelineItemRecyclerListBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.context)
            , R.layout.facebook_timeline_item_recycler_list, viewGroup, false)
        return VideoPlayerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int) {

        //Here you can fill your row view
        if (holder is VideoPlayerViewHolder) {
            val model = getItem(position)
            val genericViewHolder = holder

            // send data to view holder
            genericViewHolder.onBind(model)
        }
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

    inner class VideoPlayerViewHolder(private val binding: FacebookTimelineItemRecyclerListBinding) : RecyclerView.ViewHolder(binding.root) {

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
                callback = this@FacebookRecyclerAdapter
                index = adapterPosition
                executePendingBindings()
            }



        }
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {}

    override fun onVideoBuffering(player: Player) {}

    override fun onStartedPlaying(player: Player) {
        Log.d("playvideo", "staaaart" + player.contentDuration)

    }


    override fun onFinishedPlaying(player: Player) {}
}