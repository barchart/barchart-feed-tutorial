package com.barchart.feed.tutorial;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.feed.client.provider.BarchartFeedClient;
import com.barchart.feed.ddf.instrument.provider.DDF_InstrumentProvider;
import com.barchart.feed.tutorial.takers.CurrentBarBuilder;
import com.barchart.feed.tutorial.takers.NewTradeBuilder;
import com.barchart.feed.tutorial.takers.TopOfBookBuilder;

public class ExploringTheMarketObject {

	public static void main(final String[] args) {
	
		final String username = System.getProperty("tutorial.username");
		final String password = System.getProperty("tutorial.password");
		
		final String symbol = "GOOG";
		
		final BarchartFeedClient client = new BarchartFeedClient();
		client.login(username, password);
		
		System.out.println(DDF_InstrumentProvider.find(symbol));
		
		/*
		 * Build MarketTaker for the BAR_CURRENT to fire on NEW_BAR_CURRENT event
		 */
		final MarketTaker<MarketBar> currentBarTaker = CurrentBarBuilder.build(symbol);
		client.addTaker(currentBarTaker);
		
		/*
		 * Build MarketTaker for BOOK_TOP to fire on NEW_BOOK_TOP
		 */
		final MarketTaker<MarketBookTop> topOfBookTaker = TopOfBookBuilder.build(symbol);
		client.addTaker(topOfBookTaker);
		
		
		final MarketTaker<MarketTrade> tradeTaker = NewTradeBuilder.build(symbol);
		client.addTaker(tradeTaker);
		
		try {
			Thread.sleep(60 * 1000);
		} catch(final Exception e) {
			e.printStackTrace();
		}
		
		client.removeTaker(currentBarTaker);
		client.removeTaker(topOfBookTaker);
		client.removeTaker(tradeTaker);
		
		System.out.println("***** Main class shutting down client *****");
		client.shutdown();
		
	}
	
}
