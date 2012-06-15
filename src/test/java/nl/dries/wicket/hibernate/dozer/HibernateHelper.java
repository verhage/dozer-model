package nl.dries.wicket.hibernate.dozer;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Hibernate test helper
 * 
 * @author dries
 */
public final class HibernateHelper
{
	/**
	 * Create a session factory for testing
	 * 
	 * @param entities
	 *            the entity classes
	 * @return initialized {@link SessionFactory}
	 */
	public static SessionFactory buildSessionFactory(List<Class<? extends Serializable>> entities)
	{
		AnnotationConfiguration cfg = new AnnotationConfiguration();

		for (Class<? extends Serializable> entity : entities)
		{
			cfg.addAnnotatedClass(entity);
		}

		cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		cfg.setProperty("hibernate.show_sql", "true");
		cfg.setProperty("hibernate.hbm2ddl.auto", "create");

		cfg.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
		cfg.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:testdb");
		cfg.setProperty("hibernate.connection.username", "sa");

		return cfg.buildSessionFactory();
	}
}
