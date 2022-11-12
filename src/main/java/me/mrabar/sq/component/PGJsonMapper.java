package me.mrabar.sq.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PGJsonMapper {
  @Inject
  ObjectMapper objectMapper;

  public <T> T pgObjectToClass(Object o, Class<T> tClass) {
    if (o == null) {
      return null;
    }
    try {
      return objectMapper.readValue(((PGobject) o).getValue(), tClass);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  public String objectToJson(Object o) {
    try {
      return objectMapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      return "null";
    }
  }
}
