package com.example.aimissionlite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) { // todo next, bundle with goal_id is already there, but there is no id because you did not set the argument in detailfragment
            Log.i("michl","argument found")
        }

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_goals)
        val goalAdapter = GoalsAdapter()
        recyclerView.adapter = goalAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val viewModel: MainViewModel by viewModels {
            MainViewModel.MainViewModelFactory((this.activity?.application as AimissionApplication).repository)
        }

        viewModel.allGoals.observe(viewLifecycleOwner, Observer { goals ->
            goals?.let { goals ->
                println("!!! data changed! update adapter and so recyclerview.")
                goalAdapter.submitList(goals)
            }
        })
    }
}