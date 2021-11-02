package com.hello;

import com.hello.Greeter;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Greeter
 */
public class GreeterTest {
   @Test
   public void test_sayHello() {
      Greeter greeter = new Greeter();
      greeter.sayHello();
   }
}
