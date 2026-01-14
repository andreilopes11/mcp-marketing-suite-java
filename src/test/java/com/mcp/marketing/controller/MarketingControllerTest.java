package com.mcp.marketing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcp.marketing.model.MarketingRequest;
import com.mcp.marketing.service.MarketingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MarketingController.class)
class MarketingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MarketingService marketingService;

    @Test
    void testGenerateAds() throws Exception {
        MarketingRequest request = MarketingRequest.builder()
                .product("SaaS Analytics Platform")
                .audience("B2B Data Scientists")
                .brandVoice("Professional, Data-Driven")
                .goals(Arrays.asList("Increase Trial Signups", "Brand Awareness"))
                .build();

        mockMvc.perform(post("/api/marketing/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}

