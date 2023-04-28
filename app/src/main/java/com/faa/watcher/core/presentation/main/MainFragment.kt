package com.faa.watcher.core.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chad.library.adapter.base.animation.ScaleInAnimation
import com.faa.watcher.core.presentation.main.model.DishItemUiComparator
import com.faa.watcher.core.presentation.main.model.MainEvent
import com.faa.watcher.core.presentation.main.model.MainViewState
import com.faa.watcher.databinding.FragmentMainBinding
import com.faa.watcher.utils.collectUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel by viewModels<MainViewModel>()

    private val adapter: DishAdapter by lazy {
        DishAdapter(
            itemClicked = { item ->
                viewModel.onEvent(MainEvent.DishClicked(item))
            },
            chkSelectChanged = { item ->
                viewModel.onEvent(MainEvent.ChkSelectChanged(item))
            }
        ).apply {
            setDiffCallback(DishItemUiComparator)
            adapterAnimation = ScaleInAnimation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.init()
        binding.setupListeners()
        collectUiState(viewModel.viewState, { binding.updateScreenState(it) })
    }

    private fun FragmentMainBinding.init() {
        lstDishes.adapter = adapter
    }

    private fun FragmentMainBinding.setupListeners() {
        btnDelete.setOnClickListener { viewModel.onEvent(MainEvent.DeleteButtonClicked) }
    }

    private fun FragmentMainBinding.updateScreenState(viewState: MainViewState) {
        if (adapter.data.isEmpty() || viewState.dishes.isEmpty()) {
            adapter.setNewInstance(viewState.dishes.toMutableList())
        } else {
            adapter.setDiffNewData(viewState.dishes.toMutableList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.lstDishes.adapter = null
        _binding = null
    }
}