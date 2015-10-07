package epicVotingSystem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class VotingController
{
    private ArrayList<Staff> staffs = new ArrayList<Staff>();
    private ArrayList<Candidate> candidates = new ArrayList<Candidate>();
    private Staff theStaff;
    private Candidate theCandidate;
    private String[][] helpOptionsArr;
    private int amountOfHelpLines;

	public VotingController()		//the important parts votingController for the votingInterface to load in one go
    {
        loadStaffData();
        loadCandidateData();
        helpMe();
    }

    //loads candidates from file. 
    public void loadCandidateData()
    {
        try		//try to get the data for the candidates from the text file and put them into an arrayList
        {
             String fileName = "candidates.txt";
             File theFile = new File(fileName);
             BufferedReader reader = new BufferedReader(new FileReader(theFile) );	//load the file

             String candidateData;

             while( (candidateData = reader.readLine() )!= null)	//while there is another line on the file, thats not empty
             {
                 String[] candidateDetails = candidateData.split(",");	//Separate data at the comma
                 int code = Integer.parseInt(candidateDetails[0]);		//parse to an intager
                 int votes = Integer.parseInt(candidateDetails[2]);
                 theCandidate = new Candidate(code, candidateDetails[1], votes, candidateDetails[3]);	//add the parts of the line as a new candidate
                 candidates.add(theCandidate);	//add to the new candidate to the candidates arrayList
             }
             reader.close();
         }
         catch(IOException e)	//if fails to load data
         {
             System.out.println("Error! There was a problem with loading candidate names from file");
         }
        catch(Exception e)	//if fails to load data
        {
            System.out.println("Error! Unknown problem accoured during loading the candidate names from file.");
        }
    }

    //loads staff names from file. 
    public void loadStaffData()
    {
         try	//try to load data from text file
         {
             String fileName = "staff.txt";


             File theFile = new File(fileName);

             BufferedReader reader = new BufferedReader(new  FileReader(theFile) ); //load the file into the buffer reader

             String staffData;
             String[] staffDetails;

             while( (staffData = reader.readLine() ) != null)		//while there is another line on the file, thats not empty
             {
                 staffDetails = staffData.split(",");
                 int id = Integer.parseInt(staffDetails[0]);
                 int voted = Integer.parseInt(staffDetails[2]);
                 theStaff = new Staff(id, staffDetails[1], voted, staffDetails[3], staffDetails[4]);	//add the parts of the line as a new staff
                 staffs.add(theStaff);		//add to the new staff to the staffs arrayList
             }
             reader.close();
         }
         catch(IOException e)	//if fails to load data
         {
              System.out.println("Error! There was a problem with loading staff names from file");
         }
         catch(Exception e)	//if fails to load data
         {
             System.out.println("Error! Unknown problem accoured during loading the staff names from file.");
         }
    }

    //returns a staff if found in the staffs ArrayList
    public Staff getStaff(int id)
    {
        Iterator<Staff> it = staffs.iterator();
        while(it.hasNext() )		//while there is another staff in the arrayList
        {
            theStaff = (Staff) it.next();
            
            if(theStaff.getId()== id)		//if the staff id matches a staff id in the arrayList
            {
                return theStaff;
            }
        }
        return null;
    }

    //returns the candidate if found in the candidates ArrayList
    public Candidate getCandidate(int candidateCode)
    {
        Iterator<Candidate> it = candidates.iterator();
        
        while(it.hasNext() )		//while there is another candidate in the array list
		{
            theCandidate = (Candidate) it.next();
            
            if(theCandidate.getCandidateCode() == candidateCode)
            {
                return theCandidate;
            }
        }
        return null;
    }

    //every staff vote must be saved to file
    public void recordVote()
    {
    	java.util.Date date= new java.util.Date();								//Precise time stamp, help from http://www.mkyong.com/java/how-to-get-current-timestamps-in-java/
    	Timestamp theTimeStamp= new Timestamp(date.getTime());
    																							//help from http://www.coderanch.com/t/585264/java/java/Convert-timeStamps
    	DateFormat theDateFormat = new SimpleDateFormat("dd-MMM-YY hh:mm:ss.SSSSSSSSS");  
    	String timeStampString = theDateFormat.format(theTimeStamp);
    	
        theStaff.setVoted();									//changes the staffs voting status to voted/1
        theStaff.setTimeStampString(timeStampString);			//records the time and saves it as a time stamp for the staff
        theCandidate.addVote();									//add a vote for the candidate
        saveStaffData();										//save to file
        saveCandidateData();									//save to file
    }

    //Writes staffs data back to file
    public void saveStaffData()
    {
        try
        {
        	BufferedWriter writer = new  BufferedWriter(new FileWriter("staff.txt") );		//load the text file into the buffer reader
        	Iterator<Staff> it = staffs.iterator();
            String staffDetails;
            
            //write the whole arrayList back to the text file
            while(it.hasNext() )		//while there is another staff member
            {
                theStaff = (Staff) it.next();
                staffDetails = theStaff.getId() + "," +theStaff.getName() + "," + theStaff.hasVoted() + "," + theStaff.getPassword() + "," +theStaff.getTimeStampString() + "\n";
                writer.write(staffDetails);
            }
            writer.close();
        }
        catch(IOException e)		//if an error occurs
        {
            System.out.println("Error, while trying to save staff data. Exeception: " + e);
        }
    }

    //writes candidates data back to file
    public void saveCandidateData()
    {
        try
        {
            BufferedWriter writer = new  BufferedWriter(new FileWriter("candidates.txt") );		//load the text file into the buffer reader
            Iterator<Candidate> it = candidates.iterator();
            String candidateDetails;
            
            //Save the whole arrayList back to the text file
            while(it.hasNext() )		//while there is another candidate in the arrayList
			{
                theCandidate = (Candidate) it.next();
                candidateDetails = theCandidate.getCandidateCode() + "," +theCandidate.getName() + "," + theCandidate.getVotes() + "," + theCandidate.getDept() +"\n";
                writer.write(candidateDetails);
            }
            writer.close();
        }
        catch(IOException e)		//if an error occurs
		{
        	System.out.println("Error, while trying to save candidate data. Exeception: " + e);
        }
    }
    


/**HELP FILE READER **************************************************************************************************/

    public void helpMe(){		//added for help section

    	/**
    	 * Help thanks to:
    	 * https://www.daniweb.com/software-development/java/threads/324267/reading-file-and-store-it-into-2d-array-and-parse-it
    	 */

    	String[][] helpOptionsArr;

    	//to set the required amount of rows for the helpOptionsArr
    	List<String> rows = null;					

    	//try and get the amount of rows in the help text file
    	try
    	{
    		rows = Files.readAllLines(Paths.get("help.txt"), StandardCharsets.UTF_8);
    	} 
    	catch (IOException e)		//if error occurred
    	{
    		System.out.println("Error: Cannot get the amount of rows of help from the help file");
    		e.printStackTrace();
    	}

    	helpOptionsArr = new String[rows.size()][2];		//array is now sized to fit the amount of help topics/ lines (means you can change text file to have more or less lines in it)

    	int amountOfHelpLines = rows.size();		//to help with the display of help.txt
    	int rowCount = 0;
    	int colCount = 0;

    	try
    	{
    		BufferedReader br = new BufferedReader(new FileReader("help.txt"));	//read file
    		String line;

    		//file reading
    		while ( (line = br.readLine() ) != null) //while there are still lines of text to read, set lines of text into the Sting "line"
    		{
    			String[] helpSections = line.split("\\!");	//Split the string called "line" at every ! into sections
    			//Store the sections into a string named "helpSections"

    			//set helpSections into multidimensional array
    			for (String magicString : helpSections)					//for-each statement (http://stackoverflow.com/a/7763169)
    			{												
    				//rowCount and colCount tell it what position to save the data in
    				helpOptionsArr[rowCount][colCount] = magicString;
    				colCount++;

    			}
			
    			colCount=0;
    			rowCount++;

    		}	//END OF WHILE LOOP
    		br.close();		//close the buffer reader
    	}
        catch(IOException e)
    	{
            System.out.println("Error! There was a problem with loading help data from the file");
        }
        catch(Exception e)
        {
            System.out.println("Error! Unknown problem accoured during loading the staff names from file.");
        }
    	//set the two variables, so they can later be called from the voting interface
    	setHelpOptionsArr(helpOptionsArr);
    	setAmountOfHelpLines(amountOfHelpLines);

    }
    
/****GETTERS AND SETTERS*************/
    public int getAmountOfHelpLines() {
		return amountOfHelpLines;
	}

	public void setAmountOfHelpLines(int amountOfHelpLines) {
		this.amountOfHelpLines = amountOfHelpLines;
	}

	public String[][] getHelpOptionsArr() {
		return helpOptionsArr;
	}

	public void setHelpOptionsArr(String[][] helpOptionsArr) {
		this.helpOptionsArr = helpOptionsArr;
	}
    //returns the collection of candidates
    public ArrayList<Candidate> getCandidates()
    {
        return candidates;
    }
    
    //returns the collection of staff
    public ArrayList<Staff> getStaff()			//ADDED for ver 2.0
    {
        return staffs;
    }

    //returns total number of staffs in the collection
    public int getTotalVoters()
    {
        return staffs.size();
    }
    
}
