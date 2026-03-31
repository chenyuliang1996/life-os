package io.github.yuliangchen.lifeos.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
                .andExpect(content().string(containsString("Life OS")))
                .andExpect(content().string(org.hamcrest.Matchers.not(containsString("Demo"))));
    }

    @Test
    void shouldServeEnglishIndexFile() throws Exception {
        mockMvc.perform(get("/en/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data-default-locale=\"en-US\"")));
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
                                  "userId": "lifeos-user",
                                  "threadId": "thread-test",
                                  "input": "Plan a relaxed Tokyo trip without interrupting English study"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plan.title").value("Composite Life Plan"))
                .andExpect(jsonPath("$.plan.tasks.length()").value(3))
                .andExpect(jsonPath("$.executionRun.timeline.length()").value(5));
    }

    @Test
    void shouldReportRuntimeMode() throws Exception {
        mockMvc.perform(get("/api/v1/assistant/runtime"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode").value("orchestrator-fallback"))
                .andExpect(jsonPath("$.modelBacked").value(false));
    }

    @Test
    void shouldExposeArchitectureStatus() throws Exception {
        mockMvc.perform(get("/api/v1/system/architecture"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deploymentMode").value("single-node"))
                .andExpect(jsonPath("$.persistenceMode").value("database"))
                .andExpect(jsonPath("$.database").value("h2-file"))
                .andExpect(jsonPath("$.travelSearch").value("seeded-search-plus-optional-flyai"))
                .andExpect(jsonPath("$.travelSpecialist").value("local-travel-agent-plus-optional-a2a"));
    }

    @Test
    void shouldExposeOperationsSnapshot() throws Exception {
        mockMvc.perform(get("/api/v1/system/operations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.target.dailyActiveUsers").value(200000))
                .andExpect(jsonPath("$.target.peakQps").value(30.0))
                .andExpect(jsonPath("$.successRate").exists());
    }

    @Test
    void shouldAcceptUxTelemetry() throws Exception {
        mockMvc.perform(post("/api/v1/telemetry/ux")
                        .contentType("application/json")
                        .content("""
                                {
                                  "action": "page_bootstrap",
                                  "surface": "toc",
                                  "locale": "zh-CN",
                                  "durationMs": 420,
                                  "success": true
                                }
                                """))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldExposeRagRuntimeStatus() throws Exception {
        mockMvc.perform(get("/api/v1/system/rag"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.retrievalMode").value("text-only"));
    }

    @Test
    void shouldExposeConnectorsIncludingFlyAi() throws Exception {
        mockMvc.perform(get("/api/v1/system/connectors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].connectorName").value("search"))
                .andExpect(jsonPath("$[1].connectorName").value("flyai-search"))
                .andExpect(jsonPath("$[1].enabled").value(false));
    }

    @Test
    void shouldReturnAssistantReply() throws Exception {
        mockMvc.perform(post("/api/v1/assistant/message")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "lifeos-user",
                                  "threadId": "thread-assistant",
                                  "input": "Help me plan a Tokyo trip while keeping my English habit."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mode").value("orchestrator-fallback"))
                .andExpect(jsonPath("$.message", containsString("deterministic planner")))
                .andExpect(jsonPath("$.planId", notNullValue()));
    }

    @Test
    void shouldPersistConfirmationDecision() throws Exception {
        String response = mockMvc.perform(post("/api/v1/plans/preview")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "lifeos-user",
                                  "threadId": "thread-confirmation",
                                  "input": "Create a balanced travel schedule with reminder drafts."
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.fasterxml.jackson.databind.JsonNode json = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
        String confirmationId = json.get("confirmations").get(0).get("id").asText();

        mockMvc.perform(post("/api/v1/confirmations/{id}/decision", confirmationId)
                        .contentType("application/json")
                        .content("""
                                {
                                  "decision": "APPROVED",
                                  "comment": "approved in test"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.comment").value("approved in test"));
    }

    @Test
    void shouldUpdateProfilePreferences() throws Exception {
        mockMvc.perform(put("/api/v1/profile?userId=lifeos-user")
                        .contentType("application/json")
                        .content("""
                                {
                                  "preferences": {
                                    "travelStyle": "adventurous",
                                    "budgetLevel": "premium",
                                    "studyGoal": "japanese"
                                  },
                                  "goals": []
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preferences.travelStyle").value("adventurous"))
                .andExpect(jsonPath("$.preferences.budgetLevel").value("premium"));
    }

    @Test
    void shouldResumeExecutionAfterApprovals() throws Exception {
        String previewResponse = mockMvc.perform(post("/api/v1/plans/preview")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "lifeos-user",
                                  "threadId": "thread-resume",
                                  "input": "Create a balanced travel schedule with reminder drafts."
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.fasterxml.jackson.databind.JsonNode previewJson = new com.fasterxml.jackson.databind.ObjectMapper().readTree(previewResponse);
        String planId = previewJson.get("plan").get("id").asText();
        String confirmationId = previewJson.get("confirmations").get(0).get("id").asText();

        mockMvc.perform(post("/api/v1/confirmations/{id}/decision", confirmationId)
                        .contentType("application/json")
                        .content("""
                                {
                                  "decision": "APPROVED",
                                  "comment": "approved in test"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        mockMvc.perform(post("/api/v1/assistant/resume")
                        .contentType("application/json")
                        .content("""
                                {
                                  "planId": "%s",
                                  "locale": "en-US"
                                }
                                """.formatted(planId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resumed").value(true))
                .andExpect(jsonPath("$.executionRun.status").value("COMPLETED"))
                .andExpect(jsonPath("$.plan.tasks[2].status").value("DONE"));
    }
}
