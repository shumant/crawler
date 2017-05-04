package com.shuman.interfaces;

import java.util.Map;

public interface WordCounterService {
    Map<String, Integer> countWords(String document);
}
