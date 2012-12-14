package com.barchart.feed.tutorial.takers;

import com.barchart.feed.base.book.api.MarketBookEntry;
import com.barchart.feed.base.book.api.MarketBookTop;
import com.barchart.feed.base.book.enums.MarketBookSide;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.client.provider.MarketEventCallback;
import com.barchart.feed.client.provider.MarketTakerBuilder;
import com.barchart.util.values.util.ValueUtil;

public class TopOfBookBuilder {

	public static MarketTaker<MarketBookTop> build(final String symbol) {
		
		final MarketTakerBuilder<MarketBookTop> takerFactory = new MarketTakerBuilder<MarketBookTop>();
		
		/*
		 * Build MarketTaker for BOOK_TOP to fire on NEW_BOOK_TOP
		 */
		final MarketEventCallback<MarketBookTop> topOfBookCallback = new MarketEventCallback<MarketBookTop>() {

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final MarketBookTop top) {
				
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
		
		return takerFactory
				.addMarketField(MarketField.BOOK_TOP)
				.addSymbol(symbol)
				.addEvent(MarketEvent.NEW_BOOK_TOP)
				.addEventCallback(topOfBookCallback)
				.build();
		
	}
	
}
