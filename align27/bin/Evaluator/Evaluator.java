import java.io.*;
import java.util.*;
/**
 * Reads in results from result.txt
 * Compares it with the answer key from key.txt
 * Displays precision, recall and F1 value
 * -d for dubgging
 */

class Evaluator
{
	static final String testDir = "Test";
	static final String keyDir = "Aligned";
	//static final String testDir = "Test";

	public static void main (String [] args) throws Exception
	{
	    if (args.length == 0)
		doIt(new File(testDir), false);

	    else
		doIt(new File(testDir), true);
		
		
	}

	static void doIt(File file,boolean debug) throws Exception
	{
		if (file.isDirectory()) {
        String[] files = file.list();
				for (int i = 0; i < files.length; i++) {
					doIt(new File(file, files[i]),debug);
				}
		}
		

		else
		{
			// THIS LINE ONYL WORKS ON UNIX
			String txtfile = file.toString().substring(file.toString().lastIndexOf((int)'/')+1,file.toString().lastIndexOf((int)'_'));
			FileInputStream fis_key = new FileInputStream(keyDir + "/" + txtfile + "_Key.txt");    // answer key

			// THIS LINE WORKS FOR WINDOWS
 			//String txtfile = file.toString().substring(file.toString().lastIndexOf((int)'\\')+1,file.toString().lastIndexOf((int)'_'));
			//FileInputStream fis_key = new FileInputStream(keyDir + "\\" + txtfile + "_Key.txt");    // answer key

			System.out.println("\n"+txtfile);

			FileInputStream fis_res = new FileInputStream(file);    // alignment results

			BufferedReader br_key = new BufferedReader(new InputStreamReader(fis_key));
			BufferedReader br_res = new BufferedReader(new InputStreamReader(fis_res));
			
			Vector keyV = new Vector();
		
			int numPosAns = processKey(br_key, keyV); // # of possible answers 
		
			//printV(keyV);
			System.out.println("# of possible answers = " + numPosAns);
			processRes(keyV, debug, br_res, numPosAns);		// adjust boolena verb if don't want wrong ans printed.
			
		}
	}
	

	static int processRes(Vector keyV, boolean verb, BufferedReader br, int numPosAns) throws Exception
	{
		String line;
		StringTokenizer st;
		int count = 0;
		int correct = 0;
		int errCount = 0;

		while ((line = br.readLine()) != null)
		{
			st = new StringTokenizer(line,"\t");
			st.nextToken();
			
			StringTokenizer st1 = new StringTokenizer(st.nextToken(),":");
			st1.nextToken();
			String token = st1.nextToken();

			try 
			{
				int res = Integer.parseInt((token.trim()).trim());

			
			if (keyV.elementAt(count) != null)
			{
				if (((ansObj)(keyV.elementAt(count))).contain(res))
					correct++;
				
				else
				{
				    if (verb) {
						System.out.println(++errCount + ")Slide: " + (count+1) + " wrong.");
				    }
				}
				
			}
			
			else // case when na
			    {
				if (verb)
				    System.out.println(++errCount + ")Slide: " + (count+1) + " wrong.");
			    }
			
			count++;
			}
			
			catch (NumberFormatException e)
			{
				if ((token.trim()).equalsIgnoreCase("nil"))
				 {
				     if (keyV.elementAt(count) == null)
					 correct++;

				     else 
					 {
					     if (verb)
						 System.out.println(++errCount + ")Slide: " + (count+1) + " wrong.");

					 }
				     count++;
				     //continue;
				}
				
				else
				    throw e;
			}
				
		}
		
		//count++;   // to give total slide count.
		
		System.out.println("# correct ans = " + correct);
		System.out.println("# of ans given = " + count);

		double p = ((double)correct)/((double)count);
		double r = ((double)correct)/((double)numPosAns);
		double f1 = (2*p*r)/(p+r);
		
		System.out.println("Precision = " +  p);
		System.out.println("Recall = " +  r);
		System.out.println("F1 Value = " + f1);
		
		return correct;
		
	}
	
	/** 
	 * processKey - Returns the number of possible answers
	 */
	static int processKey(BufferedReader br, Vector v) throws Exception
	{
		String line;
		StringTokenizer st;
		int count = 0;
		
		br.readLine();  // skip first line
		
		while ((line = br.readLine()) != null)
		{
			st = new StringTokenizer(line,"\t");
			st.nextToken();
			String para = (st.nextToken()).trim();

			if (para.equals("na"))
			{
				v.addElement(null);
				count++;
			}
			
			else if (para.indexOf(";") > -1)
			{
				StringTokenizer st1 = new StringTokenizer(para,";");
				ansObj a = new ansObj();
				
				while (st1.hasMoreTokens())
				{
					String token = st1.nextToken();
					
					if (token.indexOf("-") > -1)
					{
						for (int i = Integer.parseInt(token.substring(0,token.indexOf("-")));
								 i <= Integer.parseInt(token.substring(token.indexOf("-")+1));
								 i++)
						{
							//System.out.print(i+",");
							a.ansV.addElement(new Integer(i));
							count++;
						}
					}
					
					else
					{
						//System.out.print(token+",");
						a.ansV.addElement(new Integer(Integer.parseInt(token)));
						count++;
					}
					
				}
				
				v.addElement(a);
			}

			else if (para.indexOf("-") > -1)
			{
				ansObj a = new ansObj();

				for (int i = Integer.parseInt(para.substring(0,para.indexOf("-")));
						 i <= Integer.parseInt(para.substring(para.indexOf("-")+1));
						 i++)
				{
					//System.out.print(i+",");
					a.ansV.addElement(new Integer(i));
					count++;
				}
				
				v.addElement(a);
			}
			
			else
			{
				//System.out.print(para+",");
				ansObj a = new ansObj();
				a.ansV.addElement(new Integer(Integer.parseInt(para)));
				v.addElement(a);
				count++;
			}
			
			//System.out.println();
		}// while
		
		return count;
	}

	static void printV(Vector v)
	{
		for(int i = 0; i < v.size(); i++)
		{
			System.out.println(v.elementAt(i));
		}
		
	}

}

class ansObj 
{
	Vector ansV;
	
	ansObj()
	{
		ansV = new Vector();
	}
	
	boolean contain (int k)
	{
		for (int i = 0; i < ansV.size(); i++)
			if (((Integer)(ansV.elementAt(i))).intValue() == k)
				return true;
		
		return false;
		
	}
	
	public String toString()
	{
		return ansV.toString();
	}
	
}
	
