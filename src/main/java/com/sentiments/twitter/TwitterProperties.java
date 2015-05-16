package com.sentiments.twitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.io.IOUtils;

public class TwitterProperties {
    
    private static final String PROPS_SUFFIX = ".properties";

    /**
     * Finds the properties file in the classpath and loads the properties from there.
     *
     * @return The found properties object (must be not-null)
     * @throws RuntimeException If no properties file can be found on the classpath
     */
    public static Properties loadPropertiesFromClasspath() {
      List<String> validNames = Arrays.asList("Twitter", "com.twitter.Twitter");
      for (String name: validNames) {
        Properties props = loadProperties(name);
        if (props != null) return props;
      }
      return null;
    }

    private static Properties loadProperties(String name) {
      return loadProperties(name, Thread.currentThread().getContextClassLoader());
    }

    private static Properties loadProperties(String name, ClassLoader loader){
      if(name.endsWith (PROPS_SUFFIX)) name = name.substring(0, name.length () - PROPS_SUFFIX.length ());
      name = name.replace('.', '/');
      name += PROPS_SUFFIX;
      Properties result = null;

      // Returns null on lookup failures
      System.err.println("Searching for resource: " + name);
      InputStream in = loader.getResourceAsStream (name);
      try {
        if (in != null) {
          InputStreamReader reader = new InputStreamReader(in, "utf-8");
          result = new Properties ();
          result.load(reader); // Can throw IOException
        }
      } catch (IOException e) {
        result = null;
      } finally {
        IOUtils.closeIgnoringExceptions(in);
      }

      return result;
    }
}
