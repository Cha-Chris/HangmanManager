// Tyler Kang
// CSE 143, Section AF
// TA:  Alina Liokumovich
// Assignment #4: EvilHangman

// This program manages a game of evil hangman. This version of hangman avoids choosing
// a word until it is forced to.  Instead of choosing a single word at the beginning, the game 
// chooses a pattern of letters based on already guessed letters. This pattern determines
// a set of words with the same pattern, each of which may be the correct answer. With each 
// new guess, a new pattern is created and the range of possibilities narrows until it 
// reaches a single word. 

import java.util.*;

public class HangmanManager {
   
   // Represents the current set of possible answers for the game
   private Set<String> possibleWords;
   
   // Represents the number of guesses left
   private int guesses; 
   
   // Represents the letters that have already been guessed
   private Set<Character> guessedLetters;
   
   // Represents the current pattern of possible answers
   private String pattern;
   
   // Pre: Length must not be less than 1, max must not be less than 0 (throws 
   // IllegalArgumentException if either case is violated)
   // Constructs a set of possible answers for the game, given a word length and a collection
   // to retrieve the words. The number of allotted guesses is set. The pattern of possible 
   // answers is initialized with dashes seperated by spaces. The number of dashes is equal 
   // to the length parameter.
   // Parameters:
   //    <Collection<String> dictionary> = Represents the object used to retrieve words
   //    <int length> = Represents the length of possible answers and their pattern
   //    <int max> = Represents the the number of allotted guesses
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }  
      this.possibleWords = new TreeSet<String>();
      this.guesses = max;
      this.guessedLetters = new TreeSet<Character>();
      for (String word : dictionary) {
         if (word.length() == length) {
            this.possibleWords.add(word);
         }
      }
      this.pattern = "-";
      for (int i = 1; i < length; i++) {
         this.pattern += " -";
      }
   }
   
   // Returns the current set of possible answers
   public Set<String> words() {
      return this.possibleWords;
   }  
   
   // Returns the number of guesses left
   public int guessesLeft() {
      return this.guesses;
   }
   
   // Returns the already guessed letters
   public Set<Character> guesses() {
      return this.guessedLetters;
   }
   
   // Pre: The current set of possible words must not be empty (throws 
   // IllegalStateException otherwise)
   // Returns the current pattern for the set of possible answers
   public String pattern() {
      if (this.possibleWords.isEmpty()) {
         throw new IllegalStateException();
      }
      return this.pattern;
   }
   
   // Pre: Number of guesses must be at least 1, Set of possible answers must not be
   // empty (throws IllegalStateException if either case is violated).
   // Pre: If the set of possible words is not empty, the guessed letter must not have been
   // guessed before (throws IllegalArgumentException otherwise).
   // Records a guess made by the user. Creates a new pattern using this guess. The new 
   // pattern may or may not be different from the old pattern. The new pattern corresponds 
   // to a new set of words. These words are the new set of possible answers. The number 
   // of guesses is decremented by one if the pattern changes, and stays the same if it doesn't.
   // Returns the number of occurrences of the guessed letter in the new pattern.
   // Parameters:
   //    <char guess> = Represents the guess used to narrow the possible answers and create a 
   //                   new pattern.
   public int record(char guess) {
      if (this.guesses < 1 || this.possibleWords.isEmpty()) {
         throw new IllegalStateException();
      } else if (this.guessedLetters.contains(guess)) {
         throw new IllegalArgumentException();
      }
      Map<String, Set<String>> patternsToWords = new TreeMap<String, Set<String>>();
      this.guessedLetters.add(guess);
      for (String word : this.possibleWords) {
         String nextPattern = createPatterns(word, guess);
         mapPatterns(patternsToWords, nextPattern, word);
      }
      chooseCurrentPattern(patternsToWords);
      int charCount = countCharInPattern(guess);
      this.possibleWords = patternsToWords.get(this.pattern);
      if (charCount == 0) {
         this.guesses--;
      }
      return charCount;  
   }
   
   // Takes in a pattern and pairs it with a set of words with the same pattern.
   // Parameters:
   //    <Map<String, Set<String>> patternsToWords> = Used to pair the pattern and the word.
   //    <String nextPattern> = Represents the pattern to be paired
   //    <String word> = Represents the word to be paired   
   private void mapPatterns(Map<String, Set<String>> patternsToWords, String nextPattern,
                            String word) {
      if (!patternsToWords.containsKey(nextPattern)) {
         patternsToWords.put(nextPattern, new TreeSet<String>());
      }
      patternsToWords.get(nextPattern).add(word);
   }
   
   // Returns a String pattern based on the current pattern and a letter guess. The old pattern 
   // is adjusted to add the occurrences of the guessed letter in the passed word. 
   // If the guessed letter doesn't occur in the passed word, the current pattern is returned.
   // Parameters: 
   //    <String word> = The word used to create the pattern of letters
   //    <char guess> = The letter whose occurrences are tracked in the passed word.
   //                   Occurrences of this letter are added to the old pattern.
   private String createPatterns(String word, char guess) {
      String nextPattern = "";
      for (int i = 0; i < word.length(); i++) {
         if (word.charAt(i) == guess) { 
            nextPattern += " " + guess;
         } else {
            nextPattern += " " + this.pattern.charAt(i * 2);
         }
      }
      return nextPattern.trim();
   }
   
   // Counts the occurrences of the passed letter in the current pattern
   // Parameters:
   //    <char guess> = The letter counted in the current pattern
   private int countCharInPattern(char guess) {
      int charCount = 0;
      for (int i = 0; i < this.pattern.length(); i++) {
         if (this.pattern.charAt(i) == guess) {
            charCount++;
         }
      }
      return charCount;
   }     
   
   // Decides which of the created patterns of letters to make the new current pattern 
   // Parameters:
   //    <Map<String, Set<String>> patternsToWords> = Represents the pair of patterns and 
   //                                                 words used to pick a new pattern
   private void chooseCurrentPattern(Map<String, Set<String>> patternsToWords) {
      int max = 0;
      for (String pattern : patternsToWords.keySet()) {
         int wordCount = patternsToWords.get(pattern).size();
         if (wordCount > max) {
            max = wordCount;
            this.pattern = pattern;
         }
      }
   }
   
}
