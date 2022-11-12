package me.mrabar.sq.service;

import com.querydsl.sql.SQLQueryFactory;
import me.mrabar.replayka.QBlog;
import me.mrabar.sq.component.PGJsonMapper;
import me.mrabar.sq.model.Blog;
import me.mrabar.sq.model.BlogMisc;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlogService {
  @Inject
  SQLQueryFactory queryFactory;
  @Inject
  PGJsonMapper pgJsonMapper;

  public Blog byKey(String key) {
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

}
