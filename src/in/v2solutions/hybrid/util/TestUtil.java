package in.v2solutions.hybrid.util;

import java.util.Hashtable;

public class TestUtil {
	
	/* @HELP
	@class:			TestUtil
	@method:		isTestCaseExecutable(), getData()
	@parameter:		Different parameters are passed as per the method declaration
	@notes:			
	1. Checks the Runmode of the Test Cases from the "MasterTSModule.xlsx" file
	2. Checks the Runmode of the Test Data from the MasterTSModule.xlsx File
	@returns:		All respective methods have there return types
	@END
	 */
	
	public static boolean isTestCaseExecutable(String testCase, Xls_Reader xls) {
		 /*@HELP
		@class:			TestUtil	
		@method:		isTestCaseExecutable()
		@parameter:		String testCase, Xls_Reader xls
		@notes:			\
		1. Checks the runmode from the Test Cases sheet and executes the same accordingly
		@returns:		return object array
		@END
		 */
		for (int rNum = 2; rNum <= xls.getRowCount("Test Cases"); rNum++) {
			if (testCase.equals(xls.getCellData("Test Cases", "TCID", rNum))) {
				if (xls.getCellData("Test Cases", "RunMode", rNum).equals("Y")) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public static Object[][] getData(String testCase, Xls_Reader xls) {
		 /*@HELP
		@class:			TestUtil	
		@method:		getData()
		@parameter:		String testCase, Xls_Reader xls
		@notes:			\
		1. Finds the test in xls
		2. Finds the no. of cols
		3. Find the no. of rows
		4. Puts the data in hashtable amd put hashtable in Object array 
		@returns:		return object array
		@END
		 */
		// 
		int testCaseStartRowNum = 0;
		for (int rNum = 1; rNum <= xls.getRowCount("Test Data"); rNum++) {
				if (testCase.equals(xls.getCellData("Test Data", 0, rNum))) {
				testCaseStartRowNum = rNum;
				break;
			}
		}
		//If testCase is DDT get Data
		if (testCaseStartRowNum != 0) {
			
			int colStartRowNum = testCaseStartRowNum + 1;
			int cols = 0;
			while (!xls.getCellData("Test Data", cols, colStartRowNum).equals("")) {
				cols++;
			}
			int rowStartRowNum = testCaseStartRowNum + 2;
			int rows = 0;
			while (!xls.getCellData("Test Data", 0, (rowStartRowNum + rows)).equals("")) {
				rows++;
			}
			Object[][] data = new Object[rows][1];
			Hashtable<String, String> table = null;
			// print the test data
			for (int rNum = rowStartRowNum; rNum < (rows + rowStartRowNum); rNum++) {
				table = new Hashtable<String, String>();
				for (int cNum = 0; cNum < cols; cNum++) {
					table.put(
							xls.getCellData("Test Data", cNum, colStartRowNum),
							xls.getCellData("Test Data", cNum, rNum));
					}
				data[rNum - rowStartRowNum][0] = table;
				}
				
		return data;
		} else {
			Object[][] data = new Object[1][1];
			Hashtable<String, String> table = null;
			table = new Hashtable<String, String>();
			table.put("RunMode", "Y");
			data[0][0] = table;
			return data;
		}
	}

}
