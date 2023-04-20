package com.azamovhudstc.memorygame.screens

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.azamovhudstc.memorygame.R
import com.azamovhudstc.memorygame.data.images.Database.Companion.time
import com.azamovhudstc.memorygame.data.model.BackMusic
import com.azamovhudstc.memorygame.data.model.BackMusic.startWinMusic
import com.azamovhudstc.memorygame.data.model.BackMusic.wrongMusicStart
import com.azamovhudstc.memorygame.data.model.GameModel
import com.azamovhudstc.memorygame.data.model.Level
import com.azamovhudstc.memorygame.data.room.entity.GameEntity
import com.azamovhudstc.memorygame.data.shp.MySharedPreference
import com.azamovhudstc.memorygame.databinding.DialogExitBinding
import com.azamovhudstc.memorygame.databinding.FinishGameOverDialogBinding
import com.azamovhudstc.memorygame.databinding.GameScreenBinding
import com.azamovhudstc.memorygame.viewmodel.GameScreenViewModel
import com.azamovhudstc.memorygame.viewmodel.impl.GameScreenViewModelImpl
import com.azamovhudstc.utils.ExplosionField
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import kotlin.math.abs

@AndroidEntryPoint
class GameScreen : Fragment(R.layout.game_screen) {

    private val binding: GameScreenBinding by viewBinding(GameScreenBinding::bind)
    private val viewModel: GameScreenViewModel by viewModels<GameScreenViewModelImpl>()
    private lateinit var dateFormat: SimpleDateFormat

    private var level = Level.EASY
    private var _width = 0
    private var _height = 0
    private var count = 0
    private var cliCount = 0
    var progressTime = 0

    private var countWin = 0
    private var star: Int = 0
    private var a: Long = 0
    private var list = ArrayList<ImageView>()
    private lateinit var images: ArrayList<ImageView>
    private var totalTime: Long = 0
    var mCountDownTimer: CountDownTimer? = null


    private var startTime: Long = 0
    private val args: GameScreenArgs by navArgs()
    private var number: Int = 0
    private var countProgress: MutableLiveData<Int> = MutableLiveData()
    private val shp = MySharedPreference.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showExitDialog()
        }
        BackMusic.create(requireContext())
        BackMusic.createWinMusic(requireContext())

        when (args.game.level) {
            1 -> {
                time = 45000
            }
            2 -> {
                time = 70000
            }
            3 -> {

                time = 90000
            }
        }

    }

    private fun showExitDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding =
            DialogExitBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        dialog.setCancelable(false)

        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )

        dialogBinding.buttonExitNo.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.buttonExitYes.setOnClickListener {
            viewModel.back()
            dialog.dismiss()
        }

        dialog.setView(dialogBinding.root)
        dialog.show()
    }

    private fun createCountDownTimer(timeC: Int) {

        val times = intArrayOf(timeC)
        mCountDownTimer = object : CountDownTimer(times[0].toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                times[0] -= 1000
                time -= 1000
                progressTime = (millisUntilFinished / 1000).toInt()
                println("Progresss:$progressTime")
                binding.playTimerProgress!!.progress = progressTime
                binding.playTimerText.text = progressTime.toString()
            }

            override fun onFinish() {
                //Do what you want
                times[0] = 0
                times[0] = 0
                showResultDialog()
                binding.playTimerProgress!!.progress = 0
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        number = args.game.number
        binding.playLevelRight.text = number.toString()
        when (args.game.level) {
            1 -> {
                count = 3 * 4
                level = Level.EASY
            }
            2 -> {
                count = 4 * 4
                level = Level.MEDIUM
            }
            3 -> {
                count = 4 * 6
                level = Level.HARD
            }
        }
        binding.playTimerProgress.max = time / 1000
        load()
        when (args.game.level) {
            1 -> {
                val resource=requireContext().resources
                val drawable =resource.getDrawable(R.drawable.progress_green)
                binding.playTimerProgress.progressDrawable=drawable
                binding.playTimerProgress.max = 45
                binding.playTimerText.setTextColor(requireContext().getColor(R.color.play_color_green))
            }
            2 -> {
                val resource=requireContext().resources
                val drawable =resource.getDrawable(R.drawable.progress_yellow)
                binding.playTimerProgress.progressDrawable=drawable
                binding.playTimerText.setTextColor(requireContext().getColor(R.color.play_color_yellow))
                binding.playTimerProgress.max = 70

            }
            3 -> {
                val resource=requireContext().resources
                val drawable =resource.getDrawable(R.drawable.progress_orange)
                binding.playTimerProgress.progressDrawable=drawable
                binding.playTimerText.setTextColor(requireContext().getColor(R.color.play_color_orange))
                binding.playTimerProgress.max = 90

            }
        }



        viewModel.getByNumber(args.game.level, number)
        coroutineScope.launch() {

            viewModel.gameModelLiveData.onEach {
                onClick(it)
            }.collect()

        }

    }

    private fun onClick(it: List<GameModel>) {

        Log.d("MMM", it.size.toString())

        for (i in it.indices) {
            val imageView = images[i]
            imageView.tag = it[i]
            imageView.setOnClickListener {
                if (it.rotationY == 0f) {
                    if (cliCount == 1) {
                        openView(imageView)
                        cliCount++
                        val imageView1 = list[0]
                        if (imageView1.tag == imageView.tag) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                delay(1000)
                                val mExplosionField = ExplosionField.attach2Window(activity)
                                mExplosionField.explode(imageView)
                                mExplosionField.explode(imageView1)
                                imageView.visibility = View.INVISIBLE
                                imageView1.visibility = View.INVISIBLE
                                mCountDownTimer!!.cancel()
                                if (shp.music) startWinMusic()
                                createCountDownTimer(3000.let { time += it; time }) //                        994051755
                                countWin += 2
                                if (countWin == count) {
//                                    showResultDialog()
//                                    totalTime = SystemClock.elapsedRealtime()
//                                    time.stop()


                                    dateFormat = SimpleDateFormat("sss")
                                    a = abs(startTime - totalTime)

                                    when (args.game.level) {

                                        1 -> {
                                            when (time) {
                                                in 25000..45000 -> {
                                                    star = 3
                                                }
                                                in 15000..25000 -> {
                                                    star = 2
                                                }
                                                in 0..10000 -> {
                                                    star = 1
                                                }
                                            }
                                        }
                                        2 -> {
                                            when (time) {
                                                in 45000..70000 -> {
                                                    star = 3
                                                }
                                                in 25000..45000 -> {
                                                    star = 2
                                                }
                                                in 0..25000 -> {
                                                    star = 1
                                                }
                                            }
                                        }
                                        3 -> {
                                            when (totalTime) {
                                                in 70000..90000 -> {
                                                    star = 3
                                                }
                                                in 45000..70000 -> {
                                                    star = 2
                                                }
                                                in 25000..45000 -> {
                                                    star = 1
                                                }
                                            }
                                        }
                                    }
                                    if (star >=0 ) {
                                        viewModel.saveResult(
                                            GameEntity(
                                                id = args.game.id,
                                                imageList = args.game.imageList,
                                                number = number,
                                                star = star,
                                                time = totalTime,
                                                state = true,
                                                level = args.game.level
                                            )
                                        )
                                        number++
                                        println("Number::::::::::::$number")
                                        viewModel.openNextLevel(args.game.level, number)
                                        println("Winssssssssssssssssssssssss")
                                    }
                                    viewModel.back()
                                }

                                list.clear()
                                cliCount = 0
                            }
                        } else {
                            lifecycleScope.launch(Dispatchers.Main) {
                                delay(500)
                                if (shp.vibration) vibratePhone()
                                if (shp.music) wrongMusicStart()
                                shakeView(imageView)
                                shakeView(imageView1)
                                delay(700)
                                closeView(imageView)
                                closeView(imageView1)
                                list.clear()
                                cliCount = 0
                            }

                        }
                    } else if (cliCount < 1) {
                        openView(imageView)
                        list.add(imageView)
                        cliCount++
                    }
                }
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun Fragment.vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    300, VibrationEffect.CONTENTS_FILE_DESCRIPTOR
                )
            )
        } else {
            vibrator.vibrate(300)
        }
    }


    private fun showResultDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val binding = FinishGameOverDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val alertDialog = builder.create()



//        binding.quit.setOnClickListener {
//            viewModel.back()
//            alertDialog.dismiss()
//        }
//        binding.dialogPlayTitleBottom.text= args.game.level.toString()
//        binding.dialogPlayScoreValue.text= time.toString()
        binding.buttonExitYes.setOnClickListener {
            alertDialog.dismiss()
            alertDialog.cancel()
            viewModel.back()
          /*  if (args.game.number != 32) {

                load()
                viewModel.getByNumber(
                    args.game.level, number
                )

                this.binding.playLevelRight.text = number.toString()

                alertDialog.dismiss()
            }*//**/
        }
        alertDialog.window!!.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )

        alertDialog.show()
    }

    private fun load() {
        images = ArrayList(count)
        binding.main.post {
            _width = (binding.ln.width - level.x * 10) / level.x + 10
            _height = (binding.ln.width - level.y * 10 + 200) / level.y + 10

            binding.containerImage.layoutParams.apply {
                width = _width * level.x
                height = _height * level.y + 10
            }
            loadImages()
        }
    }


    private fun loadImages() {
        for (x in 0 until level.x) {
            for (y in 0 until level.y) {
                val imageView = ImageView(requireContext())
                binding.containerImage.addView(imageView)
                val lp = imageView.layoutParams as RelativeLayout.LayoutParams

                lp.apply {

                    when (args.game.level) {
                        1 -> {
                            width = (binding.ln.width - level.x * 10) / level.x
                            height = (binding.ln.width - level.y * 10) / level.y + 10

                        }
                        2 -> {
                            width = (binding.ln.width - level.x * 10) / level.x
                            height = (binding.ln.width - level.y * 10) / level.y + 10

                        }
                        3 -> {
                            width = (binding.ln.width - level.x * 10) / level.x
                            height = (binding.ln.width - level.y * 10) / level.y + 10
                        }
                    }
                }

                /* when(args.game.level){
                     1->{
                         imageView.x = x * _width + 9f
                         imageView.y = y * _height + 9f
                     }
                     2->{
                         imageView.x = x * _width * 1f
                         imageView.y = y * _height + 5f
                     }

                     3->{
                         imageView.x = x * _width * 1f
                         imageView.y = y * _height + 2f
                     }
                 }*/

                imageView.x = x * _width * 1f
                imageView.y = y * _height * 1f
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                imageView.layoutParams = lp
                when (args.game.level) {
                    1 -> {
                        imageView.setImageResource(R.drawable.background_1)

                    }
                    2 -> {
                        imageView.setImageResource(R.drawable.background_2)

                    }
                    3 -> {
                        imageView.setImageResource(R.drawable.background_3)
                    }
                }
                images.add(imageView)

                lifecycleScope.launch(Dispatchers.Main) {
                    delay(100)
                    openView(imageView)
                    delay(1500)
                    closeView(imageView)
                }
            }
        }
    }

    override fun onDestroyView() {
        mCountDownTimer!!.cancel()
        coroutineScope.cancel()
        super.onDestroyView()

    }

    override fun onDestroy() {
        mCountDownTimer!!.cancel()
        coroutineScope.cancel()
        super.onDestroy()
    }

    private fun closeView(imageView: ImageView) {
        imageView.animate().setDuration(150).rotationY(90f).withEndAction {
            when (args.game.level) {
                1 -> {
                    imageView.setImageResource(R.drawable.background_1)

                }
                2 -> {
                    imageView.setImageResource(R.drawable.background_2)

                }
                3 -> {
                    imageView.setImageResource(R.drawable.background_3)
                }
            }
            imageView.animate().setDuration(150).rotationY(0f).start()
        }.start()
    }

    private fun openView(imageView: ImageView) {
        imageView.animate().setDuration(150).rotationY(90f).withEndAction {
            imageView.setImageResource((imageView.tag as GameModel).image)
            imageView.animate().setDuration(150).rotationY(180f)
                .setInterpolator(DecelerateInterpolator()).start()
        }.start()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }

    override fun onResume() {
        super.onResume()

        createCountDownTimer(time)

        Log.d("!@#", "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        mCountDownTimer!!.cancel()

    }

    private fun shakeView(view: View) {
        view.animate().setDuration(50).xBy(8f).withEndAction {
            view.animate().setDuration(50).xBy(-16f)
                //.setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    view.animate().setDuration(50).xBy(16f).withEndAction {
                        view.animate().setDuration(50).xBy(-8f).start()
                    }.start()
                }.start()
        }.start()
    }

}
