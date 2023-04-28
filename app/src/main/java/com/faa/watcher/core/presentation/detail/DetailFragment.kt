package com.faa.watcher.main.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.faa.watcher.R
import com.faa.watcher.common.collectUiEffect
import com.faa.watcher.common.collectUiState
import com.faa.watcher.common.showToast
import com.faa.watcher.databinding.FragmentDetailBinding
import com.faa.watcher.main.presentation.detail.model.DetailViewEffect
import com.faa.watcher.main.presentation.detail.model.DetailViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectUiState(viewModel.uiState, { binding.updateScreenState(it) })
        collectUiEffect(viewModel.viewEffect, ::reactTo)
    }

    private fun FragmentDetailBinding.updateScreenState(viewState: DetailViewState) {
        Glide.with(context ?: return)
            .load(viewState.dish?.image)
            .placeholder(R.drawable.pic_load)
            .error(R.drawable.pic_error_load)
            .into(imgImage)

        txtName.text = viewState.dish?.name.orEmpty()
        txtDescription.text = viewState.dish?.description.orEmpty()
    }

    private fun reactTo(viewEffect: DetailViewEffect) {
        when (viewEffect) {
            is DetailViewEffect.ShowMessage -> {
                showToast(viewEffect.uiText)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}