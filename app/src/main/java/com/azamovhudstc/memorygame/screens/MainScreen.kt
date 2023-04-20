package com.azamovhudstc.memorygame.screens

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.azamovhudstc.memorygame.R
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import com.azamovhudstc.memorygame.data.shp.MySharedPreference
import com.azamovhudstc.memorygame.databinding.MainScreenBinding
import com.azamovhudstc.memorygame.viewmodel.MenuScreenViewModel
import com.azamovhudstc.memorygame.viewmodel.MiniScreenViewModel
import com.azamovhudstc.memorygame.viewmodel.impl.MenuScreenViewModelImpl
import com.azamovhudstc.memorygame.viewmodel.impl.MiniScreenViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint

class MainScreen : Fragment(R.layout.main_screen) {
    private val binding: MainScreenBinding by viewBinding(MainScreenBinding::bind)

    private val viewModel: MenuScreenViewModel by viewModels<MenuScreenViewModelImpl>()
    private lateinit var level: String
    private val shp = MySharedPreference.getInstance()


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainEasyButton.setOnClickListener {
            println("Button Clicked")
            viewModel.onClickPlay("4x4\neasy")
        }
        binding.mainNormalButton.setOnClickListener {
            viewModel.onClickPlay("6x6\nmedium")
        }
        binding.mainHardButton.setOnClickListener {
            viewModel.onClickPlay(
                "6x8\n" +
                        "hard"
            )
        }
        binding.mainPlusIconButton.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogBinding =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_setting, binding.container, false)

            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.findViewById<ImageView>(R.id.settings_dialog_close).setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }
            val musicCheck = dialogBinding.findViewById<CheckBox>(R.id.settings_sound_checkbox)
            val vibrateCheck = dialogBinding.findViewById<CheckBox>(R.id.settings_timer_checkbox)
            val saveButton:AppCompatButton=dialogBinding.findViewById(R.id.settings_save_button)

            musicCheck.isChecked=shp.music
            vibrateCheck.isChecked=shp.vibration

            saveButton.setOnClickListener {
                shp.music = musicCheck.isChecked
                shp.vibration=vibrateCheck.isChecked
                dialog.cancel()
            }

//            dialogBinding.findViewById<AppCompatButton>(R.id.yes).setOnClickListener {
//
//                dialog.cancel()
//                dialog.dismiss()
//            }
            dialog.setView(dialogBinding)
            dialog.show()
        }
        binding.mainMenuIconButton.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).create()
            val dialogBinding =
                LayoutInflater.from(requireContext())
                    .inflate(R.layout.info_dialog, binding.container, false)

            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialogBinding.findViewById<AppCompatButton>(R.id.info_ok).setOnClickListener {

                dialog.cancel()
                dialog.dismiss()
            }
            dialog.setView(dialogBinding)
            dialog.show()
        }
    }
}