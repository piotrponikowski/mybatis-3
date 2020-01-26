package org.apache.ibatis.submitted.expand_collection_param;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.submitted.expand_collection_param.mapper.XmlMapper;
import org.apache.ibatis.submitted.expand_collection_param.model.User;
import org.apache.ibatis.submitted.expand_collection_param.model.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionInXmlTest {

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void initDatabase() throws Exception {
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/expand_collection_param/config/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
      sqlSessionFactory.getConfiguration().getMapperRegistry().addMapper(XmlMapper.class);
    }

    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
      "org/apache/ibatis/submitted/expand_collection_param/config/CreateDB.sql");
  }

  @Test
  public void shouldQueryByCollection() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      XmlMapper mapper = sqlSession.getMapper(XmlMapper.class);
      List<User> users = mapper.getUsers(2, Arrays.asList(UserRole.WRITER, UserRole.REVIEWER));
      assertEquals(3, users.size());
      assertEquals(1, users.get(0).getId());
      assertEquals(3, users.get(1).getId());
      assertEquals(4, users.get(2).getId());
    }
  }

}
