package main.java;

import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Contact;

public class Samples {
	/**
	 *  queries and displays the 5 newest contacts
	 */
	public static void queryContacts(EnterpriseConnection connection) {

		System.out.println("Querying for the 5 newest Contacts...");

		try {

			// query for the 5 newest contacts
			QueryResult queryResults = connection
					.query("SELECT Id, FirstName, LastName, Account.Name "
							+ "FROM Contact WHERE AccountId != NULL ORDER BY CreatedDate DESC LIMIT 5");
			if (queryResults.getSize() > 0) {
				for (int i = 0; i < queryResults.getRecords().length; i++) {
					// cast the SObject to a strongly-typed Contact
					Contact c = (Contact) queryResults.getRecords()[i];
					System.out.println("Id: " + c.getId() + " - Name: "
							+ c.getFirstName() + " " + c.getLastName()
							+ " - Account: " + c.getAccount().getName());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * create 5 test Accounts, do not need SQL
	 */
	public static void createAccounts(EnterpriseConnection connection) {

		System.out.println("Creating 5 new test Accounts...");
		Account[] records = new Account[5];

		try {

			// create 5 test accounts
			for (int i = 0; i < 5; i++) {
				Account a = new Account();
				a.setName("Test Account " + i);
				records[i] = a;
			}

			// create the records in Salesforce.com
			SaveResult[] saveResults = connection.create(records);

			// check the returned results for any errors
			for (int i = 0; i < saveResults.length; i++) {
				if (saveResults[i].isSuccess()) {
					System.out.println(i
							+ ". Successfully created record - Id: "
							+ saveResults[i].getId());
				} else {
					Error[] errors = saveResults[i].getErrors();
					for (int j = 0; j < errors.length; j++) {
						System.out.println("ERROR creating record: "
								+ errors[j].getMessage());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 *  updates the 5 newly created Accounts, need to query first
	 */
	public static void updateAccounts(EnterpriseConnection connection) {
		System.out.println("Update the 5 new test Accounts...");
		Account[] records = new Account[5];

		try {

			QueryResult queryResults = connection
					.query("SELECT Id, Name FROM Account ORDER BY "
							+ "CreatedDate DESC LIMIT 5");
			if (queryResults.getSize() > 0) {
				for (int i = 0; i < queryResults.getRecords().length; i++) {
					// cast the SObject to a strongly-typed Account
					Account a = (Account) queryResults.getRecords()[i];
					System.out.println("Updating Id: " + a.getId()
							+ " - Name: " + a.getName());
					// modify the name of the Account
					a.setName(a.getName() + " -- UPDATED");
					records[i] = a;
				}
			}

			// update the records in Salesforce.com
			SaveResult[] saveResults = connection.update(records);

			// check the returned results for any errors
			for (int i = 0; i < saveResults.length; i++) {
				if (saveResults[i].isSuccess()) {
					System.out.println(i
							+ ". Successfully updated record - Id: "
							+ saveResults[i].getId());
				} else {
					Error[] errors = saveResults[i].getErrors();
					for (int j = 0; j < errors.length; j++) {
						System.out.println("ERROR updating record: "
								+ errors[j].getMessage());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 *  delete the 5 newly created Account, need to query first
	 */
	public static void deleteAccounts(EnterpriseConnection connection) {

		System.out.println("Deleting the 5 new test Accounts...");
		String[] ids = new String[5];

		try {

			QueryResult queryResults = connection
					.query("SELECT Id, Name FROM Account ORDER BY "
							+ "CreatedDate DESC LIMIT 5");
			if (queryResults.getSize() > 0) {
				for (int i = 0; i < queryResults.getRecords().length; i++) {
					// cast the SObject to a strongly-typed Account
					Account a = (Account) queryResults.getRecords()[i];
					// add the Account Id to the array to be deleted
					ids[i] = a.getId();
					System.out.println("Deleting Id: " + a.getId()
							+ " - Name: " + a.getName());
				}
			}

			// delete the records in Salesforce.com by passing an array of Ids
			DeleteResult[] deleteResults = connection.delete(ids);

			// check the results for any errors
			for (int i = 0; i < deleteResults.length; i++) {
				if (deleteResults[i].isSuccess()) {
					System.out.println(i
							+ ". Successfully deleted record - Id: "
							+ deleteResults[i].getId());
				} else {
					Error[] errors = deleteResults[i].getErrors();
					for (int j = 0; j < errors.length; j++) {
						System.out.println("ERROR deleting record: "
								+ errors[j].getMessage());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
