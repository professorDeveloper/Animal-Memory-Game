package com.azamovhudstc.memorygame.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.azamovhudstc.memorygame.R
import com.azamovhudstc.memorygame.adapter.LevelAdapter
import com.azamovhudstc.memorygame.databinding.ScreenMiniGameBinding
import com.azamovhudstc.memorygame.viewmodel.MiniScreenViewModel
import com.azamovhudstc.memorygame.viewmodel.impl.MiniScreenViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@AndroidEntryPoint
class MinLevelScreen:Fragment(R.layout.screen_mini_game) {
    private val binding: ScreenMiniGameBinding by viewBinding(ScreenMiniGameBinding::bind)
    private val viewModel: MiniScreenViewModel by viewModels<MiniScreenViewModelImpl>()
    private val adapter: LevelAdapter by lazy { LevelAdapter() }
    private val arg: MinLevelScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (arg.level) {
            "4x4\neasy" -> {
                binding.title.text = "Easy"
                viewModel.generateEasy()
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.easyLevelsList.collectLatest {
                        if (it != null) {
                            binding.progress.visibility = View.GONE
                        } else {
                            binding.progress.visibility = View.VISIBLE
                        }
                        adapter.submitList(it)
                    }
                }

            }
            "6x6\nmedium" -> {
                viewModel.generateMedium()
                binding.title.text = "Medium"
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.mediumLevelsList.collectLatest {
                        if (it != null) {
                            binding.progress.visibility = View.GONE
                        } else {
                            binding.progress.visibility = View.VISIBLE
                        }
                        adapter.submitList(it)
                    }
                }
            }
            "6x8\nhard" -> {
                viewModel.generateHard()
                binding.title.text = "Hard"
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.hardLevelsList.collectLatest {
                        if (it != null) {
                            binding.progress.visibility = View.GONE
                        } else {
                            binding.progress.visibility = View.VISIBLE
                        }
                        adapter.submitList(it)
                    }
                }
            }
        }
        binding.back
            .setOnClickListener { viewModel.back() }
        binding.list.adapter = adapter

        adapter.setSwitchChangedListener {
            viewModel.openGameScreen(it)
        }
    }
}