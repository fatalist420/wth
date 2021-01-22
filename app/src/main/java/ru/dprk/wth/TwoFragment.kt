package ru.dprk.wth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class TwoFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_two, container, false)
        val newText = rootView.findViewById<TextView>(R.id.text_fragment)
        val buttonFt = rootView.findViewById<Button>(R.id.button_fragment)

        buttonFt.setOnClickListener() {
            newText.text = "new text fragment"
        }

        return rootView
    }

    companion object {
        fun newInstance(): TwoFragment {
            return TwoFragment()
        }
    }
}