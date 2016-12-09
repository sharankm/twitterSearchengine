package com.ir.twitterSearch;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;

public class TwitterCustomScoreProvider extends CustomScoreProvider {
	private String followers;

	public TwitterCustomScoreProvider(LeafReaderContext context, String fol) {
		super(context);
		followers = fol;
	}

	public float customScore(int doc, float subQueryScore, float valSrcScores[]) throws IOException {
		IndexReader r = context.reader();
		Document document = r.document(doc);
		Float result = Float.parseFloat(document.get(followers));
		return super.customScore(doc, subQueryScore, valSrcScores) * result;
	}
}