package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.security.AccessControlException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

@Aspect
@Order(2)
public class PermissionAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
	
	@Autowired TweetStatsServiceImpl stats;
	
	@Before("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.retweet(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) throws AccessControlException {
		System.out.printf("Permission check before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] args = joinPoint.getArgs();
		boolean validation = stats.validateReTweet((String)args[0],((Integer)args[1]).intValue());
		if(validation)
			throw new AccessControlException("You dont follow");
		System.out.printf("Permission check Completed successfully for user %s\n", (String)args[0]);
	}
	
}
