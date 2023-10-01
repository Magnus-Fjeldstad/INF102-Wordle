package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;


public class FrequencyStrategy implements IStrategy {

    private WordleWordList guesses;

    public FrequencyStrategy() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) { // O(m*k)
        if (feedback != null) { 
            guesses.eliminateWords(feedback); // O(m*k)
        }
       return guesses.getBestWord(); // O(m*k)
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }    
}

