/* File: SimpleCache.java
 * Author: Ashish Tibrewal
 * Date created: 20.07.2016
 * Date modified: 20.07.2016
 * Description: Class containing a simple cache
 */

import java.util.*;

public class SimpleCache
{
  private static SimpleCache instance;
  private static Object monitor = new Object();
  private Map<String, Object> cache = Collections.synchronizedMap(new HashMap<String, Object>());

  // Declare constructor as private to avoid a single instance of this class
  private SimpleCache()
  {
  }

  /* Method to add element to cache */
  public void put(String cacheKey, Object value)
  {
    cache.put(cacheKey, value);
  }

  /* Method to retrieve element from cache */
  public Object get(String cacheKey)
  {
    return cache.get(cacheKey);
  }

  /* Mehtod to clear an entry in the cache */
  public void clear(String cacheKey)
  {
    cache.put(cacheKey, null);
  }

  /* Mehtod to clear the entire cache */
  public void clear()
  {
    cache.clear();
  }

  /* Method to create/retrieve a cache instance */
  public static SimpleCache getInstance()
  {
    if(instance == null)
    {
      synchronized(monitor) // Creates an intrinsic lock to prevent issues that can arise when using multiple threads
      {
        if(instance == null)
        {
          instance = new SimpleCache();
        }
      }
    }
    return instance;
  }
}
