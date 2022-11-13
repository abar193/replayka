package me.mrabar.sq.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import me.mrabar.sq.exception.BlogNotFoundException;
import me.mrabar.sq.exception.ScriptKiddieException;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ErrorHandler implements ExceptionMapper<Exception> {
  private static final Logger LOG = Logger.getLogger(ErrorHandler.class);

  @Inject
  Template message;

  @Override
  public Response toResponse(Exception e) {
    return Response
        .status(400)
        .type(MediaType.TEXT_HTML)
        .entity(createTemplate(e))
        .build();
  }

  private TemplateInstance createTemplate(Exception e) {
    if (e instanceof BlogNotFoundException) {
      return message.data("message", "Invalid blogKey provided. How could this ever happen?!");
    } else if (e instanceof ScriptKiddieException) {
      return message.data("message", "Oh no, you've totally hacked this service, I surrender!")
          .data("comment", "...but really please don't abuse this service. "
              + "It's a hobby project, it's not meant to be secure.");
    } else {
      LOG.error("Unhandled exception " + e.getMessage(), e);
      return message.data("message", "Unknown exception")
          .data("comment", "It has been logged, but it's unlikely that the logs will be checked often");
    }
  }

}
