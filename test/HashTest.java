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
import timetrackingexam.help.Hash;

/**
 *
 * @author domin
 */
public class HashTest {
    
    public HashTest() {
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
    public void testHashPass(){
        Hash hash = new Hash();
        String hashedPass = hash.hashPass("password");
        
        assertEquals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8", hashedPass);
    }
    
    @Test
    public void testHashLongPass(){
        Hash hash = new Hash();
        String hashed = hash.hashPass("testwithpasswordlongerthanissha256hashwhichis64characterslongjustfewmore");
        
        assertEquals("0a4523924cd7247cc81a0e5a5a3663ecb23529cc250126a2b7a03b0acb720255", hashed);
    }
    
    @Test
    public void testShortPass(){
        Hash hash = new Hash();
        String hashed = hash.hashPass("a");
        
        assertEquals("ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb", hashed);
    }
}
