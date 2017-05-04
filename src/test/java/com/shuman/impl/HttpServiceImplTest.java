package com.shuman.impl;

import com.shuman.utils.Params;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpServiceImplTest {
    @InjectMocks
    private HttpServiceImpl httpService;

    @Mock
    private Params params;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(httpService, "httpClient", HttpClients.createDefault());
        when(params.getRequestsPerSecond()).thenReturn(10);
        httpService.init();
    }

    @Test
    public void makeRequest() throws Exception {
        Document doc = httpService.makeRequest("http://yandex.ru");
    }
}