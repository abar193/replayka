package me.mrabar.sq.rest;

import me.mrabar.sq.model.Blog;
import me.mrabar.sq.model.OverviewRow;
import me.mrabar.sq.service.BlogService;
import me.mrabar.sq.service.FeedbackService;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/rest/blog/")
public class ResultsEndpoint {

  @Inject
  FeedbackService feedbackService;
  @Inject
  BlogService blogService;

  @Path("{key}/overview")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<OverviewRow> hello(@RestPath String key) {
    return feedbackService.blogStats(key);
  }

  @Path("{key}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Blog getBlog(@RestPath String key) {
    return blogService.byKey(key);
  }
}