package utility.preprocess;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JunjieW on 2015/12/13.
 */
public class TweetPreprocessor {
    private static String[] spamStrings = {
        "Now Playing", "Check this out", "I just voted", "I favorited a YouTube video", "Check this video out",
        "Lyrics included", "Listening to", "I uploaded a YouTube video"
    };

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
            e.printStackTrace();
        }
    }

    // http://stackoverflow.com/a/12950893
    private static String removeUrl(String str) {
        //System.out.println(str);
        // TODO: characters that is not utf-8 has weird gone
        String str_urls_free = str.replaceAll("([\\s*]http.*?[\\s*])", " ").replaceAll("([\\s*]http.*)", " ").replaceAll("(http.*?[\\s*])", "");
        //System.out.println(str_urls_free);
        return str_urls_free + ".\n";
    }

    private static boolean isSpam(String line) {
        boolean result = false;
        for (String spamStr : TweetPreprocessor.spamStrings) {
            if (Pattern.compile(Pattern.quote(spamStr), Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static void batchRemoveUrl() {
        File[] folders = new File("./Mentions").listFiles();
        for (File folder: folders) {
            String folderPath = "./Mentions/" + folder.getName();
            try {
                if ( new File(folderPath).isDirectory() == false ){
                    continue;
                }
                System.out.println("Parsing folder ./Mentions/" + folder.getName() + " to remove urls...");
                File[] originalFiles = new File(folderPath).listFiles();

                BufferedReader in = null;
                BufferedWriter out = null;
                String line = "";
                for (int i = 0; i < originalFiles.length; i++) {
                    // System.out.println("    Parsing " + originalFiles[i].getName());

                    String str_outFolderPath = folderPath + "/url_free/" ;
                    File outFolder = new File(str_outFolderPath);
                    if ( !outFolder.exists())
                        outFolder.mkdir();
                    out = new BufferedWriter(new FileWriter(str_outFolderPath + originalFiles[i].getName() + "_urls_free"));
                    if (originalFiles[i].getName().contains("url_free")) {
                        continue;
                    } else {
                        in = new BufferedReader(new FileReader(originalFiles[i]));
                    }

                    int count = 0;
                    line = in.readLine();
                    while (line != null) {
                        if ( TweetPreprocessor.isSpam(line) != true) {
                            count++;
                            line = TweetPreprocessor.removeUrl(line);
                            out.append(line);
                        }
                        line = in.readLine();
                    }
                    out.flush();

                    out.close();
                    in.close();
                    System.out.println("    " + originalFiles[i].getName() + " has " + count + " non-spam mentions");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        // Combine cross week data
//        String previous = "./2009-06-end-07-05-result";
//        String later = "./2009-07-05-result";
//        TweetPreprocessor.combineDataCrossMonth(previous,later);

        // Spam removal
        TweetPreprocessor.batchRemoveUrl();
    }
}
