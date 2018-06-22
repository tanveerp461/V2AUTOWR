package in.v2solutions.hybrid.testcases;

import in.v2solutions.hybrid.util.Keywords;
import in.v2solutions.hybrid.util.TestUtil;
import in.v2solutions.hybrid.util.Constants;
import java.util.Hashtable;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import atu.testrecorder.exceptions.ATUTestRecorderException;


 public class VWR_09_Verify_Expense_Reimbursement_Functionality extends Constants {

 String TCName = "VWR_09_Verify_Expense_Reimbursement_Functionality";

 String lastTestCaseName = "VWR_07_Verify_Employees_Professional_Information";

 int runModecounter = Keywords.xls.getCellRowNum("Test Data","DDTCIDWithRunMode",TCName)+2;

 @Parameters({ "Suite-Name" })
@BeforeTest
public void beforeTest(@Optional String Suitename) {

String Actsuitename = Suitename;
	if (Actsuitename != null) 
	{
		Keywords.tsName = Actsuitename;
		Keywords.tcName = TCName;
	}
	else 
	{
		Keywords.tcName = TCName;
	}
}


 @Test(dataProvider = "getTestData")
public void verify_Expense_Reimbursement_Functionality(Hashtable<String, String> data)throws Exception {
if (!TestUtil.isTestCaseExecutable(TCName,Keywords.xls))
	throw new SkipException("Skipping the test as runmode is NO");
	{

if(getTestData().length > 1) {
	String YorN = Keywords.xls.getCellData("Test Data",0,runModecounter);
// System.out.println(YorN) => Please uncomment it to debug in case there are some issue in identifying DDT Test case instences;
if (YorN.equals("N")){
runModecounter = runModecounter+1;
	throw new SkipException("Skipping the test as runmode is NO DDT");
}
	runModecounter = runModecounter+1;
	}

Keywords k = Keywords.getKeywordsInstance();
	k.executeKeywords(TCName, data);
	}

	}

@AfterTest
public void afterTest() throws ATUTestRecorderException {
 if (TCName.equals(lastTestCaseName))
	  { System.out.println(" Last Test Case Quit ");
	if(captureVideoRecording.equals("Yes")){  
	Constants.recorder.stop();  
	System.out.println(": Video Recording Stopped "); }  
	try{  
Constants.driver.close();
}catch(Exception e){
  Constants.driver = null;
}
Constants.driver = null;
	}

	}

 @DataProvider
public Object[][] getTestData() {
return TestUtil.getData(TCName, Keywords.xls);
}
}
