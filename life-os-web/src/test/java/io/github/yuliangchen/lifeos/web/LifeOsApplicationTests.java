package io.github.yuliangchen.lifeos.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
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
                .andExpect(content().string(containsString("OtterLife")))
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
    void shouldExposePersonaPresets() throws Exception {
        mockMvc.perform(get("/api/v1/personas?locale=en-US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(18)))
                .andExpect(jsonPath("$[?(@.id=='mbti-intj')].mbtiType").value(hasItem("INTJ")))
                .andExpect(jsonPath("$[?(@.id=='mbti-intj')].temperament").value(hasItem("Analyst")))
                .andExpect(jsonPath("$[?(@.id=='guest-explorer')].userId").value(hasItem("guest-weekend")))
                .andExpect(jsonPath("$[?(@.id=='ops-reviewer')].userId").value(hasItem("persona-ops")));
    }

    @Test
    void shouldExposeSecurityOverview() throws Exception {
        mockMvc.perform(get("/api/v1/security/overview?userId=persona-ops"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trust.trustTier").value("operator"))
                .andExpect(jsonPath("$.policies[0].capability").value("knowledge.read"))
                .andExpect(jsonPath("$.recentAuditEntries").isArray());
    }

    @Test
    void shouldExposeRestrictedSecurityOverviewForGuestPersona() throws Exception {
        mockMvc.perform(get("/api/v1/security/overview?userId=guest-weekend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trust.trustTier").value("restricted"))
                .andExpect(jsonPath("$.trust.outboundNetworkAllowed").value(false));
    }

    @Test
    void shouldResolveMbtiPersonaInSecurityOverview() throws Exception {
        mockMvc.perform(get("/api/v1/security/overview?userId=persona-mbti-intj"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trust.personaId").value("mbti-intj"))
                .andExpect(jsonPath("$.trust.trustTier").value("trusted"))
                .andExpect(jsonPath("$.trust.mcpTrusted").value(true));
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
    void shouldExposeFestivalPoiFeed() throws Exception {
        mockMvc.perform(get("/api/v1/poi/festivals?locale=zh-CN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[0].pois").isArray());
    }

    @Test
    void shouldKeepModuleProbesReadOnly() throws Exception {
        int confirmationsBefore = new com.fasterxml.jackson.databind.ObjectMapper().readTree(
                mockMvc.perform(get("/api/v1/confirmations?userId=lifeos-user"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).size();

        mockMvc.perform(get("/api/v1/modules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memory").exists())
                .andExpect(jsonPath("$.orchestrator").exists());

        int confirmationsAfter = new com.fasterxml.jackson.databind.ObjectMapper().readTree(
                mockMvc.perform(get("/api/v1/confirmations?userId=lifeos-user"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString()
        ).size();

        org.junit.jupiter.api.Assertions.assertEquals(confirmationsBefore, confirmationsAfter);
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
    void shouldScopePlansByUserIdentity() throws Exception {
        mockMvc.perform(post("/api/v1/plans/preview")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "persona-travel",
                                  "threadId": "thread-travel",
                                  "input": "Plan a calm Tokyo route."
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/plans/preview")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "persona-study",
                                  "threadId": "thread-study",
                                  "input": "Protect my English habit next week."
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/plans?userId=persona-travel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("persona-travel"));

        mockMvc.perform(get("/api/v1/plans?userId=persona-study"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("persona-study"));
    }

    @Test
    void shouldRejectCrossUserPlanAccess() throws Exception {
        String previewResponse = mockMvc.perform(post("/api/v1/plans/preview")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "persona-travel",
                                  "threadId": "thread-travel",
                                  "input": "Plan a calm Tokyo route."
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.fasterxml.jackson.databind.JsonNode previewJson = new com.fasterxml.jackson.databind.ObjectMapper().readTree(previewResponse);
        String planId = previewJson.get("plan").get("id").asText();

        mockMvc.perform(get("/api/v1/plans/{planId}?userId=persona-study", planId))
                .andExpect(status().isNotFound());
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
                                  "userId": "lifeos-user",
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
