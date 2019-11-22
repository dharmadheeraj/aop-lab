package edu.sjsu.cmpe275.aop.tweet;

import java.util.*;

public class TweetStatsServiceImpl implements TweetStatsService {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */
	
	private int longTweetLength = 0;
	private Map<String,List<String>> blocked = new HashMap<String, List<String>>();
	private HashMap<String,List<String>> followed = new HashMap<String, List<String>>();
	private HashMap<String,Integer> fCount = new HashMap<String,Integer>();
	private HashMap<String,Integer> mCount = new HashMap<String,Integer>();
	private HashMap<String,Integer> pCount = new HashMap<String,Integer>();
	private Map<Set<Integer>,Set<String>> popularity = new HashMap<Set<Integer>,Set<String>>();
	private Map<Integer,String[]> tweet = new HashMap<Integer,String[]>();
		
	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub
		longTweetLength = 0;
		blocked.clear();
		followed.clear();
		fCount.clear();
		mCount.clear();
		pCount.clear();
		popularity.clear();
		tweet.clear();
	}
    
	
	public int getLengthOfLongestTweet() {
		// TODO Auto-generated method stub
		return longTweetLength;
	}

	
	public String getMostFollowedUser() {
		// TODO Auto-generated method stub
		if(fCount.isEmpty())
			return null;
		TreeMap<String,Integer> sortedMap = sortByValue(fCount);
		Map.Entry<String,Integer> entry = sortedMap.entrySet().iterator().next();
		 String user= entry.getKey();
		return user;
	}
	
	public String getMostProductiveUser() {
		if(mCount.isEmpty())
			return null;
		TreeMap<String,Integer> sortedMap = sortByValue(mCount);
		Map.Entry<String,Integer> entry = sortedMap.entrySet().iterator().next();
		 String user= entry.getKey();
		return user;
	}

	
	public String getMostPopularMessage() {
		
		for (Map.Entry<Set<Integer>,Set<String>> entry : popularity.entrySet())  
		{
			Integer mID = entry.getKey().iterator().next();
			String message = tweet.get(mID)[1];
			if(entry.getValue().size() > 1)
				pCount.put(message, entry.getValue().size()-1);
		}
		
		if(pCount.isEmpty())
			return null;
		
		TreeMap<String,Integer> sortedMap = sortByValue(pCount);
		Map.Entry<String,Integer> entry = sortedMap.entrySet().iterator().next();
		String message= entry.getKey();
		return message;
	}
	
	public void addFollower(String follower, String followee) {
		List<String> followers = followed.get(followee);
		Integer f = fCount.get(followee);
		if (followers == null)
		{
			followers = new ArrayList<String>();
			f = new Integer(0);
		}
		fCount.put(followee, new Integer(f.intValue() + 1));
		followers.add(follower);
		followed.put(followee,followers);
		System.out.println("Follwer added successfully for " + followee);
	}
	
	public void addBlocked(String user, String follower) {
		List<String> blockedUsers = blocked.get(user);
		if (blockedUsers == null)
		{
			blockedUsers = new ArrayList<String>();
		}
		blockedUsers.add(follower);
		blocked.put(user,blockedUsers);
		List<String> followers = followed.get(user);
		if(followers != null && followers.contains(follower))
			followers.remove(follower);
	}
	
	public void addTweet(String user,String message, int mID) {
		int mLength = message.length();
		if(mLength > longTweetLength)
			{
				System.out.println("Params revieved :" + message + ". Setting LongTweetLength");
				longTweetLength = mLength;
			}
		
		//Update Tweet HMap
		String[] insert = {user,message};
		tweet.put(new Integer(mID), insert);
		
		//Update Message Count HMap
		Integer tempCount = mCount.get(user);
		if(tempCount == null)
			mCount.put(user, mLength);
		else
			mCount.replace(user, new Integer(tempCount.intValue() + mLength));
		
		
		//Update Popularity HMap
		Set<String> reach = new HashSet<String>(Arrays.asList(user));
		
		if(followed.get(user) != null)
			reach.addAll(followed.get(user));
		
		System.out.println("Updating Popularity for tweet: " + mID);
		Set<Integer> temp = new HashSet<Integer>();
		temp.add(new Integer(mID));
		popularity.put(temp, reach);
		
		System.out.println("messageID:" + mID);
	}
	
	public void addReTweet(String user,int originalmID, int mID) {
		
		//Update Tweet HMap
		String[] insert = {user,tweet.get(originalmID)[1]};
		tweet.put(new Integer(mID), insert);
		
		
		Set<Integer> messageIDs = new HashSet<Integer>();
		Set<String> reach = new HashSet<String>();
		
		for(Map.Entry<Set<Integer>, Set<String>> entry: popularity.entrySet())
		{
			if(entry.getKey().contains(new Integer(originalmID)))
			{
				if(followed.get(user) != null)
					reach.addAll(followed.get(user));
				else
					reach = entry.getValue();
				messageIDs = entry.getKey();
			}
		}
		popularity.remove(messageIDs);
		messageIDs.add(new Integer(mID));
		popularity.put(messageIDs, new HashSet<String>(reach));
	}
	
	public boolean validateReTweet(String user,int mID) throws IllegalArgumentException{
		
		String[] originalUser = tweet.get(mID);
		if(originalUser == null)
			throw new IllegalArgumentException("Invalid messageID to tweet");
		
		List<String> blockedUsers = blocked.get(originalUser[0]);
		System.out.println("checking retweet codition");
		if(blockedUsers == null)
			return false;
		else
			return blockedUsers.contains(user);
	}
	
	//Sort HashMap 
    private  TreeMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
    { 
        List<Map.Entry<String, Integer> > list = 
               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
              return o2.getValue().intValue() - o1.getValue().intValue();
            } 
        }); 
          
        // put data from sorted list to Treemap  
        TreeMap<String, Integer> temp = new TreeMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 
    
}





