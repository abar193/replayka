package me.mrabar.sq.web;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.ext.web.RoutingContext;
import me.mrabar.sq.model.Blog;
import me.mrabar.sq.service.BlogService;
import me.mrabar.sq.service.UserService;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Path(InternalPage.ENDPOINT)
@Authenticated
@Produces(MediaType.TEXT_HTML)
public class InternalPage {
  public final static String ENDPOINT = "/internal";

  @Inject
  SecurityIdentity identity;

  @Inject
  UserService userService;

  @Inject
  BlogService blogService;

  @Location("internal/main")
  Template main;

  @GET
  public TemplateInstance internalPage(
      @RestQuery String blogKey,
      @RestQuery String pageName,
      @Context HttpHeaders headers,
      @Context RoutingContext context
  ) {
    var user = userService.getUser(identity);
    var blogs = userService.userBlogs(user);

    TemplateInstance templateInstance = main
        .instance()
        .data("user", user)
        .data("blogs", blogs);

    if (blogs.size() == 1) {
      blogKey = blogs.get(0).key();
    }

    Optional<Blog> selectedBlog = byKey(blogs, blogKey);

    if (selectedBlog.isPresent()) {
      var pages = selectedBlog.map(blogService::blogOverview).orElse(null);
      templateInstance.data("selectedBlog", selectedBlog.get().key());
      templateInstance = templateInstance.data("blogPages", pages);
    } else {
      templateInstance.data("selectedBlog", "");
    }

    if (selectedBlog.isPresent() && pageName != null) {
      templateInstance.data("selectedPage", pageName);
      templateInstance.data("pageFeedback", blogService.pageFeedback(selectedBlog.get(), pageName));
    }

    return templateInstance;
  }

  @Path("/blog")
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response registerBlog(
      @RestForm String blogKey,
      @RestForm String blogName,
      @RestForm String blogUrl,
      @RestForm String blogTopic
  ) {
    if (blogService.create(blogKey, blogName, blogUrl, blogTopic)) {
      blogService.assignOwnership(userService.getUser(identity), blogKey);
      return Response.seeOther(URI.create(ENDPOINT)).build();
    }
    // todo: proper validation
    throw new IllegalArgumentException("Something went wrong, but since I can't be bothered by checking params, just "
                                           + "go to DB and see for yourself if you are duplicating something, or "
                                           + "whatever.");
  }

  private Optional<Blog> byKey(List<Blog> blogs, String key) {
    return blogs.stream().filter(b -> b.key().equals(key)).findFirst();
  }
}
