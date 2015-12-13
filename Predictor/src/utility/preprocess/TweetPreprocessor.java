package utility.preprocess;

import java.io.*;

/**
 * Created by JunjieW on 2015/12/13.
 */
public class TweetPreprocessor {
    public static void combineDataCrossMonth(String folderOfPreviousEnd, String folderOfLaterBegin) {
        try {
            File[] previousEnd = new File(folderOfPreviousEnd).listFiles();
            File[] laterBegin = new File(folderOfLaterBegin).listFiles();

            if ( previousEnd.length != laterBegin.length) {
                System.out.println("The folders to combine have different number of files");
                return;
            }
            BufferedReader in = null;
            BufferedWriter out = null;
            String line = "";
            for ( int i = 0; i < previousEnd.length; i++) {
                in = new BufferedReader(new FileReader(laterBegin[i]));
                out = new BufferedWriter(new FileWriter(previousEnd[i], true));
                int count = 0;
                line = in.readLine();
                while(line != null) {
                    count ++;
                    out.append(line);
                    line = in.readLine();
                    out.flush();
                }
                out.close();
                in.close();

                in = new BufferedReader(new FileReader(previousEnd[i]));
                while(in.readLine() != null) {
                    count ++;
                }
                System.out.println(previousEnd[i].getName() + "has " + count + " mentions" );
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String previous = "./2009-06-end-07-05-result";
        String later = "./2009-07-05-result";
        TweetPreprocessor.combineDataCrossMonth(previous,later);
    }
}
