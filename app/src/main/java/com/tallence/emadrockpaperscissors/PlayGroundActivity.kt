package com.tallence.emadrockpaperscissors

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.example.emadrockpaperscissors.R
import com.example.emadrockpaperscissors.databinding.ActivityPlayGroundBinding
import com.tallence.emadrockpaperscissors.Constants.Companion.PAPER
import com.tallence.emadrockpaperscissors.Constants.Companion.ROCK
import com.tallence.emadrockpaperscissors.Constants.Companion.SCISSOR
import com.tallence.emadrockpaperscissors.Constants.Companion.winArray
import kotlin.properties.Delegates


class PlayGroundActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayGroundBinding
    private val playViewModel: PlayGroundViewModel by viewModels()

    private var rockViewX by Delegates.notNull<Float>()
    private var rockViewY by Delegates.notNull<Float>()

    private var paperViewX by Delegates.notNull<Float>()
    private var paperViewY by Delegates.notNull<Float>()

    private var scissorViewX by Delegates.notNull<Float>()
    private var scissorViewY by Delegates.notNull<Float>()

    private var mScreenWidth by Delegates.notNull<Int>()

    private var grayDraw by Delegates.notNull<Int>()
    private var greenWin by Delegates.notNull<Int>()
    private var redLost by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayGroundBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mScreenWidth = Resources.getSystem().displayMetrics.widthPixels

        binding.scissorIV.post {
            rockViewX = binding.rockIV.x
            rockViewY = binding.rockIV.y

            paperViewX = binding.paperIV.x
            paperViewY = binding.paperIV.y

            scissorViewX = binding.scissorIV.x
            scissorViewY = binding.scissorIV.y
        }

        grayDraw = ContextCompat.getColor(this, R.color.gray_draw)
        greenWin = ContextCompat.getColor(this, R.color.green_win)
        redLost = ContextCompat.getColor(this, R.color.red_lost)

        playViewModel.playGroundUiState.observe(this, {
            publishScore(it.playerScore, it.computerScore)
        })

        binding.paperIV.setOnClickListener {
            animatePlay(it, binding.rockIV, binding.scissorIV, PAPER)
        }

        binding.rockIV.setOnClickListener {
            animatePlay(it, binding.paperIV, binding.scissorIV, ROCK)
        }

        binding.scissorIV.setOnClickListener {
            animatePlay(it, binding.paperIV, binding.rockIV, SCISSOR)
        }

        binding.backArrowIV.setOnClickListener {
            onBackPressed()
        }
    }

    private fun animatePlay(
        animateView: View,
        hideView1: View,
        hideView2: View,
        playerChoice: String
    ) {

        val rockWidth = binding.rockIV.measuredWidth

        animateView.animate()
            .x(binding.paperIV.x - (mScreenWidth / 2 - (rockWidth / 1.7).toInt()))
            .y(binding.paperIV.y)
            .setDuration(500L)
            .withStartAction {
                hideView1.animate().alpha(0f).duration = 100
                hideView2.animate().alpha(0f).duration = 100
            }.withEndAction {

                val player2Choose = winArray.random()

                when (player2Choose) {
                    ROCK -> positionAnimation(binding.opponentRockIV, rockWidth)
                    PAPER -> positionAnimation(binding.opponentPaperIV, rockWidth)
                    SCISSOR -> positionAnimation(binding.opponentScissorIV, rockWidth)
                }
                playViewModel.checkWinner(playerChoice, player2Choose)
            }
    }

    private fun positionAnimation(opponentSelection: ImageView, rockWidth: Int) {
        opponentSelection.animate().alpha(1f).setDuration(100).withEndAction {
            opponentSelection.visibility = View.VISIBLE
        }.start()

        opponentSelection.animate()
            .x(paperViewX + (mScreenWidth / 2 - (rockWidth / 1.7).toInt()))
            .y(paperViewY)
            .setDuration(500)
            .start()
    }

    private fun publishScore(playerScore: Int, computerScore: Int) {

        binding.meScoreTV.text = "$playerScore"
        binding.computerScoreTV.text = "$computerScore"

        when {
            playerScore == computerScore -> animateChangeColor(grayDraw, grayDraw)
            playerScore > computerScore -> animateChangeColor(greenWin, redLost)
            else -> animateChangeColor(redLost, greenWin)
        }
    }


    private fun animateChangeColor(
        playerTextColor: Int,
        colorTextColor: Int
    ) {

        val multiAnims = AnimatorSet()
        multiAnims.playTogether(
            ObjectAnimator.ofArgb(
                binding.computerScoreTV,
                "textColor",
                Color.GRAY,
                colorTextColor
            ),
            ObjectAnimator.ofArgb(binding.computerTV, "textColor", Color.GRAY, colorTextColor),
            ObjectAnimator.ofArgb(binding.meScoreTV, "textColor", Color.GRAY, playerTextColor),
            ObjectAnimator.ofArgb(binding.meTV, "textColor", Color.GRAY, playerTextColor),
        )
        multiAnims.duration = 800
        multiAnims.doOnEnd {

            val multiAlpha = AnimatorSet()
            multiAlpha.playTogether(
                ObjectAnimator.ofFloat(binding.rockIV, "alpha", 1f),
                ObjectAnimator.ofFloat(binding.paperIV, "alpha", 1f),
                ObjectAnimator.ofFloat(binding.scissorIV, "alpha", 1f),
                ObjectAnimator.ofFloat(binding.opponentRockIV, "alpha", 0f),
                ObjectAnimator.ofFloat(binding.opponentPaperIV, "alpha", 0f),
                ObjectAnimator.ofFloat(binding.opponentScissorIV, "alpha", 0f)
            )

            // Animate for 200 milliseconds
            multiAlpha.duration = 200

            multiAlpha.doOnEnd {
                binding.opponentRockIV.visibility = View.INVISIBLE
                binding.opponentPaperIV.visibility = View.INVISIBLE
                binding.opponentScissorIV.visibility = View.INVISIBLE
                binding.rockIV.visibility = View.VISIBLE
                binding.paperIV.visibility = View.VISIBLE
                binding.scissorIV.visibility = View.VISIBLE

                binding.opponentRockIV.animate()
                    .x(rockViewX)
                    .y(rockViewY)
                    .setDuration(100)
                    .start()


                binding.opponentPaperIV.animate()
                    .x(paperViewX)
                    .y(paperViewY)
                    .setDuration(100)
                    .start()

                binding.opponentScissorIV.animate()
                    .x(scissorViewX)
                    .y(scissorViewY)
                    .setDuration(100)
                    .start()
            }

            multiAlpha.start()

            binding.rockIV.animate()
                .x(rockViewX)
                .y(rockViewY)
                .setDuration(500L)
                .start()

            binding.paperIV.animate()
                .x(paperViewX)
                .y(paperViewY)
                .setDuration(500L)
                .start()

            binding.scissorIV.animate()
                .x(scissorViewX)
                .y(scissorViewY)
                .setDuration(500L)
                .start()
        }
        multiAnims.start()
    }
}
