package app.videoplayerinsiderecyclerview.models.reposatories

import androidx.lifecycle.MutableLiveData
import app.videoplayerinsiderecyclerview.models.MediaObject
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class MediaRepo {
    // dummy data for get list of media objects
    public fun getMediaData(): MutableLiveData<MutableList<MediaObject>> {
        val data: MutableLiveData<MutableList<MediaObject>> = MutableLiveData()
        val dataObservable = Observable.create<MutableList<MediaObject>> {
            it.onNext(
                mutableListOf(
                    MediaObject(
                        "Big Buck Bunny",
                        "https://static.klliq.com/videos/uWPJnU7z5OysYjptZkBI6T1HANjC4WdP_hd.mp4",
                        "https://static.klliq.com/thumbnails/UFfUCqtb4FYwLRmI_m2Pq8xvRw-7vA-2.png",
                        "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org"
                    ),
                    MediaObject(
                        "Elephant Dream",
                        "https://static.klliq.com/videos/0HkyPAfPcmN0r5WxkYYvIHSi9jcC8Z_I_hd.mp4",
                        "https://static.klliq.com/thumbnails/uYSHHSfB6F183ZHYk1OnBjCe5C_1yseF.png",
                        "The first Blender Open Movie from 2006"
                    ),
                    MediaObject(
                        "For Bigger Blazes",
                        "https://static.klliq.com/videos/EJUhFO-_YQkH_Ll6tPppf2EkR794aTQQ_hd.mp4",
                        "https://static.klliq.com/thumbnails/5a7Byj0r5ZIKC0gV9QWCneZQZEmKCP-B.png",
                        "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV. For when you want to settle into your Iron Throne to watch the latest episodes. For \$35.\\nLearn how to use Chromecast with HBO GO and more at google.com/chromecast."
                    ),
                    MediaObject(
                        "For Bigger Escape",
                        "https://static.klliq.com/videos/QMWR5PxqxnnAILvO8iGB5ygvV47wxoDK_hd.mp4",
                        "https://static.klliq.com/thumbnails/vGRpl-Xw45xfOCborXr3bwAsl0uu_qMA.png",
                        "Introducing Chromecast. The easiest way to enjoy online video and music on your TV—for when Batman's escapes aren't quite big enough. For \$35. Learn how to use Chromecast with Google Play Movies and more at google.com/chromecast."
                    ),
                    MediaObject(
                        "For Bigger Fun",
                        "https://static.klliq.com/videos/Nca_afaqR15IQIrEDyhH9zEtoPSycv_z_hd.mp4",
                        "https://static.klliq.com/thumbnails/ZnFAHzGD9RQrRsBjJt2Pv3Y1vIAo11FX.png",
                        "Introducing Chromecast. The easiest way to enjoy online video and music on your TV. For \$35.  Find out more at google.com/chromecast."
                    )
                )
            )

            it.onComplete()
        }

        dataObservable.subscribe {
            data.value = it
        }.addTo(CompositeDisposable())

        return data
    }
}