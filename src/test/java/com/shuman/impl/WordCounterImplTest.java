package com.shuman.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WordCounterImplTest {
    @InjectMocks
    private WordCounterImpl wordCounter;

    @Test
    public void countWords() throws Exception {
        String text = "I am eg,\n" +
                "\n" +
                "List<Integer> numbers = Arrays.asList(new Integer[]{1,2,1,3,4,4});";

        Map<String, Integer> result = wordCounter.countWords(text);

        assertEquals(9, result.size());
    }

}