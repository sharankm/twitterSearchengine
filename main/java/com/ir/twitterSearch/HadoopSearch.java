package com.ir.twitterSearch;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class HadoopSearch {

	public static TweetList getHadoopResult(String searchKey) {
		TweetList list = new TweetList();
		Path path;
		try {
			if(Character.isLetterOrDigit(searchKey.charAt(0))){
				path = Paths.get("indexHadoop/folder" + searchKey.substring(0,1) + "/" + searchKey);
			} else{
				path = Paths.get("indexHadoop/foldermisc/" + searchKey);
			}
			File file = path.toFile();
			Scanner sc = new Scanner(file);
			String line = sc.nextLine();
			String[] requiredTweets = line.split("\t");
			String[] lineArr = requiredTweets[1].split(",");
			int max = lineArr.length;
			if (max > 100)
				max = 100;
			for (int i = 0; i < max; i++) {
				String tweetLine = lineArr[i];
				String[] tweetArr = tweetLine.split("\\|");
				String tweetFileName = tweetArr[0];
				String offset = tweetArr[1];
				list.add(readTweet(tweetFileName, Integer.parseInt(offset)));
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Tweet readTweet(String tweetFileName, int offset) throws IOException{
		RandomAccessFile file = new RandomAccessFile("tweetFiles/" + tweetFileName, "r");
		file.seek(offset);
		byte[] bytes = new byte[500];
		file.read(bytes);
		file.close();
		String tweetLines = new String(bytes);
		String[] lines = tweetLines.split("\n");
		Tweet tw = new Tweet();
		int startPoint = 0;
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("|") && lines[i].split("\\|")[0].trim().equals("Username")) {
				startPoint = i;
				break;
			}
		}
		for (int i = startPoint; i < lines.length; i++) {
			String line = lines[i];
			String field = "";
			String info = "";
			if (line.contains("|")) {
				String[] lineSplit = line.split("\\|");
				field = lineSplit[0].trim();
				if (lineSplit.length > 1)
					info = lineSplit[1].trim();
			}
			if (field.equals("Username")) {
				tw.setUserName(info);
			}
			if (field.equals("Name")) {
				tw.setName(info);
			}
			if (field.equals("User Location")) {
				tw.setLocation(info);
			}
			if (field.equals("#Followers")) {
				tw.setFollowers(info);
			}
			if (field.equals("Status")) {
				tw.setStatus(info);
			}
			if (field.equals("Timestamp")) {
				tw.setTime(info);
			}
			if (field.equals("Geo Location Latitude")) {
				tw.setGeolat(info);
			}
			if (field.equals("Geo Location Longitude")) {
				tw.setGeolang(info);
			}
			if (field.equals("Hashtags")) {
				tw.setHashtags(info);
				return tw;
			}
		}
		return tw;
	}
}
