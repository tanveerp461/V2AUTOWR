package in.v2solutions.hybrid.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import atu.testrecorder.ATUTestRecorder;
import edu.umass.cs.benchlab.har.HarEntries;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.HarWarning;
import edu.umass.cs.benchlab.har.tools.HarFileReader;
import net.lightbody.bmp.core.har.Har;
import java.util.Map;

public class Constants {
	/* @HELP
	@class:			Constants
	@method: 		getCurrentTime (), getConfigDetails ()
	@parameter:	Different parameters are passed as per the method declaration
	@notes:			
					1. All Global Variables used in different methods & Classes are declared in Constants Class.
					2. CopyReportsAndZip, CreateTestNGXml, DeleteReportsAndLogs Classes extends Constants Class.
	@returns:		All respective methods have there return types
	@END
	 */

//*****************************Config Details*****************************	
	public static String projectName;
	public static String SUTUrl;
	public static String SUTServer;
	public static String SUTBuildVersion;
	public static String suitetype;
	public static String deviceName;
	public static String module;
	public static String osType;
	public static String bType;
	public static String bTypeVersion;
	public static String username;
	public static String password;
	public static String contentHtml;
	public static String contentHost;
	public static String fromEmailAddress;
	public static String fromEmailPwd;
	public static String toAddress;
	public static String ccAddress;
	public static String captureVideoRecording;
	public static ATUTestRecorder recorder;
	public static String[] toAddr;
	public static String[] ccAddr;
	public static String suiteName;
	public static String waitTime;
	public static String tBedType;
	public static String deviceType;
	public static String databaseType;
	public static String dbConnection;
	public static String dbUsername;
	public static String dbPassword;
	public static String actPermalinkContent;
	
		
// ***************************Timestamp*********************************************
	public static SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_hh-mm-ss_aaa(zzz)");
	public static final java.util.Date curDate = new java.util.Date();
	public static final String strDate = sdf.format(curDate);
	public static final String strActDate = strDate.toString();
	public static long suiteStartTimeInSeconds;
	public static long suiteEndTimeInSeconds;
	public static long suiteExecutionTimeInSeconds;
	public static String totalExecutionTime;
	
// ***************************Keywords Used in Script files*********************************************
	public static String rootPath = System.getProperty("user.dir");
	public static String srcPath = "/src/in/v2solutions/hybrid/";
	public static String masterorPath = "/src/in/v2solutions/hybrid/masteror/";
	public static String imagePath =  rootPath+"/images/";
	public static String tempforderPath= rootPath+"/temp/";
	public static String harfilePath = tempforderPath+"/HAR/defaultNetworkLogs.txt";
	public static String orPath = rootPath + srcPath + "masteror/";
	public static String tcPath = rootPath + srcPath + "testcases/";
	public static String utilPath = rootPath + srcPath + "util/";
	public static String mastertsmodulePath = rootPath + srcPath + "mastertsmodule/";
	public static String tsmodulesPath = rootPath + srcPath + "tsmodules/";
	public static String tcresultfolderPath = rootPath + "/testresults/";
	public static String screenshotPath = tempforderPath + "/screenshots/";
	public static String suiteExecution_Reports_LogsPath = tcresultfolderPath+ "/SuiteExecution_Reports_Logs/";
	public static String tcresultxmlfolderPath = rootPath + "/temp/test-output/junitreports/";
	public static String sysfilesPath = rootPath + "/sysfiles/";
	public static String browserdriversFolderPath = sysfilesPath+ "/browserdrivers/";
	public static String iedriverPath = browserdriversFolderPath+ "/iedriver/IEDriverServer.exe";
	public static String chromedriverPath = browserdriversFolderPath+ "/chromedriver/chromedriver.exe";
	public static String geckodriverPath = browserdriversFolderPath+ "/geckodriver/geckodriver.exe";
	public static String edgedriverPath = browserdriversFolderPath+ "/edgedriver/MicrosoftWebDriver.exe";
	public static String dashboardPath = tcresultfolderPath+"/dashboard/";
	public static String testdataPath = rootPath+"/testdata/";
	public static String apitestdataPath= testdataPath+"/api/";
	public static String configxlsPath= rootPath + srcPath +"config/";
	public static String suitrunvideoPath= tcresultfolderPath +"CapturedVideo/";
	public static Xls_Reader configxls = new Xls_Reader(configxlsPath+"Config.xlsm");
	public static Xls_Reader xls = new Xls_Reader(mastertsmodulePath+"MasterTSModule.xlsx");
	public static String excelxExtention =".xlsx";
	public static Properties OR = null;
	public static WebDriver driver=null;
	public static FileInputStream fs = null;
	public static String tsName = "";
	public static String tcName = "";
	public static final String GUSER_XPATH = "LOGIN_USERNAME";
	public static final String GPASS_XPATH = "LOGIN_PASSWORD";
	public static final String GLOGIN = "LOGIN";
	public static String actText,actValue;
	public String actTitle,actUrl;
	public static String tname;
	public static Set <String> winIDs;
	public static Iterator<String> it;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<WebElement> countOfAllDisplayed=new ArrayList();
	public static ArrayList<String > expDimList=new ArrayList<String>();
	public static ArrayList<String > expPosList=new ArrayList<String>();
	public static String inORpath = rootPath + srcPath +"or/";
	public static String outORPath = rootPath + masterorPath + "OR.properties";
	public static String logsPath = tempforderPath + "/logs/";
	
	public static String verificationReportPath = logsPath+"VerificationSummary.html";
	public static MongoClient mongoClient;
	public static MongoDatabase db;
	public File harFile = new File(harfilePath);
	public static DesiredCapabilities capabilities;
	public static HarEntries entries;
	public static HarLog log;
	public static List<HarWarning> warnings = new ArrayList<HarWarning>();
	public static HarFileReader r = new HarFileReader();
	public static Har har;
	public static int SYNC_WAIT = 3000;
	public static int SMALL_WAIT = 2000;
	public static int MID_WAIT = 5000;
	public static int LONG_WAIT = 15000;
	public int CONFIG_IMPLICIT_WAIT_TIME;
//	public static String downloadFolderPath= "C:\\Users\\tanveer.patel\\Downloads\\";
	
	
			
// ****************************DeleteReportsAndLogs*****************************
	public static final String SRC_FOLDER1 = tempforderPath + "/XSLT_Reports/output";
	public static final String SRC_FOLDER2 = tempforderPath + "/logs";
	public static final String SRC_FOLDER3 = tempforderPath + "/screenshots";
	public static final String SRC_FOLDER4 = tempforderPath+"test-output";
	public static final String SRC_FOLDER5 = rootPath + "/testng.xml";
	public static final String SRC_FOLDER6 = mastertsmodulePath +"/MasterTSModule.xlsx";
	public static File[] files = new File[6];
	@SuppressWarnings("rawtypes")

// ****************************CreateTestNGXml*****************************
	public static ArrayList SuiteTCNames = new ArrayList();
	
// **************************CopyReportsAndZip**************************************
	public static SimpleDateFormat sdf1 = new SimpleDateFormat("hhmmss");
	public static final java.util.Date curDate1 = new java.util.Date();
	public static final String startTime = sdf1.format(curDate1).toString();
	public static File[] srcFolder = new File[3];
	public static File destFolder;
	public static String filename;
	public static String LatestLogFileName;
	public static long total,endTime;
	public static String Hyphen = "[-]";
	public static String Space = "[ ]";
	public static String Dot = "[.]";
	public static String DotZip = "[.]"+"[zip]";
	public static String SuiteRunTimeStamp;
	public static String Latestfile;
	public static String Latestresultsfolder;
	public static String Underscore = "_";
	public static String Forwardslash="/";
	public static String failedDataInXlsx = "VerificationFaliureMatricsWithErrorScreenShot.xlsx";
	public static String failedDataInText = "FailedDataInText.txt";
	public static String scrFileName=null;
	public static String globalExpText;
	public static Vector<String> verificationData=new Vector<String>();
	public static String verificationSummaryText = "VerificationSummaryText.txt";
	
	// ***************************CreateDashboard****************************************
	public static Collection<String> filesPath = new ArrayList<String>();
	public static ArrayList <String> fileNames = new ArrayList<String>();
	public static File[] listFiles;
	public static File[] listFileNames;
	public static ArrayList<String> TestSummary = new ArrayList<String>();
	@SuppressWarnings("rawtypes")
	public static ArrayList Dashboard = new ArrayList();
	public static double TOTAL_TC=0, TC_EXECUTED=0, TC_PASS=0, TC_SKIP=0, TC_FAIL=0;
	public static double AUTO_COVERAGE=0,  PERCENT_PASS=0, PERCENT_FAIL=0,PERCENT_SKIP=0;
	public static String  EXECUTION_TIME=SuiteRunTimeStamp;
	public static WebDriverWait wait;
	public static Map<String, String> mobileEmulation;
	//public static String sTransferName;
	
	// ***************************DB settings for inserting execution results****************************************
/*	public static Statement stmt = null;
	public static Connection dbHandler = null;
	public static String url = "jdbc:mysql://192.168.30.112:3307/";
	public static String dbName = "dashboard";
	public static String dbdriver = "com.mysql.jdbc.Driver";
	public static String dbuserName = "root";
	public static String dbpassword = "mail_123";
	public static String sql;
	public static String tableName = "Automation_Dashboard";*/

	public static String getCurrentTime (){
		/* @HELP
		@class:				Constants
		@method:			getCurrentTime ()
		@parameter:		No Parameters
		@notes:				Gets the current time stamp as per the format of "dd_MMM_yyyy_hh-mm-ss_aaa(zzz)"
		@returns:			Returns the String in date format "dd_MMM_yyyy_hh-mm-ss-aaa(zzz)
		@END
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_hh-mm-ss_aaa(zzz)");
		java.util.Date curDate = new java.util.Date();
		String strDate = sdf.format(curDate);
		String strActDate = strDate.toString();
		return strActDate;
	}
	
	public static void getBrowserVersion() throws Exception {
		/*@HELP
		@class:				Constants
		@method:			getBrowserVersion()
		@parameter:		No Parameters
		@notes:				Getting the browser version at runtime when "openBrowser; keyword is executed and wirtes the same in a text file.
		@returns:			No return type
		@END
	*/	
	if(tBedType.equals("MOBILE_EMULATION")){
			System.out.println("INFO:=> Loading Mobile Emulator : "+mobileEmulation.get("deviceName"));
			bTypeVersion = mobileEmulation.get("deviceName");
		}else{	
		String browserName = "";
		String browserVersion = "";
		getConfigDetails();
		Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
		browserName = caps.getBrowserName().toUpperCase();
		browserVersion = caps.getVersion();
		
		System.out.println(browserName + " " + browserVersion);
		bTypeVersion = browserName + " " + browserVersion;
		}
		File file = new File(SRC_FOLDER2 + "/Browserversion.txt");
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(bTypeVersion);
		output.close();

	}
	
	public static void getBrowserVersionfromTextFile() {
		/* @HELP
		@class:				Constants
		@method:			getBrowserVersionfromTextFile()
		@parameter:		No Parameters
		@notes:				Reads the browser version from the Text File.
		@returns:			No return type
		@END*/
		 
		//System.out.println("INFO:=> Reading Browserversion.txt file");
		String fileName = SRC_FOLDER2 + "/Browserversion.txt";
		try {
			FileReader inputFile = new FileReader(fileName);
			BufferedReader bufferReader = new BufferedReader(inputFile);
			String bVersion;
			while ((bVersion = bufferReader.readLine()) != null) {
				bTypeVersion = bVersion;
			}
			bufferReader.close();
		} catch (Exception e) {
			System.out.println("Error while reading Browserversion.txt file line by line:"+ e.getMessage());
		}
	}

	public static void getConfigDetails (){
		/* @HELP
		@class:				Constants
		@method:			getConfigDetails ()
		@parameter:		No Parameters
		@notes:				Getting all the details from Master xslx file from sheet "config details"  and storing the same in a global variable
		@returns:			No return type
		@END
		 */
		
		
		for (int rNum = 2; rNum <= configxls.getRowCount("Config Details"); rNum++) {
			if (configxls.getCellData("Config Details", "Key", rNum).equals("ProjectName")){
				projectName=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("SUTUrl")){
				SUTUrl=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("SUTServer")){
				SUTServer=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("SUTBuildVersion")){
				SUTBuildVersion=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("TestBed")){
				tBedType=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("OS")){
				osType=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("Browser/API")){
				bType=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("DeviceName")){
				deviceName=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("SuiteType")){
				suitetype=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("Module")){
				module=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("WaitTime")){
				waitTime=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("DatabaseType")){
				databaseType=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("DBConnection")){
				dbConnection=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("DBUsername")){
				dbUsername=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("DBPassword")){
				dbPassword=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("username")){
			username=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("password")){
				password=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("contentHtml")){
				contentHtml=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("contentHost")){
				contentHost=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("fromEmailAddress")){
				fromEmailAddress=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("fromEmailPwd")){
				fromEmailPwd=configxls.getCellData("Config Details", "Data", rNum);
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("toAddress")){
				toAddress=configxls.getCellData("Config Details", "Data", rNum);
				int toCount = StringUtils.countMatches(toAddress, ",");
				int i=0;
				toAddr=new String[toCount+1];
				for (String tempto: toAddress.split(",", toCount+1)){
				toAddr[i]=tempto;
				i++;
			}
			}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("ccAddress")){
				ccAddress=configxls.getCellData("Config Details", "Data", rNum);
				int ccCount = StringUtils.countMatches(ccAddress, ",");
				int i=0;
				ccAddr=new String[ccCount+1];
				for (String tempcc: ccAddress.split(",", ccCount+1)){
				ccAddr[i]=tempcc;
				i++;
			}
		}
			else if (configxls.getCellData("Config Details", "Key", rNum).equals("CaptureVideoRecording")){
				captureVideoRecording=configxls.getCellData("Config Details", "Data", rNum);
			}
	
			
	}
		suiteName=projectName+"_"+SUTBuildVersion+"--"+module+"_"+suitetype;
		//suiteName=projectName+"_"+suitetype+"_"+SUTBuildVersion;
}
	
	public static void highlightElement(WebElement element) throws InterruptedException {
		/* @HELP
		@class:				Constants
		@method:			highlightElement ()
		@parameter:		WebElement element
		@notes:				Highlights the Web Objects for verifications
		@returns:			No return type
		@END
		 */
		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);",element, "color: red; border: 3px solid red;");
     }

	public static void unhighlightElement(WebElement element) throws InterruptedException {
	/* @HELP
	@class:				Constants
	@method:			highlightElement ()
	@parameter:		WebElement element
	@notes:				Highlights the Web Objects for verifications
	@returns:			No return type
	@END
	 */	JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', arguments[1]);",element, "color: black; border: 2px white;");
	}
	
	public static String getCurrentTimeForScreenShot (){
		/* @HELP
		@class:				Constants
		@method:			getCurrentTimeForScreenShot ()
		@parameter:		No Parameters
		@notes:				Gets the current time stamp as per the format of "dd_MMM_hh-mm-ss_aaa(zzz)"
		@returns:			Returns the String in date format "dd_MMM_hh-mm-ss-aaa(zzz)
		@END*/
		 
		SimpleDateFormat sdf = new SimpleDateFormat("ddMM_hhmmss_aaa(zzz)");
		java.util.Date curDate = new java.util.Date();
		String strDate = sdf.format(curDate);
		String strActDate = strDate.toString();
		return strActDate;
	}

	public static WebElement returnElementIfPresent(String ObjectIdentifier) {
			
			WebElement element = null;
			String objectIdentifierType="";
			String objectIdentifierValue="";
			String objectArray[]=null;
			WebDriverWait wait = new WebDriverWait(driver, 20);
			
			try {
					String object = OR.getProperty(ObjectIdentifier);
					objectArray = object.split("__");
			}catch(Exception e){
					System.out.println("RESULT: FAIL - Please Append Object Identifier Type & __ to "+ObjectIdentifier);
					//APP_LOGS.debug("RESULT: FAIL - Please Append Object Identifier Type & __ to "+ObjectIdentifier);
			}
			
			try{
			objectIdentifierType=objectArray[0].trim();
			objectIdentifierValue=objectArray[1].trim();

				switch (objectIdentifierType) {
				case "id":
					element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objectIdentifierValue)));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [id]");
					break;
				case "cssSelector":
					element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(objectIdentifierValue)));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [cssSelector]");		
					break;
				case "linkText":
					element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objectIdentifierValue)));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [linkText]");
					break;
				case "xpath":
					element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objectIdentifierValue)));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [xpath]");
					break;
					
				case "name":
					element=wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(objectIdentifierValue)));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [name]");
					break;
					
				
				default:
					System.out.println("RESULT: FAIL - Please check your identifier Type in OR file  for => "+ ObjectIdentifier);
					//APP_LOGS.debug("RESULT: FAIL - Please check your identifier Type in OR file  for => "+ ObjectIdentifier);
				}

			} catch (Exception e) {
				System.out.println("Exception: "+e.getMessage());
				System.out.println("Exception: "+e.getLocalizedMessage());
				System.out.println("RESULT: FAIL - Unable to locate "+ObjectIdentifier+" element using its "+objectIdentifierType+" identifier , This can be because new code/content deployemnt on AUT. Please check and update OR file");
				//APP_LOGS.debug("RESULT: FAIL - Unable to locate "+ObjectIdentifier+" element using its "+objectIdentifierType+" identifier , This can be because new code/content deployemnt on AUT. Please check and update OR file");
				return null;
			}
			return element;
		}

	public static List<WebElement> returnElementsIfPresent(String ObjectIdentifier) {

			List<WebElement> lsElements = null;
			String objectIdentifierType="";
			String objectIdentifierValue="";
			String objectArray[]=null;
					
			try {
				String object = OR.getProperty(ObjectIdentifier);
				objectArray= object.split("__");
			}catch(Exception e){
				System.out.println("\nRESULT: FAIL - Please Append Object Identifier Type & __ to "+ObjectIdentifier);
				//APP_LOGS.debug("RESULT: FAIL - Please Append Object Identifier Type & __ to "+ObjectIdentifier);
			}
			try{
			
			objectIdentifierType=objectArray[0].trim();
			objectIdentifierValue=objectArray[1].trim();
				switch (objectIdentifierType) {
				case "id":
					lsElements=driver.findElements(By.id(objectIdentifierValue));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [id]");
					break;
				case "cssSelector":
					lsElements=driver.findElements(By.cssSelector(objectIdentifierValue));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [cssSelector]");
					break;
				case "linkText":
					lsElements=driver.findElements(By.linkText(objectIdentifierValue));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [linkText]");
					break;
				case "xpath":
					lsElements=driver.findElements(By.xpath(objectIdentifierValue));
					Thread.sleep(250);
					//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [xpath]");
					break;
				default:
					System.out.println("RESULT: FAIL - Please check your identifier Type in OR file  for => "+ ObjectIdentifier);
					//APP_LOGS.debug("RESULT: FAIL - Please check your identifier Type in OR file  for => "+ ObjectIdentifier);
				}

			} catch (Exception e) {
				System.out.println("\nRESULT: FAIL - Unable to locate "+ObjectIdentifier+" element using its "+objectIdentifierType+" identifier , This can be because new code/content deployemnt on AUT. Please check and update OR file");
				//APP_LOGS.debug("RESULT: FAIL - Unable to locate "+ObjectIdentifier+" element using its "+objectIdentifierType+" identifier , This can be because new code/content deployemnt on AUT. Please check and update OR file");
				return null;
			}
			return lsElements;
		}

	public static boolean isElementPresent(String ObjectIdentifier) {
				
				String objectIdentifierType="";
				String objectIdentifierValue="";
				String objectArray[]=null;
				WebDriverWait wait = new WebDriverWait(driver, 10);
				
				try {
						String object = OR.getProperty(ObjectIdentifier);
						objectArray = object.split("__");
				}catch(Exception e){
						System.out.println("\nRESULT: FAIL - Please Append Object Identifier Type & __ to "+ObjectIdentifier);
						//APP_LOGS.debug("RESULT: FAIL - Please Append Object Identifier Type & __ to "+ObjectIdentifier);
				}
				
				try{
				objectIdentifierType=objectArray[0].trim();
				objectIdentifierValue=objectArray[1].trim();

					switch (objectIdentifierType) {
					case "id":
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(objectIdentifierValue)));
						Thread.sleep(250);
						//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [id]");
						break;
					case "cssSelector":
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(objectIdentifierValue)));
						Thread.sleep(250);
						//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [cssSelector]");		
						break;
					case "linkText":
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(objectIdentifierValue)));
						Thread.sleep(250);
						//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [linkText]");
						break;
					case "xpath":
						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(objectIdentifierValue)));
						Thread.sleep(250);
						//System.out.println("OR    : "+ObjectIdentifier+ " element is present on Page & identified by [xpath]");
						break;
					default:
						System.out.println("\nRESULT: FAIL - Please check your identifier Type in OR file  for => "+ ObjectIdentifier);
						//APP_LOGS.debug("RESULT: FAIL - Please check your identifier Type in OR file  for => "+ ObjectIdentifier);
					}

				} catch (Exception e) {
					System.out.println("\nINFO  : "+ObjectIdentifier+" Element is Not Present on Web Page");
					//APP_LOGS.debug("INFO  : "+ObjectIdentifier+" Element is Not Present on Web Page");
					return false;
				}
				return true;
			}
	
}
	
	