package com.cyberknights4911.scouting.match

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cyberknights4911.scouting.databinding.FragmentMatchListBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of [MatchJson]es.
 */
@AndroidEntryPoint
class MatchFragment : Fragment() {
    private val matchViewModel: MatchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener( "teamSelect") { _, bundle ->
            val teamId = bundle.getString("teamId")!!
            val matchId = bundle.getString("matchId")!!
            findNavController().navigate(
                MatchFragmentDirections.matchesToAutoStart(matchId, teamId)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentMatchListBinding = FragmentMatchListBinding.inflate(
            inflater, container, false)

        val matchAdapter = MatchAdapter(
            MatchListener {
                findNavController().navigate(
                    MatchFragmentDirections.matchesToTeams(it.matchTbaKey)
                )
                Log.d("MatchFragment", "Match selected: ${it.matchTbaKey}")
            }
        )

        with (binding) {
            list.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = matchAdapter
            }
        }
        matchViewModel.matchesWithTeams.observe(viewLifecycleOwner) {
            it?.let {
                matchAdapter.submitList(it)
            }
        }

        return binding.root
    }

}