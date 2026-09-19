package lab3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import balloon.Balloon;


public class BalloonTests {
	
	 private static final double EPSILON = 10e-07;
	
	@Test
    public void testInitial()
    {
      Balloon b = new Balloon(5);
      assertEquals(false, b.isPopped());
      Balloon b2 = new Balloon(-5);
      assertEquals(0, b2.getRadius());
    }

    @Test
    public void testInitialRaduis()
    {
      String msg = "A newly constructed Balloon should have the raduis 0";
      Balloon b = new Balloon(5);
      assertEquals(msg, 0, b.getRadius(), EPSILON);
    }

    @Test
    public void testPop()
    {
      Balloon b = new Balloon(5);
      b.pop();
      assertEquals(0, b.getRadius(), EPSILON);
      assertEquals(true, b.isPopped());
    }

    @Test
    public void testDeflate()
    {
      Balloon b = new Balloon(5);
      b.deflate();
      assertEquals(0, b.getRadius(), EPSILON);
      assertEquals(false, b.isPopped());
    }
    
    @Test
    public void testisPopped()
    {
    	Balloon b = new Balloon(5);
        b.pop();
        assertEquals(true, b.isPopped());
    	
    }
    
    @Test
    public void testgetRadius()
    {
    	Balloon b = new Balloon(5);
    	assertEquals(0, b.getRadius());
    }
    
    @Test
    public void testblow()
    {
    	Balloon b = new Balloon(5);
    	b.blow(3);
    	assertEquals(3, b.getRadius());
    	b.blow(10);
    	assertEquals(true, b.isPopped());
    	
    }

   
	

}
