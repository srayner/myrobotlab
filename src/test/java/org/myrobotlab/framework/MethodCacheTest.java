package org.myrobotlab.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.myrobotlab.codec.CodecUtils;
import org.myrobotlab.logging.Level;
import org.myrobotlab.logging.LoggingFactory;
import org.myrobotlab.service.Clock;
import org.myrobotlab.service.Runtime;
import org.myrobotlab.service.TestCatcher;
import org.myrobotlab.service.TestCatcher.Ball;
import org.myrobotlab.service.interfaces.HttpDataListener;
import org.myrobotlab.test.AbstractTest;

public class MethodCacheTest extends AbstractTest {

  static MethodCache cache;
  static TestCatcher tester;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    cache = MethodCache.getInstance();
    cache.cacheMethodEntries(TestCatcher.class);
    cache.clear();
    assertEquals(0, cache.getObjectSize());

    cache.cacheMethodEntries(Runtime.class);
    cache.cacheMethodEntries(TestCatcher.class);
    cache.cacheMethodEntries(Clock.class);
    // non-service entry
    cache.cacheMethodEntries(TestCatcher.class);
    cache.cacheMethodEntries(TestCatcher.class);
    assertEquals(3, cache.getObjectSize());

    tester = (TestCatcher) Runtime.start("tester", "TestCatcher");
  }

  /**
   * find missing methods which did not appear through first cache pass
   * 
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  @Test
  public void findTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    // FIXME FIXME - CodecUtils.decode("",
    // FIXME FIXME - verify cache entry exist after being resolved through isAssignable !!!
    Object ret = null;
    Method method = null;

    // testing a method defined with an interface parameter and whos ordinal has several other
    // definitions - this effectively should be one of the hardest strongly typed situations to resolve
    method = cache.getMethod(TestCatcher.class, "invokeTest", (HttpDataListener)tester);
    ret = method.invoke(tester, tester);
    assertEquals(tester, ret);

    method = cache.getMethod(TestCatcher.class, "invokeTest", "echo");
    ret = method.invoke(tester, "echo");
    assertEquals("echo", ret);

    // interface test - method with a interface parameter
    method = cache.getMethod(Runtime.class, "register", tester);
    ret = method.invoke(Runtime.getInstance(), tester);
    assertEquals(tester, ret);

    // primitive parameter only test
    method = cache.getMethod(TestCatcher.class, "primitiveOnlyMethod", 3);
    ret = method.invoke(tester, 5);
    assertEquals(5, ret);

    // super overloaded test
    Integer[] testArray = new Integer[] { 3, 5, 10 };
    method = cache.getMethod(TestCatcher.class, "getPin", new Object[] { testArray });
    ret = method.invoke(tester, new Object[] { testArray });
    assertEquals(testArray, ret);

  }

  @Test
  public void strongTypeTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    Object ret = null;
    Method method = null;

    // method = cache.getMethod(TestCatcher.class, "getPin", new Integer(3);

    // log.info("Clock - {}",
    // CodecUtils.toJson(cache.getRemoteMethods("Clock")));
    log.info("TestCatcher - {}", CodecUtils.toJson(cache.getRemoteMethods("org.myrobotlab.framework.TestCatcher")));
    cache.getRemoteMethods();

    method = cache.getMethod(TestCatcher.class, "getPin", 3);
    ret = method.invoke(tester, 3);
    assertEquals(3, ret);

    method = cache.getMethod(TestCatcher.class, "primitiveOnlyMethod", 3);
    ret = method.invoke(tester, 5);
    assertEquals(5, ret);

    Integer[] testArray = new Integer[] { 3, 5, 10 };
    method = cache.getMethod(TestCatcher.class, "getPin", new Object[] { testArray });
    ret = method.invoke(tester, new Object[] { testArray });
    assertEquals(testArray, ret);

  }

  @Test
  public void ancestorTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    Object ret = null;
    Method method = null;

    log.info("TestCatcher - {}", CodecUtils.toJson(cache.getRemoteMethods("org.myrobotlab.framework.TestCatcher")));
    cache.getRemoteMethods();

    // service super type test
    method = cache.getMethod(TestCatcher.class, "stopService");
    ret = method.invoke(tester);
    assertEquals(null, ret);
        
    method = cache.getMethod(TestCatcher.class, "isRunning");
    ret = method.invoke(tester);
    assertEquals(false, ret);

    method = cache.getMethod(TestCatcher.class, "startService");
    ret = method.invoke(tester);
    assertEquals(null, ret);

    method = cache.getMethod(TestCatcher.class, "isRunning");
    ret = method.invoke(tester);
    assertEquals(true, ret);
    
    Integer[] testArray = new Integer[] { 3, 5, 10 };
    method = cache.getMethod(TestCatcher.class, "getPin", new Object[] { testArray });
    ret = method.invoke(tester, new Object[] { testArray });
    assertEquals(testArray, ret);

  }

  @Test
  public void keyTest() {

  }

  /**
   * Testing json
   * 
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   * @throws InstantiationException 
   * @throws SecurityException 
   * @throws NoSuchMethodException 
   * @throws ClassNotFoundException 
   */
  @Test
  public void lossyJsonMethodTest() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException {

    String[] encodedParams = new String[] {CodecUtils.toJson("blah")};
    
    Object[] params = cache.getDecodedJsonParameters(TestCatcher.class, "invokeTest", encodedParams);
    Method method = cache.getMethod(TestCatcher.class, "invokeTest", params);
    Object ret = method.invoke(tester, params);
    log.info("ret is {}", ret);
    log.info("ret returned {} of type {}", ret, ret.getClass().getSimpleName());
    assertNotNull(ret); // arbitrary that i resolves to string

    encodedParams = new String[] {CodecUtils.toJson(5.0)};
    params = cache.getDecodedJsonParameters(TestCatcher.class, "onDouble", encodedParams);
    method = cache.getMethod(TestCatcher.class, "onDouble", params);
    ret = method.invoke(tester, params);
    log.info("ret returned {} of type {}", ret, ret.getClass().getSimpleName());
    assertEquals(5.0, ret); // arbitrary float vs int/double

    encodedParams = new String[] {CodecUtils.toJson(5)};
    params = cache.getDecodedJsonParameters(TestCatcher.class, "onInt", encodedParams);
    method = cache.getMethod(TestCatcher.class, "onInt", params);
    ret = method.invoke(tester, params);
    log.info("ret returned {} of type {}", ret, ret.getClass().getSimpleName());
    assertEquals(5, ret); // arbitrary float vs int/double

    // complex object throw
    Ball ball = new Ball();
    ball.name = "my ball";
    ball.type = "football";
    ball.rating = 5;
    encodedParams = new String[] {CodecUtils.toJson(ball)};
    params = cache.getDecodedJsonParameters(TestCatcher.class, "catchBall", encodedParams);
    method = cache.getMethod(TestCatcher.class, "catchBall", params);
    ret = method.invoke(tester, params);
    log.info("ret returned {} of type {}", ret, ret.getClass().getSimpleName());
    assertEquals("my ball", ball.name); 
    assertTrue(5 == ball.rating); 
  }

  public static void main(String[] args) {
    try {

      LoggingFactory.init(Level.DEBUG);

      MethodCacheTest test = new MethodCacheTest();
      // test.strongTypeTest();
      test.findTest();

    } catch (Exception e) {
      log.error("main threw", e);
    }
  }

}
