package com.faa.watcher.core.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.animation.ScaleInAnimation
import com.faa.watcher.core.presentation.main.model.MainEvent
import com.faa.watcher.core.presentation.main.model.MainViewEffect
import com.faa.watcher.core.presentation.main.model.MainViewState
import com.faa.watcher.core.presentation.model.DishItemUiComparator
import com.faa.watcher.databinding.FragmentMainBinding
import com.faa.watcher.utils.LinearSpacingDecoration
import com.faa.watcher.utils.collectUiEffect
import com.faa.watcher.utils.collectUiState
import com.faa.watcher.utils.showToast
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
        collectUiEffect(viewModel.viewEffect, ::reactTo)
    }

    private fun FragmentMainBinding.init() {
        binding.lstDishes.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.lstDishes.addItemDecoration(LinearSpacingDecoration.default)
        lstDishes.adapter = adapter
    }

    private fun FragmentMainBinding.setupListeners() {
        btnDelete.setOnClickListener { viewModel.onEvent(MainEvent.DeleteButtonClicked) }
        networkState.btnRetry.setOnClickListener { viewModel.onEvent(MainEvent.ReloadData) }
    }

    private fun FragmentMainBinding.updateScreenState(viewState: MainViewState) {
        networkState.progress.isVisible = viewState.isLoading
        networkState.containerErrorState.isVisible = viewState.isError
        containerContent.isVisible = viewState.isContainerMainIsVisible
        binding.txtNotFound.isVisible = viewState.isTxtNotFoundIsVisible
        binding.btnDelete.isEnabled = viewState.isBtnDeleteEnabled

        if (adapter.data.isEmpty() || viewState.dishes?.isEmpty() == true) {
            adapter.setNewInstance(viewState.dishes?.toMutableList())
        } else {
            adapter.setDiffNewData(viewState.dishes?.toMutableList())
        }
    }

    private fun reactTo(viewEffect: MainViewEffect) {
        when (viewEffect) {
            is MainViewEffect.ShowMessage -> {
                showToast(viewEffect.uiText)
            }

            is MainViewEffect.MoveDetailScreen -> {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(viewEffect.id))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.lstDishes.adapter = null
        _binding = null
    }
}