package io.github.yuliangchen.lifeos.infra.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_profiles")
public class UserProfileEntity {

    @Id
    @Column(name = "user_id", nullable = false, length = 128)
    private String userId;

    @Column(name = "preferences_json", nullable = false, columnDefinition = "TEXT")
    private String preferencesJson;

    @Column(name = "goals_json", nullable = false, columnDefinition = "TEXT")
    private String goalsJson;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPreferencesJson() {
        return preferencesJson;
    }

    public void setPreferencesJson(String preferencesJson) {
        this.preferencesJson = preferencesJson;
    }

    public String getGoalsJson() {
        return goalsJson;
    }

    public void setGoalsJson(String goalsJson) {
        this.goalsJson = goalsJson;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
