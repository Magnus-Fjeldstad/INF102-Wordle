package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.AnswerType;
import no.uib.inf102.wordle.model.word.WordleCharacter;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SupremeAi implements IStrategy {
    private WordleWordList guesses;
    private WordleWordList guessesCopy;
    private HashMap<Character, Integer> confirmedGreen;
    private HashMap<Character, Integer> confirmedYellow;
    private Random random;

    //Best seed so far: 2 1993 wins 11 is good also 3,624 guesses
    public SupremeAi() {
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        // System.out.println("\nGuesseCopy: "+guessesCopy.size());
        // System.out.println("Guess: "+guesses.size());
        

        if (feedback != null) {
            guesses.eliminateWords(feedback);
            confimredPostions(feedback);
        }

        if (guesses.size() <= 3) {
            String bestWord = "";
            // System.out.println("\nThis is the getBestWord: " +guesses.getBestWord());
            
            // System.out.println("This is the calculated bestWord: "+bestWord);
            List<String> remainingGuesses = guesses.possibleAnswers();
            bestWord = remainingGuesses.get(random.nextInt(remainingGuesses.size()));
            guesses.remove(bestWord);
            return bestWord;
        } else {
            return guesses.getBestWord();
        }
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

    @Override
    public void reset() {
        guesses = new WordleWordList();
        guessesCopy = new WordleWordList(guesses.possibleAnswers());
        confirmedGreen = new HashMap<>();
        confirmedYellow = new HashMap<>();
        random = new Random();
    }

    private String calculateBestWordByProbability() {
        // Maps the common letters for all of the possible guessing words.
        List<HashMap<Character, Integer>> originalCommonLetters = guessesCopy.mapCommonLetters(guessesCopy.possibleAnswers());
    
        List<String> possibleAnswers = guesses.possibleAnswers();
    
        HashMap<String, Double> wordProbabilities = new HashMap<>();
    
        int totalLetterCount = 0;
        
        for (String word : possibleAnswers) {
            totalLetterCount += word.length();
        }

    
        double totalProbability = 0.0; // Total probability for normalization
    
        for (String word : possibleAnswers) {
            double wordProbability = calculateWordProbability(word, totalLetterCount);
    
            // Adjust the word probability based on common letters
            for (int i = 0; i < originalCommonLetters.size(); i++) {
                int commonCount = originalCommonLetters.get(i).getOrDefault(word.charAt(i), 0);
                wordProbability *= Math.pow(commonCount / (double) totalLetterCount, 2);
            }
    
            wordProbabilities.put(word, wordProbability);
            totalProbability += wordProbability; // Accumulate probabilities for normalization
        }
    
        // Normalize probabilities
        for (String word : possibleAnswers) {
            wordProbabilities.put(word, wordProbabilities.get(word) / totalProbability);
        }
    
        // Find the word with the highest probability
        String bestWord = null;
        double maxProbability = Double.NEGATIVE_INFINITY;
        for (Map.Entry<String, Double> entry : wordProbabilities.entrySet()) {
            if (entry.getValue() > maxProbability) {
                bestWord = entry.getKey();
                maxProbability = entry.getValue();
            }
        }
    
        if (bestWord != null) {
            return bestWord;
        } else {
            // In case of error, return the best word based on the original algorithm
            return guesses.getBestWord();
        }
    }
    
    
    
    

    private double calculateWordProbability(String word, int totalLetterCount) {
        return (double) word.length() / totalLetterCount;
    }

    private String getBestWordSub4() {
		List<HashMap<Character, Integer>> charCountMapsList = guessesCopy.mapCommonLetters(guessesCopy.possibleAnswers());
        System.out.println(charCountMapsList);
		String bestWord = "";
		int highScore = -1;

		for (String word : guesses.possibleAnswers()) {
			int wordPoints = 0;

			for (int currentMap = 0; currentMap < charCountMapsList.size(); currentMap++) {
				char c = word.charAt(currentMap);
				wordPoints += charCountMapsList.get(currentMap).getOrDefault(c, 0);
			}
			
			if (wordPoints > highScore) {
				highScore = wordPoints;
				bestWord = word;
			}
		
		}
		//System.out.println(wordMap);
		return bestWord;
	}

}
