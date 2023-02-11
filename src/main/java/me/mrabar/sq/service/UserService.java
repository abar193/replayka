package me.mrabar.sq.service;

import com.querydsl.sql.SQLQueryFactory;
import io.quarkus.oidc.UserInfo;
import io.quarkus.security.identity.SecurityIdentity;
import me.mrabar.replayka.QBlog;
import me.mrabar.replayka.QBlogOwnership;
import me.mrabar.replayka.QSocialLogin;
import me.mrabar.sq.component.PGJsonMapper;
import me.mrabar.sq.model.Blog;
import me.mrabar.sq.model.BlogMisc;
import me.mrabar.sq.model.User;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class UserService {
  private static final Logger LOG = Logger.getLogger(UserService.class);

  @Inject
  SQLQueryFactory queryFactory;
  @Inject
  PGJsonMapper pgJsonMapper;

  public User getUser(SecurityIdentity identity) {
    var info = getUserInfo(identity);
    var login = getLogin(info);

    var user = fetchUser(login, "GITHUB");
    return (user != null) ? user : registerUser(login, "GITHUB");
  }

  public List<Blog> userBlogs(User user) {
    var b = QBlog.blog;

    var results = queryFactory.select(b.blogId, b.blogName, b.blogKey, b.blogMisc)
        .from(b)
        .join(QBlogOwnership.blogOwnership).on(QBlogOwnership.blogOwnership.blogId.eq(b.blogId))
        .where(QBlogOwnership.blogOwnership.loginId.eq(user.id()))
        .fetchResults();

    if (results == null || results.isEmpty()) {
      return List.of();
    }

    return results.getResults().stream().map(f -> new Blog(
        f.get(b.blogId),
        f.get(b.blogName),
        f.get(b.blogKey),
        pgJsonMapper.pgObjectToClass(f.get(b.blogMisc), BlogMisc.class)
    )).toList();
  }

  private UserInfo getUserInfo(SecurityIdentity identity) {
    return (UserInfo) identity.getAttributes().get("userinfo");
  }

  private String getLogin(UserInfo info) {
    return info.getString("login");
  }

  private User fetchUser(String login, String source) {
    var l = QSocialLogin.socialLogin;
    var result = queryFactory
        .select(l.id, l.name, l.source)
        .from(l)
        .where(l.name.eq(login).and(l.source.eq(source)))
        .fetchFirst();

    return Optional.ofNullable(result)
        .map(t -> new User(
                 t.get(l.id),
                 t.get(l.name),
                 t.get(l.source)
             )
        ).orElse(null);
  }

  private User registerUser(String login, String source) {
    var l = QSocialLogin.socialLogin;

    var r = queryFactory.insert(l)
        .columns(l.name, l.source)
        .values(login, source)
        .execute();

    if (r <= 0) {
      throw new IllegalStateException(
          String.format("Failed to register new user with login %s and source %s", login, source)
      );
    }

    LOG.info(String.format("New %s user with login '%s'", source, login));
    return fetchUser(login, source);
  }
}
