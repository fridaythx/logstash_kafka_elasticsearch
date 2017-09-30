package com.friday.dal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.friday.App;
import com.friday.utils.PropertiesUtil;

/**
 * MyBatis SqlSession工厂
 * @author Friday
 *
 */
public class SqlSessionFactory {
	private static SqlSessionFactory factory;
	private static org.apache.ibatis.session.SqlSessionFactory _factory;

	private SqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		Properties appProps = PropertiesUtil.getProperties(App.APP_PROPS_PATH);
		_factory = builder.build(inputStream,
				"production".equals(appProps.getProperty("env")) ? "production" : "development");
	}

	public static synchronized SqlSessionFactory getInstance() throws IOException {
		if (factory == null) {
			factory = new SqlSessionFactory();
		}
		return factory;
	}

	public SqlSession getSqlSession() {
		return _factory.openSession();
	}

	public void closeSqlSession(SqlSession session) {
		try {
			if (session != null) {
				session.close();
			}
		} catch (Exception e) {
		}
	}
}
