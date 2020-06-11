package app.videoplayerinsiderecyclerview.views.customRecyclerView

import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.videoplayerinsiderecyclerview.R
import app.videoplayerinsiderecyclerview.models.MediaObject
import com.bumptech.glide.RequestManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.util.*

class VideoPlayerRecyclerView : RecyclerView {
    private enum class VolumeState {
        ON, OFF
    }

    // ui
    private var thumbnail: ImageView? = null
    private var volumeControl: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var viewHolderParent: View? = null
    private var frameLayout: FrameLayout? = null
    private var videoSurfaceView: PlayerView? = null
    private var videoPlayer: SimpleExoPlayer? = null

    // vars
    private var mediaObjects = ArrayList<MediaObject>()
    private var videoSurfaceDefaultHeight = 0
    private var screenDefaultHeight = 0
    private var playPosition = -1
    private var isVideoViewAdded = false
    private var requestManager: RequestManager? = null

    // controlling playback state
    private var volumeState: VolumeState? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        val display =
            (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val point = Point()
        display.getSize(point)
        videoSurfaceDefaultHeight = point.x
        screenDefaultHeight = point.y
        videoSurfaceView = PlayerView(getContext())
        videoSurfaceView!!.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory: TrackSelection.Factory =
            AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // 2. Create the player
        videoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        // Bind the player to the view.
        videoSurfaceView!!.useController = false
        videoSurfaceView!!.player = videoPlayer
        setVolumeControl(VolumeState.ON)
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_IDLE) {
                    Log.d(
                        TAG,
                        "onScrollStateChanged: called."
                    )
                    if (thumbnail != null) { // show the old thumbnail
                        thumbnail!!.visibility = View.VISIBLE
                    }

                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    if (!recyclerView.canScrollVertically(1)) {
                        //playVideo(true)
                    } else {
                        //playVideo(false)
                    }
                }
            }

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        addOnChildAttachStateChangeListener(object : OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {}
            override fun onChildViewDetachedFromWindow(view: View) {
                if (viewHolderParent != null && viewHolderParent == view) {
                    resetVideoView()
                }
            }
        })
        videoPlayer!!.addListener(object : Player.EventListener {
            override fun onTimelineChanged(
                timeline: Timeline,
                manifest: Any?,
                reason: Int
            ) {
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(
                playWhenReady: Boolean,
                playbackState: Int
            ) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.e(
                            TAG,
                            "onPlayerStateChanged: Buffering video."
                        )
                        if (progressBar != null) {
                            progressBar!!.visibility = View.VISIBLE
                        }
                    }
                    Player.STATE_ENDED -> {
                        Log.d(
                            TAG,
                            "onPlayerStateChanged: Video ended."
                        )
                        videoPlayer!!.seekTo(0)
                    }
                    Player.STATE_IDLE -> {
                    }
                    Player.STATE_READY -> {
                        Log.e(
                            TAG,
                            "onPlayerStateChanged: Ready to play."
                        )
                        if (progressBar != null) {
                            progressBar!!.visibility = View.GONE
                        }
                        if (!isVideoViewAdded) {
                            addVideoView()
                        }
                    }
                    else -> {
                    }
                }
            }

            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPlayerError(error: ExoPlaybackException) {}
            override fun onPositionDiscontinuity(reason: Int) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
        })
    }

//    fun playVideo(isEndOfList: Boolean) {
//        val targetPosition: Int
//        if (!isEndOfList) {
//            val startPosition =
//                (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
//            var endPosition =
//                (layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
//
//            // if there is more than 2 list-items on the screen, set the difference to be 1
//            if (endPosition - startPosition > 1) {
//                endPosition = startPosition + 1
//            }
//
//            // something is wrong. return.
//            if (startPosition < 0 || endPosition < 0) {
//                return
//            }
//
//            // if there is more than 1 list-item on the screen
//            targetPosition = if (startPosition != endPosition) {
//                val startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition)
//                val endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition)
//                if (startPositionVideoHeight > endPositionVideoHeight) startPosition else endPosition
//            } else {
//                startPosition
//            }
//        } else {
//            targetPosition = mediaObjects.size - 1
//        }
//        Log.d(
//            TAG,
//            "playVideo: target position: $targetPosition"
//        )
//
//        // video is already playing so return
//        if (targetPosition == playPosition) {
//            return
//        }
//
//        // set the position of the list-item that is to be played
//        playPosition = targetPosition
//        if (videoSurfaceView == null) {
//            return
//        }
//
//        // remove any old surface views from previously playing videos
//        videoSurfaceView!!.visibility = View.INVISIBLE
//        removeVideoView(videoSurfaceView)
//        val currentPosition =
//            targetPosition - (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
//        val child = getChildAt(currentPosition) ?: return
//        val holder = child.tag as VideoPlayerViewHolder
//        if (holder == null) {
//            playPosition = -1
//            return
//        }
//        thumbnail = holder.thumbnail
//        progressBar = holder.progressBar
//        volumeControl = holder.volumeControl
//        viewHolderParent = holder.itemView
//        requestManager = holder.requestManager
//        frameLayout = holder.itemView.findViewById(R.id.media_container)
//        videoSurfaceView!!.player = videoPlayer
//        viewHolderParent!!.setOnClickListener(videoViewClickListener)
//        val dataSourceFactory: DataSource.Factory =
//            DefaultDataSourceFactory(
//                context,
//                Util.getUserAgent(
//                    context!!,
//                    "RecyclerView VideoPlayer"
//                )
//            )
//        val mediaUrl = mediaObjects[targetPosition].media_url
//        if (mediaUrl != null) {
//            val videoSource: MediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse(mediaUrl))
//            videoPlayer!!.prepare(videoSource)
//            videoPlayer!!.playWhenReady = true
//        }
//    }

    private val videoViewClickListener =
        OnClickListener { toggleVolume() }

    /**
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight
     * @param playPosition
     * @return
     */
    private fun getVisibleVideoSurfaceHeight(playPosition: Int): Int {
        val at =
            playPosition - (layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        Log.d(
            TAG,
            "getVisibleVideoSurfaceHeight: at: $at"
        )
        val child = getChildAt(at) ?: return 0
        val location = IntArray(2)
        child.getLocationInWindow(location)
        return if (location[1] < 0) {
            location[1] + videoSurfaceDefaultHeight
        } else {
            screenDefaultHeight - location[1]
        }
    }

    // Remove the old player
    private fun removeVideoView(videoView: PlayerView?) {
        val parent = videoView?.parent as ViewGroup? ?: return
        val index = parent.indexOfChild(videoView)
        if (index >= 0) {
            parent.removeViewAt(index)
            isVideoViewAdded = false
            viewHolderParent!!.setOnClickListener(null)
        }
    }

    private fun addVideoView() {
        frameLayout!!.addView(videoSurfaceView)
        isVideoViewAdded = true
        videoSurfaceView!!.requestFocus()
        videoSurfaceView!!.visibility = View.VISIBLE
        videoSurfaceView!!.alpha = 1f
        thumbnail!!.visibility = View.GONE
    }

    private fun resetVideoView() {
        if (isVideoViewAdded) {
            removeVideoView(videoSurfaceView)
            playPosition = -1
            videoSurfaceView!!.visibility = View.INVISIBLE
            thumbnail!!.visibility = View.VISIBLE
        }
    }

    fun releasePlayer() {
        if (videoPlayer != null) {
            videoPlayer!!.release()
            videoPlayer = null
        }
        viewHolderParent = null
    }

    private fun toggleVolume() {
        if (videoPlayer != null) {
            if (volumeState == VolumeState.OFF) {
                Log.d(
                    TAG,
                    "togglePlaybackState: enabling volume."
                )
                setVolumeControl(VolumeState.ON)
            } else if (volumeState == VolumeState.ON) {
                Log.d(
                    TAG,
                    "togglePlaybackState: disabling volume."
                )
                setVolumeControl(VolumeState.OFF)
            }
        }
    }

    private fun setVolumeControl(state: VolumeState) {
        volumeState = state
        if (state == VolumeState.OFF) {
            videoPlayer!!.volume = 0f
            animateVolumeControl()
        } else if (state == VolumeState.ON) {
            videoPlayer!!.volume = 1f
            animateVolumeControl()
        }
    }

    private fun animateVolumeControl() {
        if (volumeControl != null) {
            volumeControl!!.bringToFront()
            if (volumeState == VolumeState.OFF) {
                requestManager!!.load(R.drawable.ic_volume_off_grey_24dp)
                    .into(volumeControl!!)
            } else if (volumeState == VolumeState.ON) {
                requestManager!!.load(R.drawable.ic_volume_up_grey_24dp)
                    .into(volumeControl!!)
            }
            volumeControl!!.animate().cancel()
            volumeControl!!.alpha = 1f
            volumeControl!!.animate()
                .alpha(0f)
                .setDuration(600).startDelay = 1000
        }
    }

    fun setMediaObjects(mediaObjects: ArrayList<MediaObject>) {
        this.mediaObjects = mediaObjects
    }

    companion object {
        private const val TAG = "VideoPlayerRecyclerView"
    }
}