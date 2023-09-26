package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uib.inf102.wordle.model.word.AnswerType;
import no.uib.inf102.wordle.model.word.WordleCharacter;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;
/*
 * Gjør et guess på den originale listen
 * Lag en kopi av den originale listen der man i tilleg fjerner alle grønne grå bokstavene
 * gjør et nytt guess på den originale listen med det beste ordet basert på den kopierte listen
 * Fortsett slik til man finner det beste ordet.
 */

public class MyAi implements IStrategy {

    private WordleWordList guesses;
    private WordleWordList copyList;
    private Integer guessCount = 0;

    private HashMap<Character, Integer> confirmedGreen;
    private HashMap<Character, Integer> confirmedYellow;

    public MyAi() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
            copyList.eliminateWords(feedback);
            confimredPostions(feedback);
            System.out.println("this is green" + confirmedGreen);
            System.out.println("this is yellow" + confirmedYellow);
        }
        if (guessCount == 0) {
            guessCount++;
            return guesses.getBestWord();
        }
        if (guessCount == 1) {
            guessCount++;
            removeGreenFromCopyList();
            return copyList.getBestWord();
        }

        return guesses.getBestWord();
    }

    private void removeGreenFromCopyList() {
        List<String> wordsToRemove = new ArrayList<>();

        for (String word : copyList.possibleAnswers()) {
            boolean containsGreen = false;

            for (char c : word.toCharArray()) {
                if (confirmedGreen.containsKey(c)) {
                    containsGreen = true;
                    break;
                }
            }

            if (containsGreen) {
                wordsToRemove.add(word);
            }
        }

        for (String wordToRemove : wordsToRemove) {
            copyList.remove(wordToRemove);
        }
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
        copyList = new WordleWordList();
        confirmedGreen = new HashMap<>();
        confirmedYellow = new HashMap<>();
    }

    private void confimredPostions(WordleWord feedback) {
        int index = 0;
        for (WordleCharacter wc : feedback) {
            if (wc.answerType == AnswerType.CORRECT) {
                confirmedGreen.put(wc.letter, index);
            }
            if (wc.answerType == AnswerType.WRONG_POSITION) {
                confirmedYellow.put(wc.letter, index);
            }
            index++;
        }
    }
}
