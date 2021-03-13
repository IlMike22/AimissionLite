package com.example.aimissionlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aimissionlite.models.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : IDetailFragment, Fragment() {
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModel.DetailViewModelFactory((this.activity?.application as AimissionApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_save_goal).setOnClickListener {
            val goalTitle = view.findViewById<EditText>(R.id.edit_text_title).text.toString()
            val goalDescription =
                view.findViewById<EditText>(R.id.edit_text_description).text.toString()

            val currentDate = viewModel.getCurrentDate()

            val newGoal = Goal(
                id = 0,
                title = goalTitle,
                description = goalDescription,
                creationDate = currentDate,
                changeDate = currentDate
//                isRepeated = false,
//                genre = Genre.UNKNOWN,  // this must already be implemented
//                status = Status.UNKOWN,// this must already be implemented
//                priority = Priority.UNKNOWN // this must already be implemented
            )

            addGoal(newGoal)


            // call via viewmodel insert() and insert the new dataset into room.
            // use the id of the new goal to pass it to main fragment
            // there we ready out the new goal again because we should not transport the complete goal via navigation
            // after we have read the new goal in main fragment we update the recycler view after we added the new goal (?)

            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun addGoal(goal: Goal) {
        viewModel.insert(goal)
    }
}