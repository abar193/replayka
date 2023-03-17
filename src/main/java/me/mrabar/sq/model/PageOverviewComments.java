package me.mrabar.sq.model;

import java.sql.Timestamp;
import java.util.List;

public record PageOverviewComments(
    String blogName,
    String pageName,
    Timestamp weekNumber,
    long viewed,
    long responded,
    Double score,
    List<String> comments // potential weak point
) {
}
