package epicVotingSystem;

import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * File Name :
 * author : 21300006
 * Date :
 * Description :
 */

public class VotingInterface
{
	//Global private variables
	private VotingController vc;
	private Staff theStaff;
	private Candidate theCandidate;

	private final String USERNAME = "admin";
	private final String PASSWORD ="admin123";

	private int numberOfStaff = 0;
	private int numberOfCandidates = 0;

	private String startDateForVotesString = null;
	private int daysCanVote = 7;
	private Date dateTest;
	
	private BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
	
	//program start
	public static void main(String[] args)
	{
		//run start() void
		VotingInterface vi = new VotingInterface();		//vi is the location votingInterface.java
		vi.start();
	}

	public void start()
	{
		vc = new VotingController();	//vi is the location votingController.java
		commenceVoting(); 				//run commenceVoting()
	}

/**Main screen/ menu**/
	public void commenceVoting()
	{
		boolean systemQuit = false;
		while (!systemQuit)
		{
			String input = null;
			System.out.println("\n\t\t============== eVoting System =====================\n\n");
			System.out.print("Enter \"v\" to Vote as staff \nOR \"a\" to login in as system administrator \nOR \"h\" for help: ");
			input = getInput();

			if (input.equalsIgnoreCase("V") )		//if user enters "V" they choose to vote
			{
				timeRange();							//check the date
			}
			else if (input.equalsIgnoreCase("A") )	//if user enters "A" they choose to go to the admin menu
			{
				validateAdmin();					//validate admin
				//systemQuit = manageAdmin();
			}
			else if (input.equalsIgnoreCase("h") ){		//new (added for iteration 1)
														//if user enters "H" they choose to go to the help menu
				displayHelp();							//display help menu
			}
			else
			{
				System.out.println("Your input was not recognised");		//to trap errors
			}
		}
	}

	//screen input reader
	private String getInput()
	{
		String theInput = "";

		try
		{
			theInput = in.readLine().trim();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		return theInput;
	}
	
/**Staff login, before voting**/
	public void manageVote()
	{

		boolean moveOn = false; 
		int count = 0;
		//loop for each voter
		while (moveOn == false)
		{
			System.out.print("Please enter your staff ID or \"q\" to quit:");
			String input =getInput();				//get the input from the user
			count++;
			
			if (count > 2)		//if tried more than 3 go back to the home screen
			{
				moveOn = true;
				System.out.println("\nYou have entered an incorrect ID too many times");
			}
			else if(input.equalsIgnoreCase("q") )		//if staff chooses to quit trying to login
			{
				moveOn = true;
			}
			else			//if the amount of login attempts is less than 3 and user didn't choose to quit
			{
				try
				{
					theStaff = vc.getStaff(Integer.parseInt(input) );
					if(theStaff.hasVoted() == 1)							//check to see if staff have already voted
					{
						System.out.println("\nYou have voted and cannot vote again\nGood bye...!");
						moveOn = true;
					}
					else if (theStaff.hasVoted() == 0)		//if staff haven't voted, proceed get the password from the user and check to see if its correct
					{
						checkPassword();
						moveOn = true;
					}
					else			//to trap possible errors
					{
						System.out.println("There seems to be a problem.  Contact your administrator");
					}
				}
				catch(NumberFormatException e)		//if not a number
				{
					System.out.println("Invalid entry - you must enter a number\nPlease try again");
				}
				catch(NullPointerException e)		//if empty
				{
					System.out.println("Error, not found");
				}
			}			
		}
		System.out.print("going back to main screen...");	//when loop is exited with out a valid staff ID 
	}														//(when option to quit is chosen or the user entered the wrong ID to many times)

	//checks staff password 
	private void checkPassword(){

		int passAttemptCount = 0;

		String input = null;

		String thePassword = theStaff.getPassword();			//get the password from the staff array list
		System.out.print("Please enter your password or \"q\" to quit: ");

		//enter staff password
		input = getInput();

		//checks to see if password does not match
		while(!input.equals(thePassword) )		//while password doesn't match
		{

			passAttemptCount++;      	 

			if(input.equalsIgnoreCase("q")  ){		//if quit is chosen
				commenceVoting();					//call the code for the main screen, to put them back to the start
			}
			else if(passAttemptCount > 2){			//if the user entered the password wrong more than 3 times
				System.out.println("\nYou have entered the wrong password three times\nGoing back to main screen...");
				commenceVoting();			//call the code for the main screen, to put them back to the start
			}
			else{
				System.out.print("\nYour password was incorrect\nPlease enter your password again or \"q\" to quit: ");
			}
			input = getInput();

		}
		getStaffVote();			//once password matches get the staff's vote
	}
	
	//manages staff vote
	private void getStaffVote()
	{
		int candidateCode;
		boolean retry = true;

		displayVotingScreen();		//display the candidates the staff can vote for

		while (retry)
		{
			System.out.print("\n\nEnter your candidate's code OR enter Q to quit voting : ");
			try{
				String input = getInput();

				if (input.equalsIgnoreCase("Q") )		//if user chooses to quit exit without voting for anyone
				{
					retry = false;
				}
				else									//else get the users vote
				{
					candidateCode = Integer.parseInt(input);		//try to set the users input to a number (from a String)
					theCandidate = vc.getCandidate(candidateCode);	//try to get the the candidate from the users input
					System.out.print("\nYou have selected " + theCandidate.getName()+ ". \n\nEnter  Y to confirm or any other key to Cancel, then press ENTER : ");

					if (getInput().equalsIgnoreCase("y"))
					{
						vc.recordVote();		//save the users vote (including the staffs time stamp, vote status to true and +1 to the candidates votes)
						System.out.println("\n\nThanks for voting " + theStaff.getName() + ". Bye!!!");
						retry = false;
					}
					else{
						displayVotingScreen();		//if the staff cancels their selection show them the candidates again and don't leave the loop
					}
				}
			}
			catch(NumberFormatException e)		//not a number exception
			{
				System.out.println("That was not a number you entered\nPlease try again");
			}
			catch(NullPointerException e)		//empty exception
			{
				System.out.println("This candidate code does not exit\nPlease try again");
			}
			catch(Exception e){					//General exception
				System.out.println("We have a problem, please contact your administrator");
			}
		}
	}
	
/**display the text for the voting selection**/	
	public void displayVotingScreen()
	{

		System.out.println("\nWelcome "+ theStaff.getName()+"!\n");
		setNumberOfCandidates(0);

		ArrayList<?> candidates = vc.getCandidates();		//get all the candidates from the voting controller

		Iterator<?> it = candidates.iterator();
		System.out.println("\tCode\tCandidate Name\tDepartment");
		System.out.println("\t====\t==============\t==========\n");
		
		//displays all the candidates
		while(it.hasNext() )		//while there is another candidate to display
		{   
			theCandidate = (Candidate)it.next();
			System.out.println("\t" + theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t" + theCandidate.getDept() );
			setNumberOfCandidates(getNumberOfCandidates() + 1);		//increment the number of candidates (private variable) by 1
		}
	}


/**Validate the admin login**/
	private void validateAdmin()
	{
		boolean adminQuit = false;
		int count = 0;

		while (!adminQuit)
		{
			count++;
			System.out.print("\nYou have entered Administration space. \nEnter username or \"Q\" to quit : " );
			String input = getInput();
			
			if (input.equalsIgnoreCase("q") ) //if user chooses to quit exits the loop, which means the user goes back to the main screen
			{ 
				adminQuit = true;
			}
			else
			{

				String username, password = null;
				
				username = input.trim();
				username.toLowerCase();						//sets the user-name to lower-case
															//user can input capital letters and it wont effect the login (user name is now not case sensitive)
				System.out.print("Please enter password: ");	
				password = getInput().trim();

				if(validateAdmin(username, password) ) 	//calls the other validateAdmin and passes the users input (password and user-name)
				{ 										//if comes back true
					adminQuit = true;	
					manageAdmin();					//go to the admin menu
				}
				else if(count > 2){				//if login failed more than 3 times, exit loop (user goes back to the main screen)
					adminQuit = true;
				}
				else		//else the password and/or user-name doesn't match
				{
					System.out.println("Incorrect username/password.");
				}
			}
		}
	}
	
	//validates administrator user name & password
	public boolean validateAdmin(String username, String password)
	{
		if(	username.equalsIgnoreCase(USERNAME) && ( password.equals(PASSWORD) )	)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

/**Admin menu**/
	private boolean manageAdmin()
	{
		boolean adminQuit = false;
		boolean systemQuit = false;

		while (!adminQuit){

			//Display all the options available to the admin
			System.out.println("\nOptions");

			System.out.print("To end voting enter \"Stop\" \n"
					+ "To view the voting results and stats enter \"R\" (including the option to see which staff has/has not voted)\n"
					+ "To view/ delete / add staff enter \"S\"\n"
					+ "To view/ delete / add candidates enter \"C\"\n"
					+ "To view/ change the days where users can vote enter \"D\" \n"
					+ "To quit, back to the main menu, enter \"Q\":\n");
			
			String input = getInput();

			switch(input.toLowerCase() )	//solve problem where capital letters are ignored
			{
				//if user chose to quit
				case "q": input.equalsIgnoreCase("q");
						System.out.println("Going back to main menu...");			
						adminQuit = true;
				break;
				
				//if user chose to stop the voting system
				case "stop": input.equalsIgnoreCase("stop");
						//stop system and go to back to the main screen
						adminQuit = true;
						systemQuit = true;
						
						setStartDateForVotesString(null);
						System.out.println("Voting System Closed, by setting the days staff can vote to zero\n"
								+ "If you want to re-open the voting system, you need to set a new/ reset thet date range and start date (in the admin menu)");
						break;
				
				//if user chose to see the voting results 
				case "r": input.equalsIgnoreCase("r");
						
						printVoteResults();			//go to results screen
				break;
				
				//if user chose to go to the staff section
				case "s": input.equalsIgnoreCase("s");
						//go to staff editing
						displayAdminStaff();
				break;
			
				//if user chose to go to the candidate section
				case "c": input.equalsIgnoreCase("c");
						//go to candidate editing
						displayAdminCandidates();
				break;
			
				//if user chose to see the time range dates and have to option to change them
				case "d": input.equalsIgnoreCase("d");
						//go to date range editor
						canChangeTimeToVote();
				break;
				
				//trap errors, if input isn't recognized program shows the admin menu again
				default: System.out.println("Your input was not recognised");
			}
		}
		return systemQuit;
	}


	//prints out the voting results
	public void printVoteResults()
	{
		ArrayList<?> candidates = vc.getCandidates();		//get the candidates from the voting controller
		int totalVoters = vc.getTotalVoters();				//get the total amount of votes from the voting controller
		double totalVoted = 0;
		int candidateVotes = 0;


		//formatting display
		DecimalFormat df = new DecimalFormat("###.##");

		Iterator<?> it = candidates.iterator();
		System.out.println("\n\t\t VOTING STATISTICS");
		System.out.println("\t\t=========================\n");
		System.out.println("Code\tName\t\t\tVotes\t(Vote%)");
		System.out.println("____\t____\t\t\t_____\t______\n");

		//calculate total voted
		while(it.hasNext() ) 		//while there is another candidate
		{
			theCandidate = (Candidate) it.next();
			totalVoted += theCandidate.getVotes();		// count total votes for this candidate
		}

		it = candidates.iterator();
		
		while(it.hasNext() ) //while there is another candidate
		{
			theCandidate = (Candidate) it.next();
			candidateVotes = theCandidate.getVotes();		//get the counted votes
			System.out.println(theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t\t" +
				candidateVotes +"\t(" + df.format((candidateVotes/totalVoted)*100) +"%)");
		}

		System.out.println("\nNumbers on voting list: " + totalVoters);
		System.out.println("Numbers voted: " + totalVoted + "(" + df.format((totalVoted/totalVoters)*100)  + "%)\n");
		
		System.out.print("To view the staff who HAVE voted enter \"H\" or To view staff who have NOT voted enter \"N\" \n"
				+ "or Press any letter/ number/ symbol to go back to the admin menu: ");
				
		
		String input = getInput();
		
		if(input.equalsIgnoreCase("h") )
		{
			//GO DISPLAY STAFF WHO HAVE VOTED
			viewStaffWhoVoted();
		}
		else if(input.equalsIgnoreCase("n") )
		{
			//GO DISPLAY STAFF WHO HAVE NOT VOTED
			viewStaffWhoHaventVoted();
		}
		else	//if input isn't n or h put the user back to the admin menu
		{
			System.out.println("...going back to admin menu ");
		}
	}
	
	//Display staff who have voted
	public void viewStaffWhoVoted() 
	{
		setNumberOfStaff(0);
		ArrayList<?> staffs = vc.getStaff();		//get staff from voting controller

		Iterator<?> it = staffs.iterator();
		System.out.println("\n\t***Staff who have voted***\n(If the list is empty then all staff have not voted)\n");
		System.out.println("\tID\tStaff Name\t\tTimestamp");
		System.out.println("\t==\t==========\t\t=========\n");
		
		while(it.hasNext() )		//while there is another staff member 
		{   
			theStaff = (Staff)it.next();
			
			
			if(theStaff.hasVoted() == 1)			//if staff has voted status set to voted
			{
				System.out.println("\t" + theStaff.getId() + "\t" + theStaff.getName() + "\t\t" + theStaff.getTimeStampString() );
			}
			else
			{
					
			}
			
			setNumberOfStaff(getNumberOfStaff() + 1);		//add 1 to the total number of staff
		}
			
		System.out.print("\nTo view staff who have NOT voted enter \"N\" or Press any letter/ number/ symbol to go back to the admin menu: ");
		String input = getInput();
			
		if(input.equalsIgnoreCase("n") )
		{
			//GO DISPLAY STAFF WHO HAVE NOT VOTED
			viewStaffWhoHaventVoted();
		}
		else
		{
			System.out.println("...going back to admin menu ");
		}
	}
		
	public void viewStaffWhoHaventVoted()
	{
		setNumberOfStaff(0);
		ArrayList<?> staffs = vc.getStaff();		//get staff from the voting controller

		Iterator<?> it = staffs.iterator();
			
		System.out.println("\n\t***Staff who have NOT voted***\n(If list is empty then all staff have already voted)\n");
		System.out.println("\tID\tStaff Name");
		System.out.println("\t==\t==========\n");
			
		while(it.hasNext() )	//while there is another staff member 
		{   
			theStaff = (Staff)it.next();
			
			if(theStaff.hasVoted() == 0)		//if staff has voted status set to not voted
			{
				System.out.println("\t" + theStaff.getId() + "\t" + theStaff.getName() );
			}

			setNumberOfStaff(getNumberOfStaff() + 1);	//add 1 to the total number of staff
		}
			
		System.out.print("\nTo view staff who have voted enter \"H\" or Press any letter/ number/ symbol to go back to the admin menu: ");
		String input = getInput();
			
		if(input.equalsIgnoreCase("h") )
		{
			//GO DISPLAY STAFF WHO HAVE VOTED
			viewStaffWhoVoted();
		}
		else
		{
			System.out.println("...going back to admin menu ");
		}
		
	}


/**admin candidate section**************************************************************************************/

	public void displayAdminCandidates()
	{
		setNumberOfCandidates(0);

		ArrayList<?> candidates = vc.getCandidates();		//get candidates from the voting controller

		Iterator<?> it = candidates.iterator();
		System.out.println("\tID\tCandidate Name\tDepartment");
		System.out.println("\t====\t==============\t==========\n");
		
		while(it.hasNext() )		//keep going untill there is no more candidates to display
		{   	
			theCandidate = (Candidate)it.next();
			System.out.println("\t" + theCandidate.getCandidateCode() + "\t" + theCandidate.getName() + "\t" + theCandidate.getDept() );
			setNumberOfCandidates(getNumberOfCandidates() + 1);
		}
		
		System.out.print("\nDo you want to add enter \"A\" or do you want to delete enter \"D\" (\"Q\" to quit): ");
		String input = getInput();

		if(input.equalsIgnoreCase("a") )		//if user selects add
		{
			//go to add candidate section
			addAdminCandidate();
		}
		else if(input.equalsIgnoreCase("d") )	//if user selects delete
		{
			//go to delete candidate section
			deleteAdminCandidate();
		}
		else if (input.equalsIgnoreCase("q") )	//if user selects quit
		{
			//puts them back to the admin menu
		}
		else									//to trap any invalid input, puts user back to the admin menu anyway
		{ 
			System.out.println("Sorry but your input was not recognised.");
		}

		System.out.println("...going back to previous screen");
	}

	public void addAdminCandidate()
	{
		ArrayList<Candidate> candidates = vc.getCandidates();		//get candidates from the voting controller
		boolean again = false;

		do 
		{
			int candidateCode = 0;		//Default for candidateCode is 0
			String name; 
			String dept;
			boolean validID = false;

			//User input, for the new Staff member
			System.out.print("Please enter a new candidate code: ");
			String input = getInput();
			boolean isNumeric = false;

			while (validID == false)		//while the candidate code is invalid
			{

				try  									//help from http://stackoverflow.com/a/1102916
				{  
					candidateCode = Integer.parseInt(input); 
					isNumeric = true;
				}  
				catch(NumberFormatException nfe)  
				{  
					isNumeric = false;  
				}  


				if(input.equalsIgnoreCase("q") )		//if user chooses to quit
				{
					System.out.println("Going back to previous screen...");
					displayAdminStaff();				//go back and show the list of staff
				}
				else if( isNumeric)					//if the user input is actually a number
				{
					theCandidate = vc.getCandidate(candidateCode); //try to get the candidate data from the voting controllers array list

					//if candidate code doesn't already exist
					if ( vc.getCandidate(candidateCode) == null ) 
					{
						validID = true;
					}
					else		//if the candidate code already exists
					{
						validID = false;
						System.out.println("\nThis candidate code already exists...");
						System.out.print("Please enter a new candidate code: ");
						input = getInput();					//make the user try again, used for error checking, so there can be no added duplicate values
					}

				}
				else		//if the user input was invalid, for error checking
				{
					System.out.println("Sorry but your input was not recognised");
					System.out.print("Please enter a new candidate code: ");
					input = getInput();		//make the user try again
				}

			}
			
			//if made it out of loop the candidate code is a number and doesn't exist
			
			candidateCode = Integer.parseInt(input);		//set the users last input (that has been validated) as the numeric value for candiadateCode


			boolean nameCheck = false;
			
			while(!nameCheck )		//while the name inputed by the user is invalid
			{
				System.out.print("\nPlease enter the name of the new candidate \n(first name  then last name e.g. John Doe): ");         	
				input = getInput();
				
				if (input.length() <= 3)	//must be greater than 3 charters in length
				{	
					System.out.println("\nPeoples names should have more than three charactors (in their two names, in total) in them, try again");
				}							//help from http://stackoverflow.com/a/8248376
				else if (input.matches(".*\\d.*") || input.contains("^.*[^a-zA-Z0-9 ].*$")) //Matches numbers or symbols
				{  
					System.out.println("\nPeoples names should not have numbers or symbols other than \"-\" in them, try again");
				}
				else if(!input.contains(" ") ) //if there is no space between the names
				{
					System.out.println("\nPeoples names should have a space between first name and last, try again");
				}
				else if( !input.contains("^.*[^a-zA-Z0-9 ].*$") && input.contains("-")  && input.contains(" ") ) //name has no symbols or numbers but does contain a "-" and has a space
				{   
					nameCheck = true; 
				}
				else if( (!input.matches("^.*[^a-zA-Z0-9 ].*$") )&& input.contains(" ") )	//name has no symbols or numbers and has a space
				{ 
					nameCheck = true; 
				}
				else		//to minimize errors
				{
					System.out.println("Sorry an error occurred, try again");
				}
			
			}

			name = input;

			boolean containingComma = false;
			
			while(!containingComma)		//to make sure that there is no extra commas in the text file
			{
				System.out.print("Please enter a department for the new candidate: ");
				input = getInput();
				
				if(input.contains(",") )		//if contains a comma
				{
					System.out.println("NO COMMAS! You will corrupt the data");
				}
				else if(input.length() < 1 )		//if blank
				{
					System.out.println("Department shouldn't be blank");
				}
				else{
					containingComma = true;
				}
			}
			dept = input;

			//add data to the array list
			theCandidate = new Candidate(candidateCode, name, 0, dept);
			candidates.add(theCandidate);

			//save the data to the text file
			vc.saveCandidateData();		//save the candidates arrayList to the text file (done on the controller)
			System.out.print("***The new candidate has been saved***\nIf you want to exit enter \"q\" or If you want to add another new candidate hit enter: ");
			
			input = getInput();

			if(input.equalsIgnoreCase("q") )
			{
				again = false;
				System.out.println("...Exiting");
			}
			else
			{
				again = true;
			}
		}while(again == true);		//does once no matter what, if user doesn't select the option to quit, it makes them add another candidate
	}

	public void deleteAdminCandidate()
	{
		ArrayList<Candidate> candidates = vc.getCandidates();	//get the candidates from the voting controller
		boolean again = true;

		do  
		{
			int candidateCode = 0;
			boolean validID = false;
				
			//User input, for the new candidate
			System.out.print("Please enter the code of the candidate you want to delete or \"Q\" to quit: ");
			String input = getInput();
			boolean isNumeric = false;

			while (validID == false)		//check to see if its a number
			{
					
				try  									//help from http://stackoverflow.com/a/1102916
				{  
					candidateCode = Integer.parseInt(input); 
					isNumeric = true;
				}  
				catch(NumberFormatException nfe)  
				{  
					isNumeric = false;  
				}  


				if(input.equalsIgnoreCase("q") )		//if user chooses to quit
				{
					System.out.println("Going back to previous screen...");
					validID = true;
					again = false;
				}
				else if( isNumeric)		//if its a number 
				{
					theCandidate = vc.getCandidate(candidateCode); 	//try to find the candidate by the user inputed candidateCode

					//if candidate code does not exist
					if ( vc.getCandidate(candidateCode) == null ) 
					{
						validID = false;
						System.out.println("\nThis candidate code does not exists...");
						System.out.print("Please enter a candidate code that does or \"Q\" to quit: ");
						input = getInput();
					}
					else		//else it must exist, find and delete
					{
						validID = true;
						candidateCode = Integer.parseInt(input);

						//Find the position of the array in the array list
						int code = 0;
						int count = 0;
						boolean keepGoing = true;
						Iterator<?> it = candidates.iterator();

						while(it.hasNext() && keepGoing)		//while there is another candidate and the user inputed candidate is not found
						{	
							theCandidate = (Candidate)it.next();
							code = theCandidate.getCandidateCode();
						
							if(code == candidateCode)		//if candidate found on arrayList
							{
								keepGoing = false;
							}
							else		//else keep going, and increment count by 1
							{
								count++;
							}
							setNumberOfCandidates(getNumberOfCandidates() + 1);
						}
						//when loop is finished it has found the position of the candidate that the user wants to delete	
						
						System.out.println("\nAre you sure you want to delete \n" + theCandidate.getName() + ", from " + theCandidate.getDept() + "(candidate code: " + theCandidate.getCandidateCode() + ")");
						System.out.print("Enter \"Y\" for yes, Enter any other charactor for no: ");
						input = getInput();
								
						//check to see if user really wants to kill the candidate
						if (input.equalsIgnoreCase("y") )
						{
							candidates.remove(count);	//remove from arrayList
							
							//save the modified arrayList back to the text file
							vc.saveCandidateData();
							System.out.println("\n***The candidate has been deleted***");
						}
						else		//user must not want to remove the candidate
						{
								
						}
										
						System.out.print("\nIf you want to exit enter \"q\" or If you want to delete another candidate hit enter: ");
						input = getInput();

						if(input.equalsIgnoreCase("q") )	//if the user choses to quit
						{
							again = false;
							System.out.println("...Exiting");
						}
						else		//user must want to delete another one
						{
							again = true;
						}
					}

				}
				else		//error checking, if user didn't even enter a valid number
				{
					System.out.println("Sorry but your input was not recognised");
					System.out.print("Please enter a candidate code, that exists: ");
					input = getInput();
					validID = false;
				}

			} //end of validID loop
				
		}while(again == true);//end of again loop


	}



/**admin staff section********************************************************************************************************/

	public void displayAdminStaff()
	{
		setNumberOfStaff(0);
		ArrayList<?> staffs = vc.getStaff();		//get staff from voting controller

		Iterator<?> it = staffs.iterator();
		
		System.out.println("\tID\tStaff Name\t\tPassword");	//have decided to show the admin the staffs password, so when the staff forget the admin can tell them their password
		System.out.println("\t==\t==========\t\t========\n");
		
		while(it.hasNext() )	//while there is another staff member
		{   
			theStaff = (Staff)it.next();
			System.out.println("\t" + theStaff.getId() + "\t" + theStaff.getName() + "\t\t" + theStaff.getPassword() );
			setNumberOfStaff(getNumberOfStaff() + 1);
		}
		//inform user about what they can do with the passwords they can see
		System.out.println("\nNotice: the staffs passwords are private and the sole reason you can see them is so you can remind staff of their password if they ask for it\n***Only the staff who asked for their password, not other staffs passwords.***\n***You cannot use other staffs logins to vote for them***");

		System.out.println("\nDo you want to add enter \"A\" or do you want to delete enter \"D\" (\"Q\" to quit)");
		String input =getInput();


		if(input.equalsIgnoreCase("a") )		//user selected to add a staff member
		{
			//go to add staff section
			adminAddStaffcheck();
		}
		else if(input.equalsIgnoreCase("d") )		//user selected to delete a staff member
		{
			//go to delete staff section
			deleteAdminStaff();
		}
		else if (input.equalsIgnoreCase("q") )		//user chose to quit
		{
			
		}
		else		//error checking, if user didn't select any of the options, send them back to the admin menu anyway
		{ 
			System.out.println("Sorry but your input was not recognised");
		}

	}

	public void adminAddStaffcheck()
	{
		ArrayList<Staff> staffs = vc.getStaff();		//get the staff form the voting controller
		boolean again = false;

		do 		//add multiple staff with out going back to the admin menu
		{
			int id = 0;		//id is set to 0 by default
			String name; 
			String password;
			boolean validID = false;

			//User input, for the new Staff member
			System.out.print("Please enter a new staff ID: ");
			String input = getInput();
			boolean isNumeric = false;

			while (validID == false)		//while the ID is invalid
			{
				//check to see if its a number
				try  									//help from http://stackoverflow.com/a/1102916
				{  
					id = Integer.parseInt(input); 
					isNumeric = true;
				}  
				catch(NumberFormatException nfe)  
				{  
					isNumeric = false;  
				}  


				if(input.equalsIgnoreCase("q") ) //quit option
				{
					System.out.println("Going back to previous screen...");
					displayAdminStaff();
				}
				else if( isNumeric) 		//if its a number
				{
					theStaff = vc.getStaff(id); 

					//if ID doesn't already exist
					if ( vc.getStaff(id ) == null )
					{
						validID = true;
					}
					else //staff ID does exist
					{
						validID = false;
						System.out.println("\nThis ID already exists...");
						System.out.print("Please enter a new staff ID: ");
						input = getInput();
					}

				}
				else		//else ask again, traps invalid input
				{
					System.out.println("Sorry but your input was not recognised");
					System.out.print("Please enter a new staff ID: ");
					input = getInput();
				}

			}		//user only leaves the loop when they enter a staff ID that doesn't already exist
					//this means there cannot be duplicate staff IDs entered by the user, 
					//helps maintain integrity of the staff text file

			id = Integer.parseInt(input);		//sets the validated user input as the new ID number

			
			boolean nameCheck = false;
			
			while(!nameCheck )		//while the name is invalid
			{
				System.out.print("Please enter the name of the new staff member \n(first name  then last name e.g. John Doe): ");         	
				input = getInput();
				
				//must be greater than 3 charters in length
				if (input.length() <= 3)
				{
					System.out.println("\nPeoples names should have more than three charactors (in their two names, in total) in them, try again");
				}
				//Matches numbers or symbols
				else if (input.matches(".*\\d.*") || input.contains("^.*[^a-zA-Z0-9 ].*$"))
				{  
					System.out.println("\nPeoples names should not have numbers or symbols other than \"-\" in them, try again");
				}
				else if(!input.contains(" ") )  //if does not contain a space
				{
					System.out.println("\nPeoples names should have a space between first name and last, try again");
				}
				//name has no symbols or numbers but does contain a "-" and has a space
				else if( !input.contains("^.*[^a-zA-Z0-9 ].*$") && input.contains("-")  && input.contains(" ") )
				{
					nameCheck = true; 
				}
				//name has no symbols or numbers and has a space
				else if( (!input.matches("^.*[^a-zA-Z0-9 ].*$") )&& input.contains(" ") )
				{ 
					nameCheck = true; 
				}
				else	//error trapping
				{
					System.out.println("Sorry an error occurred, try again");
				}
			
			}

			name = input;
			
			boolean containingComma = false;
			
			while(!containingComma)		//to make sure that there is no extra comma in the text file
			{
				System.out.print("Please enter a password for the new staff member: ");
				input = getInput();
				
				if(input.contains(",") )		//if contains a comma
				{
					System.out.println("NO COMMAS! You will corrupt the saved data");
				}
				else if(input.length() <= 3 )	//if user tries to set a password smaller than 3 charactors long
				{
					
					System.out.println("Password shouldn't be less than 3 charactors long");
				}
				else
				{
					containingComma = true;
				}
			}
			
			password = input;		//set validated user inputed password as the password
			
			//add data to the array list
			theStaff = new Staff(id, name, 0, password, null);
			staffs.add(theStaff);

			//save the data to the text file, via the voting controller
			vc.saveStaffData();
			
			System.out.print("***The new Staff member has been saved***\nIf you want to exit enter \"q\" or If you want to add another new member hit enter: ");
			input = getInput();

			if(input.equalsIgnoreCase("q") )	//if user chooses to quit
			{
				again = false;
				System.out.println("...Exiting");
			}
			else		//else they must want to add another staff member
			{
				again = true;
			}
		}while(again == true);
	}



	public void deleteAdminStaff()
	{
		ArrayList<Staff> staffs = vc.getStaff();	//get the staff list form the voting controller
		boolean again = true;

		do  //do the loop at least once
		{
			int id = 0;
			boolean validID = false;
				
			//User input, for the new candidate
			System.out.print("Please enter the Staff ID of the stafe member you want to delete or \"Q\" to quit: ");
			String input = getInput();
			
			boolean isNumeric = false;

			while (validID == false)
			{
				//see if the users input is a number
				try  									//help from http://stackoverflow.com/a/1102916
				{  
					id = Integer.parseInt(input); 
					isNumeric = true;
				}  
				catch(NumberFormatException nfe)  
				{  
					isNumeric = false;  
				}  


				if(input.equalsIgnoreCase("q") )		//if user chose to quit without deleting any staff
				{
					System.out.println("Going back to previous screen...");
					validID = true;
					again = false;
				}
				else if( isNumeric)	//if users input is in fact a number
				{
					theStaff = vc.getStaff(id); 	//try to find the staff (by their ID) in the arrayList of staff

					//if staff code does not exist
					if ( vc.getStaff(id) == null ) 
					{
						validID = false;
						System.out.println("\nThis Staff ID does not exists...");
						System.out.print("Please enter a Staff ID that does or \"Q\" to quit: ");
						input = getInput();
					}
					else	//the staff member must exist
					{
						validID = true;
						id = Integer.parseInt(input);

						//Find the position of the array in the array list
						int code = 0;
						int count = 0;
						boolean keepGoing = true;
						
						Iterator<?> it = staffs.iterator();
						
						while(it.hasNext() && keepGoing)	//while there is another staff member in the array list
						{	
							theStaff = (Staff)it.next();
							code = theStaff.getId();
						
							if(code == id)		//when staff ID is found exit the loop, without incrementing the counter by 1
							{
								keepGoing = false;
							}
							else		//if not this staff member then add 1 to the counter
							{
								count++;
							}
							setNumberOfStaff(getNumberOfStaff() + 1);
						}
								
						System.out.println("\nAre you sure you want to delete \n" + theStaff.getName() + "(Staff ID: " + theStaff.getId() + ")");
						System.out.print("Enter \"Y\" for yes, Enter any other charactor for no: ");
						input = getInput();
								
						//check to see if user wants to kill;
						if (input.equalsIgnoreCase("y") )
						{
							staffs.remove(count);	//remove from arrayList
							
							//save the modified arrayList back to the text file
							vc.saveStaffData();
							System.out.println("\n***The Staff member has been deleted***");
						}
						else		//the user must not want to delete the staff member
						{
							System.out.println("\nDeletion is canceled");	
						}
											
						System.out.print("\nIf you want to exit enter \"q\" or If you want to delete another Staff member hit enter or any other charactor: ");
						input = getInput();

						if(input.equalsIgnoreCase("q") )		//if user choose to quit
						{
							again = false;
							System.out.println("...Exiting");
						}
						else		//user doesn't want to quit, so they must want to delete another staff member
						{
							again = true;
						}
					}

				}
				else		//error trapping, user didn't even enter a number for the staff ID they wanted to delete
				{
					System.out.println("Sorry but your input was not recognised");
					System.out.print("Please enter a Staff ID, that exists: ");
					
					input = getInput();		//ask them again and see if they can enter a number this time
					validID = false;
				}
			} //end of validID loop
					
		}while(again == true);//end of again loop
	}


/**Date ballot is open section**/	
	public void timeRange(){
		//DATE RANGE
		java.util.Date date= new java.util.Date();				

		SimpleDateFormat theNewDateFormat = new SimpleDateFormat("yyyy-MMM-dd");		//format of date e.g. 2014-jan-09

		String currentDateString = theNewDateFormat.format(date.getTime() ); //get the current date	

		String startDateString = getStartDateForVotesString();		//get the private start date and days that the user has to vote
		int dateRange = getDaysCanVote();

		if(getStartDateForVotesString() == null)	//if admin has shut down/ hasn't opened voting yet
		{
			System.out.println("I am sorry but the system administrator has stopped voting or hasnt opened voting yet"
							+ "(Admin must login to the admin menu and set/ re-set the start date)");
			
		}
		else	//continue admin hasn't shutdown voting or hasn't set the start date yet
		{
			//Calculate end date										help from http://stackoverflow.com/a/428966
			Calendar calendarVar = Calendar.getInstance();
			try
			{
				calendarVar.setTime(theNewDateFormat.parse(startDateString));
			}
			catch (ParseException e1)
			{
				System.out.println("Error: Cannot set the start date from the saved string, to calculate the current end date");
			}
		
			calendarVar.add(Calendar.DATE, dateRange);  // number of days to add
			String endDateString = theNewDateFormat.format(calendarVar.getTime() );  //the new end date


			//convert to dates to easily see if the current date is in the specified time frame
			Date currentDate = null;		//make sure that the string is empty
			try
			{
				currentDate = new SimpleDateFormat("yyyy-MMM-dd").parse(currentDateString);
			}
			catch (ParseException e)
			{
				System.out.println("Error: Cannot set the current date from the saved string");
			}

			Date startDate = null;		//make sure that the string is empty
			try
			{
				startDate = new SimpleDateFormat("yyyy-MMM-dd").parse(startDateString);
			}
			catch (ParseException e)
			{
				System.out.println("Error: Cannot set the start date from the saved string");
			}

			Date endDate = null;		//make sure that the string is empty
			try
			{
				endDate = new SimpleDateFormat("yyyy-MMM-dd").parse(endDateString);
			}
			catch (ParseException e)
			{
			System.out.println("Error: Cannot set the end date from the saved string");
			}

			//checks to see if date is within range
			if(!currentDate.after(endDate) && !currentDate.before(startDate) )
			{
				manageVote();		//allow the user to proceed in going to the voting screen
			}
			else if(currentDate == null || startDate == null || endDate == null)	//if the dates couldn't be set for some reason
			{
				System.out.println("Error dates could not be set correctly");
			}
			else		//date must be out of range
			{
				System.out.println("I am sorry the current date: " + currentDateString + " is out of the date range. "
					+ "\n(You can only vote between: " + startDateString + " and " + endDateString + ")\ngoing back to main screen...");
			}
		}
	}

//displays the current date the ballot is open, the number of days is open and calculates the end date. then asks the user if they want to change these dates 
	public void canChangeTimeToVote()
	{
		if(getStartDateForVotesString() == null)	//if there hasn't been a start date set yet
		{
			System.out.println("Currently there is no start date");
		}
		else		//there was a previous start date set
		{
			int currentDateRange = getDaysCanVote();					//get the previous date and days open
			String originalStartDate =  getStartDateForVotesString();							

			//Calculate end date										
			Calendar calendarVar = Calendar.getInstance();
			SimpleDateFormat theNewDateFormat = new SimpleDateFormat("yyyy-MMM-dd");

			try		//try to set the string containing the original start date as a date, which should be valid, because only a valid one can be set... but just in case
			{
				calendarVar.setTime(theNewDateFormat.parse(originalStartDate));
			}
			catch (ParseException e1)
			{
				System.out.println("Error: Cannot set the original start date from the string to a date");
			}
			
			calendarVar.add(Calendar.DATE, currentDateRange);  // number of days to add
			String endDateString = theNewDateFormat.format(calendarVar.getTime() );  // end new date

			//display
			System.out.println("\nThe ballot currently opens on the " + originalStartDate + " and is open for the total of " + currentDateRange + " days\nThat makes a close date of: " + endDateString + "\n");
		}
		
		System.out.print("Do you want to change these/ set new times? (\"Y\" or \"N\"): ");
			
		String input = getInput();
		
		//while use fails to answer yes (y) or no (n)
		while(!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n") )
		{
			System.out.print("Input not recognised\nPlease answer \"y\" or \"n\": ");
			input = getInput();
		}

		if(input.equalsIgnoreCase("y") )		//if user choses yes
		{
			while(!input.equalsIgnoreCase("q") )	//while the user doesn't want to quit
			{
				System.out.print("\nDo you want to change the start date, enter \"S\", or the days the ballot is open, enter \"D\" or \"Q\" to quit:  ");
				input = getInput();
					
				if(input.equalsIgnoreCase("S") )	//user chose option to change the start date
				{
					//go to edit start date
					changeDateToStartVote();
				}
				else if(input.equalsIgnoreCase("d") )	//user chose the option to change the day range
				{
					//go to edit days
					changeDaysToVote();
				}
				else if(input.equalsIgnoreCase("q") )	//user chose to quit
				{
					//go back to admin menu
					System.out.println("...going back to previous screen");
				}
				else		//error trapping in case the user entered an invalid input
				{
					System.out.println("Your input was not recognised");
				}

			}

		}
		else if(input.equalsIgnoreCase("n") )		//user chose not to change the current days open and the start date
		{
			System.out.println("...going back to admin screen");
			manageAdmin(); 			
		}
		else{
			System.out.println("I dont know how it broke, but contact your system admin");
		}
	}
	
//allows the admin to change the days the ballot is open
	public void changeDaysToVote(){

		System.out.print("How many days do you want the ballot to be open: ");

		boolean isNumeric = false;
		int inputNumber;
		
		while(isNumeric == false)	//while the user fails to input a number
		{
			String input = getInput();
			
			//try to pass the String to a number
			try
			{  
				inputNumber = Integer.parseInt(input); 
				isNumeric = true;
			}  
			catch(NumberFormatException nfe)  
			{  
				isNumeric = false;  
			}

			if(isNumeric)		//if the user actually entered a number
			{
				inputNumber = Integer.parseInt(input);
				
				if(inputNumber >= 1)	//as long as the user tries to set the date range greater than 1
				{
					setDaysCanVote(inputNumber);
					int currentDateRange = getDaysCanVote();
					System.out.println("The new date range is " + currentDateRange + " days");
				}
				else	//if the day range is less than 1
				{
					System.out.println("Please dont try to set the days where staff can vote to 0, "
							+ "\nif you want to stop staff from voting enter \"stop\" in the admin menu");
				}
				
			}
			else		//error trapping invalid inputs
			{
				System.out.print("Your input was not recognised as a number, try again: ");
			}
		}
		isNumeric = false; 

	}
	
//allow the admin to change the start/open date for the ballot
	public void changeDateToStartVote()
	{
		boolean realDate = false;
		String combinedDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");
		
		while(!realDate)		//while the user fails to enter a valid date
		{
			int year = 0;				//set all the variables to 0 or null at the start
			String month = null;
			int date = 0;
			combinedDate = null;

			
			/**To minimize error entered by the user, so they know which part of the date is entered incorrectly i have asked them to enter the year, month and day separately 
			 * While entering the values in separately and only proceeding if they are in a valid format, there is less chance that they will enter a date in the incorrect format
			 * Finally when all the parts of the date is entered correctly then the program checks to see if it is a real date in that format and sees if the date has already happened and warns the user
			 * **/
			
			System.out.println("\nWhat do you want the start date to be, for the ballot to open, to minimise error you have to set the year month and day separately");

			//get users input for year
			while(year < 2014 || year > 3014)		//if program is used over passed 3014 give me a medal, other wise it is for error checking
			{	
				boolean isNumeric = false;
				System.out.print("First, please enter the year you want the ballot to be open (in the format of yyyy e.g. 2014): ");
				String input = getInput();

				//tries to pass the users input to an integer 
				try  									//help from http://stackoverflow.com/a/1102916
				{  
					year = Integer.parseInt(input); 
					isNumeric = true;
				}  
				catch(NumberFormatException nfe)  
				{  
					isNumeric = false;  
				}  

				if(isNumeric)	//if is a real number
				{	
					year = Integer.parseInt(input);
						
					if(year < 2013 || year > 3015) //if this program is being used after a 1000 years give me a medal. 
					{									//In the time being, anything larger than 3014 and smaller than 2013 is considered invalid by the program
						System.out.println("The year has to be between 2013 and 3014");
					}
					else
					{
						System.out.println("that was number, but not a vaild year, try again");
					}
						
				}	
				else	//if not a number inform the user, that they will have to enter the number again
				{
					System.out.print("Sorry but that was not recognised as a number, try again: ");
						
				}
				
				isNumeric = false;

			}
			//once a valid year has been inputed by the user, set it as the year
			
			System.out.println("The new year is temporarily set to: " + year);


			//get users input for month
			while(month == null)	//while user fails to enter one of the months written in the switch
			{
				System.out.print("\nSecond, enter the month you want the ballot to be open (first three letters of the month, in the format of mmm e.g. jan): ");
				String input = getInput();

				switch (input.toLowerCase() ) //solves problem of upper case being ignored
				{
				case "jan": month = "jan";
				break;
				case "feb": month = "feb";
				break;
				case "mar": month = "mar";
				break;
				case "apr": month = "apr";
				break;
				case "may": month = "may";
				break;
				case "jun": month = "jun";
				break;
				case "jul": month = "jul";
				break;
				case "aug": month = "aug";
				break;
				case "sep": month = "sep";
				break;
				case "oct": month = "oct";
				break;
				case "nov": month = "nov";
				break;
				case "dec": month = "dec";
				break;
				default: 	System.out.println("Your input was not in the correct format ");		//if input fails to be a month from the cases above
							month = null;
				break;	
				}

			}
			
			//once the month has been entered in the correct format
			System.out.println("The new month is temporarily set to: " + month);

			//set day
			while(date < 1 || date > 31)	//while input is not a number between 1 and 31 
			{
				System.out.print("\nFinally, enter the day you want the ballot to be open (in the format of dd e.g. 05 or 21): ");
				String input = getInput();			
				boolean isNumeric = false;
				
				while(!isNumeric)	//while not a number
				{
					//try to parse users input to a integer
					try  									//help from http://stackoverflow.com/a/1102916
					{  
						date = Integer.parseInt(input); 
						isNumeric = true;
					}  
					catch(NumberFormatException nfe)  //notify user that its not a number
					{  
						isNumeric = false; 
						System.out.print("Sorry but that was not recognised as a number or was not between 1-31, try again: ");
						input = getInput();
					}
					
				}
				
				if (date < 1 || date > 31)	//notify user that that number isn't in the correct range
				{
					System.out.println("Day was not with in the correct range (01-31)");
				}
				
				date = Integer.parseInt(input);		//set date to users previous input
				isNumeric = false;					//sets back to false in case the user didn't enter a number in the correct range and has to try again
			
			}		//exit of loop when number is with in the set range

			System.out.println("The new day is temporarily set to: " + date);


			combinedDate = year + "-" + month+ "-" + date;		//set the combined user inputs as one sting
			System.out.println("...Testing to see if " + combinedDate +" is a real date");

			//Check to see if the user inputed a valid date, after going through the error checking
			//error checking doesn't stop user from entering incorrect dates like 2014-feb-31 (there's no 31th in February)
			
			//try to parse the combined inputs from the user as a real date
			try 
			{
											//help from http://viralpatel.net/blogs/check-string-is-valid-date-java/ 
				sdf.setLenient(false);		//without setting this to false dates are not checked correctly
				
				setDateTest(sdf.parse(combinedDate) );
				
				System.out.println("\nYay " + combinedDate + " is a vaild date");		//notify the user that the date is valid
				realDate = true;
	 
			} 
			catch (ParseException e) //notify the user that the date is invalid
			{
				System.out.println("I am sorry, but " + combinedDate + " is not a vaild date");
				realDate = false;
			}		

		}
		
		String str = sdf.format(dateTest);		//convert to string
		setStartDateForVotesString(str);		//set the new start date
		
		Date todayDate = new Date();

		if(realDate && todayDate.after(dateTest) )	//warn the user if the date has already happened, still saves the new date
		{
			System.out.println("\nJust as a warning " + combinedDate + " has apparently already happened/ started (today is: " + todayDate + ").\nThe new date you have entered however has still been saved");					
		}
		else		//the date hasn't happened and its saves the date
		{
			System.out.println("\nYour date of: " + combinedDate + "has been saved");					
		}
		
	}
	
/**HELP SECTION**/
	//display the help from the title chosen from the user
	public void displayHelp()
	{
		String[][] helpArr = vc.getHelpOptionsArr();		//get the help data and the amount of "lines" in the help data's array, from the voting controller
		int helpLines = vc.getAmountOfHelpLines();
		
		System.out.println("\n\n\t\t==========Welcome to the Help section==========\n");
		int count = 0;

		boolean goHome = false;
		while (!goHome)			//stay in the help section in till the user chooses to exit
		{
			//display help option headings
			while(helpLines != count)
			{
				System.out.println(count + ". " + helpArr[count][0]);		//program adds the numbers (they are not in the test file) and displays the heading for all the topics
				count++;
			}
			
			int inputNumber;
			String input = null;
			
			//get the selection from the user, via the number that corresponds with the title
			System.out.print("\n\nPlease input the number of one of the " + helpLines + " headings you want to view, or \"Q\" to quit to the main menu: ");
			input = getInput();


			boolean isNumeric = false;
			
			//check its a number
			try  									//help from http://stackoverflow.com/a/1102916
			{  
				inputNumber = Integer.parseInt(input); 
				isNumeric = true;
			}  
			catch(NumberFormatException nfe)  
			{  
				isNumeric = false;  
			}  

			if(input.equalsIgnoreCase("q") )	//if the user selects the option to quit
			{
				goHome = true;
				System.out.println("Going back to main menu...");
			}
			else if( isNumeric )		//if its a valid number
			{
				inputNumber = Integer.parseInt(input);
				
				try		//try to get the get the topic data from the users input
				{
					System.out.println("\nYou have selected topic " + inputNumber);
					System.out.println("\n\t\t============== " + helpArr[inputNumber][0] + " =====================\n\n");
					System.out.println(helpArr[inputNumber][1]);
				}
				catch (Exception e)	//if users selection isn't one of the options, or if there is an error
				{
					System.out.println("Sorry but your input number was not a topic");
				}
			}
			else		//error trapping, input wasnt even a number or q to quit
			{
				System.out.println("Sorry but your input was not recognised");
			}
		}


	}


/**GETTERS and SETTERS*********************************************************************/
	public int getDaysCanVote() {
		return daysCanVote;
	}

	public void setDaysCanVote(int daysCanVote) {
		this.daysCanVote = daysCanVote;
	}

	public String getStartDateForVotesString() {
		return startDateForVotesString;
	}

	public void setStartDateForVotesString(String startDateForVotesString) 
	{
		this.startDateForVotesString = startDateForVotesString;
	}
	public Date getDateTest() {
		return dateTest;
	}

	public void setDateTest(Date dateTest) {
		this.dateTest = dateTest;
	}
	public int getNumberOfStaff() {
		return numberOfStaff;
	}

	public void setNumberOfStaff(int numberOfStaff) {
		this.numberOfStaff = numberOfStaff;
	}

	public int getNumberOfCandidates() 
	{
		return numberOfCandidates;
	}

	public void setNumberOfCandidates(int numberOfCandidates) 
	{
		this.numberOfCandidates = numberOfCandidates;
	}
}