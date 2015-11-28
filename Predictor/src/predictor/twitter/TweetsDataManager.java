package predictor.twitter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by JunjieW on 2015/11/28.
 *
 * INPUT: datasets, songlist
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
    protected String datasetPath = "C:\\Users\\DC-IT-Dev\\Desktop\\NYU2015FALL-Course-Material\\PA\\Final Project\\Data-stanford\\tweets2009-06-data\\tweets2009-06.txt";
    protected String keywordsPath = "./UmdScraper/20090530"; // Song titles

    protected LineNumberReader bfrDataset = null;

    ArrayList<String> keywords = new ArrayList<String>();

    ArrayList<Integer> splitPosition = new ArrayList<Integer>();
    ArrayList<String> dateOfSaturdaysInMonth = new ArrayList<String>();
    private int baseYear;
    private int baseMonth;

    public TweetsDataManager(int year, int month) {
        baseYear = year;
        baseMonth = month;
    }

    public void getSplitPositions() {
        try {
            bfrDataset = new LineNumberReader(new FileReader(datasetPath));
            String line;
            for (String str_dateOfSat : dateOfSaturdaysInMonth) {
                do {
                    line = bfrDataset.readLine();
                    // http://stackoverflow.com/a/86832
                    if ( Pattern.compile(Pattern.quote(str_dateOfSat), Pattern.CASE_INSENSITIVE).matcher(line).find() ) {
                        int pos = bfrDataset.getLineNumber();
                        if (DEBUG) System.out.println(pos);
                        splitPosition.add(pos);
                        break;
                    }
                } while ( line != null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void splitWeeklyData() {
        getDateOfSaturdaysInMonth(baseYear, baseMonth);
        getSplitPositions();
        //readKeywords();
    }

    protected void readKeywords() {
        try {
            BufferedReader bfrKeywordsFile = new BufferedReader(new FileReader(keywordsPath));
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

    protected void getDateOfSaturdaysInMonth(int year, int month) {
        // http://stackoverflow.com/a/9909488  && http://www.mkyong.com/java/java-date-and-calendar-examples/
        Calendar calendar = Calendar.getInstance();
        // calendarl.set() is month-zero base, January = 0;
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String str_date = dateFormat.format(date);

            if (dayOfWeek == Calendar.SATURDAY) {
                dateOfSaturdaysInMonth.add(str_date);
                System.out.println(str_date);
            }
        }
    }

    public static void main(String[] args) {
        TweetsDataManager tdm = new TweetsDataManager(2009, 6);
        tdm.splitWeeklyData();
    }


}
