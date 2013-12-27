package org.swb.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesLoader
{
    private static final String SUFFIX = ".properties";
    private static final String IMPORT_DEFAULT = "@IMPORT_DEFAULT";
    
    /**
     * Looks up a resource named 'name' in the classpath. The resource must map
     * to a file with .properties extention. The name is assumed to be absolute
     * and can use either "/" or "." for package segment separation with an
     * optional leading "/" and optional ".properties" suffix. Thus, the
     * following names refer to the same resource:
     * 
     * <pre>
     * some.pkg.Resource
     * some.pkg.Resource.properties
     * some/pkg/Resource
     * some/pkg/Resource.properties
     * /some/pkg/Resource
     * /some/pkg/Resource.properties
     * </pre>
     * 
     * @param name
     *            classpath resource name [may not be null]
     * @param loader
     *            classloader through which to load the resource [null is
     *            equivalent to the application loader]
     * 
     * @return resource converted to java.util.Properties
     * @throws IllegalArgumentException
     *             if the resource was not found
     */

    public static Properties loadProperties(String name, ClassLoader loader)
    {
        return loadProperties(name, loader, true);
    }
    
    /**
     * A convenience overload of {@link #loadProperties(String, ClassLoader)}
     * that uses the current thread's context classloader.
     */
    public static Properties loadProperties(final String name)
    {
        return loadProperties(name, true);
    }
    public static Properties loadProperties(final String name, final boolean trim)
    {
        return loadProperties(name, Thread.currentThread().getContextClassLoader(), trim);
    }
    
    private static Properties loadProperties(String name, ClassLoader loader, final boolean trim)
    {
        if (name == null) throw new IllegalArgumentException("null input: name");
        if (name.startsWith("/")) name = name.substring(1);
        if (name.endsWith(SUFFIX)) name = name.substring(0, name.length() - SUFFIX.length());

        Properties result = null;
        InputStream in = null;
        try
        {
            if (loader == null) loader = ClassLoader.getSystemClassLoader();
            name = name.replace('.', '/');
            if (!name.endsWith(SUFFIX)) name = name.concat(SUFFIX);

            // Returns null on lookup failures:
            in = loader.getResourceAsStream(name);
            if (in != null)
            {
                result = new Properties();
                result.load(in); // Can throw IOException
            }
        }
        catch (Exception e)
        {
            result = null;
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (Throwable ignore)
                {
                }
            }
        }

        if (result == null)
        {
            throw new IllegalArgumentException("could not load [" + name + "]" + " as " + ("a classloader resource"));
        }

        result = trim(result, trim);
        if (result.containsKey(IMPORT_DEFAULT))
        {
        	Properties defaultsProps =  loadProperties(result.getProperty(IMPORT_DEFAULT), loader, trim);
        	defaultsProps.putAll(result);
        	return defaultsProps;
        }
        
        return result;
    }


    public static Properties loadProperties(InputStream in, ClassLoader loader)
    {
        return loadProperties(in, true, loader);
    }
    private static Properties loadProperties(InputStream in, boolean trim, ClassLoader loader)
    {
        Properties result = null;
        try
        {
            result = new Properties();
            result.load(in); // Can throw IOException
        }
        catch (Exception e)
        {
            result = null;
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (Throwable ignore)
                {
                }
            }
        }
        result = trim(result, trim);
        if (result.containsKey(IMPORT_DEFAULT))
        {
        	Properties defaultsProps =  loadProperties(result.getProperty(IMPORT_DEFAULT), loader, trim);
        	defaultsProps.putAll(result);
        	return defaultsProps;
        }
        
        return result;
    }

    /* ************** */
    /* Utility method */
    /* ************** */
    
    private static Properties trim(Properties result, boolean trim)
    {
        if (!trim) return result;
        
        Set keys = result.keySet();
        for (Object key: keys)
        {
            String k = (String) key;
            result.setProperty(k, result.getProperty(k).trim());
        }
        return result;
    }
}

