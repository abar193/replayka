package me.mrabar.sq;

import com.querydsl.sql.*;
import io.agroal.api.AgroalDataSource;

import javax.inject.Singleton;

public class DbConfig {

  @Singleton
  public SQLQueryFactory factory(AgroalDataSource dataSource) {
    SQLTemplates templates = PostgreSQLTemplates.builder().printSchema().build();
    Configuration configuration = new Configuration(templates);
    return new SQLQueryFactory(configuration, dataSource);
  }

}
