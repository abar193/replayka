package me.mrabar.sq.service;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQueryFactory;
import me.mrabar.replayka.QRequest;
import me.mrabar.replayka.QResponse;
import me.mrabar.sq.component.PGJsonMapper;
import me.mrabar.sq.exception.BlogNotFoundException;
import me.mrabar.sq.model.RequestInfo;
import me.mrabar.sq.web.InternalPage;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;
import java.util.*;

@Singleton
public class GatheringService {
  private static final Logger LOG = Logger.getLogger(GatheringService.class);
  private static final List<String> STORED_HEADERS = List.of(
      "Referer",
      HttpHeaders.ACCEPT_LANGUAGE,
      HttpHeaders.USER_AGENT
  );
  @Inject
  SQLQueryFactory factory;
  @Inject
  BlogService blogService;
  @Inject
  PGJsonMapper jsonMapper;

  public String createRequestId(
      String blogKey,
      String page,
      String ip,
      Map<String, String> headers
  ) throws BlogNotFoundException {

    var blog = blogService.get(blogKey);
    if (blog == null) {
      throw new BlogNotFoundException();
    }

    var requestId = getRequestId();
    var info = new RequestInfo(ip, headers);

    if (!insertRequest(blog.id(), page, requestId, info)) {
      LOG.warn("Failed to insert request record, go figure.");
    }

    return requestId.toString();
  }

  public void saveFeedback(String requestId, int score, String comment) {
    var r = QResponse.response;
    factory.insert(r)
        .columns(r.requestUuid, r.score, r.comment)
        .values(UUID.fromString(requestId), score, comment)
        .execute();
  }

  private boolean insertRequest(long blogId, String page, UUID requestId, RequestInfo info) {
    var r = QRequest.request;

    var i = factory.insert(r)
        .columns(r.blogId, r.page, r.requestUuid, r.info)
        .values(
            blogId,
            page,
            requestId,
            Expressions.template(String.class, "{0}::jsonb", jsonMapper.objectToJson(info))
        )
        .execute();

    return i > 0;
  }

  public Map<String, String> cleanHeaders(HttpHeaders headers) {
    var m = new HashMap<String, String>();
    for (String h : STORED_HEADERS) {
      if (headers.getHeaderString(h) != null) {
        m.put(h, headers.getHeaderString(h));
      }
    }
    if (Optional
        .ofNullable(headers.getHeaderString(HttpHeaders.COOKIE))
        .map(h -> h.contains(InternalPage.USER_TYPE_COOKIE))
        .orElse(false)
    ) {
      m.put(InternalPage.USER_TYPE_COOKIE, InternalPage.BLOG_OWNER); // don't need the session cookies in my db
    }
    return m;
  }

  private UUID getRequestId() {
    return UUID.randomUUID();
  }
}
