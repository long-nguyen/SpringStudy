package com.controller;

import org.springframework.batch.item.ItemProcessor;

import com.models.Report;


/**
 * This class is a custom processor for an item.
 * It do a simple work: Print out which item is being processed
 * @author long.nguyen-tien
 *
 */
public class ReportItemProcessor implements ItemProcessor<Report, Report> {

	@Override
	public Report process(Report item) throws Exception {
		System.out.println("Processing item..."+ item);
		return item;
	}

}
