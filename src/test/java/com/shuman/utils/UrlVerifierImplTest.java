package com.shuman.utils;

import org.apache.commons.validator.routines.UrlValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UrlVerifierImplTest {
    @InjectMocks
    private UrlVerifierImpl urlVerifier;

    @Mock
    private UrlValidator urlValidator;

    @Before
    public void init () {
        when(urlValidator.isValid(anyString())).thenReturn(true);

        urlVerifier.setRootHostName("http://yandex.ru");
    }

    @Test
    public void addRoot() throws Exception {
        urlVerifier.setRootHostName("http://yandex.ru");

        String rootUrl = (String) ReflectionTestUtils.getField(urlVerifier, "rootHostName");
        assertEquals("yandex.ru", rootUrl);

    }

}