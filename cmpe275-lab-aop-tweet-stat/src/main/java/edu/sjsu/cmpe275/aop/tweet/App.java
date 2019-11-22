package edu.sjsu.cmpe275.aop.tweet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        /***
         * Following is a dummy implementation of App to demonstrate bean creation with Application context.
         * You may make changes to suit your need, but this file is NOT part of the submission.
         */

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        TweetService tweeter = (TweetService) ctx.getBean("tweetService");
        TweetStatsService stats = (TweetStatsService) ctx.getBean("tweetStatsService");

        try {
//        	tweeter.follow("alex", "bob");
//            int msg = tweeter.tweet("bob", "first tweet");
//            tweeter.retweet("alex", msg);
//            tweeter.block("bob", "alex");
//            tweeter.retweet("alex", msg);
//            tweeter.tweet("alex","This is the longest message that is in Twitter");
            
            //Shivani testing 
//            tweeter.follow("bob","alex");
//            tweeter.follow("cathy","alex");
//            tweeter.follow("elsa","alex");
//            tweeter.follow("alex","bob");
//            tweeter.follow("dan","bob");
//            tweeter.follow("elsa","bob");
//            tweeter.follow("frank","cathy");
//            tweeter.block("alex","elsa");
//            int tweet1=tweeter.tweet("dan","Is it a popular message?");
//            int retweet1=tweeter.retweet("bob",tweet1);
//            tweeter.retweet("dan",retweet1);
//            stats.resetStatsAndSystem();
            
            //test 2
//            tweeter.follow("bob", "alex");
//            int tweet1=tweeter.tweet("alex","HI");
//            tweeter.block("alex","bob");
//            tweeter.retweet("bob", tweet1);
            
            //AJ Test cases
            int msg = tweeter.tweet("alex", "first tweet");
            System.out.println(msg);
            int msg2 = tweeter.tweet("alex", "Second tweet");
            System.out.println(msg2);
            tweeter.follow("bob", "alex");
            tweeter.retweet("bob", msg);
            tweeter.block("alex", "bob");
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Most productive user: " + stats.getMostProductiveUser());
        System.out.println("Most popular user: " + stats.getMostFollowedUser());
        System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
        System.out.println("Most popular message: " + stats.getMostPopularMessage());
        ctx.close();
    }
}
