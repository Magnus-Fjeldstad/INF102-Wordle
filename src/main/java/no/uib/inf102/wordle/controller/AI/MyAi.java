package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.uib.inf102.wordle.model.word.AnswerType;
import no.uib.inf102.wordle.model.word.WordleCharacter;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

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
            copyList = eliminateGreenAsGrey(copyList, feedback);
            confirmedPositions(feedback);
            removeGreenFromCopyList();
        }

        if (guessCount == 1) {
            System.out.println("CopyListPossible answers "+copyList.possibleAnswers());
            return copyList.getBestWord();
        }
        guessCount ++;

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
        copyList = new WordleWordList(guesses.possibleAnswers());
        confirmedGreen = new HashMap<>();
        confirmedYellow = new HashMap<>();
    }

    private void confirmedPositions(WordleWord feedback) {
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



    private WordleWordList eliminateGreenAsGrey(WordleWordList wordList, WordleWord feedback) {
        if (feedback == null) {
            return wordList;
        }
    
        List<String> filteredPossibleAnswers = new ArrayList<>();
    
        for (String currentGuess : wordList.possibleAnswers()) {
            boolean validWord = true;
    
            for (Map.Entry<Character, Integer> entry : confirmedGreen.entrySet()) {
                char greenLetter = entry.getKey();
                int greenPosition = entry.getValue();
    
                if (currentGuess.length() <= greenPosition || // Check if the word is long enough
                    currentGuess.charAt(greenPosition) != greenLetter || // If the letter at the confirmed position is different
                    currentGuess.indexOf(greenLetter) != greenPosition) { // or if the letter exists elsewhere
                    validWord = false;
                    break;
                }
            }
    
            if (validWord) {
                filteredPossibleAnswers.add(currentGuess);
            }
        }
    
        return new WordleWordList(filteredPossibleAnswers);
    }    
}