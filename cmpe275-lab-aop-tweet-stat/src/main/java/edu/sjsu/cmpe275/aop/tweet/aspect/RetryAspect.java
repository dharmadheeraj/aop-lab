package edu.sjsu.cmpe275.aop.tweet.aspect;

import java.io.IOException;
import java.util.HashMap;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

@Aspect
@Order(1)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */
	
	private HashMap<String,Integer> retries = new HashMap<String,Integer>();
	public void setRetry(String function) {
		this.retries.put(function,new Integer(4));
	}

	@Around("execution(public int edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public int dummyAdviceOne(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		
		setRetry(joinPoint.getSignature().getName());
		int attempts = 0;
		IOException ioException;
		Integer result = null;
		
		try{
			do {
		          attempts++;
		          System.out.println("Trying : " + attempts);
		          try {
		        	  result = (Integer) joinPoint.proceed();
		        	  System.out.printf("Finished the executuion of the metohd %s with result %s\n", joinPoint.getSignature().getName(), result);
		        	  return result.intValue();
		          }
		          catch(IOException ex) {
		          	ioException = ex;
		          }
		      } while(attempts <4);
				System.out.println("Retries completed");
		     ioException.printStackTrace();
		     return 1;
		} catch (Throwable e) {
			System.out.printf("Aborted the executuion of the metohd %s\n", joinPoint.getSignature().getName());
			throw e;
		}
	}
	
	@Around("execution(public void edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public void dummyAdviceTwo(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.printf("Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		
		setRetry(joinPoint.getSignature().getName());
		int attempts = 0;
		IOException ioException = new IOException();
		
		try{
			do {
		          attempts++;
		          System.out.println("Trying : " + attempts);
		          try {
		        	  joinPoint.proceed();
		        	  System.out.printf("Finished the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		        	  return;
		          }
		          catch(IOException ex) {
		          	ioException = ex;
		          }
		      } while(attempts <4);
				System.out.println("Retries completed");
		      throw ioException;
		} catch (Throwable e) {
			System.out.printf("Aborted the executuion of the metohd %s\n", joinPoint.getSignature().getName());
			throw e;
		}
	}
}
