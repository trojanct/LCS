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
    static int MAXINPUTSIZE  = (int) Math.pow(2,14);
    static int MININPUTSIZE  =  1;

    public static void main( String[] args)
    {

        //calling full experiment with text file names, running the three experiments as usual for consistency
        runFullExperiment("BookTest-Exp1-ThrowAway.txt");
        runFullExperiment("BookTest-Exp2.txt");
        runFullExperiment("BookTest-Exp3.txt");


    }
    // modified timing code given to us, changed so that input sizes were reduced and went one at a time
    static void runFullExperiment(String resultsFileName){


        String randomstring1, randomstring2,Book;

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

            // Gets random Strings to use in tests of same size;
            randomstring1 = randomString(inputSize);
            randomstring2 = randomString(inputSize);
            Book = getBook();
            //int bookIndex1 = (int)( (Book.length()-inputSize)+ Math.random() *((Book.length() - inputSize) - MINV));
            //int bookIndex2 = (int)( (Book.length()-inputSize)+ Math.random() *((Book.length() - inputSize) - MINV));
            //randomstring1 = Book.substring(bookIndex1,bookIndex1 +inputSize);
            //randomstring2 = Book.substring(bookIndex2,bookIndex2+ inputSize);

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

                //Testing String
                test( randomstring1, randomstring2);
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
        //Performance testing Strings
        String test1 = "aaaaaaaaaa";
        String test2 = "aaaaaaaaaa";
        String test3 = "bbbbbbbbbb";
        String test4 = "";
        String test5 = "samefdsafw4ewsagweg";
        String test6 = "opk,'ol'kok'ksame";
        String test7 = "ABCDEFG????wxwxyuiop?";
        String test8 = "?????jklABCDEFwxyzABC";
        String Book1 = getBook();


        //Calls for both the improved and Brute force Strings
        //int length = LcsBrute(tester1,tester2);

        int testlength = Lcstesting(tester1, tester2);

        //System.out.print("The length is "+ length + "\n") ;
        System.out.print("The test length is "+ testlength + "\n");

    }

    public static int Lcstesting(String s1, String s2) {
        int i, j, k;
        int lcsLen = 0;
        int small,big;
        k = 0;
        //If either length is zero will print out 0
        if(s1.length() == 0 || s2.length() == 0)
        {
            return 0;
        }
        // lines of code distinguishing which one was the longest
      /*  if(s1.length() > s2.length())
        {
            big = s1.length();
            small = s2.length();
        }
        else
        {
            big = s2.length();
            small = s1.length();
        }

*/
      //setting up the for the algorithm
        small = s1.length();
        big = s2.length();
        int [][] array = new int[small+1][big+1];

        // algorithm starting with the two for loops
        for(i=0; i <= small; i++)
        {
            //If the length of the find is bigger than the array than there is no point in going forward
            if(lcsLen >= small - i)
            {
                return lcsLen;
            }
            for(j=0; j <= big; j++)
            {
                if( i == 0 || j == 0)
                {
                    array[i][j] = 0;
                }
                // Setting up the the 2d array to measure the lengths
                else if(s1.charAt( i - 1  ) == s2.charAt(j -1 ))
                {
                    array[i][j] = array[i - 1][ j-1 ]+1;
                    lcsLen = Integer.max(lcsLen, array[i][j]);
                }
                else
                {
                    array[i][j] = 0;
                }
            }
        }


        //returning the results
        return lcsLen;
    }



    public static int LcsBrute(String s1, String s2) {
        int i, j, k;
        int lcsLen = 0;

        //Brute Force algorithm
        for(i=0; i <= s1.length(); i++)
        {
            for(j=0; j <= s2.length(); j++)
            {
                // extra for loop using k for finding length
                for(k=0; k < s1.length()-i  && k < s2.length()-j  ; k++)

                {
                    if( s1.charAt(i+k) != s2.charAt(j+k))
                    {
                        break;
                    }


                }
                // comparing lengths
                if(k > lcsLen)
                {
                    lcsLen = k;
                }
            }
        }

        // returning legths
        return lcsLen;
    }

    public static String randomString( int inputSize )
    {
        int i,temp;
        Random random = new Random();

        //Code used to find random characters examples showed other than
        //alphabet so put in usable ascii
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
    //Long string with frankenstein
    public static String getBook()
    {
        String Book =
                "Title: Frankenstein\n" +
                "_To Mrs. Saville, England._\n" +
                "\n" +
                "\n" +
                "St. Petersburgh, Dec. 11th, 17—.\n" +
                "\n" +
                "\n" +
                "You will rejoice to hear that no disaster has accompanied the\n" +
                "commencement of an enterprise which you have regarded with such evil\n" +
                "forebodings.  I arrived here yesterday, and my first task is to assure\n" +
                "my dear sister of my welfare and increasing confidence in the success\n" +
                "of my undertaking.\n" +
                "\n" +
                "I am already far north of London, and as I walk in the streets of\n" +
                "Petersburgh, I feel a cold northern breeze play upon my cheeks, which\n" +
                "braces my nerves and fills me with delight.  Do you understand this\n" +
                "feeling?  This breeze, which has travelled from the regions towards\n" +
                "which I am advancing, gives me a foretaste of those icy climes.\n" +
                "Inspirited by this wind of promise, my daydreams become more fervent\n" +
                "and vivid.  I try in vain to be persuaded that the pole is the seat of\n" +
                "frost and desolation; it ever presents itself to my imagination as the\n" +
                "region of beauty and delight.  There, Margaret, the sun is for ever\n" +
                "visible, its broad disk just skirting the horizon and diffusing a\n" +
                "perpetual splendour.  There—for with your leave, my sister, I will put\n" +
                "some trust in preceding navigators—there snow and frost are banished;\n" +
                "and, sailing over a calm sea, we may be wafted to a land surpassing in\n" +
                "wonders and in beauty every region hitherto discovered on the habitable\n" +
                "globe.  Its productions and features may be without example, as the\n" +
                "phenomena of the heavenly bodies undoubtedly are in those undiscovered\n" +
                "solitudes.  What may not be expected in a country of eternal light?  I\n" +
                "may there discover the wondrous power which attracts the needle and may\n" +
                "regulate a thousand celestial observations that require only this\n" +
                "voyage to render their seeming eccentricities consistent for ever.  I\n" +
                "shall satiate my ardent curiosity with the sight of a part of the world\n" +
                "never before visited, and may tread a land never before imprinted by\n" +
                "the foot of man. These are my enticements, and they are sufficient to\n" +
                "conquer all fear of danger or death and to induce me to commence this\n" +
                "laborious voyage with the joy a child feels when he embarks in a little\n" +
                "boat, with his holiday mates, on an expedition of discovery up his\n" +
                "native river. But supposing all these conjectures to be false, you\n" +
                "cannot contest the inestimable benefit which I shall confer on all\n" +
                "mankind, to the last generation, by discovering a passage near the pole\n" +
                "to those countries, to reach which at present so many months are\n" +
                "requisite; or by ascertaining the secret of the magnet, which, if at\n" +
                "all possible, can only be effected by an undertaking such as mine.\n" +
                "\n" +
                "These reflections have dispelled the agitation with which I began my\n" +
                "letter, and I feel my heart glow with an enthusiasm which elevates me\n" +
                "to heaven, for nothing contributes so much to tranquillise the mind as\n" +
                "a steady purpose—a point on which the soul may fix its intellectual\n" +
                "eye.  This expedition has been the favourite dream of my early years. I\n" +
                "have read with ardour the accounts of the various voyages which have\n" +
                "been made in the prospect of arriving at the North Pacific Ocean\n" +
                "through the seas which surround the pole.  You may remember that a\n" +
                "history of all the voyages made for purposes of discovery composed the\n" +
                "whole of our good Uncle Thomas’ library.  My education was neglected,\n" +
                "yet I was passionately fond of reading.  These volumes were my study\n" +
                "day and night, and my familiarity with them increased that regret which\n" +
                "I had felt, as a child, on learning that my father’s dying injunction\n" +
                "had forbidden my uncle to allow me to embark in a seafaring life.\n" +
                "\n" +
                "These visions faded when I perused, for the first time, those poets\n" +
                "whose effusions entranced my soul and lifted it to heaven.  I also\n" +
                "became a poet and for one year lived in a paradise of my own creation;\n" +
                "I imagined that I also might obtain a niche in the temple where the\n" +
                "names of Homer and Shakespeare are consecrated.  You are well\n" +
                "acquainted with my failure and how heavily I bore the disappointment.\n" +
                "But just at that time I inherited the fortune of my cousin, and my\n" +
                "thoughts were turned into the channel of their earlier bent.\n" +
                "\n" +
                "Six years have passed since I resolved on my present undertaking.  I\n" +
                "can, even now, remember the hour from which I dedicated myself to this\n" +
                "great enterprise.  I commenced by inuring my body to hardship.  I\n" +
                "accompanied the whale-fishers on several expeditions to the North Sea;\n" +
                "I voluntarily endured cold, famine, thirst, and want of sleep; I often\n" +
                "worked harder than the common sailors during the day and devoted my\n" +
                "nights to the study of mathematics, the theory of medicine, and those\n" +
                "branches of physical science from which a naval adventurer might derive\n" +
                "the greatest practical advantage.  Twice I actually hired myself as an\n" +
                "under-mate in a Greenland whaler, and acquitted myself to admiration. I\n" +
                "must own I felt a little proud when my captain offered me the second\n" +
                "dignity in the vessel and entreated me to remain with the greatest\n" +
                "earnestness, so valuable did he consider my services.\n" +
                "\n" +
                "And now, dear Margaret, do I not deserve to accomplish some great purpose?\n" +
                "My life might have been passed in ease and luxury, but I preferred glory to\n" +
                "every enticement that wealth placed in my path. Oh, that some encouraging\n" +
                "voice would answer in the affirmative! My courage and my resolution is\n" +
                "firm; but my hopes fluctuate, and my spirits are often depressed. I am\n" +
                "about to proceed on a long and difficult voyage, the emergencies of which\n" +
                "will demand all my fortitude: I am required not only to raise the spirits\n" +
                "of others, but sometimes to sustain my own, when theirs are failing.\n" +
                "\n" +
                "This is the most favourable period for travelling in Russia.  They fly\n" +
                "quickly over the snow in their sledges; the motion is pleasant, and, in\n" +
                "my opinion, far more agreeable than that of an English stagecoach.  The\n" +
                "cold is not excessive, if you are wrapped in furs—a dress which I have\n" +
                "already adopted, for there is a great difference between walking the\n" +
                "deck and remaining seated motionless for hours, when no exercise\n" +
                "prevents the blood from actually freezing in your veins.  I have no\n" +
                "ambition to lose my life on the post-road between St. Petersburgh and\n" +
                "Archangel.\n" +
                "\n" +
                "I shall depart for the latter town in a fortnight or three weeks; and my\n" +
                "intention is to hire a ship there, which can easily be done by paying the\n" +
                "insurance for the owner, and to engage as many sailors as I think necessary\n" +
                "among those who are accustomed to the whale-fishing. I do not intend to\n" +
                "sail until the month of June; and when shall I return? Ah, dear sister, how\n" +
                "can I answer this question? If I succeed, many, many months, perhaps years,\n" +
                "will pass before you and I may meet. If I fail, you will see me again soon,\n" +
                "or never.\n" +
                "\n" +
                "Farewell, my dear, excellent Margaret. Heaven shower down blessings on you,\n" +
                "and save me, that I may again and again testify my gratitude for all your\n" +
                "love and kindness.\n" +
                "\n" +
                "Your affectionate brother,\n" +
                "\n" +
                "R. Walton\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Letter 2\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "_To Mrs. Saville, England._\n" +
                "\n" +
                "\n" +
                "Archangel, 28th March, 17—.\n" +
                "\n" +
                "\n" +
                "How slowly the time passes here, encompassed as I am by frost and snow!\n" +
                "Yet a second step is taken towards my enterprise.  I have hired a\n" +
                "vessel and am occupied in collecting my sailors; those whom I have\n" +
                "already engaged appear to be men on whom I can depend and are certainly\n" +
                "possessed of dauntless courage.\n" +
                "\n" +
                "But I have one want which I have never yet been able to satisfy, and the\n" +
                "absence of the object of which I now feel as a most severe evil, I have no\n" +
                "friend, Margaret: when I am glowing with the enthusiasm of success, there\n" +
                "will be none to participate my joy; if I am assailed by disappointment, no\n" +
                "one will endeavour to sustain me in dejection. I shall commit my thoughts\n" +
                "to paper, it is true; but that is a poor medium for the communication of\n" +
                "feeling. I desire the company of a man who could sympathise with me, whose\n" +
                "eyes would reply to mine. You may deem me romantic, my dear sister, but I\n" +
                "bitterly feel the want of a friend. I have no one near me, gentle yet\n" +
                "courageous, possessed of a cultivated as well as of a capacious mind, whose\n" +
                "tastes are like my own, to approve or amend my plans. How would such a\n" +
                "friend repair the faults of your poor brother! I am too ardent in execution\n" +
                "and too impatient of difficulties. But it is a still greater evil to me\n" +
                "that I am self-educated: for the first fourteen years of my life I ran wild\n" +
                "on a common and read nothing but our Uncle Thomas’ books of voyages.\n" +
                "At that age I became acquainted with the celebrated poets of our own\n" +
                "country; but it was only when it had ceased to be in my power to derive its\n" +
                "most important benefits from such a conviction that I perceived the\n" +
                "necessity of becoming acquainted with more languages than that of my native\n" +
                "country. Now I am twenty-eight and am in reality more illiterate than many\n" +
                "schoolboys of fifteen. It is true that I have thought more and that my\n" +
                "daydreams are more extended and magnificent, but they want (as the painters\n" +
                "call it) _keeping;_ and I greatly need a friend who would have sense\n" +
                "enough not to despise me as romantic, and affection enough for me to\n" +
                "endeavour to regulate my mind.\n" +
                "\n" +
                "Well, these are useless complaints; I shall certainly find no friend on the\n" +
                "wide ocean, nor even here in Archangel, among merchants and seamen. Yet\n" +
                "some feelings, unallied to the dross of human nature, beat even in these\n" +
                "rugged bosoms. My lieutenant, for instance, is a man of wonderful courage\n" +
                "and enterprise; he is madly desirous of glory, or rather, to word my phrase\n" +
                "more characteristically, of advancement in his profession. He is an\n" +
                "Englishman, and in the midst of national and professional prejudices,\n" +
                "unsoftened by cultivation, retains some of the noblest endowments of\n" +
                "humanity. I first became acquainted with him on board a whale vessel;\n" +
                "finding that he was unemployed in this city, I easily engaged him to assist\n" +
                "in my enterprise.\n" +
                "\n" +
                "The master is a person of an excellent disposition and is remarkable in the\n" +
                "ship for his gentleness and the mildness of his discipline. This\n" +
                "circumstance, added to his well-known integrity and dauntless courage, made\n" +
                "me very desirous to engage him. A youth passed in solitude, my best years\n" +
                "spent under your gentle and feminine fosterage, has so refined the\n" +
                "groundwork of my character that I cannot overcome an intense distaste to\n" +
                "the usual brutality exercised on board ship: I have never believed it to be\n" +
                "necessary, and when I heard of a mariner equally noted for his kindliness\n" +
                "of heart and the respect and obedience paid to him by his crew, I felt\n" +
                "myself peculiarly fortunate in being able to secure his services. I heard\n" +
                "of him first in rather a romantic manner, from a lady who owes to him the\n" +
                "happiness of her life. This, briefly, is his story. Some years ago he loved\n" +
                "a young Russian lady of moderate fortune, and having amassed a considerable\n" +
                "sum in prize-money, the father of the girl consented to the match. He saw\n" +
                "his mistress once before the destined ceremony; but she was bathed in\n" +
                "tears, and throwing herself at his feet, entreated him to spare her,\n" +
                "confessing at the same time that she loved another, but that he was poor,\n" +
                "and that her father would never consent to the union. My generous friend\n" +
                "reassured the suppliant, and on being informed of the name of her lover,\n" +
                "instantly abandoned his pursuit. He had already bought a farm with his\n" +
                "money, on which he had designed to pass the remainder of his life; but he\n" +
                "bestowed the whole on his rival, together with the remains of his\n" +
                "prize-money to purchase stock, and then himself solicited the young\n" +
                "woman’s father to consent to her marriage with her lover. But the old\n" +
                "man decidedly refused, thinking himself bound in honour to my friend, who,\n" +
                "when he found the father inexorable, quitted his country, nor returned\n" +
                "until he heard that his former mistress was married according to her\n" +
                "inclinations. “What a noble fellow!” you will exclaim. He is\n" +
                "so; but then he is wholly uneducated: he is as silent as a Turk, and a kind\n" +
                "of ignorant carelessness attends him, which, while it renders his conduct\n" +
                "the more astonishing, detracts from the interest and sympathy which\n" +
                "otherwise he would command.\n" +
                "\n" +
                "Yet do not suppose, because I complain a little or because I can\n" +
                "conceive a consolation for my toils which I may never know, that I am\n" +
                "wavering in my resolutions.  Those are as fixed as fate, and my voyage\n" +
                "is only now delayed until the weather shall permit my embarkation.  The\n" +
                "winter has been dreadfully severe, but the spring promises well, and it\n" +
                "is considered as a remarkably early season, so that perhaps I may sail\n" +
                "sooner than I expected.  I shall do nothing rashly:  you know me\n" +
                "sufficiently to confide in my prudence and considerateness whenever the\n" +
                "safety of others is committed to my care.\n" +
                "\n" +
                "I cannot describe to you my sensations on the near prospect of my\n" +
                "undertaking.  It is impossible to communicate to you a conception of\n" +
                "the trembling sensation, half pleasurable and half fearful, with which\n" +
                "I am preparing to depart.  I am going to unexplored regions, to “the\n" +
                "land of mist and snow,” but I shall kill no albatross; therefore do not\n" +
                "be alarmed for my safety or if I should come back to you as worn and\n" +
                "woeful as the “Ancient Mariner.”  You will smile at my allusion, but I\n" +
                "will disclose a secret.  I have often attributed my attachment to, my\n" +
                "passionate enthusiasm for, the dangerous mysteries of ocean to that\n" +
                "production of the most imaginative of modern poets.  There is something\n" +
                "at work in my soul which I do not understand.  I am practically\n" +
                "industrious—painstaking, a workman to execute with perseverance and\n" +
                "labour—but besides this there is a love for the marvellous, a belief\n" +
                "in the marvellous, intertwined in all my projects, which hurries me out\n" +
                "of the common pathways of men, even to the wild sea and unvisited\n" +
                "regions I am about to explore.\n" +
                "\n" +
                "But to return to dearer considerations. Shall I meet you again, after\n" +
                "having traversed immense seas, and returned by the most southern cape of\n" +
                "Africa or America?  I dare not expect such success, yet I cannot bear to\n" +
                "look on the reverse of the picture.  Continue for the present to write to\n" +
                "me by every opportunity: I may receive your letters on some occasions when\n" +
                "I need them most to support my spirits.  I love you very tenderly. \n" +
                "Remember me with affection, should you never hear from me again.\n" +
                "\n" +
                "Your affectionate brother,\n" +
                "  Robert Walton\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Letter 3\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "_To Mrs. Saville, England._\n" +
                "\n" +
                "\n" +
                "July 7th, 17—.\n" +
                "\n" +
                "\n" +
                "My dear Sister,\n" +
                "\n" +
                "I write a few lines in haste to say that I am safe—and well advanced\n" +
                "on my voyage.  This letter will reach England by a merchantman now on\n" +
                "its homeward voyage from Archangel; more fortunate than I, who may not\n" +
                "see my native land, perhaps, for many years.  I am, however, in good\n" +
                "spirits:  my men are bold and apparently firm of purpose, nor do the\n" +
                "floating sheets of ice that continually pass us, indicating the dangers\n" +
                "of the region towards which we are advancing, appear to dismay them. We\n" +
                "have already reached a very high latitude; but it is the height of\n" +
                "summer, and although not so warm as in England, the southern gales,\n" +
                "which blow us speedily towards those shores which I so ardently desire\n" +
                "to attain, breathe a degree of renovating warmth which I had not\n" +
                "expected.\n" +
                "\n" +
                "No incidents have hitherto befallen us that would make a figure in a\n" +
                "letter.  One or two stiff gales and the springing of a leak are\n" +
                "accidents which experienced navigators scarcely remember to record, and\n" +
                "I shall be well content if nothing worse happen to us during our voyage.\n" +
                "\n" +
                "Adieu, my dear Margaret.  Be assured that for my own sake, as well as\n" +
                "yours, I will not rashly encounter danger.  I will be cool,\n" +
                "persevering, and prudent.\n" +
                "\n" +
                "But success _shall_ crown my endeavours.  Wherefore not?  Thus far I\n" +
                "have gone, tracing a secure way over the pathless seas, the very stars\n" +
                "themselves being witnesses and testimonies of my triumph.  Why not\n" +
                "still proceed over the untamed yet obedient element?  What can stop the\n" +
                "determined heart and resolved will of man?\n" +
                "\n" +
                "My swelling heart involuntarily pours itself out thus.  But I must\n" +
                "finish.  Heaven bless my beloved sister!\n" +
                "\n" +
                "R.W.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Letter 4\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "_To Mrs. Saville, England._\n" +
                "\n" +
                "\n" +
                "August 5th, 17—.\n" +
                "\n" +
                "\n" +
                "So strange an accident has happened to us that I cannot forbear\n" +
                "recording it, although it is very probable that you will see me before\n" +
                "these papers can come into your possession.\n" +
                "\n" +
                "Last Monday (July 31st) we were nearly surrounded by ice, which closed\n" +
                "in the ship on all sides, scarcely leaving her the sea-room in which\n" +
                "she floated.  Our situation was somewhat dangerous, especially as we\n" +
                "were compassed round by a very thick fog.  We accordingly lay to,\n" +
                "hoping that some change would take place in the atmosphere and weather.\n" +
                "\n" +
                "About two o’clock the mist cleared away, and we beheld, stretched out\n" +
                "in every direction, vast and irregular plains of ice, which seemed to\n" +
                "have no end.  Some of my comrades groaned, and my own mind began to\n" +
                "grow watchful with anxious thoughts, when a strange sight suddenly\n" +
                "attracted our attention and diverted our solicitude from our own\n" +
                "situation.  We perceived a low carriage, fixed on a sledge and drawn by\n" +
                "dogs, pass on towards the north, at the distance of half a mile; a\n" +
                "being which had the shape of a man, but apparently of gigantic stature,\n" +
                "sat in the sledge and guided the dogs.  We watched the rapid progress\n" +
                "of the traveller with our telescopes until he was lost among the\n" +
                "distant inequalities of the ice.\n" +
                "\n" +
                "This appearance excited our unqualified wonder. We were, as we believed,\n" +
                "many hundred miles from any land; but this apparition seemed to denote that\n" +
                "it was not, in reality, so distant as we had supposed. Shut in, however, by\n" +
                "ice, it was impossible to follow his track, which we had observed with the\n" +
                "greatest attention.\n" +
                "\n" +
                "About two hours after this occurrence we heard the ground sea, and before\n" +
                "night the ice broke and freed our ship. We, however, lay to until the\n" +
                "morning, fearing to encounter in the dark those large loose masses which\n" +
                "float about after the breaking up of the ice. I profited of this time to\n" +
                "rest for a few hours.\n" +
                "\n" +
                "In the morning, however, as soon as it was light, I went upon deck and\n" +
                "found all the sailors busy on one side of the vessel, apparently\n" +
                "talking to someone in the sea.  It was, in fact, a sledge, like that we\n" +
                "had seen before, which had drifted towards us in the night on a large\n" +
                "fragment of ice.  Only one dog remained alive; but there was a human\n" +
                "being within it whom the sailors were persuading to enter the vessel.\n" +
                "He was not, as the other traveller seemed to be, a savage inhabitant of\n" +
                "some undiscovered island, but a European.  When I appeared on deck the\n" +
                "master said, “Here is our captain, and he will not allow you to perish\n" +
                "on the open sea.”\n" +
                "\n" +
                "On perceiving me, the stranger addressed me in English, although with a\n" +
                "foreign accent.  “Before I come on board your vessel,” said he,\n" +
                "“will you have the kindness to inform me whither you are bound?”\n" +
                "\n" +
                "You may conceive my astonishment on hearing such a question addressed\n" +
                "to me from a man on the brink of destruction and to whom I should have\n" +
                "supposed that my vessel would have been a resource which he would not\n" +
                "have exchanged for the most precious wealth the earth can afford.  I\n" +
                "replied, however, that we were on a voyage of discovery towards the\n" +
                "northern pole.\n" +
                "\n" +
                "Upon hearing this he appeared satisfied and consented to come on board.\n" +
                "Good God!  Margaret, if you had seen the man who thus capitulated for\n" +
                "his safety, your surprise would have been boundless.  His limbs were\n" +
                "nearly frozen, and his body dreadfully emaciated by fatigue and\n" +
                "suffering.  I never saw a man in so wretched a condition.  We attempted\n" +
                "to carry him into the cabin, but as soon as he had quitted the fresh\n" +
                "air he fainted.  We accordingly brought him back to the deck and\n" +
                "restored him to animation by rubbing him with brandy and forcing him to\n" +
                "swallow a small quantity.  As soon as he showed signs of life we\n" +
                "wrapped him up in blankets and placed him near the chimney of the\n" +
                "kitchen stove.  By slow degrees he recovered and ate a little soup,\n" +
                "which restored him wonderfully.\n" +
                "\n" +
                "Two days passed in this manner before he was able to speak, and I often\n" +
                "feared that his sufferings had deprived him of understanding.  When he\n" +
                "had in some measure recovered, I removed him to my own cabin and\n" +
                "attended on him as much as my duty would permit.  I never saw a more\n" +
                "interesting creature:  his eyes have generally an expression of\n" +
                "wildness, and even madness, but there are moments when, if anyone\n" +
                "performs an act of kindness towards him or does him any the most\n" +
                "trifling service, his whole countenance is lighted up, as it were, with\n" +
                "a beam of benevolence and sweetness that I never saw equalled.  But he\n" +
                "is generally melancholy and despairing, and sometimes he gnashes his\n" +
                "teeth, as if impatient of the weight of woes that oppresses him.\n" +
                "\n" +
                "When my guest was a little recovered I had great trouble to keep off\n" +
                "the men, who wished to ask him a thousand questions; but I would not\n" +
                "allow him to be tormented by their idle curiosity, in a state of body\n" +
                "and mind whose restoration evidently depended upon entire repose.\n" +
                "Once, however, the lieutenant asked why he had come so far upon the ice\n" +
                "in so strange a vehicle.\n" +
                "\n" +
                "His countenance instantly assumed an aspect of the deepest gloom, and\n" +
                "he replied, “To seek one who fled from me.”\n" +
                "\n" +
                "“And did the man whom you pursued travel in the same fashion?”\n" +
                "\n" +
                "“Yes.”\n" +
                "\n" +
                "“Then I fancy we have seen him, for the day before we picked you up we\n" +
                "saw some dogs drawing a sledge, with a man in it, across the ice.”\n" +
                "\n" +
                "This aroused the stranger’s attention, and he asked a multitude of\n" +
                "questions concerning the route which the dæmon, as he called him, had\n" +
                "pursued.  Soon after, when he was alone with me, he said, “I have,\n" +
                "doubtless, excited your curiosity, as well as that of these good\n" +
                "people; but you are too considerate to make inquiries.”\n" +
                "\n" +
                "“Certainly; it would indeed be very impertinent and inhuman in me to\n" +
                "trouble you with any inquisitiveness of mine.”\n" +
                "\n" +
                "“And yet you rescued me from a strange and perilous situation; you have\n" +
                "benevolently restored me to life.”\n" +
                "\n" +
                "Soon after this he inquired if I thought that the breaking up of the\n" +
                "ice had destroyed the other sledge.  I replied that I could not answer\n" +
                "with any degree of certainty, for the ice had not broken until near\n" +
                "midnight, and the traveller might have arrived at a place of safety\n" +
                "before that time; but of this I could not judge.\n" +
                "\n" +
                "From this time a new spirit of life animated the decaying frame of the\n" +
                "stranger. He manifested the greatest eagerness to be upon deck to watch for\n" +
                "the sledge which had before appeared; but I have persuaded him to remain in\n" +
                "the cabin, for he is far too weak to sustain the rawness of the atmosphere.\n" +
                "I have promised that someone should watch for him and give him instant\n" +
                "notice if any new object should appear in sight.\n" +
                "\n" +
                "Such is my journal of what relates to this strange occurrence up to the\n" +
                "present day.  The stranger has gradually improved in health but is very\n" +
                "silent and appears uneasy when anyone except myself enters his cabin.\n" +
                "Yet his manners are so conciliating and gentle that the sailors are all\n" +
                "interested in him, although they have had very little communication\n" +
                "with him.  For my own part, I begin to love him as a brother, and his\n" +
                "constant and deep grief fills me with sympathy and compassion.  He must\n" +
                "have been a noble creature in his better days, being even now in wreck\n" +
                "so attractive and amiable.\n" +
                "\n" +
                "I said in one of my letters, my dear Margaret, that I should find no friend\n" +
                "on the wide ocean; yet I have found a man who, before his spirit had been\n" +
                "broken by misery, I should have been happy to have possessed as the brother\n" +
                "of my heart.\n" +
                "\n" +
                "I shall continue my journal concerning the stranger at intervals,\n" +
                "should I have any fresh incidents to record.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "August 13th, 17—.\n" +
                "\n" +
                "\n" +
                "My affection for my guest increases every day.  He excites at once my\n" +
                "admiration and my pity to an astonishing degree.  How can I see so\n" +
                "noble a creature destroyed by misery without feeling the most poignant\n" +
                "grief?  He is so gentle, yet so wise; his mind is so cultivated, and\n" +
                "when he speaks, although his words are culled with the choicest art,\n" +
                "yet they flow with rapidity and unparalleled eloquence.\n" +
                "\n" +
                "He is now much recovered from his illness and is continually on the deck,\n" +
                "apparently watching for the sledge that preceded his own. Yet, although\n" +
                "unhappy, he is not so utterly occupied by his own misery but that he\n" +
                "interests himself deeply in the projects of others. He has frequently\n" +
                "conversed with me on mine, which I have communicated to him without\n" +
                "disguise. He entered attentively into all my arguments in favour of my\n" +
                "eventual success and into every minute detail of the measures I had taken\n" +
                "to secure it. I was easily led by the sympathy which he evinced to use the\n" +
                "language of my heart, to give utterance to the burning ardour of my soul\n" +
                "and to say, with all the fervour that warmed me, how gladly I would\n" +
                "sacrifice my fortune, my existence, my every hope, to the furtherance of my\n" +
                "enterprise. One man’s life or death were but a small price to pay for\n" +
                "the acquirement of the knowledge which I sought, for the dominion I should\n" +
                "acquire and transmit over the elemental foes of our race. As I spoke, a\n" +
                "dark gloom spread over my listener’s countenance. At first I\n" +
                "perceived that he tried to suppress his emotion; he placed his hands before\n" +
                "his eyes, and my voice quivered and failed me as I beheld tears trickle\n" +
                "fast from between his fingers; a groan burst from his heaving breast. I\n" +
                "paused; at length he spoke, in broken accents: “Unhappy man! Do you\n" +
                "share my madness? Have you drunk also of the intoxicating draught? Hear me;\n" +
                "let me reveal my tale, and you will dash the cup from your lips!”\n" +
                "\n" +
                "Such words, you may imagine, strongly excited my curiosity; but the\n" +
                "paroxysm of grief that had seized the stranger overcame his weakened\n" +
                "powers, and many hours of repose and tranquil conversation were\n" +
                "necessary to restore his composure.\n" +
                "\n" +
                "Having conquered the violence of his feelings, he appeared to despise\n" +
                "himself for being the slave of passion; and quelling the dark tyranny of\n" +
                "despair, he led me again to converse concerning myself personally. He asked\n" +
                "me the history of my earlier years. The tale was quickly told, but it\n" +
                "awakened various trains of reflection. I spoke of my desire of finding a\n" +
                "friend, of my thirst for a more intimate sympathy with a fellow mind than\n" +
                "had ever fallen to my lot, and expressed my conviction that a man could\n" +
                "boast of little happiness who did not enjoy this blessing.\n" +
                "\n" +
                "“I agree with you,” replied the stranger; “we are\n" +
                "unfashioned creatures, but half made up, if one wiser, better, dearer than\n" +
                "ourselves—such a friend ought to be—do not lend his aid to\n" +
                "perfectionate our weak and faulty natures. I once had a friend, the most\n" +
                "noble of human creatures, and am entitled, therefore, to judge respecting\n" +
                "friendship. You have hope, and the world before you, and have no cause for\n" +
                "despair. But I—I have lost everything and cannot begin life\n" +
                "anew.”\n" +
                "\n" +
                "As he said this his countenance became expressive of a calm, settled\n" +
                "grief that touched me to the heart.  But he was silent and presently\n" +
                "retired to his cabin.\n" +
                "\n" +
                "Even broken in spirit as he is, no one can feel more deeply than he\n" +
                "does the beauties of nature.  The starry sky, the sea, and every sight\n" +
                "afforded by these wonderful regions seem still to have the power of\n" +
                "elevating his soul from earth.  Such a man has a double existence:  he\n" +
                "may suffer misery and be overwhelmed by disappointments, yet when he\n" +
                "has retired into himself, he will be like a celestial spirit that has a\n" +
                "halo around him, within whose circle no grief or folly ventures.\n" +
                "\n" +
                "Will you smile at the enthusiasm I express concerning this divine\n" +
                "wanderer?  You would not if you saw him.  You have been tutored and\n" +
                "refined by books and retirement from the world, and you are therefore\n" +
                "somewhat fastidious; but this only renders you the more fit to\n" +
                "appreciate the extraordinary merits of this wonderful man.  Sometimes I\n" +
                "have endeavoured to discover what quality it is which he possesses that\n" +
                "elevates him so immeasurably above any other person I ever knew.  I\n" +
                "believe it to be an intuitive discernment, a quick but never-failing\n" +
                "power of judgment, a penetration into the causes of things, unequalled\n" +
                "for clearness and precision; add to this a facility of expression and a\n" +
                "voice whose varied intonations are soul-subduing music.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "August 19th, 17—.\n" +
                "\n" +
                "\n" +
                "Yesterday the stranger said to me, “You may easily perceive, Captain\n" +
                "Walton, that I have suffered great and unparalleled misfortunes.  I had\n" +
                "determined at one time that the memory of these evils should die with\n" +
                "me, but you have won me to alter my determination.  You seek for\n" +
                "knowledge and wisdom, as I once did; and I ardently hope that the\n" +
                "gratification of your wishes may not be a serpent to sting you, as mine\n" +
                "has been.  I do not know that the relation of my disasters will be\n" +
                "useful to you; yet, when I reflect that you are pursuing the same\n" +
                "course, exposing yourself to the same dangers which have rendered me\n" +
                "what I am, I imagine that you may deduce an apt moral from my tale, one\n" +
                "that may direct you if you succeed in your undertaking and console you\n" +
                "in case of failure.  Prepare to hear of occurrences which are usually\n" +
                "deemed marvellous.  Were we among the tamer scenes of nature I might\n" +
                "fear to encounter your unbelief, perhaps your ridicule; but many things\n" +
                "will appear possible in these wild and mysterious regions which would\n" +
                "provoke the laughter of those unacquainted with the ever-varied powers\n" +
                "of nature; nor can I doubt but that my tale conveys in its series\n" +
                "internal evidence of the truth of the events of which it is composed.”\n" +
                "\n" +
                "You may easily imagine that I was much gratified by the offered\n" +
                "communication, yet I could not endure that he should renew his grief by\n" +
                "a recital of his misfortunes.  I felt the greatest eagerness to hear\n" +
                "the promised narrative, partly from curiosity and partly from a strong\n" +
                "desire to ameliorate his fate if it were in my power.  I expressed\n" +
                "these feelings in my answer.\n" +
                "\n" +
                "“I thank you,” he replied, “for your sympathy, but it is\n" +
                "useless; my fate is nearly fulfilled.  I wait but for one event, and then I\n" +
                "shall repose in peace.  I understand your feeling,” continued he,\n" +
                "perceiving that I wished to interrupt him; “but you are mistaken, my\n" +
                "friend, if thus you will allow me to name you; nothing can alter my\n" +
                "destiny; listen to my history, and you will perceive how irrevocably it is\n" +
                "determined.”\n" +
                "\n" +
                "He then told me that he would commence his narrative the next day when I\n" +
                "should be at leisure. This promise drew from me the warmest thanks. I have\n" +
                "resolved every night, when I am not imperatively occupied by my duties, to\n" +
                "record, as nearly as possible in his own words, what he has related during\n" +
                "the day. If I should be engaged, I will at least make notes. This\n" +
                "manuscript will doubtless afford you the greatest pleasure; but to me, who\n" +
                "know him, and who hear it from his own lips—with what interest and\n" +
                "sympathy shall I read it in some future day! Even now, as I commence my\n" +
                "task, his full-toned voice swells in my ears; his lustrous eyes dwell on me\n" +
                "with all their melancholy sweetness; I see his thin hand raised in\n" +
                "animation, while the lineaments of his face are irradiated by the soul\n" +
                "within. Strange and harrowing must be his story, frightful the storm which\n" +
                "embraced the gallant vessel on its course and wrecked it—thus!\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Chapter 1\n" +
                "\n" +
                "I am by birth a Genevese, and my family is one of the most\n" +
                "distinguished of that republic.  My ancestors had been for many years\n" +
                "counsellors and syndics, and my father had filled several public\n" +
                "situations with honour and reputation.  He was respected by all who\n" +
                "knew him for his integrity and indefatigable attention to public\n" +
                "business.  He passed his younger days perpetually occupied by the\n" +
                "affairs of his country; a variety of circumstances had prevented his\n" +
                "marrying early, nor was it until the decline of life that he became a\n" +
                "husband and the father of a family.\n" +
                "\n" +
                "As the circumstances of his marriage illustrate his character, I cannot\n" +
                "refrain from relating them.  One of his most intimate friends was a\n" +
                "merchant who, from a flourishing state, fell, through numerous\n" +
                "mischances, into poverty.  This man, whose name was Beaufort, was of a\n" +
                "proud and unbending disposition and could not bear to live in poverty\n" +
                "and oblivion in the same country where he had formerly been\n" +
                "distinguished for his rank and magnificence.  Having paid his debts,\n" +
                "therefore, in the most honourable manner, he retreated with his\n" +
                "daughter to the town of Lucerne, where he lived unknown and in\n" +
                "wretchedness.  My father loved Beaufort with the truest friendship and\n" +
                "was deeply grieved by his retreat in these unfortunate circumstances.\n" +
                "He bitterly deplored the false pride which led his friend to a conduct\n" +
                "so little worthy of the affection that united them.  He lost no time in\n" +
                "endeavouring to seek him out, with the hope of persuading him to begin\n" +
                "the world again through his credit and assistance.\n" +
                "\n" +
                "Beaufort had taken effectual measures to conceal himself, and it was ten\n" +
                "months before my father discovered his abode. Overjoyed at this discovery,\n" +
                "he hastened to the house, which was situated in a mean street near the\n" +
                "Reuss. But when he entered, misery and despair alone welcomed him. Beaufort\n" +
                "had saved but a very small sum of money from the wreck of his fortunes, but\n" +
                "it was sufficient to provide him with sustenance for some months, and in\n" +
                "the meantime he hoped to procure some respectable employment in a\n" +
                "merchant’s house. The interval was, consequently, spent in inaction;\n" +
                "his grief only became more deep and rankling when he had leisure for\n" +
                "reflection, and at length it took so fast hold of his mind that at the end\n" +
                "of three months he lay on a bed of sickness, incapable of any exertion.\n" +
                "\n" +
                "His daughter attended him with the greatest tenderness, but she saw\n" +
                "with despair that their little fund was rapidly decreasing and that\n" +
                "there was no other prospect of support.  But Caroline Beaufort\n" +
                "possessed a mind of an uncommon mould, and her courage rose to support\n" +
                "her in her adversity.  She procured plain work; she plaited straw and\n" +
                "by various means contrived to earn a pittance scarcely sufficient to\n" +
                "support life.\n" +
                "\n" +
                "Several months passed in this manner.  Her father grew worse; her time\n" +
                "was more entirely occupied in attending him; her means of subsistence\n" +
                "decreased; and in the tenth month her father died in her arms, leaving\n" +
                "her an orphan and a beggar.  This last blow overcame her, and she knelt\n" +
                "by Beaufort’s coffin weeping bitterly, when my father entered the\n" +
                "chamber.  He came like a protecting spirit to the poor girl, who\n" +
                "committed herself to his care; and after the interment of his friend he\n" +
                "conducted her to Geneva and placed her under the protection of a\n" +
                "relation.  Two years after this event Caroline became his wife.\n" +
                "\n" +
                "There was a considerable difference between the ages of my parents, but\n" +
                "this circumstance seemed to unite them only closer in bonds of devoted\n" +
                "affection.  There was a sense of justice in my father’s upright mind\n" +
                "which rendered it necessary that he should approve highly to love\n" +
                "strongly.  Perhaps during former years he had suffered from the\n" +
                "late-discovered unworthiness of one beloved and so was disposed to set\n" +
                "a greater value on tried worth.  There was a show of gratitude and\n" +
                "worship in his attachment to my mother, differing wholly from the\n" +
                "doting fondness of age, for it was inspired by reverence for her\n" +
                "virtues and a desire to be the means of, in some degree, recompensing\n" +
                "her for the sorrows she had endured, but which gave inexpressible grace\n" +
                "to his behaviour to her.  Everything was made to yield to her wishes\n" +
                "and her convenience.  He strove to shelter her, as a fair exotic is\n" +
                "sheltered by the gardener, from every rougher wind and to surround her\n" +
                "with all that could tend to excite pleasurable emotion in her soft and\n" +
                "benevolent mind.  Her health, and even the tranquillity of her hitherto\n" +
                "constant spirit, had been shaken by what she had gone through.  During\n" +
                "the two years that had elapsed previous to their marriage my father had\n" +
                "gradually relinquished all his public functions; and immediately after\n" +
                "their union they sought the pleasant climate of Italy, and the change\n" +
                "of scene and interest attendant on a tour through that land of wonders,\n" +
                "as a restorative for her weakened frame.\n" +
                "\n" +
                "From Italy they visited Germany and France. I, their eldest child, was born\n" +
                "at Naples, and as an infant accompanied them in their rambles. I remained\n" +
                "for several years their only child. Much as they were attached to each\n" +
                "other, they seemed to draw inexhaustible stores of affection from a very\n" +
                "mine of love to bestow them upon me. My mother’s tender caresses and\n" +
                "my father’s smile of benevolent pleasure while regarding me are my\n" +
                "first recollections. I was their plaything and their idol, and something\n" +
                "better—their child, the innocent and helpless creature bestowed on\n" +
                "them by Heaven, whom to bring up to good, and whose future lot it was in\n" +
                "their hands to direct to happiness or misery, according as they fulfilled\n" +
                "their duties towards me. With this deep consciousness of what they owed\n" +
                "towards the being to which they had given life, added to the active spirit\n" +
                "of tenderness that animated both, it may be imagined that while during\n" +
                "every hour of my infant life I received a lesson of patience, of charity,\n" +
                "and of self-control, I was so guided by a silken cord that all seemed but\n" +
                "one train of enjoyment to me.\n" +
                "\n" +
                "For a long time I was their only care. My mother had much desired to have a\n" +
                "daughter, but I continued their single offspring. When I was about five\n" +
                "years old, while making an excursion beyond the frontiers of Italy, they\n" +
                "passed a week on the shores of the Lake of Como. Their benevolent\n" +
                "disposition often made them enter the cottages of the poor. This, to my\n" +
                "mother, was more than a duty; it was a necessity, a\n" +
                "passion—remembering what she had suffered, and how she had been\n" +
                "relieved—for her to act in her turn the guardian angel to the\n" +
                "afflicted. During one of their walks a poor cot in the foldings of a vale\n" +
                "attracted their notice as being singularly disconsolate, while the number\n" +
                "of half-clothed children gathered about it spoke of penury in its worst\n" +
                "shape. One day, when my father had gone by himself to Milan, my mother,\n" +
                "accompanied by me, visited this abode. She found a peasant and his wife,\n" +
                "hard working, bent down by care and labour, distributing a scanty meal to\n" +
                "five hungry babes. Among these there was one which attracted my mother far\n" +
                "above all the rest. She appeared of a different stock. The four others were\n" +
                "dark-eyed, hardy little vagrants; this child was thin and very fair. Her\n" +
                "hair was the brightest living gold, and despite the poverty of her\n" +
                "clothing, seemed to set a crown of distinction on her head. Her brow was\n" +
                "clear and ample, her blue eyes cloudless, and her lips and the moulding of\n" +
                "her face so expressive of sensibility and sweetness that none could behold\n" +
                "her without looking on her as of a distinct species, a being heaven-sent,\n" +
                "and bearing a celestial stamp in all her features.\n" +
                "\n" +
                "The peasant woman, perceiving that my mother fixed eyes of wonder and\n" +
                "admiration on this lovely girl, eagerly communicated her history. She was\n" +
                "not her child, but the daughter of a Milanese nobleman. Her mother was a\n" +
                "German and had died on giving her birth. The infant had been placed with\n" +
                "these good people to nurse: they were better off then. They had not been\n" +
                "long married, and their eldest child was but just born. The father of their\n" +
                "charge was one of those Italians nursed in the memory of the antique glory\n" +
                "of Italy—one among the _schiavi ognor frementi,_ who exerted\n" +
                "himself to obtain the liberty of his country. He became the victim of its\n" +
                "weakness. Whether he had died or still lingered in the dungeons of Austria\n" +
                "was not known. His property was confiscated; his child became an orphan and\n" +
                "a beggar. She continued with her foster parents and bloomed in their rude\n" +
                "abode, fairer than a garden rose among dark-leaved brambles.\n" +
                "\n" +
                "When my father returned from Milan, he found playing with me in the hall of\n" +
                "our villa a child fairer than pictured cherub—a creature who seemed\n" +
                "to shed radiance from her looks and whose form and motions were lighter\n" +
                "than the chamois of the hills. The apparition was soon explained. With his\n" +
                "permission my mother prevailed on her rustic guardians to yield their\n" +
                "charge to her. They were fond of the sweet orphan. Her presence had seemed\n" +
                "a blessing to them, but it would be unfair to her to keep her in poverty\n" +
                "and want when Providence afforded her such powerful protection. They\n" +
                "consulted their village priest, and the result was that Elizabeth Lavenza\n" +
                "became the inmate of my parents’ house—my more than\n" +
                "sister—the beautiful and adored companion of all my occupations and\n" +
                "my pleasures.\n" +
                "\n" +
                "Everyone loved Elizabeth.  The passionate and almost reverential\n" +
                "attachment with which all regarded her became, while I shared it, my\n" +
                "pride and my delight.  On the evening previous to her being brought to\n" +
                "my home, my mother had said playfully, “I have a pretty present for my\n" +
                "Victor—tomorrow he shall have it.”  And when, on the morrow, she\n" +
                "presented Elizabeth to me as her promised gift, I, with childish\n" +
                "seriousness, interpreted her words literally and looked upon Elizabeth\n" +
                "as mine—mine to protect, love, and cherish.  All praises bestowed on\n" +
                "her I received as made to a possession of my own.  We called each other\n" +
                "familiarly by the name of cousin.  No word, no expression could body\n" +
                "forth the kind of relation in which she stood to me—my more than\n" +
                "sister, since till death she was to be mine only.\n";

        return Book;
    }
    public static String getotherbook()
    {
        String Book ="";


        return Book;
    }
}

