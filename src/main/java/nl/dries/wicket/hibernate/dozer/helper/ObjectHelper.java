package nl.dries.wicket.hibernate.dozer.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

/**
 * Reflection/proxy helper functions
 * 
 * @author dries
 */
public final class ObjectHelper
{
	/** Logger */
	private static final Logger LOG = LoggerFactory.getLogger(ObjectHelper.class);

	/** Util -> private */
	private ObjectHelper()
	{
	}

	/**
	 * Get a value using reflection
	 * 
	 * @param object
	 *            in this object
	 * @param property
	 *            the property to get
	 * @return its value
	 */
	public static Object getValue(Object object, String property)
	{
		Object value = null;
		
		Field field = ReflectionUtils.findField(object.getClass(), property);
		if (field != null)
		{
			try
			{
				ReflectionUtils.makeAccessible(field);
				value = ReflectionUtils.getField(field, object);
			}
			catch (SecurityException e)
			{
				LOG.error(String.format("Cannot get value for property %s in object %s of class %s", property, object, object.getClass()), e);
			}
			catch (IllegalStateException e)
			{
				LOG.error(String.format("Cannot get value for property %s in object %s of class %s", property, object, object.getClass()), e);
			}
		}
		else
		{
			LOG.error(String.format("Cannot find field %s in class %s of object %s", property, object.getClass(), object));
		}
		
		return value;
	}

	/**
	 * Set a value using reflection
	 * 
	 * @param object
	 *            target object
	 * @param property
	 *            target property to set
	 * @param value
	 *            the value to set
	 */
	public static void setValue(Object object, String property, Object value)
	{
		try
		{
			PropertyUtils.setProperty(object, property, value);
		}
		catch (IllegalAccessException e)
		{
			LOG.error(String.format("Cannot set value %s for property %s in object %s", value, property, object), e);
		}
		catch (InvocationTargetException e)
		{
			LOG.error(String.format("Cannot set value %s for property %s in object %s", value, property, object), e);
		}
		catch (NoSuchMethodException e)
		{
			LOG.error(String.format("Cannot set value %s for property %s in object %s", value, property, object), e);
		}
	}

	/**
	 * Deproxy a Hibernate enhanced object, only call when sure the object is initialized, otherwise (unwanted)
	 * intialization wil take place
	 * 
	 * @param object
	 *            the input object
	 * @return deproxied object
	 */
	@SuppressWarnings("unchecked")
	public static <U> U deproxy(U object)
	{
		if (object instanceof HibernateProxy)
		{
			HibernateProxy hibernateProxy = (HibernateProxy) object;
			LazyInitializer lazyInitializer = hibernateProxy.getHibernateLazyInitializer();

			return (U) lazyInitializer.getImplementation();
		}
		return object;
	}
}
