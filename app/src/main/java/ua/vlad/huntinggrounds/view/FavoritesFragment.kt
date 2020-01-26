package ua.vlad.huntinggrounds.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.vlad.huntinggrounds.MainActivity
import ua.vlad.huntinggrounds.R

class FavoritesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar!!.setTitle(R.string.menu_favorites)
    }
}
