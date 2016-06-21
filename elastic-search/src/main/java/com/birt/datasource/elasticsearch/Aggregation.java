package com.birt.datasource.elasticsearch;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Aggregation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public String getAggregation(String host, int port, String index, String type, String term, String field) {
		
		@SuppressWarnings("rawtypes")
		AggregationBuilder agg = AggregationBuilders.terms(term).field(field);
			
		Client client;
		try {
			client = TransportClient.builder().build()
			.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
			
			SearchResponse response = client.prepareSearch(index)
							.setTypes(type)
							.setSize(0)
					   //     .setQuery(QueryBuilders.termQuery("multi", "test"))
					        .addAggregation(agg)
					        .execute().actionGet();
					
					
					JSONArray jarr = new JSONArray();
			
					StringTerms subjects = (StringTerms) response.getAggregations().asMap().get(term);
					for (Bucket bucket : subjects.getBuckets()) {
					    String key = (String) bucket.getKey();
					    long docCount = bucket.getDocCount();
					    
					    // do something with the key and the doc count
					    
					    Map<String, Long> gh= new HashMap<String, Long>();
					    gh.put(key, docCount);
					    
					    JSONObject json = new JSONObject();
					    json.putAll( gh );
					    
					    jarr.add(json);
					}
					
					client.close();
					
				    return jarr.toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}