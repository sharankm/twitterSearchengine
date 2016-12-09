package com.ir.twitterSearch;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneSearch {
	
	public static TweetList getLuceneResult(String searchKey) {
		TweetList list = new TweetList();
		try {
			Path path = Paths.get("indexLucene");
			Directory directory = FSDirectory.open(path);
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser queryParser = new QueryParser("Status", new StandardAnalyzer());
			Query query = queryParser.parse(searchKey);
			CustomScoreQuery queryWithScore = new TwitterCustomQuery(query);
			TopDocs topDocs = indexSearcher.search(queryWithScore, 100);
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				Tweet tw = new Tweet();
				tw.setGeolang(document.get("Geo Location Longitude"));
				tw.setGeolat(document.get("Geo Location Latitude"));
				tw.setHashtags(document.get("Hashtags"));
				tw.setLocation(document.get("User Location"));
				tw.setStatus(document.get("Status"));
				tw.setUserName(document.get("Username"));
				tw.setName(document.get("Name"));
				tw.setTime(document.get("Timestamp"));
				tw.setFollowers(document.get("Followers"));
				list.add(tw);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
