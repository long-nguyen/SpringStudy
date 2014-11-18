package main.java;

import com.sforce.soap.enterprise.DescribeGlobalResult;
import com.sforce.soap.enterprise.DescribeSObjectResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Field;
import com.sforce.soap.enterprise.FieldType;
import com.sforce.soap.enterprise.PicklistEntry;
import com.sforce.ws.ConnectionException;

public class Util {
	/**
	 * In order to check which Salesforce object is visible to us
	 */
	public static void describeAllObject(EnterpriseConnection connection) {
		
		try {
			DescribeGlobalResult dgr = connection.describeGlobal();
			System.out.println("Accessible object:");
			for (int i = 0; i < dgr.getSobjects().length; i++) {
				System.out.println(dgr.getSobjects()[i].getName());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * To printout metadata of the object we want to check(view properties)
	 * 
	 * @param objectToDescribe
	 */
	public static void describeSingleObject(EnterpriseConnection connection, String objectToDescribe) {
		try {
			// Call describeSObjects() passing in an array with one object type
			DescribeSObjectResult[] dsrArray = connection
					.describeSObjects(new String[] { objectToDescribe });

			// Since we described only one sObject, we should have only
			// one element in the DescribeSObjectResult array.
			DescribeSObjectResult dsr = dsrArray[0];

			// First, get some object properties
			System.out.println("\n\nObject Name: " + dsr.getName());

			if (dsr.getCustom())
				System.out.println("Custom Object");
			if (dsr.getLabel() != null)
				System.out.println("Label: " + dsr.getLabel());

			// Get the permissions on the object
			// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

			if (dsr.getCreateable())
				System.out.println("Createable");
			if (dsr.getDeletable())
				System.out.println("Deleteable");
			if (dsr.getQueryable())
				System.out.println("Queryable");
			if (dsr.getReplicateable())
				System.out.println("Replicateable");
			if (dsr.getRetrieveable())
				System.out.println("Retrieveable");
			if (dsr.getSearchable())
				System.out.println("Searchable");
			if (dsr.getUndeletable())
				System.out.println("Undeleteable");
			if (dsr.getUpdateable())
				System.out.println("Updateable");

			// Print object fields ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
			System.out.println("Number of fields: " + dsr.getFields().length);

			// Now, retrieve metadata for each field
			for (int i = 0; i < dsr.getFields().length; i++) {
				// Get the field
				Field field = dsr.getFields()[i];

				// Write some field properties
				System.out.println("Field name: " + field.getName());
				System.out.println("\tField Label: " + field.getLabel());

				// This next property indicates that this
				// field is searched when using
				// the name search group in SOSL
				if (field.getNameField())
					System.out.println("\tThis is a name field.");

				if (field.getRestrictedPicklist())
					System.out.println("This is a RESTRICTED picklist field.");

				System.out.println("\tType is: " + field.getType());

				if (field.getLength() > 0)
					System.out.println("\tLength: " + field.getLength());

				if (field.getScale() > 0)
					System.out.println("\tScale: " + field.getScale());

				if (field.getPrecision() > 0)
					System.out.println("\tPrecision: " + field.getPrecision());

				if (field.getDigits() > 0)
					System.out.println("\tDigits: " + field.getDigits());

				if (field.getCustom())
					System.out.println("\tThis is a custom field.");

				// Write the permissions of this field
				if (field.getNillable())
					System.out.println("\tCan be nulled.");
				if (field.getCreateable())
					System.out.println("\tCreateable");
				if (field.getFilterable())
					System.out.println("\tFilterable");
				if (field.getUpdateable())
					System.out.println("\tUpdateable");

				// If this is a picklist field, show the picklist values
				if (field.getType().equals(FieldType.picklist)) {
					System.out.println("\t\tPicklist values: ");
					PicklistEntry[] picklistValues = field.getPicklistValues();

					for (int j = 0; j < field.getPicklistValues().length; j++) {
						System.out.println("\t\tValue: "
								+ picklistValues[j].getValue());
					}
				}

				// If this is a foreign key field (reference),
				// show the values
				if (field.getType().equals(FieldType.reference)) {
					System.out.println("\tCan reference these objects:");
					for (int j = 0; j < field.getReferenceTo().length; j++) {
						System.out.println("\t\t" + field.getReferenceTo()[j]);
					}
				}
				System.out.println("");
			}
		} catch (ConnectionException ce) {
			ce.printStackTrace();
		}
	}

}
