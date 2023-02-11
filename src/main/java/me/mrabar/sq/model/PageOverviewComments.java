package me.mrabar.sq.model;

import java.util.List;

public record PageOverviewComments(
    String blogName,
    String pageName,
    long viewed,
    long responded,
    Double score,
    List<String> comments // potential weak point
) {
}
