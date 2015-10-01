package billboardpredictor.main;

import twitter4j.*;
import java.util.*;


/**
 * Created by Ssangwook on 9/24/2015.
 */
public class TwitterCall {

    public static void main(String[] args) throws TwitterException {

        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
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

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(listener);

        // From here we feed the query with some request-parameters.
        // You guys can find the description for thoses request-parameters at https://dev.twitter.com/streaming/overview/request-parameters
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

        // filter() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
        // But we need to find the method that can restrict our query in language = en
        twitterStream.filter(new FilterQuery(0, followArray, trackArray));
    }

}
