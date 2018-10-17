package com.technocratsid.action;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;

public class SentimentRestAction extends BaseRestHandler {
	
	private static String NAME = "_sentiment";

	@Inject
	public SentimentRestAction(Settings settings, RestController restController) {
		super(settings);
		restController.registerHandler(RestRequest.Method.POST, "/" + NAME, this);
		restController.registerHandler(RestRequest.Method.GET, "/" + NAME, this);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
		Map<String, Object> params = XContentHelper.convertToMap(request.content(), false, request.getXContentType()).v2();
		SentimentRestRequest restRequest = new SentimentRestRequest();
		if (params.containsKey("text")) {
			restRequest.setText((String) params.get("text"));
		}
		return channel -> {
			XContentBuilder builder = channel.newBuilder();
			AccessController.doPrivileged((PrivilegedAction) () -> {
				SentimentService service = new SentimentService(restRequest);
				try {
					service.calculateSentiment(builder);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
	        });
			channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
		};
	}

}
