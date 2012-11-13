package com.barchart.feed.tutorial;

import java.util.concurrent.atomic.AtomicInteger;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.Market;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.client.provider.BarchartFeedClient;
import com.barchart.feed.client.provider.MarketEventCallback;
import com.barchart.feed.client.provider.MarketTakerFactory;
import com.barchart.util.values.util.ValueUtil;

public class ExploringTheMarketObject {

	public static void main(final String[] args) {
	
		final String username = System.getProperty("tutorial.username");
		final String password = System.getProperty("tutorial.password");
		
		final BarchartFeedClient client = new BarchartFeedClient();
		client.login(username, password);
		
		final MarketTakerFactory takerFactory = new MarketTakerFactory();
		
		/*
		 * Build MarketTaker for the BAR_CURRENT to fire on NEW_BAR_CURRENT event
		 */
		final MarketEventCallback currentBarCallback = new MarketEventCallback() {

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market market) {
				
				final MarketBar bar = market.get(MarketField.BAR_CURRENT);
				
				final double high = ValueUtil.asDouble(bar.get(MarketBarField.HIGH));
				final double low = ValueUtil.asDouble(bar.get(MarketBarField.LOW));
				final double open = ValueUtil.asDouble(bar.get(MarketBarField.OPEN));
				final double close = ValueUtil.asDouble(bar.get(MarketBarField.CLOSE));
				
				System.out.println(new StringBuilder()
						.append("NewBarCurrent fired => ")
						.append("High:" + high + "\t")
						.append("Low:" + low + "\t")
						.append("Open:" + open + "\t")
						.append("Close:" + close).toString());
				
			}
			
		};
		
		takerFactory.addSymbol("GOOG")
				.addEvent(MarketEvent.NEW_BAR_CURRENT)
				.addEventCallback(currentBarCallback);
		
		client.addTaker(takerFactory.build());
		
		
		/*
		 * Build MarketTaker for BOOK_TOP to fire on NEW_BOOK_TOP
		 */
		final MarketEventCallback topOfBookCallback = new MarketEventCallback() {

			final AtomicInteger counter = new AtomicInteger(0);
			
			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final Market market) {
				
				//check counter
				
				final MarketBookTop top = market.get(MarketField.BOOK_TOP);
				
				final MarketBookEntry askTopEntry = top.side(MarketBookSide.ASK);
				final double askPrice = ValueUtil.asDouble(askTopEntry.price());
				final long askQty = askTopEntry.size().asLong();
				
				final MarketBookEntry bidTopEntry = top.side(MarketBookSide.BID);
				final double bidPrice = ValueUtil.asDouble(bidTopEntry.price());
				final long bidQty = bidTopEntry.size().asLong();

				System.out.println(new StringBuilder()
						.append("New top of book event fired => ")
						.append("AskQty: " + askQty + "\t")
						.append("AskPrice:" + askPrice + "\t")
						.append("BidPrice:" + bidPrice + "\t")
						.append("BidQty:" + bidQty + "\t"));
				
			}
			
		};
		
		client.addTaker(takerFactory.addSymbol("GOOG")
				.addEvent(MarketEvent.MARKET_UPDATED)
				.addEventCallback(topOfBookCallback).build());
		
		try {
			Thread.sleep(30 * 1000);
		} catch(final Exception e) {
			e.printStackTrace();
		}
		
		//Test removing takers here
		
		System.out.println("***** Main class shutting down client *****");
		client.shutdown();
		
	}
	
}
