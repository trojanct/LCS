import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.io.*;
import java.util.Random;

public class LCS {

    static ThreadMXBean bean = ManagementFactory.getThreadMXBean( );

    static String ResultsFolderPath = "/home/cody/LCS/"; // pathname to results folder
    static FileWriter resultsFile;
    static PrintWriter resultsWriter;

    static long MAXV =  2000000000;
    static long MINV = -2000000000;
    static int numberOfTrials = 100;
    static int MAXINPUTSIZE  = (int) Math.pow(2,12);
    static int MININPUTSIZE  =  1;

    public static void main( String[] args)
    {

        //calling full experiment with text file names, running the three experiments as usual for consistency
        runFullExperiment("Test-Exp1-ThrowAway.txt");
        runFullExperiment("Test-Exp2.txt");
        runFullExperiment("Test-Exp3.txt");


    }
    // modified timing code given to us, changed so that input sizes were reduced and went one at a time
    static void runFullExperiment(String resultsFileName){


        String randomstring1, randomstring2;

        try {

            resultsFile = new FileWriter(ResultsFolderPath + resultsFileName);
            resultsWriter = new PrintWriter(resultsFile);

        } catch(Exception e) {

            System.out.println("*****!!!!!  Had a problem opening the results file "+ResultsFolderPath+resultsFileName);
            return; // not very foolproof... but we do expect to be able to create/open the file...

        }



        ThreadCpuStopWatch BatchStopwatch = new ThreadCpuStopWatch(); // for timing an entire set of trials
        ThreadCpuStopWatch TrialStopwatch = new ThreadCpuStopWatch(); // for timing an individual trial


        resultsWriter.println("#InputSize    AverageTime"); // # marks a comment in gnuplot data
        resultsWriter.flush();
        /* for each size of input we want to test: in this case starting small and doubling the size each time */
        for(int inputSize=MININPUTSIZE;inputSize<=MAXINPUTSIZE; inputSize*=2) {
            // progress message...
            System.out.println("Running test for input size "+inputSize+" ... ");


            /* repeat for desired number of trials (for a specific size of input)... */
            long batchElapsedTime = 0;
            // generate a list of randomly spaced integers in ascending sorted order to use as test input
            // In this case we're generating one list to use for the entire set of trials (of a given input size)
            // but we will randomly generate the search key for each trial
            System.out.print("    Generating test data...");

            randomstring1 = randomString(inputSize);
            randomstring2 = randomString(inputSize);

            System.out.println("...done.");
            System.out.print("    Running trial batch...");


            /* force garbage collection before each batch of trials run so it is not included in the time */
            System.gc();




            // instead of timing each individual trial, we will time the entire set of trials (for a given input size)
            // and divide by the number of trials -- this reduces the impact of the amount of time it takes to call the
            // stopwatch methods themselves
            BatchStopwatch.start(); // comment this line if timing trials individually


            // run the tirals
            for (long trial = 0; trial < numberOfTrials; trial++) {
                // generate a random key to search in the range of a the min/max numbers in the list
                // long testSearchKey = (long) (0 + Math.random() * (testList[testList.length-1]));
                /* force garbage collection before each trial run so it is not included in the time */
                // System.gc();


                //TrialStopwatch.start(); // *** uncomment this line if timing trials individually
                /* run the function we're testing on the trial input */
                test( randomstring1, randomstring1);
                //long counted = fastestthree(testList);
                // batchElapsedTime = batchElapsedTime + TrialStopwatch.elapsedTime(); // *** uncomment this line if timing trials individually

            }

            batchElapsedTime = BatchStopwatch.elapsedTime(); // *** comment this line if timing trials individually
            double averageTimePerTrialInBatch = (double) batchElapsedTime / (double)numberOfTrials; // calculate the average time per trial in this batch


            /* print data for this size of input */
            resultsWriter.printf("%12d  %15.2f \n",inputSize, averageTimePerTrialInBatch); // might as well make the columns look nice
            resultsWriter.flush();
            System.out.println(" ....done.");

        }

    }



    public static void test(String tester1, String tester2)
    {
        String test1 = "aaaaaaaaaa";
        String test2 = "aaaaaaaaaa";
        String test3 = "bbbbbbbbbb";
        String test4 = "";
        String test5 = "samefdsafw4ewsagweg";
        String test6 = "opk,'ol'kok'ksame";
        String test7 = "ABCDEFG????wxwxyuiop?";
        String test8 = "?????jklABCDEFwxyzABC";


        //int length = LcsBrute(tester1,tester2);

        int testlength = Lcstesting(tester1, tester2);

        //System.out.print("The length is "+ length + "\n") ;
        System.out.print("The test length is "+ testlength + "\n");

    }

    public static int Lcstesting(String s1, String s2) {
        int i, j, k;
        int lcsLen = 0;
        int small,big;


        if(s1.length() == 0 || s2.length() == 0)
        {
            return 0;
        }
        if(s1.length() < s2.length())
        {
            big = s1.length();
            small = s2.length();
        }
        else
        {
            big = s2.length();
            small = s1.length();
        }

        for(i=0; i <= small; i++)
        {
            if(lcsLen >= small - i)
            {
                return lcsLen;
            }
            for(j=0; j <= big; j++)
            {
                for(k=0; k < s1.length()-i  && k < s2.length()-j  ; k++)

                {
                    if( s1.charAt(i+k) != s2.charAt(j+k))
                    {
                        break;
                    }


                }
                if(k > lcsLen)
                {
                    lcsLen = k;
                }
            }
        }


        return lcsLen;
    }



    public static int LcsBrute(String s1, String s2) {
        int i, j, k;
        int lcsLen = 0;

        for(i=0; i <= s1.length(); i++)
        {
            for(j=0; j <= s2.length(); j++)
            {
                for(k=0; k < s1.length()-i  && k < s2.length()-j  ; k++)

                {
                    if( s1.charAt(i+k) != s2.charAt(j+k))
                    {
                        break;
                    }


                }
                if(k > lcsLen)
                {
                    lcsLen = k;
                }
            }
        }


        return lcsLen;
    }

    public static String randomString( int inputSize )
    {
        int i,temp;
        Random random = new Random();


        StringBuilder randomString = new StringBuilder(inputSize);
        for( i = 0; i <= inputSize; i++)
        {
            temp = random.nextInt((126 - 32)+1) + 32;
            randomString.append((char)temp);

        }
        System.out.println(randomString);
        String returnString = new String(randomString);
        return returnString;
    }
}

