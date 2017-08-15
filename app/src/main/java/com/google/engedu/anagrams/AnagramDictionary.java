package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> dictionary;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWord;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        dictionary = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWord = new HashMap<>();
        while((line = in.readLine()) != null) {
            String word = line.trim();
            dictionary.add(word);
            wordSet.add(word);
            String temp = sortLetters(word);
            if (lettersToWord.containsKey(temp)) {
                lettersToWord.get(temp).add(word);
            } else {
                ArrayList<String> value = new ArrayList<>();
                value.add(word);
                lettersToWord.put(temp, value);
            }

            int size = word.length();
            if (sizeToWord.containsKey(size)) {
                sizeToWord.get(size).add(word);
            } else {
                ArrayList<String> members = new ArrayList<>();
                members.add(word);
                sizeToWord.put(size, members);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word)) {
            return false;
        }

        int lw = word.length();
        int lb = base.length();
        for (int i = 0; i < lw - lb; i++) {
            if (word.substring(i, i + lb).equals(base)) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        String temp = sortLetters(targetWord);
        if (lettersToWord.containsKey(temp)) {
            return lettersToWord.get(temp);
        } else {
            return new ArrayList<String>();
        }
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char ch = 'a'; ch <= 'z'; ch++) {
            String newWord = word + String.valueOf(ch);
            if (sizeToWord.containsKey(newWord.length())) {
                ArrayList<String> target = sizeToWord.get(newWord.length());
                for (String s : target) {
                    if (isGoodWord(s, word) && lettersToWord.containsKey(sortLetters(s))) {
                        result.addAll(lettersToWord.get(sortLetters(s)));
                    }
                }
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        while (true) {
            int index = random.nextInt(dictionary.size());
            String next = dictionary.get(index);
            ArrayList<String> ans = getAnagramsWithOneMoreLetter(next);
            if (ans.size() < MIN_NUM_ANAGRAMS) {
                continue;
            } else {
                return next;
            }
        }
    }

    public String sortLetters(String source) {
        char[] letters = source.toCharArray();
        Arrays.sort(letters);
        return Arrays.toString(letters);
    }
}
