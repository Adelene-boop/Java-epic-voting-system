package epicVotingSystem;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class TestVotingInterface {

	
	
	private static final Object[][] String = null;

	@Test
	public void testVotingInterface() {
		VotingInterface test = new VotingInterface();
		
		test.setNumberOfStaff(100);
			assertEquals(100, test.getNumberOfStaff() );
			
		test.setNumberOfCandidates(20);
			assertEquals(20, test.getNumberOfCandidates() );
		
		test.setDaysCanVote(7);
			assertEquals(7, test.getDaysCanVote() );
			
		test.setStartDateForVotesString("2014-OCT-22");
			assertEquals("2014-OCT-22", test.getStartDateForVotesString() );
			
			
		test.setDateTest(new Date() );
			assertEquals(new Date(), test.getDateTest() );
		
			
		//test validate admin
		test.validateAdmin("admin", "admin123"); //test if true
		test.validateAdmin("admin", "password"); //test if false
		
		//test help section
		System.out.println("Test help section:\nPlease input any values to test, enter \"q\" when finished ");
		
	}

}
