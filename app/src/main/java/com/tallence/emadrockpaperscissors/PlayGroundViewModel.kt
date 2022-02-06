package com.tallence.emadrockpaperscissors

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tallence.emadrockpaperscissors.Constants.Companion.playArray
import com.tallence.emadrockpaperscissors.Constants.Companion.winArray

class PlayGroundViewModel: ViewModel() {

    private var playerScore = 0
    private var computerScore = 0

    private val playGroundDataState = MutableLiveData<PlayGroundDataState>()
    val playGroundUiState : LiveData<PlayGroundDataState> get() = playGroundDataState

    fun checkWinner(playerChoice: String, computerChoice: String){
        val playerIndex = playArray.indexOf(playerChoice)
        val computerIndex = winArray.indexOf(computerChoice)

        Log.d("GameStatus", "ViewModel: playerScore $playerScore computerScore $computerScore")

        if (playerChoice != computerChoice){
            if (playerIndex == computerIndex){
                emitPlayGroundUiState( ++playerScore, computerScore)
            } else {
                emitPlayGroundUiState( playerScore, ++computerScore)
            }
        } else {
            emitPlayGroundUiState( playerScore, computerScore)
        }
    }

    private fun emitPlayGroundUiState(
        playerScore: Int = 0,
        computerScore: Int = 0
    ){
        val dataState = PlayGroundDataState( playerScore, computerScore)
        playGroundDataState.postValue(dataState)
    }

    data class PlayGroundDataState(
        val playerScore: Int,
        val computerScore: Int
    )
}