package com.example.aimissionlite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.aimissionlite.models.*
import com.example.aimissionlite.models.domain.Genre
import com.example.aimissionlite.models.domain.Goal
import com.example.aimissionlite.models.domain.Priority
import com.example.aimissionlite.models.domain.Status
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : IDetailFragment, Fragment() {
    private var choosenChipGenre: Chip? = null
    private var choosenChipPriority: Chip? = null

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

        val chipGroupGenre = view.findViewById<ChipGroup>(R.id.chip_group_genre) // todo use data-binding
        val chipGroupPriority = view.findViewById<ChipGroup>(R.id.chip_group_priority) // todo use data-binding

        chipGroupGenre.setOnCheckedChangeListener { group, checkedId ->
            choosenChipGenre = view.findViewById(checkedId) // todo use data-binding
            Toast.makeText(activity, "${choosenChipGenre?.text} clicked", Toast.LENGTH_SHORT).show()
        }

        chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            choosenChipPriority = view.findViewById(checkedId) // todo use data-binding
            Toast.makeText(activity, "${choosenChipPriority?.text} clicked", Toast.LENGTH_SHORT)
                .show()
        }

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
                changeDate = currentDate,
                isRepeated = false,
                genre = choosenChipGenre.toGenre(),
                status = Status.UNKOWN,
                priority = choosenChipPriority.toPriority()
            )

            addGoal(newGoal)

            val bundle =
                bundleOf(resources.getString(R.string.bundle_argument_goal_title) to goalTitle)
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
        }
    }

    override fun addGoal(goal: Goal) {
        viewModel.insert(goal)
    }

    companion object {
        private fun Chip?.toGenre(): Genre =
            when (this?.id) {
                R.id.chip_genre_business -> Genre.BUSINESS
                R.id.chip_genre_socialising -> Genre.SOCIALISING
                R.id.chip_genre_fittness -> Genre.FITTNESS
                R.id.chip_genre_money -> Genre.MONEY
                R.id.chip_genre_partnership -> Genre.PARTNERSHIP
                R.id.chip_genre_health -> Genre.HEALTH
                else -> Genre.UNKNOWN
            }

        private fun Chip?.toPriority(): Priority =
            when (this?.id) {
                R.id.chip_priority_low -> Priority.LOW
                R.id.chip_priority_high -> Priority.HIGH
                else -> Priority.NORMAL
            }
    }
}