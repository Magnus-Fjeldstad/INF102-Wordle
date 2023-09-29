package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.uib.inf102.wordle.model.word.AnswerType;
import no.uib.inf102.wordle.model.word.WordleCharacter;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class BetterAi implements IStrategy {

    private WordleWordList guesses;
    private WordleWordList copyList;
    private Integer guessCount = 0;
    private List<String> possibleGuesses;

    private HashMap<Character, Integer> confirmedGreen;
    private HashMap<Character, Integer> confirmedYellow;

    public BetterAi() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
            eliminateWordsFromGuesses(feedback);
            confimredPostions(feedback);
            
        }

        if (guessCount == 0) {
            guessCount++;
            removeGreenFromCopyList();
            return guesses.getBestWord();
        } else if (guessCount == 1) {
            guessCount++;
            // Check if the probability is below a threshold to use entropy-based strategy
            double probabilityThreshold = 0.2; // Adjust this threshold as needed
            if (calculateCurrentProbability() < probabilityThreshold) {
                System.out.println("best word form entopy: " + getBestWordFromEntropy());
                return getBestWordFromEntropy();
            }
        }

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

    @Override
    public void reset() {
        guesses = new WordleWordList();
        copyList = new WordleWordList(guesses.getAllWords());
        possibleGuesses = new ArrayList<>(copyList.getAllWords());
        confirmedGreen = new HashMap<>();
        confirmedYellow = new HashMap<>();
        confirmedGreen.clear();
        confirmedYellow.clear();
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

    private void eliminateWordsFromGuesses(WordleWord feedback) {
        if (feedback == null) {
            return;
        }

        List<String> filteredPossibleAnswers = new ArrayList<>();

        for (String currentGuess : copyList.getAllWords()) {
            if (WordleWord.isPossibleWord(currentGuess, feedback) && !containsGreenLetter(currentGuess, feedback)) {
                filteredPossibleAnswers.add(currentGuess);
            }
        }

        possibleGuesses = filteredPossibleAnswers;
    }

    private boolean containsGreenLetter(String word, WordleWord feedback) {
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (confirmedGreen.containsKey(letter) && confirmedGreen.get(letter) == i) {
                return true;
            }
        }
        return false;
    }

    private String getBestWordFromEntropy() {
        double minEntropy = Double.MAX_VALUE;
        String bestWord = possibleGuesses.isEmpty() ? "" : possibleGuesses.get(0);

        for (String word : possibleGuesses) {
            double wordEntropy = calculateWordEntropy(word);

            if (wordEntropy < minEntropy) {
                minEntropy = wordEntropy;
                bestWord = word;
            }
        }

        return bestWord;
    }

    private double calculateWordEntropy(String word) {
        double wordEntropy = 0.0;

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            double letterProbability = calculateLetterProbability(word, letter);
            if (letterProbability > 0) {
                wordEntropy -= letterProbability * Math.log(letterProbability) / Math.log(2);
            }
        }

        return wordEntropy;
    }

    private double calculateLetterProbability(String word, char letter) {
        int count = 0;
        int total = 0;

        for (String guess : possibleGuesses) {
            total++;
            if (guess.charAt(guessCount - 1) == letter) {
                count+= 1000;
            }
        }

        return (double) count / total;
    }

    private double calculateCurrentProbability() {
        // Example: Calculate probability based on the number of remaining possible
        // words
        int remainingWords = guesses.possibleAnswers().size();
        int totalWords = copyList.getAllWords().size();

        if (totalWords > 0) {
            return (double) remainingWords / totalWords;
        } else {
            // Handle the case when there are no words left to guess
            return 0.0;
        }
    }

}
