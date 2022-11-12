package me.mrabar.sq.model;

import javax.ws.rs.core.MultivaluedMap;

public record RequestInfo(String ip, MultivaluedMap<String, String> headers) {
}
