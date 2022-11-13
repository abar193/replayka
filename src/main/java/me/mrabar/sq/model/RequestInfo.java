package me.mrabar.sq.model;

import java.util.Map;

public record RequestInfo(String ip, Map<String, String> headers) {
}
