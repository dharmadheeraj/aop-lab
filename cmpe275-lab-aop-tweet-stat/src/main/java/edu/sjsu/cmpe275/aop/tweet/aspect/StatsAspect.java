package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(0)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Autowired TweetStatsServiceImpl stats;
	
	//Before execution of Follow and Block
	@Before("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void beforeVoid(JoinPoint joinPoint) throws IllegalArgumentException{
		System.out.printf("Before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		if(args[0] == null || args[1] == null || (String)args[0] == "" || (String)args[1] == "")
			throw new IllegalArgumentException("Thers is a null Argument. Please validate");
		if(args[0] == args[1])
			throw new IllegalArgumentException("Users cannot be same while following");
	}
	
	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..))")
	public void beforeTweet(JoinPoint joinPoint) throws IllegalArgumentException{
		System.out.printf("Before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		if(args[0] == null || args[1] == null || (String)args[0] == "" || (String)args[1] == "")
			throw new IllegalArgumentException("Thers is a null Argument. Please validate");
		if(((String) args[1]).length() > 140)
			throw new IllegalArgumentException("The length of Message is more than 140");
	}
	
	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))")
	public void beforeReTweet(JoinPoint joinPoint) throws IllegalArgumentException{
		System.out.printf("Before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		if(args[0] == null || (String)args[0] == "")
			throw new IllegalArgumentException("Thers is a null Argument. Please validate");
	}
	
	@After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void dummyAfterAdvice(JoinPoint joinPoint) {
		System.out.printf("After the executuion of the metohd %s\n", joinPoint.getSignature().getName());
	}
	
	@AfterReturning(pointcut="execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.tweet(..)) && args(user,message)",returning="retVal")
	public void afterTweet(JoinPoint jp, String user, String message, int retVal) {
		stats.addTweet(user, message, retVal);
	}
	
	@AfterReturning(pointcut="execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..)) && args(user,messageId)",returning="retVal")
	public void afterTweet(JoinPoint jp, String user, int messageId, int retVal) {
		System.out.println("in After Returning for retweet");
		stats.addReTweet(user, messageId, retVal);
	}
	
	@AfterReturning(pointcut="execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.follow(..)) && args(follower,followee)")
	public void afterFollow(JoinPoint jp, String follower, String followee) {
		stats.addFollower(follower, followee);
	}
	
	@AfterReturning(pointcut="execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.block(..)) && args(user,follower)")
	public void afterBlock(JoinPoint jp, String user, String follower) {
		stats.addBlocked(user, follower);
	}
	
}
