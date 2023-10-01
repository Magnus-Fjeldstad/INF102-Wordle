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

  - See the methods used for more in depht comments on the runtime

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

- See the methods used for more in depht comments on the runtime

## Task 3 - FrequencyStrategy

- `FrequencyStrategy::makeGuess`: \(O(m \* k)\)
  - This method's time complexity is primarily influenced by two key operations. First, it conditionally invokes the `eliminateWords` method, which has a complexity of \(O(m \* k)\) due to filtering operations on the list of possible answers. Second, the method calls `getBestWord` which, in evaluating the best word based on frequency criteria, also operates with a complexity of \(O(m \* k)\). Since both of these are linear with respect to the number of possible answers (`m`) and the length of words (`k`), the combined runtime is \(O(m \* k)\).

  - See the methods used for more in depht comments on the runtime


# Task 4 - Make your own (better) AI

For this task you do not need to give a runtime analysis.
Instead, you must explain your code. What was your idea for getting a better result? What is your strategy?

_For task 4 ive tried many different ideas, removing the CORRECT letters from allWords and finding the best word whitout CORRECT letters, Entropy Strat based on a youtube video on worlde by 3Blue1Brown and some more. In the end i figured i try to analyse where frequency strategy struggles, my findings where that frequency struggels when there are 3 or more correct letters. This is becuase the way i implemented frequency startegy the "frequency map" shrinks in size by each guess. So lets say i have 5 words left

Bandy
Bobby
Limit
Snake
Adieu

By the way i implemented frequency it will always choose the word with the higest "wordpoint" and wordpoint is determined by how many times a letter occurs in the given index of a word. So in this case it would probably choose Bobby. I´ve seen many times that it will have 3 or even 4 correct letters and would still use 2 to 3 guesses to get the right word.

Then i figured my new Startegy. When there are 3 or more correct letters in the feedBack a new strategy is used to find the "bestWord". I made a Class that uses a method that gets the remaining possible chars at the indecies that does not contain an correct letter. Then it finds a word with the most unique charachters from the original AllWords. Then with the help of that feedback it will more efficently find the Correct answer with fewer guesses than frequency. 

In my test´s running 20 000 games the FinalAI manages to get 20000/20000 correct while frequecny only get 19875/20000. The FinalAI even manages to guess it in slighty less avg guesses.

I´ve also left in one of my other "AI's" this is the "BestAI". It uses an entropy based scoring system to get the best word. You can take a look if you want but it´s the "FinalAI" that is my entry for task 4.
_
