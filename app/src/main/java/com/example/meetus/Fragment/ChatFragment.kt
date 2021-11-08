package com.example.meetus.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.meetus.R

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //الوصول الى عنصر بداخل ال activity_main
        val textViewTitle=activity?.findViewById<TextView>(R.id.title_toolbar_textView)
        textViewTitle?.text="Chats"

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

}