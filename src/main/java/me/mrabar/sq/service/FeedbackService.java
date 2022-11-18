package me.mrabar.sq.service;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQueryFactory;
import me.mrabar.replayka.QBlog;
import me.mrabar.replayka.QRequest;
import me.mrabar.replayka.QResponse;
import me.mrabar.sq.model.OverviewRow;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class FeedbackService {
  @Inject
  SQLQueryFactory factory;

  public List<OverviewRow> blogStats(String blogKey) {
    var blog = QBlog.blog;
    var request = QRequest.request;
    var response = QResponse.response;
    var result = factory
        .select(
            request.page,
            request.requestUuid.count(),
            response.requestUuid.count(),
            response.score.avg(),
            Expressions.template(String[].class, "array_agg({0})", response.comment)
        )
        .from(request)
        .join(blog).on(blog.blogId.eq(request.blogId))
        .leftJoin(response).on(response.requestUuid.eq(request.requestUuid))
        .where(blog.blogKey.eq(blogKey))
        .groupBy(request.page)
        .fetch();

    return result.stream().map(t -> new OverviewRow(
        blogKey,
        t.get(0, String.class),
        t.get(1, Long.class),
        t.get(2, Long.class),
        t.get(3, Double.class),
        Arrays.asList(t.get(4, String[].class)).stream().filter(Objects::nonNull).collect(Collectors.toList())
    )).collect(Collectors.toList());
  }
}
