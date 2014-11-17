package main.java;

import org.springframework.batch.item.ItemProcessor;


/**
 * This class is a custom processor for an item.
 * It do a simple work: Print out which item is being processed
 * @author long.nguyen-tien
 *
 */
public class CustomItemProcessor implements ItemProcessor<User, User> {

	@Override
	public User process(User item) throws Exception {
		System.out.println("Processing item..."+ item);
		return item;
	}

}
