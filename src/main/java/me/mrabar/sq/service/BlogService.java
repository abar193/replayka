package me.mrabar.sq.service;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLQueryFactory;
import me.mrabar.replayka.QBlog;
import me.mrabar.replayka.QBlogOwnership;
import me.mrabar.replayka.QRequest;
import me.mrabar.replayka.QResponse;
import me.mrabar.sq.component.PGJsonMapper;
import me.mrabar.sq.model.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Singleton
public class BlogService {
  @Inject
  SQLQueryFactory queryFactory;
  @Inject
  PGJsonMapper pgJsonMapper;

  public Blog get(String key) {
    var b = QBlog.blog;
    var f = queryFactory.select(b.blogId, b.blogName, b.blogKey, b.blogMisc)
        .from(b)
        .where(b.blogKey.eq(key))
        .fetchFirst();

    if (f == null) {
      return null;
    }

    return new Blog(
        f.get(b.blogId),
        f.get(b.blogName),
        f.get(b.blogKey),
        pgJsonMapper.pgObjectToClass(f.get(b.blogMisc), BlogMisc.class)
    );
  }

  public boolean create(String key, String name, String url, String topic) {
    var misc = new BlogMisc(url, topic, LocalDateTime.now());
    long records = queryFactory.insert(QBlog.blog)
        .columns(QBlog.blog.blogKey, QBlog.blog.blogName, QBlog.blog.blogMisc)
        .values(
            key,
            name,
            Expressions.template(String.class, "{0}::jsonb", pgJsonMapper.objectToJson(misc))
        )
        .execute();

    return records > 0;
  }

  public boolean assignOwnership(User user, String blogKey) {
    return assignOwnership(user, get(blogKey));
  }

  public boolean assignOwnership(User user, Blog blog) {
    long records = queryFactory.insert(QBlogOwnership.blogOwnership)
        .columns(QBlogOwnership.blogOwnership.loginId, QBlogOwnership.blogOwnership.blogId)
        .values(user.id(), blog.id())
        .execute();

    return records > 0;
  }

  // todo: U & D for Blog

  public List<PageOverview> blogPages(Blog blog) {
    return queryFactory.select(
            QRequest.request.page,
            QRequest.request.requestUuid.count(),
            QResponse.response.score.avg(),
            QResponse.response.comment.count()
        )
        .from(QBlog.blog)
        .join(QRequest.request).on(QRequest.request.blogId.eq(QBlog.blog.blogId))
        .join(QResponse.response).on(QResponse.response.requestUuid.eq(QRequest.request.requestUuid))
        .where(QBlog.blog.blogId.eq(blog.id()))
        .groupBy(QRequest.request.page)
        .fetchResults()
        .getResults()
        .stream()
        .map(p -> new PageOverview(
            p.get(QRequest.request.page),
            Optional.ofNullable(p.get(1, Long.class)).orElse(-1L),
            Optional.ofNullable(p.get(2, Double.class)).orElse(-1.0),
            Optional.ofNullable(p.get(3, Long.class)).orElse(-1L)
        ))
        .toList();
  }

  public List<PageFeedback> pageFeedback(Blog blog, String pageName) {
    return queryFactory.select(QResponse.response.score, QResponse.response.comment, QResponse.response.responseTs)
        .from(QBlog.blog)
        .join(QRequest.request).on(QRequest.request.blogId.eq(QBlog.blog.blogId))
        .join(QResponse.response).on(QResponse.response.requestUuid.eq(QRequest.request.requestUuid))
        .where(QBlog.blog.blogId.eq(blog.id()).and(QRequest.request.page.eq(pageName)))
        .fetchResults()
        .getResults()
        .stream()
        .map(p -> new PageFeedback(
                 pageName,
                 Optional.ofNullable(p.get(0, Integer.class)).orElse(-1),
                 p.get(1, String.class),
                 Optional.ofNullable(p.get(2, Timestamp.class)).map(Timestamp::toLocalDateTime).orElse(null)
             )
        )
        .toList();
  }

  public List<PageOverviewComments> blogOverview(Blog selectedBlog) {
    var blog = QBlog.blog;
    var request = QRequest.request;
    var response = QResponse.response;
    var result = queryFactory
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
        .where(blog.blogKey.eq(selectedBlog.key()))
        .groupBy(request.page)
        .fetch();

    return result.stream().map(t -> new PageOverviewComments(
        selectedBlog.key(),
        decodeUrl(t.get(0, String.class)),
        t.get(1, Long.class),
        t.get(2, Long.class),
        t.get(3, Double.class),
        Arrays.stream(t.get(4, String[].class))
            .filter(Objects::nonNull)
            .filter(Predicate.not(String::isBlank))
            .collect(Collectors.toList())
    )).collect(Collectors.toList());
  }

  private String decodeUrl(String string) {
    try {
      return URLDecoder.decode(string, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      return string;
    }
  }

}
