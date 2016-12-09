package com.ir.twitterSearch;

import java.io.IOException;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;

public class TwitterCustomQuery extends CustomScoreQuery {

	public TwitterCustomQuery(Query subQuery) {
		super(subQuery);
	}

	protected CustomScoreProvider getCustomScoreProvider(LeafReaderContext context) throws IOException {
		return new TwitterCustomScoreProvider(context, "Followers");
	}
}