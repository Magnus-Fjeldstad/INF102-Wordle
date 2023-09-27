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
    private Integer guessCount;

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
            copyList = eliminateGreenAsGrey(copyList, feedback);
            confirmedPositions(feedback);
            removeGreenFromCopyList();
            removeWordsWithoutYellowFromCopyList();
        }

        if (guessCount == 0) {
            guessCount++;
            return orate();
            //return guesses.getBestWord(guesses.possibleAnswers());
        } 
        if (guessCount == 1 || guessCount == 2 && confirmedGreen.size() <= 1 && confirmedYellow.size() <= 1) {
            if (copyList.size()!=0) {
                guessCount++;
                return copyList.getBestWord(guesses.getAllWords());
            }
        }
        //System.out.println("Best word form all words: "+copyList.getBestWord(copyList.getAllWords()));

        guessCount++;
        // If copyList is empty, fall back to using guesses.
        return guesses.getBestWord(guesses.possibleAnswers());
    }

    private void removeGreenFromCopyList() {
        List<String> wordsToRemove = new ArrayList<>();

        for (String word : copyList.getAllWords()) {
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

    private void removeGreyFromCopyList(WordleWord feedback) {
        List<String> wordsToRemove = new ArrayList<>();
        for (String word : copyList.getAllWords()) {
            if (containsGreyLetter(word, feedback)) {
                wordsToRemove.add(word);
            }
        }
        for (String wordToRemove : wordsToRemove) {
            copyList.remove(wordToRemove);
        }
    }

    private void removeWordsWithoutYellowFromCopyList() {
        List<String> wordsToRemove = new ArrayList<>();
        for (String word : copyList.getAllWords()) {
            if (!containsYellowLetter(word)) {
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
        copyList = new WordleWordList(guesses.getAllWords());
        confirmedGreen = new HashMap<>();
        confirmedYellow = new HashMap<>();
        guessCount = 0;
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

        for (String currentGuess : wordList.getAllWords()) {
            boolean validWord = true;

            for (Map.Entry<Character, Integer> entry : confirmedGreen.entrySet()) {
                char greenLetter = entry.getKey();
                int greenPosition = entry.getValue();

                int firstOccurrence = currentGuess.indexOf(greenLetter);
                int lastOccurrence = currentGuess.lastIndexOf(greenLetter);

                if (currentGuess.length() <= greenPosition ||
                        currentGuess.charAt(greenPosition) != greenLetter ||
                        (firstOccurrence != lastOccurrence && firstOccurrence != greenPosition)) {
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

    private boolean containsGreyLetter(String word, WordleWord feedback) {
        for (WordleCharacter wc : feedback) {
            if (wc.answerType == AnswerType.WRONG && word.contains(String.valueOf(wc.letter))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsYellowLetter(String word) {
        for (Character c : confirmedYellow.keySet()) {
            if (word.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }
    public String orate() {
        return "orate";
    }
}