package billboardpredictor.main;

import twitter4j.*;

import java.io.*;
import java.util.*;

import twitter4j.auth.OAuth2Token;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


/**
 * Created by Ssangwook on 9/24/2015.
 */
public class TwitterCall {
    private TwitterStream theTwitterStream;
    private String[] theKeywords;
    Properties theProp = new Properties();
    ConfigurationBuilder cb = null;
    File file = new File("outputTweeterSteam.txt");

    StatusListener listener = new StatusListener() {
        @Override
        public void onStatus(Status status) {
            // this is where we print it out
            System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
            // let's try to write it into text file
            try {
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(status.getUser().getName() + ":" + status.getText());
                output.flush();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {
            System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
        }

        @Override
        public void onStallWarning(StallWarning warning) {
            System.out.println("Got stall warning:" + warning);
        }

        @Override
        public void onException(Exception ex) {
            ex.printStackTrace();
        }
    };

    TwitterCall(){
        super();
        try {
            theProp.load(new FileInputStream("src/billboardpredictor/main/resources/twitter.properties"));
            cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(theProp.getProperty("oauth.consumerKey"));
            cb.setOAuthConsumerSecret(theProp.getProperty("oauth.consumerSecret"));
            cb.setApplicationOnlyAuthEnabled(true);
            cb.setOAuthAccessToken(theProp.getProperty("oauth.accessToken"));
            cb.setOAuthAccessTokenSecret(theProp.getProperty("oauth.accessTokenSecret"));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testSampleCode(){
        // ==== From Junjie ====
        // using getInstance without parameter will use library inbeded token by default
        // ==== End From Junjie ====
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

        twitterStream.addListener(listener);

        // ==== From Junjie ====
        // From here we feed the query with some request-parameters.
        // You guys can find the description for thoses request-parameters at https://dev.twitter.com/streaming/overview/request-parameters
        // request-parameters: follow
        // ==== End From Junjie ====
        ArrayList<Long> follow = new ArrayList<Long>();
        follow.add((long)6385432);// Or we can use the Long.parseLong(String) to parse any string from input query request-parameters file;
        follow.add((long)14230524);
        // request-parameters: track
        ArrayList<String> track = new ArrayList<String>();
        track.add("I");
        track.add(" ");

        long[] followArray = new long[follow.size()];
        for (int i = 0; i < follow.size(); i++) {
            followArray[i] = follow.get(i);
        }
        String[] trackArray = track.toArray(new String[track.size()]);

        // ==== From Junjie ====
        // filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        // But we need to find the method that can restrict our query in language = en
        // ==== End From Junjie ====
        twitterStream.filter(new FilterQuery(0, followArray, trackArray));
    }

    public void testStreamByOurToken(){

        try {
            theProp.load(new FileInputStream("src/billboardpredictor/main/resources/twitter.properties"));
            cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(theProp.getProperty("oauth.consumerKey"));
            cb.setOAuthConsumerSecret(theProp.getProperty("oauth.consumerSecret"));
            //cb.setApplicationOnlyAuthEnabled(true);
            cb.setOAuthAccessToken(theProp.getProperty("oauth.accessToken"));
            cb.setOAuthAccessTokenSecret(theProp.getProperty("oauth.accessTokenSecret"));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Configuration config = cb.build();
        theTwitterStream = new TwitterStreamFactory(config).getInstance();
        theTwitterStream.addListener(listener);

        // request-parameters: follow
        ArrayList<Long> follow = new ArrayList<Long>();
        follow.add((long)6385432);// Or we can use the Long.parseLong(String) to parse any string from input query request-parameters file;
        follow.add((long)14230524);

        // request-parameters: track
        ArrayList<String> track = new ArrayList<String>();
        track.add("I");
        track.add(" ");
        long[] followArray = new long[follow.size()];
        for (int i = 0; i < follow.size(); i++) {
            followArray[i] = follow.get(i);
        }
        String[] trackArray = track.toArray(new String[track.size()]);
        theTwitterStream.filter(new FilterQuery(0, followArray, trackArray));

    }

    public void testAuth() throws TwitterException {
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        OAuth2Token token = twitter.getOAuth2Token();
        System.out.print("token_type: " + token.getTokenType() + "\n access_token: " + token.getAccessToken() );
    }

    public void anotherTest() throws TwitterException {
//        if (args.length < 1) {
//            System.out.println("java twitter4j.examples.search.SearchTweets [query]");
//            System.exit(-1);
//        }
//
//        Twitter twitter = new TwitterFactory().getInstance();
//        twitter.getOAuth2Token();
//
//        try {
//            Query query = new Query();
//            QueryResult result;
//            do {
//                result = twitter.search(query);
//                List<Status> tweets = result.getTweets();
//                for (Status tweet : tweets) {
//                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
//                }
//            } while ((query = result.nextQuery()) != null);
//            System.exit(0);
//        } catch (TwitterException te) {
//            te.printStackTrace();
//            System.out.println("Failed to search tweets: " + te.getMessage());
//            System.exit(-1);
//        }
    }


    public static void main(String[] args) throws TwitterException {

        TwitterCall tc = new TwitterCall();

        int testFuncNum = 1;
        if (testFuncNum == 0){
            tc.testSampleCode();
        }else if (testFuncNum == 1){
            tc.testStreamByOurToken();
        }else if (testFuncNum == 2){
            tc.testAuth();
        }
    }

}
