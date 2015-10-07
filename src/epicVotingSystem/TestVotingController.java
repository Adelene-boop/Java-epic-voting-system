package epicVotingSystem;

import org.junit.Test;

public class TestVotingController {

	@Test
	public void testVotingController() {

		VotingController test = new VotingController();
		
		test.getCandidate(1);
		test.getStaff(1);
		test.getCandidates();
		test.getStaff();
		test.recordVote();
		test.saveCandidateData();
		test.saveStaffData();
		test.getTotalVoters();
		test.helpMe();
		test.getAmountOfHelpLines();
		test.getHelpOptionsArr();
	}
}