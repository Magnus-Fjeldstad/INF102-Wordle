package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
            copyList = eliminateGreenAsGrey(copyList, feedback);
            confirmedPositions(feedback);
            removeGreenFromCopyList();
            removeGreyFromCopyList(feedback);
            removeWordsWithoutYellowFromCopyList();
        }

        if (guessCount == 0) {
            guessCount++;
            return guesses.getBestWord();
        }

        if (confirmedGreen.size() >= 1
                || (confirmedYellow.size() >= 1 && confirmedGreen.size() < 3 && confirmedYellow.size() < 3)) {
            // Prioritize words with at least one "WRONG_POSITION" (yellow) letter for the
            // second guess
            if (copyList.size() != 0) {
                String secondGuess = getBestWordWithYellowForSecondGuess(feedback);
                if (secondGuess != null) {
                    guessCount++;
                    return secondGuess;
                }
            }
        }

        // If copyList is empty or there are no words with yellow letters in feedback
        // positions, fall back to using guesses.
        return guesses.getBestWord();
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

    private String getBestWordWithYellowLetter() {
        List<HashMap<Character, Integer>> charCountMapsList = guesses.mapCommonLetters(guesses.getAllWords());
        HashMap<String, Integer> wordMap = new HashMap<>();
        String bestWord = "";
        int highScore = -1;

        for (String word : guesses.getAllWords()) {
            int wordPoints = 0;

            for (int currentMap = 0; currentMap < charCountMapsList.size(); currentMap++) {
                char c = word.charAt(currentMap);
                wordPoints += charCountMapsList.get(currentMap).getOrDefault(c, 0);
            }

            if (wordPoints > highScore) {
                highScore = wordPoints;
                bestWord = word;
                wordMap.put(bestWord, wordPoints);
            }
        }

        return bestWord;
    }

    public String getBestWordWithYellowForSecondGuess(WordleWord feedback) {
        List<String> possibleWords = guesses.getAllWords();

        String previousGuess = guesses.getBestWord(); // Get the previous guess

        if (feedback != null) {
            List<String> wordsWithYellowInFeedbackPosition = new ArrayList<>();

            for (String word : possibleWords) {
                if (containsYellowLetterInFeedbackPosition(word, feedback) && !containsGreyLetter(word, feedback)
                        && hasNoDuplicateCharacters(word) && !hasYellowInSamePosition(word, previousGuess, feedback)) {
                    wordsWithYellowInFeedbackPosition.add(word);
                }
            }

            if (!wordsWithYellowInFeedbackPosition.isEmpty()) {
                // Prioritize words with yellow letters in feedback positions, no grey letters,
                // no duplicate characters, and no yellow in the same position as the previous
                // guess
                return wordsWithYellowInFeedbackPosition.get(0); // Choose the first suitable word
            }
        }

        // Fall back to the original method if no suitable words are found
        return getBestWordWithYellowLetter();
    }

    // Add this method to check if a word has no duplicate characters
    private boolean hasNoDuplicateCharacters(String word) {
        Set<Character> seenCharacters = new HashSet<>();
        for (char c : word.toCharArray()) {
            if (seenCharacters.contains(c)) {
                return false; // Found a duplicate character
            }
            seenCharacters.add(c);
        }
        return true; // No duplicate characters found
    }

    // Add this method to check if yellow letters are not in the same position as
    // the previous guess
    private boolean hasYellowInSamePosition(String currentGuess, String previousGuess, WordleWord feedback) {
        for (WordleCharacter wc : feedback) {
            if (wc.answerType == AnswerType.WRONG_POSITION) {
                int position = confirmedYellow.getOrDefault(wc.letter, -1);
                if (position >= 0 && position < currentGuess.length() && currentGuess.charAt(position) == wc.letter) {
                    return true; // Yellow in the same position as the previous guess
                }
            }
        }
        return false; // No yellow in the same position as the previous guess
    }

    // Add this method to check for yellow letters in feedback positions
    private boolean containsYellowLetterInFeedbackPosition(String word, WordleWord feedback) {
        for (WordleCharacter wc : feedback) {
            if (wc.answerType == AnswerType.WRONG_POSITION) {
                int position = confirmedYellow.getOrDefault(wc.letter, -1);
                if (position >= 0 && position < word.length() && word.charAt(position) == wc.letter) {
                    return true;
                }
            }
        }
        return false;
    }
}
