package com.codepath.apps.simpletweets.utils;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // utilize twitter API
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "vVQYjYOva8xIsXL2Lwb7YHGOP";       // Change this
	public static final String REST_CONSUMER_SECRET = "RCCXOMdQ7vstVo7JyC8GODjqmhgH6RAmWH0RjCVXJfbOlLKyBc"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	// Get the Home twitter timeline
	// count = 25, since_id = 1
	public void getHomeTimeline(long page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		//  1 for infinite scrolling
		if(page != 0){
			params.put("max_id", String.valueOf(page));
		}
		params.put("count", 25);
		params.put("since_id", 1);
		getClient().get(apiUrl, params, handler);
	}

	// COMPOSE TWEETS
	public void postTweet(String body, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}

	//GET CURRENT USER INFO
	public void getLoginUserInfo(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("account/verify_credentials.json");
		//RequestParams params = new RequestParams();
		getClient().get(apiUrl, handler);
	}

	// Get the Mentions twitter timeline
	public void getMentionsTimeline(long page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		//  1 for infinite scrolling
		if(page != 0){
			params.put("max_id", String.valueOf(page));
		}
		params.put("count", 25);
		getClient().get(apiUrl, params, handler);
	}

	// GET Specific USER Info
	public void getUserInfo(String screenName, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	// GET USER TIMELINE
	public void getUserTimeline(long page, String screenName, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		//  1 for infinite scrolling
		if(page != 0){
			params.put("max_id", String.valueOf(page));
		}
		params.put("count", 25);
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	//GET USER Following List(Friends List)
	public void getFriendList(long page, String screenName, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("friends/list.json");
		RequestParams params = new RequestParams();
		//  1 for infinite scrolling
		if(page != 0){
			params.put("cursor", String.valueOf(page));
		}
		params.put("count", 25);
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	//GET USER Follower List
	public void getFollowerList(long page, String screenName, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("followers/list.json");
		RequestParams params = new RequestParams();
		//  1 for infinite scrolling
		if(page != 0){
			params.put("cursor", String.valueOf(page));
		}
		params.put("count", 25);
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	//GET search results
	public void getSearchTweets(long page, String query, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("search/tweets.json");
		RequestParams params = new RequestParams();
		//  1 for infinite scrolling
		if(page != 0){
			params.put("max_id", String.valueOf(page));
		}
		params.put("q", query);
		params.put("count", 25);
		getClient().get(apiUrl, params, handler);
	}



	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}
