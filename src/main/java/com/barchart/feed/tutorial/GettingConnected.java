package com.barchart.feed.tutorial;

import com.barchart.feed.client.api.FeedStateListener;
import com.barchart.feed.client.enums.FeedState;
import com.barchart.feed.client.provider.BarchartFeedClient;

public class GettingConnected {

	public static void main(final String[] args) {
		
		final String username = System.getProperty("tutorial.username");
		final String password = System.getProperty("tutorial.password");
		
		final BarchartFeedClient client = new BarchartFeedClient();
		
		final FeedStateListener feedListener = new FeedStateListener() {

			@Override
			public void stateUpdate(final FeedState state) {

				System.out.println("Feed is now in state: " + state);

			}

		};
		
		client.login(username, password);
		
		client.bindFeedStateListener(feedListener);
		
		try {
			Thread.sleep(10 * 1000);
		} catch(final Exception e) {
			e.printStackTrace();
		}
		
		client.shutdown();
		
	}
	
}
