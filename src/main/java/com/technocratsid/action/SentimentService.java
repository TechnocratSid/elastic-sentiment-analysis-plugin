package com.technocratsid.action;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;

import com.technocratsid.analyzer.SentimentAnalyzer;

public class SentimentService {

	private SentimentRestRequest request;
	private SentimentAnalyzer sentimentAnalyzer;

	public SentimentService(SentimentRestRequest request) {
		this.request = request;
		sentimentAnalyzer = new SentimentAnalyzer();
		sentimentAnalyzer.initialize();
	}

	public void calculateSentiment(XContentBuilder builder) throws IOException {
		if (builder != null) {
			synchronized (builder) {
				builder.startObject()
						.field("sentiment_score", sentimentAnalyzer.getSentimentResult(request.getText()).getSentimentScore())
						.field("sentiment_type", sentimentAnalyzer.getSentimentResult(request.getText()).getSentimentType())
						.field("very_positive", sentimentAnalyzer.getSentimentResult(request.getText()).getSentimentClass().getVeryPositive()+"%")
						.field("positive", sentimentAnalyzer.getSentimentResult(request.getText()).getSentimentClass().getPositive()+"%")
						.field("neutral", sentimentAnalyzer.getSentimentResult(request.getText()).getSentimentClass().getNeutral()+"%")
						.field("negative", sentimentAnalyzer.getSentimentResult(request.getText()).getSentimentClass().getNegative()+"%")
						.field("very_negative", sentimentAnalyzer.getSentimentResult(request.getText()).getSentimentClass().getVeryNegative()+"%")
				.endObject();
			}
		}
	}

}
