package epicVotingSystem;

import  org.junit.Assert;
import org.junit.Test;


public class TestCandidateClass {

	
	@Test
	public void TestCandidateConstructor() {
		Candidate test = new Candidate(1, "Maranda Lawson", 99, "Cerberus");
		
		Assert.assertEquals("Maranda Lawson", test.getName());
		Assert.assertEquals(1, test.getCandidateCode() );
		Assert.assertEquals(99, test.getVotes() );
		Assert.assertEquals("Cerberus", test.getDept() );
	}
	
	@Test
	public void testCandidateObjectfieldsSetMethodsFuntionsCorrectly(){
		
		Candidate test = new Candidate();
		test.setDept("Specter");
		test.addVote();
		Assert.assertEquals("Specter", test.getDept() );
	}

}

