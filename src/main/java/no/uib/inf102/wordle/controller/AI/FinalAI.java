package no.uib.inf102.wordle.controller.AI;

import java.util.HashSet;
import java.util.List;

import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class FinalAI implements IStrategy {

    private WordleWordList guesses;

    public FinalAI() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {

        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

        if(shouldUseUniqueCharsStrat(feedback)){
            return getWordWithMostUniqueChars(feedback);
        }

        return guesses.getBestWord();
    }

    @Override
    public void reset() {
        guesses = new WordleWordList();
    }

    /**
     * Method to get the remaining characters from the possible answers.
     *
     * @param feedback The feedback received after a guess.
     * @return a HashSet of the remaining characters at the index.
     */
    private HashSet<Character> getRemainingChars(WordleWord feedback, List<String> possibleAnswers) {
        List<Integer> wrongIndices = feedback.getWrongLetterIndices();

        HashSet<Character> remainingChars = new HashSet<>();
        for (String word : possibleAnswers) {
            for (int index : wrongIndices) {
                if (index < word.length()) {
                    remainingChars.add(word.charAt(index));
                }
            }
        }
        return remainingChars;
    }

    /**
     * Returns the word from the possible guesses with the most unique characters.
     *
     * @param feedback The feedback received after a guess.
     * @return The word with the most unique characters or an empty string if the
     *         conditions are not met.
     */
    private String getWordWithMostUniqueChars(WordleWord feedback) {

        List<String> possibleAnswers = guesses.possibleAnswers();
        List<String> allGuessingWords = guesses.getAllWords();

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

        return mostUniqueCharsWord;
    }

    /**
     * 
     * @param feedback The feedback received after a guess.
     * @return true if the unique chars strategy should be used, false otherwise.
     */
    private boolean shouldUseUniqueCharsStrat(WordleWord feedback){
        return feedback != null && feedback.getCorrectLetters() >= 3 && guesses.possibleAnswers().size() >= 4;
    }
}
