package me.mrabar.sq.model;

import java.util.List;

public record OverviewRow(
    String blogName,
    String pageName,
    long viewed,
    long responded,
    double score,
    List<String> comments // potential weak point
) {
}
