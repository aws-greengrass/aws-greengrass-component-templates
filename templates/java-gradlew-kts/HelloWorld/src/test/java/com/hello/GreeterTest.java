package com.hello;

import com.hello.Greeter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * Unit test for Greeter
 */
public class GreeterTest {
    @Test
    public void test_sayHello() {
        Greeter greeter = new Greeter();
        assertEquals("Hello World!", greeter.sayHello("World"));
    }
}
