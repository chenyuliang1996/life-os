package io.github.yuliangchen.lifeos.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LifeOsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldServeHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("index.html"));
    }

    @Test
    void shouldServeStaticIndexFile() throws Exception {
        mockMvc.perform(get("/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Life OS")));
    }

    @Test
    void shouldExposeExecutableModules() throws Exception {
        mockMvc.perform(get("/api/v1/modules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.domain").exists())
                .andExpect(jsonPath("$.agents").exists())
                .andExpect(jsonPath("$.orchestrator").exists());
    }

    @Test
    void shouldPreviewPlan() throws Exception {
        mockMvc.perform(post("/api/v1/plans/preview")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "demo-user",
                                  "threadId": "thread-test",
                                  "input": "Plan a relaxed Tokyo trip without interrupting English study"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plan.title").value("Composite Life Plan"))
                .andExpect(jsonPath("$.plan.tasks.length()").value(3))
                .andExpect(jsonPath("$.executionRun.timeline.length()").value(4));
    }
}
