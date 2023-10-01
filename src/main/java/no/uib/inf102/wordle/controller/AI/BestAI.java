package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class BestAI implements IStrategy {

    private WordleWordList guesses;
    private final int WORD_LENGTH = 5;
  
    public BestAI() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }
        List<String> possibleWords = guesses.possibleAnswers(); 
        HashMap<Character, List<Integer>> frequencies = mapLetters(possibleWords);
        return bestWord(possibleWords, frequencies);
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }

    private HashMap<Character, List<Integer>> mapLetters(List<String> possibleAnswers) {
        HashMap<Character, List<Integer>> charCountMap = new HashMap<>();
        for (String word : possibleAnswers) {
            for (int i = 0; i < WORD_LENGTH; i++) {
                char c = word.charAt(i);
                if (!charCountMap.containsKey(c)) {
                    charCountMap.put(c, new ArrayList<>(Collections.nCopies(WORD_LENGTH, 0)));
                }
                charCountMap.get(c).set(i, charCountMap.get(c).get(i) + 1);
            }
        }
        return charCountMap;
    }

    private HashMap<String, Double> wordScore(List<String> possibleWords, HashMap<Character, List<Integer>> frequencies) {
        HashMap<String, Double> words = new HashMap<>();
        List<Integer> max_freq = new ArrayList<>(Collections.nCopies(WORD_LENGTH, 0));

        for (Character c : frequencies.keySet()) {
            for (int i = 0; i < WORD_LENGTH; i++) {
                if (max_freq.get(i) < frequencies.get(c).get(i)) {
                    max_freq.set(i, frequencies.get(c).get(i));
                }
            }
        }

        for (String word : possibleWords) {
            double score = 1.0;
            for (int i = 0; i < WORD_LENGTH; i++) {
                char c = word.charAt(i);
                score *= 1 + Math.pow(frequencies.get(c).get(i) - max_freq.get(i), 2);
            }
            words.put(word, score);
        }
        return words;
    }

    private String bestWord(List<String> possibleWords, HashMap<Character, List<Integer>> frequencies) {
        double max_score = Double.MAX_VALUE; 
        String best_word = ""; 
        HashMap<String, Double> scores = wordScore(possibleWords, frequencies);

        for (String word : possibleWords) {
            if (scores.get(word) < max_score) {
                max_score = scores.get(word);
                best_word = word;
            }
        }
        return best_word;
    }
}
