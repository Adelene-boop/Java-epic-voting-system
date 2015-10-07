package epicVotingSystem;

/**
 * File Name : 
 * author : 
 * Date :
 * Description :
 */
public class Candidate {

    int candidateCode = 999;//999 is not an eligible candidate
    String name = null;
    int votes = 0; //total votes
    String dept;
    
    //constructor

    
    public Candidate(int candidateCode, String name, int votes, String dept)
    {
        this.candidateCode = candidateCode;
        this.name = name;
        this.votes = votes;
        this.dept = dept;
    }
    
   public Candidate() {
		// TODO Auto-generated constructor stub
	}

/** public Candidate(){
    	
    }**/
    

    public int getCandidateCode ()
    {
        return candidateCode;
    }

    public String getName()
    {
        return  name;
    }

    public int getVotes()
    {
        return  votes;
    }

    public void addVote()
    {
        votes++;
    }

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

}
