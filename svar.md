# Runtime Analysis
For each method of the tasks give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented new methods not listed you must add these as well, e.g. any helper methods. You need to show how you analyzed any methods used by the methods listed below.**

The runtime should be expressed using these three parameters:
   * `n` - number of words in the list allWords
   * `m` - number of words in the list possibleWords
   * `k` - number of letters in the wordleWords


## Task 1 - matchWord
* `WordleAnswer::matchWord`: O(?)
    * The matchWord method is O(k) since the method have 3 single nested foreloops witch each iterates over the wordlength withch in this case is 5. The method uses a HashMap to store Characters -> Integers. To get and put from an HashMap is both O(1). 

## Task 2 - EliminateStrategy
* `WordleWordList::eliminateWords`: O(?)
    * *Insert description of why the method has the given runtime*

## Task 3 - FrequencyStrategy
* `FrequencyStrategy::makeGuess`: O(m*k)
    * *Insert description of why the method has the given runtime*



# Task 4 - Make your own (better) AI
For this task you do not need to give a runtime analysis. 
Instead, you must explain your code. What was your idea for getting a better result? What is your strategy?

*My idea for implementing my own Wordle AI is to employ a frequency strategy on the first guess. This is mathematically the best word choice based on the list of possible answers. Then, on the second guess, I remove all impossible words from the list, eliminating any words that do not contain the possible answers. In contrast to the frequency strategy, I also eliminate words marked as CORRECT in an attempt to maximize the feedback on the second guess. The feedback from the second guess is then used to remove the words I obtained from that guess. Afterward, if I have found 2 or more green or yellow letters, I revert to the original frequency strategy. The idea here is that I have received enough feedback from the first two guesses to make more accurate guesses on the third guess than the fourth, thus reducing the number of times the AI needs to use 4 or more guesses.*
