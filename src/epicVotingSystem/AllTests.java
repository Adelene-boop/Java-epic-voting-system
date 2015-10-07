package epicVotingSystem;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCandidateClass.class, TestStaffClass.class,
		TestVotingController.class, TestVotingInterface.class })
public class AllTests {

}
