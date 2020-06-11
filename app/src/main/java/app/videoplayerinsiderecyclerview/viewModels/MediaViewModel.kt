package app.videoplayerinsiderecyclerview.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.videoplayerinsiderecyclerview.models.MediaObject
import app.videoplayerinsiderecyclerview.models.reposatories.MediaRepo

class MediaViewModel: ViewModel() {
    private val mediaData: MutableLiveData<MutableList<MediaObject>> = MediaRepo().getMediaData()
    fun getMedia(): MutableLiveData<MutableList<MediaObject>>{
        return mediaData
    }
}