# Runtime Analysis

For each method of the tasks give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented new methods not listed you must add these as well, e.g. any helper methods. You need to show how you analyzed any methods used by the methods listed below.**

The runtime should be expressed using these three parameters:

- `n` - number of words in the list allWords
- `m` - number of words in the list possibleWords
- `k` - number of letters in the wordleWords

## Task 1 - matchWord

- `WordleAnswer::matchWord`: O(k)/O(1)

  1. **Checking Length Equality**: This is an \(O(1)\) operation.

  2. **Initialization of the HashMap**: This is done in constant time, \(O(1)\).

  3. **Looping over `wordleWords`**:
     Since this loop runs `k` times and consists of \(O(1)\) operations inside, its complexity is \(O(k)\).

  4. **Second loop over `wordleWords`**:
     Similarly, this loop also runs `k` times with \(O(1)\) operations, so its complexity is also \(O(k)\).

  5. **Third loop over `wordleWords`**:
     Yet again, the loop runs for `k` iterations with constant-time operations, making it \(O(k)\).

  Combining the loops' complexities, the overall time complexity is \(O(3k)\). However, in big O notation, we drop constant multipliers, leaving us with:

  **Overall Time Complexity**: \(O(k)\)

## Task 2 - EliminateStrategy

- `WordleWordList::eliminateWords`: O(m*k)
  # Time Complexity Analysis

Considering the provided methods, let's express the runtime using the following parameters:

- `m`: number of words in the list `possibleAnswers`
- `k`: number of letters in the `wordleWords`

### `eliminateWords` Method

1. **Iterating over `possibleAnswers`**: The loop runs `m` times.
2. **Calling `isPossibleWord` for each word in `possibleAnswers`**: Given the `matchWord` and `getWordString` methods, the `isPossibleWord` method has a complexity of \(O(k)\).

Combining these two steps, the overall time complexity of the `eliminateWords` method is \(O(m \* k)\).

## Task 3 - FrequencyStrategy

- `FrequencyStrategy::makeGuess`: \(O(m \* k)\)
  - This method's time complexity is primarily influenced by two key operations. First, it conditionally invokes the `eliminateWords` method, which has a complexity of \(O(m \* k)\) due to filtering operations on the list of possible answers. Second, the method calls `getBestWord` which, in evaluating the best word based on frequency criteria, also operates with a complexity of \(O(m \* k)\). Since both of these are linear with respect to the number of possible answers (`m`) and the length of words (`k`), the combined runtime is \(O(m \* k)\).


# Task 4 - Make your own (better) AI

For this task you do not need to give a runtime analysis.
Instead, you must explain your code. What was your idea for getting a better result? What is your strategy?

_My idea for implementing my own Wordle AI is to employ a frequency strategy on the first guess. This is mathematically the best word choice based on the list of possible answers. Then, on the second guess, I remove all impossible words from the list, eliminating any words that do not contain the possible answers. In contrast to the frequency strategy, I also eliminate words marked as CORRECT in an attempt to maximize the feedback on the second guess. The feedback from the second guess is then used to remove the words I obtained from that guess. Afterward, if I have found 2 or more green or yellow letters, I revert to the original frequency strategy. The idea here is that I have received enough feedback from the first two guesses to make more accurate guesses on the third guess than the fourth, thus reducing the number of times the AI needs to use 4 or more guesses._
