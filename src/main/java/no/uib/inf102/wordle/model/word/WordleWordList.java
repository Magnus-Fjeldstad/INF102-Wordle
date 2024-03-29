package no.uib.inf102.wordle.model.word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;



import no.uib.inf102.wordle.resources.GetWords;

/**
 * This class describes a structure of two lists for a game of Wordle: The list
 * of words that can be used as guesses and the list of words that can be
 * possible answers.
 */
public class WordleWordList {

	/**
	 * All words in the game. These words can be used as guesses.
	 */
	private List<String> allWords;

	/**
	 * A subset of <code>allWords</code>. <br>
	 * </br>
	 * These words can be the answer to a wordle game.
	 */
	private List<String> possibleAnswers;

	/**
	 * Create a WordleWordList that uses the full words and limited answers of the
	 * GetWords class.
	 */
	public WordleWordList() {
		this(GetWords.ALL_WORDS_LIST, GetWords.ANSWER_WORDS_LIST);
	}

	/**
	 * Create a WordleWordList with the given <code>words</code> as both guesses and
	 * answers.
	 * 
	 * @param words
	 */
	public WordleWordList(List<String> words) {
		this(words, words);
	}

	/**
	 * Create a WordleWordList with the given lists as guessing words and answers.
	 * <code>answers</code> must be a subset of <code>words</code>.
	 * 
	 * @param words   The list of words to be used as guesses
	 * @param answers The list of words to be used as answers
	 * @throws IllegalArgumentException if the given <code>answers</code> were not a
	 *                                  subset of <code>words</code>.
	 */
	public WordleWordList(List<String> words, List<String> answers) {
		HashSet<String> wordsSet = new HashSet<String>(words);
		if (!wordsSet.containsAll(answers))
			throw new IllegalArgumentException("The given answers were not a subset of the valid words.");

		this.allWords = new ArrayList<>(words);
		this.possibleAnswers = new ArrayList<>(answers);
	}

	/**
	 * Get the list of all guessing words.
	 * 
	 * @return all words
	 */
	public List<String> getAllWords() {
		return allWords;
	}

	/**
	 * Returns the list of possible answers.
	 * 
	 * @return
	 */
	public List<String> possibleAnswers() {
		return possibleAnswers;
	}

	/**
	 * Eliminates words from the possible answers list using the given feedback.
	 * 
	 * @param feedback The feedback to use for eliminating words.
	 */
	public void eliminateWords(WordleWord feedback) { // O(m*k)
		if (feedback == null) {
			return;
		}

		List<String> filteredPossibleAnswers = new ArrayList<>(); // O(1)

		for (String currentGuess : possibleAnswers) {// O(m)
			if (WordleWord.isPossibleWord(currentGuess, feedback)) { //O(k)
				filteredPossibleAnswers.add(currentGuess); //O(1)
			}
		}
		possibleAnswers = filteredPossibleAnswers; //O(1)
	}

	/**
	 * Returns the amount of possible answers in this WordleWordList
	 * 
	 * @return size of
	 */
	public int size() {
		return possibleAnswers.size();
	}

	/**
	 * Removes the given <code>answer</code> from the list of possible answers.
	 * 
	 * @param answer
	 */
	public void remove(String answer) {
		possibleAnswers.remove(answer);

	}

	/**
	 * Returns the word length in the list of valid guesses.
	 * 
	 * @return
	 */
	public int wordLength() {
		return allWords.get(0).length();
	}

	/**
	 * 
	 * @param possibleAnswers takes in a WordleWordList of possible answers
	 * @return a list of maps where each map tracks the most frequent chars at each
	 *         index
	 */
	private List<HashMap<Character, Integer>> mapCommonLetters(List<String> possibleAnswers) { // O(m*k)
		List<HashMap<Character, Integer>> charCountMapsList = new ArrayList<>(); // O(1)

		for (int i = 0; i < wordLength(); i++) { // O(k)
			charCountMapsList.add(new HashMap<>()); // O(1)
		}

		for (String word : possibleAnswers) { // O(m)
			for (int i = 0; i < wordLength(); i++) { // O(k)
				char c = word.charAt(i); // O(1)
				charCountMapsList.get(i).put(c, charCountMapsList.get(i).getOrDefault(c, 0) + 1); // O(1)
			}
		}
		return charCountMapsList;
	}

	/**
	 * 
	 * @return the best possible word given the possible answers
	 */
	public String getBestWord() { // O(m*k)
		List<HashMap<Character, Integer>> charCountMapsList = mapCommonLetters(possibleAnswers()); // O(m*k)
		String bestWord = "";
		int highScore = -1;

		for (String word : possibleAnswers()) { // O(m)
			int wordPoints = 0;

			for (int currentMap = 0; currentMap < charCountMapsList.size(); currentMap++) { // O(k) since list size is k
				char c = word.charAt(currentMap); // O(1)
				wordPoints += charCountMapsList.get(currentMap).getOrDefault(c, 0); // O(1)
			}
			
			if (wordPoints > highScore) { // O(1)
				highScore = wordPoints; // O(1)
				bestWord = word; // O(1)
			}
		
		}
		return bestWord;
	}
}
