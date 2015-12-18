package predictor.twitter;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JunjieW on 2015/11/28.
 *
 * INPUT: datasets, list of songs
 * OUTPUT: tweets grouped by keywords on weekly basis
 *
 * 1. split data files on weekly basis
 * 2. read song list and seach on data file accordingly
 * 3. remove non-useful tags in original dataset
 * 4. remove spam
 *
 */
public class TweetsDataManager {

    private boolean DEBUG = true;
    protected String datasetPath = "";
    //protected String keywordsPath = "./UmdScraper/20090627"; // Song titles

    private LineNumberReader bfrDataset = null;

    Vector<String> keywords = new Vector<String>();

    ArrayList<Integer> splitPosition = new ArrayList<Integer>();
    ArrayList<String> dateOfSundaysInMonth = new ArrayList<String>();
    private int baseYear;
    private int baseMonth;

    public TweetsDataManager(int year, int month) {
        baseYear = year;
        baseMonth = month;
    }

    //============================== For Dataset Spliting ===================================
    protected void getDateOfSundaysInMonth(int year, int month) {
        // http://stackoverflow.com/a/9909488  && http://www.mkyong.com/java/java-date-and-calendar-examples/
        Calendar calendar = Calendar.getInstance();
        // calendarl.set() is month-zero base, i.e. January = 0;
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String str_date = dateFormat.format(date);

            if (dayOfWeek == Calendar.SUNDAY) {
                dateOfSundaysInMonth.add(str_date);
                System.out.println(str_date);
            }
        }
    }

    public void getSplitPositions() {
        try {
            bfrDataset = new LineNumberReader(new FileReader(datasetPath));
            String line;
            for (String str_dateOfSat : dateOfSundaysInMonth) {
                do {
                    line = bfrDataset.readLine();
                    if (line.length() > 0 && line.charAt(0) == 'T') {
                        // http://stackoverflow.com/a/86832
                        Matcher matcher = Pattern.compile(Pattern.quote(str_dateOfSat), Pattern.CASE_INSENSITIVE).matcher(line);
                        if ( matcher.find() ) {
                            int pos = bfrDataset.getLineNumber();
                            if (DEBUG) System.out.println(pos + ":" + line);
                            splitPosition.add(pos);

                            break;
                        }
                    }
                } while ( line != null);
            }

            while ( bfrDataset.readLine() != null){
                // Do nothing, just go to the end of the file and get the line count
            }
            System.out.println("last line is line: " + bfrDataset.getLineNumber() + "+1\n");
            splitPosition.add(bfrDataset.getLineNumber());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void splitWeeklyData(String str_pathToMonthlyData) {
        this.datasetPath = str_pathToMonthlyData;
        getDateOfSundaysInMonth(baseYear, baseMonth);
        getSplitPositions();
        //readKeywords();
        try {
            //bfrDataset = new LineNumberReader(new FileReader(datasetPath));
            bfrDataset = new LineNumberReader(new InputStreamReader(new FileInputStream(this.datasetPath), "UTF8"));
            BufferedWriter bfrWriter;
            String file_name;
            for (int i = 0; i < splitPosition.size(); ++i) {
                if (i == splitPosition.size() - 1) {
                    file_name = dateOfSundaysInMonth.get(0).substring(0,dateOfSundaysInMonth.get(0).length() - 3 ) + "-end";
                } else {
                    file_name = dateOfSundaysInMonth.get(i);
                }
                String line;

                //bfrWriter = new BufferedWriter(new FileWriter(file_name, false));
                bfrWriter = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file_name,false), "UTF8"));
                while ( bfrDataset.getLineNumber() < splitPosition.get(i)) {
                    line = bfrDataset.readLine();
                    if (line.length() > 0) {
                        // skip the tag at the beginning of each line, and trim the blank space on both sides
                        if (line.charAt(0) == 'W') {
                            bfrWriter.append(line.substring(1).trim() + "\n");
                        }
                    }
                }
                bfrWriter.flush();
                bfrWriter.close();
                if (DEBUG) System.out.print("====== " + file_name +" Done ======\n");
            }
            bfrDataset.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //============================== End of Dataset Spliting ===================================

    //============================== For Keywords Searching ===================================
    protected void readKeywords(String str_pathToWeeklyKeywords) {

        try {
            //BufferedReader bfrKeywordsFile = new BufferedReader(new FileReader(this.keywordsPath));
            BufferedReader bfrKeywordsFile = new BufferedReader(new FileReader(str_pathToWeeklyKeywords));
            String line;
            int numOfKeywords = 0;
            do{
                line = bfrKeywordsFile.readLine();
                if ( line != null && line.charAt(0) != '#') {
                    ++numOfKeywords;
                    keywords.add(line);
                }
            } while (line != null);
            // number of keywords should be 100, keywords file has 102 lines,
            if (DEBUG) System.out.println("Keyword count:" + numOfKeywords);
            bfrKeywordsFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void searchMentionsInFile(String inputFileName) {
        try {
            LineNumberReader bufReader;
            BufferedWriter bufWriter;
            String str_output_path = "./" + inputFileName + "-result";
            File output_folder = new File(str_output_path);
            if ( !output_folder.exists())
                output_folder.mkdir();
            //for (int i = 0; i < keywords.size(); ++i) {
            for (int i = 0; i < keywords.size(); ++i) {
                String str_out_file_name = str_output_path + "/" + (i+1) + "_" + keywords.elementAt(i).replace(" ","_").replace("?","").replace("!","");
                bufReader = new LineNumberReader(new InputStreamReader(new FileInputStream(inputFileName), "UTF8"));
                bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(str_out_file_name,false), "UTF8"));

                long mentionCount = 0;
                String line = "";
                while (line != null) {
                    line = bufReader.readLine();
                    if (line != null && line.contains(keywords.elementAt(i))) {
                        bufWriter.append(line + "\n");
                        mentionCount++;
                    }
                }
                bufWriter.flush();
                bufWriter.close();

                bufReader.close();
                if (DEBUG) System.out.println("\"" + (i+1) + " " + keywords.elementAt(i) + "\"" + " has " + mentionCount + " mentions" );
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //============================== End of Keywords Searching ===================================

    public static void main(String[] args) {
        TweetsDataManager tdm = new TweetsDataManager(2009, 8);
//        tdm.splitWeeklyData("./tweets2009-08.txt");
//        System.out.println("====== Finish Splitting Dataset=======");

        System.out.println("====== Start to Read Keywords =======");
        tdm.readKeywords("./UmdScraper/20090815");
        System.out.println("====== Finish Reading Keywords =======");
        tdm.searchMentionsInFile("2009-08-16");
        System.out.println("====== Finish Start Searching =======");

    }


}
