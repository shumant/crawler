package com.shuman.impl;

import com.shuman.interfaces.WordCounterService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class WordCounterImpl implements WordCounterService {
    private final Pattern wordTokenizer = Pattern.compile("[^a-zA-Zа-яА-Я]+");

    @Override
    public Map<String, Integer> countWords(String document) {
        Map<String, Integer> result = new HashMap<>();
        Arrays.stream(wordTokenizer.split(document)).forEach(word -> {
            word = word.toLowerCase();
            Integer oldValue = result.getOrDefault(word, 0);
            result.put(word, oldValue + 1);
        });

        return result;
    }
}
