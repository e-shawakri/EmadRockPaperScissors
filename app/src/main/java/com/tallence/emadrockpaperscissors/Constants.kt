package com.tallence.emadrockpaperscissors

class Constants {
    companion object {
        const val PAPER = "paper"
        const val ROCK = "rock"
        const val SCISSOR = "scissor"

        val playArray = arrayListOf(PAPER, ROCK, SCISSOR)
        val winArray = arrayListOf(ROCK, SCISSOR, PAPER)
    }
}