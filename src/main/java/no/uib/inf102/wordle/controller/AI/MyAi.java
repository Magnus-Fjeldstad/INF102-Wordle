package no.uib.inf102.wordle.controller.AI;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class MyAi implements IStrategy {

    private WordleWordList guesses;

    public MyAi() {
        reset();
    }
  

    @Override
    public String makeGuess(WordleWord feedback) {
       //TODO
       return "";
    }
    

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }
}
