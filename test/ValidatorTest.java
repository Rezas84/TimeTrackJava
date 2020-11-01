/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import timetrackingexam.help.Validator;

/**
 *
 * @author domin
 */
public class ValidatorTest {

    public ValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testValidEmail() {
        boolean email = Validator.validateEmail("user@gmail.com");

        assertEquals(true, email);
    }

    @Test
    public void testWithoutTopLevelDomain() {
        boolean email = Validator.validateEmail("user@gmail");

        assertEquals(false, email);
    }

    @Test
    public void testWithoutAtSign() {
        boolean email = Validator.validateEmail("usergmail.com");

        assertEquals(false, email);
    }

    @Test
    public void testWithDKTopLevelDomain() {
        boolean email = Validator.validateEmail("user@gmail.dk");

        assertEquals(true, email);
    }

    @Test
    public void testWithoutEmailUser() {
        boolean email = Validator.validateEmail("@gmail.com");

        assertEquals(false, email);
    }

    @Test
    public void testWithoutDomain() {
        boolean email = Validator.validateEmail("user@.com");

        assertEquals(false, email);
    }

    @Test
    public void testWithSpaces() {
        boolean email = Validator.validateEmail("user @ gmail .com");

        assertEquals(false, email);
    }

    @Test
    public void testNull() {
        boolean email = Validator.validateEmail(null);

        assertEquals(false, email);
    }

    @Test
    public void testEmpty() {
        boolean email = Validator.validateEmail("");

        assertEquals(false, email);
    }
}
