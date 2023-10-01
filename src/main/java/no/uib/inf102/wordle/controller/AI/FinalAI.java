package no.uib.inf102.wordle.controller.AI;

import java.util.HashSet;
import java.util.List;


import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class FinalAI implements IStrategy {

    WordleWordList guesses;

    public FinalAI() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

        if (feedback != null) {
            List<String> possibleAnswers = guesses.possibleAnswers();
            List<String> allGuessingWords = guesses.getAllWords();

            if (feedback.getCorrectLetters() >= 3 && possibleAnswers.size() >= 3) {

                HashSet<Character> remainingChars = getRemainingChars(feedback, possibleAnswers);

                int uniqueCharsCount = 0;
                String mostUniqueCharsWord = "";

                for (String word : allGuessingWords) {
                    HashSet<Character> wordChars = new HashSet<>();
                    for (char c : word.toCharArray()) {
                        if (remainingChars.contains(c)) {
                            wordChars.add(c);
                        }
                    }
                    if (wordChars.size() > uniqueCharsCount) {
                        uniqueCharsCount = wordChars.size();
                        mostUniqueCharsWord = word;
                    }
                }

                if (!mostUniqueCharsWord.isEmpty()) {
                    return mostUniqueCharsWord;
                }
            }
        }
        return guesses.getBestWord();
    }


    @Override
    public void reset() {
        guesses = new WordleWordList();
    }

    private  HashSet<Character> getRemainingChars(WordleWord feedback, List<String> possibleAnswers){
        List<Integer> wrongIndices = feedback.getWrongLetterIndices();

        int firstIndex = Integer.MIN_VALUE;
        int secondIndex = Integer.MIN_VALUE;

        if (wrongIndices.size() > 0) {
            firstIndex = wrongIndices.get(0);
        }
        if (wrongIndices.size() > 1) {
            secondIndex = wrongIndices.get(1);
        }
        HashSet<Character> remainingChars = new HashSet<>();
        for (String word : possibleAnswers) {
            remainingChars.add(word.charAt(firstIndex));
            if (secondIndex != Integer.MIN_VALUE) {
                remainingChars.add(word.charAt(secondIndex));
            }
        }
        return remainingChars;
    }
}
