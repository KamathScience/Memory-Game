package com.dkamath.memorygame.models

import com.dkamath.memorygame.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, customImages: List<String>?){

    val cards: List<MemoryCard>
    var numPairsFound = 0
    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init{
        if(customImages == null){
            val chosenImage: List<Int> = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImage = (chosenImage + chosenImage).shuffled()
            cards = randomizedImage.map { MemoryCard(it) }
        } else{
           val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map{
                MemoryCard(it.hashCode(), it)
            }
        }
    }

    fun flipCard(position: Int) : Boolean{
        numCardFlips++
        val card = cards[position]
        var foundMatch = false
        if(indexOfSingleSelectedCard == null){
            restoreCards()
            indexOfSingleSelectedCard = position
        }else{
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
        return foundMatch
    }

    private fun checkForMatch(position1 : Int, position2 : Int) : Boolean{
        if(cards[position1].identifier != cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards(){
        for(card in cards){
            if(!card.isMatched){
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean{
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean{
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2
    }
}
