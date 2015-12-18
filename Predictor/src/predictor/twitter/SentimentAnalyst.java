package predictor.twitter;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by JunjieW on 2015/12/14.
 */
public class SentimentAnalyst {
    public static int evaluateSentiment(String text) {
        Properties props = new Properties();
        props.setProperty("annotators","tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        int num_sentiment = 0;
        String str_sentiment = sentences.get(0).get(SentimentCoreAnnotations.SentimentClass.class);
        if (str_sentiment == "Positive") {
            num_sentiment = 1;
        } else if (str_sentiment == "Negative") {
            num_sentiment = -1;
        } else if (str_sentiment == "Neutral") {
            num_sentiment = 0;
        }
        return num_sentiment;
    }

    public static void batchEvaluation() {

        Properties props = new Properties();
        props.setProperty("annotators","tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        File[] folders = new File("./Mentions-aft-Cleaning").listFiles();
        for (File folder: folders) {
            String folderPath = "./Mentions-aft-Cleaning/" + folder.getName();
            try {
                if ( new File(folderPath).isDirectory() == false ){
                    continue;
                }
                System.out.println("Parsing folder ./Mentions-aft-Cleaning/" + folder.getName() + " to do sentimental analysis...");
                File[] originalFiles = new File(folderPath).listFiles();

                BufferedReader in = null;
                BufferedWriter out = null;
                String line = "";
                for (int i = 0; i < originalFiles.length; i++) {
                    // System.out.println("    Parsing " + originalFiles[i].getName());

                    String str_outFolderPath = folderPath + "/url_free/" ;
                    File outFolder = new File(str_outFolderPath);
//                    if ( !outFolder.exists())
//                        outFolder.mkdir();
//                    out = new BufferedWriter(new FileWriter(str_outFolderPath + originalFiles[i].getName() + "_urls_free"));

                    in = new BufferedReader(new FileReader(originalFiles[i]));
                    int count = 0;
                    int posCount = 0;
                    int negCount = 0;
                    int neuCount = 0;
                    line = in.readLine();

                    String wholeText = "";
                    while (line != null) {
                        wholeText += line + "\n";
                        count++;
                        line = in.readLine();
                    }

                    if (count > 300) {
                        System.out.println("    " + originalFiles[i].getName() + " has " + count + " non-spam mentions"
                                + ", but is not reliable with the common phase as its title.");
                        continue;
                    }

                    Annotation annotation = pipeline.process(wholeText);
                    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
                    for (CoreMap sentence : sentences) {
                        String str_sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

                        if (str_sentiment.contains("Positive") || str_sentiment.contains("Very positive")) {
                            posCount++;
                        } else if (str_sentiment.contains("Negative") || str_sentiment.contains("Very negative")) {
                            negCount++;
                        } else if (str_sentiment.equals("Neutral")) {
                            neuCount++;
                        }
                        //System.out.println(sentiment + "\t" + sentence);
                    }


                    //out.flush();

                    //out.close();
                    in.close();
                    System.out.println("    " + originalFiles[i].getName() + " has " + count + " non-spam mentions"
                            + ", posCount=" + posCount+ ", negCount=" + negCount + ", neuCount=" + neuCount);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // http://stackoverflow.com/a/26928471
    public static void main(String[] args) throws IOException {

        // Uncomment batchEvaluation to analyze all the tweets files in "[project]/mentions/"
        //batchEvaluation();

        // Uncomment demo() to run the demo on a sample file
        //demo();
    }

    public static void demo() {
        Properties props = new Properties();
        String sample_file_content = "Phonte doing 'THE SONG' @theroxy last sunday       this was shot at about my vantage point.\n" +
                "@janiro what do u play to keep it extra crunk after Swag Surfin and THE SONG.\n" +
                "ROFFLE @phontigalo doin' the neo soul version of \"THE SONG\" and \"Make the Trap Say Aye\"  Bout to start singin' all ignorant music now.\n" +
                "I think @TimmyDS has some kind of hookup at 102 9FM  Because there is no way the songs THE SONG & Halle Berry can be this popular.\n" +
                "Soulja Boy performs \"THE SONG\" @ Hot 107 9 Birthday Bash.\n" +
                "to hear my versions of THE SONG and Birthday Sex.\n" +
                "RT @souljaboytellem - THE SONG.\n" +
                "Boo hiss hiss boo @souljaboytellem - THE SONG.\n" +
                "RT @souljaboytellem - THE SONG.\n" +
                "RT @souljaboytellem: - THE SONG (Lol creative).\n" +
                "RT @souljaboytellem: - THE SONG.\n" +
                "Lol thats cute    Rt: - THE SONG (via @souljaboytellem).\n" +
                "LMFAO RT @souljaboytellem: - THE SONG.\n" +
                "LMAO RT @souljaboytellem - THE SONG.\n" +
                "I had to do it   lol    RT @souljaboytellem: - THE SONG.\n" +
                "Bruce Banner turns this on b4 he turns into the Incredible Hulk    RT @souljaboytellem: - THE SONG.\n" +
                "I bet Obama has this switch in the White House   You see his limp        RT @souljaboytellem: - THE SONG.\n" +
                "just loved Soulja Boy, Gigamesh - THE SONG (DiscoTech Remix) on @hypem.\n" +
                "Haha  Awesome  RT @souljaboytellem: - THE SONG.\n" +
                "Tryin To Fnd Out What i Should Start Of With 4 Da Show 2night    THE SONG Remix by me   Or Im On Fye.\n" +
                "So far 'THE SONG' by Soulja Boy Tell'em is ranked #25 today  Video & Lyrics.\n" +
                "Justin Jbar Rucker Birthday Bash Performance of \"THE SONG\":.\n" +
                "Freekey Zekey Does His Wallin Out Version To Soulja Boy's \"THE SONG\" + Has A 'Face' Off With His Family.\n";
        props.setProperty("annotators","tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = pipeline.process(sample_file_content);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentiment + "\t" + sentence);
        }
    }
}
