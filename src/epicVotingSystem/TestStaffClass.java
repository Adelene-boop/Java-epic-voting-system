package epicVotingSystem;


import org.junit.Assert;
import org.junit.Test;

public class TestStaffClass {

	@Test
	public void TeststaffConstructor() {
		Staff test = new Staff(7, "John Sheppard", 1, "stopTheReapers", "2010-03-08 14:59:30.252" );
		
		Assert.assertEquals(7, test.getId() );
		Assert.assertEquals("John Sheppard", test.getName() );
		Assert.assertEquals(1, test.hasVoted() );
		Assert.assertEquals("stopTheReapers", test.getPassword() );
		Assert.assertEquals("2010-03-08 14:59:30.252", test.getTimeStampString() );
	}
	
	@Test
	public void testStaffObjectfieldsSetMethodsFuntionsCorrectly(){
		
		Staff test = new Staff();
		test.setName("Zaeed");
		test.setId(2);
		test.setPassword("Jessie");		//name of his rifle
		test.setTimeStampString("2014-05-09 11:45:13.128");
		test.setVoted();
		
		
		Assert.assertEquals("Zaeed", test.getName() );
		Assert.assertEquals( 2, test.getId() );
		Assert.assertEquals("Jessie", test.getPassword() );
		Assert.assertEquals("2014-05-09 11:45:13.128", test.getTimeStampString() );
		Assert.assertEquals(1, test.hasVoted() );
	}


}
