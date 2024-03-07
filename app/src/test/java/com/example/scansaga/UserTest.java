package com.example.scansaga;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link User} class.
 */
public class UserTest {

    private User user;

    /**
     * Sets up the test fixture. Called before every test case method.
     */
    @Before
    public void setUp() {
        user = new User("John", "Doe", "john.doe@example.com", "1234567890");
    }

    /**
     * Tests if the User object is correctly initialized with the constructor.
     */
    @Test
    public void testUserConstructor() {
        assertEquals("John", user.getFirstname());
        assertEquals("Doe", user.getLastname());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhone());
    }

    /**
     * Tests the firstname setter and getter.
     */
    @Test
    public void testSetGetFirstname() {
        user.setFirstname("Jane");
        assertEquals("Jane", user.getFirstname());
    }

    /**
     * Tests the lastname setter and getter.
     */
    @Test
    public void testSetGetLastname() {
        user.setLastname("Smith");
        assertEquals("Smith", user.getLastname());
    }

    /**
     * Tests the email setter and getter.
     */
    @Test
    public void testSetGetEmail() {
        user.setEmail("jane.smith@example.com");
        assertEquals("jane.smith@example.com", user.getEmail());
    }

    /**
     * Tests the phone setter and getter.
     */
    @Test
    public void testSetGetPhone() {
        user.setPhone("0987654321");
        assertEquals("0987654321", user.getPhone());
    }
}

