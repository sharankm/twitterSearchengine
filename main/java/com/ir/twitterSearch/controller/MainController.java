package com.ir.twitterSearch.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ir.twitterSearch.HadoopSearch;
import com.ir.twitterSearch.LuceneSearch;
import com.ir.twitterSearch.TweetList;

@Controller
public class MainController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homepage() {
		return "index";
	}

	@RequestMapping(value = "/luceneSearch/{keyword}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<TweetList> getLuceneResultList(@PathVariable("keyword") String keyword) {
		if(keyword.contains("HashTag")){
			String concatStr = "#".concat(keyword.substring(7, keyword.length()));
			keyword = concatStr;
		}
		TweetList list = LuceneSearch.getLuceneResult(keyword);
		return new ResponseEntity<TweetList>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/hadoopSearch/{keyword}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<TweetList> getHadoopResultList(@PathVariable("keyword") String keyword) {
		if(keyword.contains("HashTag")){
			String concatStr = "#".concat(keyword.substring(7, keyword.length()));
			keyword = concatStr;
		}
		TweetList list = HadoopSearch.getHadoopResult(keyword);
		return new ResponseEntity<TweetList>(list, HttpStatus.OK);
	}

}
