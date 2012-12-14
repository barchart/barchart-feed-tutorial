package com.barchart.feed.tutorial.takers;

import com.barchart.feed.base.bar.api.MarketBar;
import com.barchart.feed.base.bar.enums.MarketBarField;
import com.barchart.feed.base.instrument.values.MarketInstrument;
import com.barchart.feed.base.market.api.MarketTaker;
import com.barchart.feed.base.market.enums.MarketEvent;
import com.barchart.feed.base.market.enums.MarketField;
import com.barchart.feed.client.provider.MarketEventCallback;
import com.barchart.feed.client.provider.MarketTakerBuilder;
import com.barchart.util.values.util.ValueUtil;

public class CurrentBarBuilder {

	public static MarketTaker<MarketBar> build(final String symbol) {
		
		final MarketTakerBuilder<MarketBar> takerFactory = new MarketTakerBuilder<MarketBar>();
		
		/*
		 * Build MarketTaker for the BAR_CURRENT to fire on NEW_BAR_CURRENT event
		 */
		final MarketEventCallback<MarketBar> currentBarCallback = new MarketEventCallback<MarketBar>() {

			@Override
			public void onMarketEvent(final MarketEvent event,
					final MarketInstrument instrument, final MarketBar bar) {
				
				final double high = ValueUtil.asDouble(bar.get(MarketBarField.HIGH));
				final double low = ValueUtil.asDouble(bar.get(MarketBarField.LOW));
				final double open = ValueUtil.asDouble(bar.get(MarketBarField.OPEN));
				final double close = ValueUtil.asDouble(bar.get(MarketBarField.CLOSE));
				
				System.out.println(bar.get(MarketBarField.CLOSE).mantissa() + ":" + 
						bar.get(MarketBarField.CLOSE).exponent());
				
				System.out.println(new StringBuilder()
						.append("NewBarCurrent fired => ")
						.append("High:" + high + "\t")
						.append("Low:" + low + "\t")
						.append("Open:" + open + "\t")
						.append("Close:" + close).toString());
				
			}
			
		};
		
		return takerFactory
				.addMarketField(MarketField.BAR_CURRENT)
				.addSymbol(symbol)
				.addEvent(MarketEvent.NEW_BAR_CURRENT)
				.addEventCallback(currentBarCallback)
				.build();
		
	}
	
}
