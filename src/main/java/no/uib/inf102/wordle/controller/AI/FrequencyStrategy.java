package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FrequencyStrategy implements IStrategy {
    private WordleWordList guesses;
    private WordleWordList answers;

    private HashMap<Character, Integer> firstLetter;
    private HashMap<Character, Integer> secondLetter;
    private HashMap<Character, Integer> thirdLetter;
    private HashMap<Character, Integer> fourthLetter;
    private HashMap<Character, Integer> fifthLetter;

    public FrequencyStrategy() {
        reset();
    }

    /*
     * Makes 5 maps that track the most frequent chars at each of the indexes
     * for possible answer
     */
    private void makeMaps() {
        for (int i = 0; i < answers.size(); i++) {
            String currentWord = answers.possibleAnswers().get(i);
            
            firstLetter.put(currentWord.charAt(0), firstLetter.getOrDefault(currentWord.charAt(0), 0) + 1);
            secondLetter.put(currentWord.charAt(1), secondLetter.getOrDefault(currentWord.charAt(1), 0) + 1);
            thirdLetter.put(currentWord.charAt(2), thirdLetter.getOrDefault(currentWord.charAt(2), 0) + 1);
            fourthLetter.put(currentWord.charAt(3), fourthLetter.getOrDefault(currentWord.charAt(3), 0) + 1);
            fifthLetter.put(currentWord.charAt(4), fifthLetter.getOrDefault(currentWord.charAt(4), 0) + 1);
        }
        System.out.println(firstLetter);
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        return "salve";
    }

    private char getMostFrequentChar(HashMap<Character, Integer> map) {
        char mostFrequentChar = ' ';
        int maxFrequency = 0;
    
        for (char key : map.keySet()) {
            int frequency = map.get(key);
            if (frequency > maxFrequency) {
                maxFrequency = frequency;
                mostFrequentChar = key;
            }
        }
    
        return mostFrequentChar;
    }

    private void filterPossibleWords(WordleWord feedback){

    }
    

    @Override
    public void reset() {
        guesses = new WordleWordList();
        answers= new WordleWordList();
        answers.possibleAnswers();
        firstLetter = new HashMap<>();
        secondLetter = new HashMap<>();
        thirdLetter = new HashMap<>();
        fourthLetter = new HashMap<>();
        fifthLetter = new HashMap<>();
        makeMaps();
    }
}
