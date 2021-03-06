package com.example.aimissionlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.button_save_goal).setOnClickListener {
            val goalTitle = view.findViewById<EditText>(R.id.edit_text_title).text
            // call via viewmodel insert() and insert the new dataset into room. better is to transform this value to first fragment where we
            // already have the viewmodel defined. This should be done via jetpack navigation. There you also can put values to transfer to.
            // todo make complete goal creatable with all attributes and navigate it with jetpack nav
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }
}