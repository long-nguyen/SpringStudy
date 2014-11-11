import org.springframework.batch.item.ItemProcessor;

/**
 * This is an item processor which transfrom person name to upcase
 * @author long.nguyen-tien
 *
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person>{

	@Override
	public Person process(Person person) throws Exception {
		final String firstName = person.getFirstName().toUpperCase();
		final String lastName = person.getLastName().toUpperCase();
		final Person transformedPerson = new Person(firstName, lastName);
		return transformedPerson;
	}

}
