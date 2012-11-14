package com.barchart.feed.tutorial.takers;

import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.base.trade.api.MarketTrade;
import com.barchart.feed.base.trade.enums.MarketTradeField;
import com.barchart.feed.client.provider.MarketEventCallback;
import com.barchart.feed.client.provider.MarketTakerFactory;
import com.barchart.util.values.util.ValueUtil;

public class NewTradeBuilder {
	
	public static MarketTaker<MarketTrade> build(final String symbol) {
		
		final MarketTakerFactory<MarketTrade> takerFactory = new MarketTakerFactory<MarketTrade>();
		
		final MarketEventCallback<MarketTrade> topOfBookCallback = new MarketEventCallback<MarketTrade>() {

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final MarketTrade trade) {
				
				final double tradePrice = ValueUtil.asDouble(trade.get(MarketTradeField.PRICE));
				final long tradeSize = trade.get(MarketTradeField.SIZE).asLong();
				
				System.out.println(new StringBuilder()
						.append("New trade event fired => ")
						.append("Trade Price: " + tradePrice + "\t")
						.append("Trade Size: " + tradeSize + "\t"));
				
			}

		};
		
		return takerFactory
				.addMarketField(MarketField.TRADE)
				.addSymbol(symbol)
				.addEvent(MarketEvent.NEW_TRADE)
				.addEventCallback(topOfBookCallback)
				.build();
		
	}

}
