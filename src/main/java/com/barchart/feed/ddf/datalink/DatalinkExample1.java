/**
 * 
 */
package com.barchart.feed.ddf.datalink;

import com.barchart.feed.ddf.datalink.api.DDF_FeedClient;
import com.barchart.feed.ddf.datalink.api.DDF_FeedHandler;
import com.barchart.feed.ddf.datalink.enums.DDF_FeedEvent;
import com.barchart.feed.ddf.datalink.provider.DDF_FeedClientFactory;
import com.barchart.feed.ddf.message.api.DDF_BaseMessage;
import com.barchart.feed.ddf.message.enums.DDF_MessageType;
import com.barchart.feed.ddf.util.FeedDDF;

/**
 * Example class for DDF_FeedClient and DDF_FeedHandler
 * <p>
 * In this simple example, a DDF_FeedClient is created using the
 * DDF_FeedClientFactory and then used to login to the server and request data.
 * <p>
 * A sample DDF_FeedHandler is provided to the FeedClient, specifying the
 * behavior of handling feed events as well as the received data.
 * 
 */
public class DatalinkExample1 {

	private static final String username = "andrei";
	private static final String password = "andrei";

	private static final String symbol = "GOOG";

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		/*
		 * Here we use the default FeedClient configuration. See
		 * DDF_FeedClientFactory for other options.
		 */
		final DDF_FeedClient feedClient = DDF_FeedClientFactory.newInstance();

		final DatalinkExample1 d = new DatalinkExample1();
		/*
		 * Create a new FeedHandler object and bind it to the FeedClient
		 */
		feedClient.bind(d.new MyFeedHandler());

		feedClient.login(username, password);

		/*
		 * Sends a CharSequence command to the data server to begin streaming
		 * raw data for the specified symbol.
		 * 
		 * See com.barchart.feed.ddf.util.FeedDDF for common use examples as
		 * well as static builders.
		 * 
		 * See http://www.ddfplus.com/client/jerq/index.shtml for server command
		 * protocol.
		 */
		feedClient.post(FeedDDF.tcpStreamRaw(symbol));

		try {
			Thread.sleep(300000);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * MyFeedHandler gives the FeedClient two behaviors:
	 * 
	 * 1) How to handle the different FeedEvents the server sends it. 2) How to
	 * handle the data messages the server sends it.
	 * 
	 */
	public class MyFeedHandler implements DDF_FeedHandler {

		/**
		 * This basic handleEvent implementation only alerts the user that he
		 * has been disconnected.
		 * 
		 * See DDF_FeedEvent for all possible feed events returned by server.
		 */
		@Override
		public void handleEvent(final DDF_FeedEvent event) {

			if (event != DDF_FeedEvent.HEART_BEAT) {
				System.out.println("### FEED EVENT : " + event);
			}

			switch (event) {

			case HEART_BEAT:
				// Do nothing on hear beat
				break;
			case LINK_DISCONNECT:
				// Alert user of disconnect
				System.out.println("Attempting to reconnect");
				break;
			default:
				break;

			}

		}

		/**
		 * This basic handleMessage implementation prints to console every
		 * message that is not a heart beat/time stamp.
		 */
		@Override
		public void handleMessage(final DDF_BaseMessage message) {

			if (!message.getMessageType().equals(DDF_MessageType.TIME_STAMP)) {
				System.out.println(message.getMessageType().name() + " "
						+ message.toString());
			}

		}

	}

}
