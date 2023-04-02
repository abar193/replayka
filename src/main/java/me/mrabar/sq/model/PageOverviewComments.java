package me.mrabar.sq.model;

import java.sql.Timestamp;
import java.util.List;

public record PageOverviewComments(
    String blogName,
    String pageName,
    Timestamp weekNumber,
    int viewedGenuine,
    int viewedCrawlers,
    int viewedOwner,
    long responded,
    Double score,
    List<String> comments // potential weak point
) {
}
