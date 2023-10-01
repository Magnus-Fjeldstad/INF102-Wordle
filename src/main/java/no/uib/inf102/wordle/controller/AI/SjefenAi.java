package no.uib.inf102.wordle.controller.AI;

import no.uib.inf102.wordle.model.word.AnswerType;
import no.uib.inf102.wordle.model.word.WordleCharacter;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class SjefenAi implements IStrategy {
    private WordleWordList guesses;
  

    public SjefenAi() {
        reset();
    }

    /*
     * Given the feedback it will either use the frequency strategy from the latter task,
     * or it will find a word that has charcaters that are not in 
     */
    @Override
    public String makeGuess(WordleWord feedback) {
        if(feedback != null){
            guesses.eliminateWords(feedback);
            List<String> possibleAnswers = guesses.possibleAnswers();
            //If the feedback has more than 2 correct letters and there are more than 2 possible answers
            if (feedback.getCorrectLetters() > 2 && possibleAnswers.size() > 2) {
                //Find the first and second wrong letter
                int firstIndex = -1, secondIndex = -1, counter = 0;
                for (WordleCharacter wc : feedback) {
                    if (wc.answerType == AnswerType.WRONG) {
                        if (firstIndex == -1) {
                            firstIndex = counter;
                        } else {
                            secondIndex = counter;
                            break;
                        }
                    }
                    counter++;
                }

                //Finds all the unique charcters in possible answers
                Set<Character> uniqueChars = new HashSet<>();
                for (String word : possibleAnswers) {
                    uniqueChars.add(word.charAt(firstIndex)); //Collects all characters that are unique in the first wrong letters position
                    if (secondIndex != -1) {
                        uniqueChars.add(word.charAt(secondIndex)); //Collects all characters that are unique in the second wrong letters position
                    }
                    
                }

                int max = 0;
                String maxWord = "";
                //Finds the word with the most unique charcaters
                for (String word : guesses.getAllWords()){
                    Set<Character> wordChars = new HashSet<>();
                    for (char c : word.toCharArray()) {
                        if (uniqueChars.contains(c)) {
                            wordChars.add(c);
                        }
                    }
                    //Chooses the word with the most unique characters
                    if (wordChars.size() > max && wordChars.size() > 1) {
                        max = wordChars.size();
                        maxWord = word;
                    }
                }
                if (!maxWord.isEmpty()) {
                    return maxWord;
                }
            }
        }
        return guesses.getBestWord();
    }


    @Override
    public void reset() {
        guesses = new WordleWordList();
    }

}
