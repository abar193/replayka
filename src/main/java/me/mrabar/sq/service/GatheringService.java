package me.mrabar.sq.service;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.sql.SQLQueryFactory;
import me.mrabar.replayka.QRequest;
import me.mrabar.replayka.QResponse;
import me.mrabar.sq.component.PGJsonMapper;
import me.mrabar.sq.exception.BlogNotFoundException;
import me.mrabar.sq.model.RequestInfo;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MultivaluedMap;
import java.util.UUID;

@Singleton
public class GatheringService {
  private static final Logger LOG = Logger.getLogger(GatheringService.class);
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
      MultivaluedMap<String, String> headers
  ) throws BlogNotFoundException {

    var blog = blogService.byKey(blogKey);
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
        .columns(r.requestId, r.score, r.comment, r.requestUuid)
        .values(requestId, score, comment, UUID.fromString(requestId))
        .execute();
  }

  private boolean insertRequest(long blogId, String page, UUID requestId, RequestInfo info) {
    var r = QRequest.request;
    var pb = new PathBuilderFactory();

    var i = factory.insert(r)
        .columns(r.blogId, r.page, r.requestId, r.requestUuid, r.info)
        .values(
            blogId,
            page,
            requestId.toString(),
            requestId,
            Expressions.template(String.class, "{0}::jsonb", jsonMapper.objectToJson(info))
        )
        .execute();

    return i > 0;
  }

  private UUID getRequestId() {
    return UUID.randomUUID();
  }
}
