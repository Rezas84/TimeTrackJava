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
import timetrackingexam.be.LoggedUser;

/**
 *
 * @author domin
 */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoggedUserTest {

    public LoggedUserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        LoggedUser.init(1, "username", "email@gmail.com", 1);
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
    public void testId() {
        LoggedUser user = LoggedUser.getInstance();
        assertEquals(1, user.id);
    }

    @Test
    public void testName() {
        LoggedUser user = LoggedUser.getInstance();
        assertEquals("username", user.name);
    }

    @Test
    public void testEmail() {
        LoggedUser user = LoggedUser.getInstance();
        assertEquals("email@gmail.com", user.email);
    }

    @Test
    public void testRights() {
        LoggedUser user = LoggedUser.getInstance();
        assertEquals(1, user.rights);
    }
    
    @Test
    public void testInstance(){
        LoggedUser i1 = LoggedUser.getInstance();
        LoggedUser i2 = LoggedUser.getInstance();
        assertEquals(i1, i2);
    }

    @Test(expected = AssertionError.class)
    public void initializeAfterInitialized() {
        LoggedUser.init(1, "username", "email@gmail.com", 1);
    }

    @Test(expected = AssertionError.class)
    public void testRemoveInstance() {
        LoggedUser.removeInstance();
        LoggedUser user = LoggedUser.getInstance();
    }
}
