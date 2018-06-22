package in.v2solutions.hybrid.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateTestNGXml extends Constants {
	
	@SuppressWarnings("unchecked")
	/*
	 @HELP
	 @class: CreateTestNGXml extends Constants (Single Level of Inheritance)
	 @methods: getSuiteTCs(), createTestngXml()
	 @parameter: Different parameters are passed as per the method declaration
	 @notes: Generates a runtime "testng.xml" as per the details mentioned in Master xls file in "config details" sheet. This Class is a ANT target in Suite Execution Batch file.
	 @returns: All respective methods have there return types
	 @END
	 */
	
	public static void getSuiteTCs() throws IOException {		
		/* @HELP
		@class:				CreateTestNGXml	
		@method:			getSuiteTCs()
		@parameter:		No Parameter
		@notes:				Gets all the Test Cases for the Suite type as per the selection made in "Config.xslx" file 
		@returns:			No Return type
		@END
		 */
		for (int rNum = 2; rNum <= xls.getRowCount("Test Cases"); rNum++) {
			if (suitetype.equals("Regression")) {
				SuiteTCNames.add(xls.getCellData("Test Cases", "TCID", rNum));
				}	else if (suitetype.equals("P1")||suitetype.equals("P2")||suitetype.equals("P3")||suitetype.equals("P1_P2")||suitetype.equals("P1_P3")||suitetype.equals("P2_P3")||suitetype.equals("Smoke")||suitetype.equals("Sanity")) {

				if (suitetype.contains(Underscore)) {
					String[] FirstSplit = suitetype.split(Underscore);
					String firstString = "", secondString = "";
					firstString = FirstSplit[0].toString();
					secondString = FirstSplit[1].toString();
					if ((xls.getCellData("Test Cases", "TestCaseType", rNum)).contains(firstString)
							|| (xls.getCellData("Test Cases", "TestCaseType", rNum)).contains(secondString)) {
						SuiteTCNames.add(xls.getCellData("Test Cases", "TCID", rNum));
						}
					}
				if (xls.getCellData("Test Cases", "TestCaseType", rNum).contains(suitetype)) {
				SuiteTCNames.add(xls.getCellData("Test Cases", "TCID", rNum));
				}
			} 
		}
	}

	public static void createTestngXml() throws IOException {
		/* @HELP
		@class:				CreateTestNGXml	
		@method:			createTestngXml()
		@parameter:		No Parameter
		@notes:				Creates a "tesng.xml" file as per the Suite type selection in "Config.xlsx"
		@returns:			No Return type
		@END
		 */
		String TestngXmlPath = rootPath + "/testng.xml";
		String TestngXmlData = "<!DOCTYPE suite SYSTEM \"http://testng.org/testng-1.0.dtd\" >"+"\n"+"<suite name=" + '"' + suitetype + '"' + ">\n" + "<parameter name=" + '"' + "Suite-Name" + '"' + " value=" + '"'+ suiteName + '"' + "/>\n";
		for (int x = 0; x < SuiteTCNames.size(); x++) {
			getConfigDetails();
				String TempTestingXmlData = "<test name=" + '"' + SuiteTCNames.get(x) + '"' + ">\n" + "<classes>\n" + "<class name=" + '"'+ "in.v2solutions.hybrid.testcases." + SuiteTCNames.get(x) + '"' + "></class>\n" + "</classes>\n" + "</test>\n";
				TestngXmlData = TestngXmlData.concat(TempTestingXmlData);
			}
		TestngXmlData = TestngXmlData.concat("</suite>");
		BufferedWriter bw = new BufferedWriter(new FileWriter(TestngXmlPath));
		bw.write(TestngXmlData);
		bw.close();
		System.out.println("INFO:=> TestNG XML File is Created.");
	}

	public static void main(String[] args) throws IOException {
		/* @HELP
		@class:				CreateTestNGXml	
		@method:			main()
		@parameter:		String[] args
		@notes:				Calls all the methods of the CreateTestNGXml Class
		@returns:			No Return type
		@END
		 */
		getConfigDetails();
		getSuiteTCs();
		createTestngXml();
	}
}
