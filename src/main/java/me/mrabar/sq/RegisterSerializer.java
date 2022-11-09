package me.mrabar.sq;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.jackson.ObjectMapperCustomizer;

import javax.inject.Singleton;

@Singleton
public class RegisterSerializer implements ObjectMapperCustomizer {
  public void customize(ObjectMapper mapper) {
//    mapper.registerModule(new CustomModule());
  }
}
