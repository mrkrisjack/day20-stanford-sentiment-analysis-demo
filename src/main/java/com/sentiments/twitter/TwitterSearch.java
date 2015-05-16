package com.sentiments.twitter;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSearch {
    private static Twitter twitter = null;

    public TwitterSearch() {
        if(twitter == null) {
            Properties twitterProperties = TwitterProperties.loadPropertiesFromClasspath();
            ConfigurationBuilder cb = new ConfigurationBuilder();
            if(twitterProperties != null) {
                cb.setDebugEnabled(true).setOAuthConsumerKey(twitterProperties.getProperty("oauthConsumerKey"))
                .setOAuthConsumerSecret(twitterProperties.getProperty("consumerSecret"))
                .setOAuthAccessToken(twitterProperties.getProperty("oauthAccessToken"))
                .setOAuthAccessTokenSecret(twitterProperties.getProperty("oauthAccessTokenSecret"));
            } else {
                cb.setDebugEnabled(true).setOAuthConsumerKey(System.getenv("TWITTER_OAUTH_CONSUMER_KEY"))
                .setOAuthConsumerSecret(System.getenv("TWITTER_OAUTH_CONSUMER_SECRET"))
                .setOAuthAccessToken(System.getenv("TWITTER_OAUTH_ACCESS_TOKEN"))
                .setOAuthAccessTokenSecret(System.getenv("TWITTER_OAUTH_ACCESS_TOKEN_SECRET"));
            }
            TwitterFactory tf = new TwitterFactory(cb.build());
            twitter = tf.getInstance();
        }
    }

    public List<Status> search(String keyword) {
        Query query = new Query(keyword + " -filter:retweets -filter:links -filter:replies -filter:images");
        query.setCount(20);
        query.setLocale("en");
        query.setLang("en");;
        try {
            QueryResult queryResult = twitter.search(query);
            return queryResult.getTweets();
        } catch (TwitterException e) {
            // ignore
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
