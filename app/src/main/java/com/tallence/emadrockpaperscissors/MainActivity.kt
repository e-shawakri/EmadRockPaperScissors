package com.tallence.emadrockpaperscissors

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat.getTransitionName
import com.example.emadrockpaperscissors.databinding.ActivityMainBinding
import android.util.Pair as UtilPair


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startPlayingBtn.setOnClickListener {
            val intent = Intent(this, PlayGroundActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this,
                UtilPair.create(binding.rockIV, getTransitionName(binding.rockIV)),
                UtilPair.create(binding.paperIV, getTransitionName(binding.paperIV)),
                UtilPair.create(binding.scissorIV, getTransitionName(binding.scissorIV)))

            startActivity(intent, options.toBundle())

        }
    }
}