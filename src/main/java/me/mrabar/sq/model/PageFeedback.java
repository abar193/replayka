package me.mrabar.sq.model;

import java.time.LocalDateTime;

public record PageFeedback(String pageName, int score, String comment, LocalDateTime timestamp) {
}
