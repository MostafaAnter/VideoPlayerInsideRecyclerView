# VideoPlayerInsideRecyclerView
Integrate RecyclerView with ExoPlayer — The clean way — and customization
this is the implementation of that article [meduim article](https://medium.com/mindorks/working-with-exoplayer-the-clean-way-and-customization-fac81e5d39ba)

<img src="https://media.giphy.com/media/ibAOpHOHg9w8Wfabuy/giphy.gif" width="300">

# Demo

[Youtube video](https://youtu.be/KynEZzerD8M)

# Tips

### for smooth scrolling and avoiding players overlaps use `TextureView` nested of `SurfaceView`

Note, `TextureView` can only be used in a hardware accelerated window. When rendered in software, TextureView will draw nothing. so after   set surface type
 

    <com.google.android.exoplayer2.ui.PlayerView android:id="@+id/player_view"
     app:surface_type="texture_view"
     android:layout_width="match_parent"
     android:layout_height="match_parent"/>

you need to make sure that hardware acceleration is enabled, go to manifest file and ad this line

- At application level: `<application android:hardwareAccelerated="true" ...>`


### implement `onViewRecycled` inside your adapter then call player.release to release player when item is recycled

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
            releaseRecycledPlayers(position)
        super.onViewRecycled(holder)
    }
    
    
    
    
    
# Licence

    Copyright @2020 by Mostafa Anter

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




