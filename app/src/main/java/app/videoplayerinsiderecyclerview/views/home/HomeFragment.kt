package app.videoplayerinsiderecyclerview.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.videoplayerinsiderecyclerview.R
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // go to faceHomePage
        facebook_button.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_facebookPlayerFragment)
        }

        // go to tiktok home page
        tiktok_button.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_tikTokPlayerFragment)
        }



    }
}