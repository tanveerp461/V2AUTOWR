package in.v2solutions.hybrid.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.lightbody.bmp.BrowserMobProxy;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import atu.testrecorder.ATUTestRecorder;
import atu.testrecorder.exceptions.ATUTestRecorderException;

public class Keywords extends Constants {
	/*
	 * @HELP
	 * 
	 * @class: Keywords
	 * 
	 * @Singleton Class: Keywords getKeywordsInstance()
	 * 
	 * @ constructor: Keywords()
	 * 
	 * @methods: OpenBrowser(), Navigate(), NavigateTo(), ResizeBrowser(),
	 * Login(), Input(), InputDDTdata(), Click(),
	 * SelectValueFromDropDownWithAnchorTags(), SelectValueFromDropDown(),
	 * SelectUnselectCheckbox(), GetText(), GetDollarPrice(),
	 * GetCountOfAllWebElements(), GetCountOfDisplayedWebElements(),
	 * GetCountOfImagesDisplayed(), GetSizeOfImages(), GetPositionOfImages(),
	 * Wait(), VerifyText(), VerifyTextDDTdata(), VerifyDollarPrice(),
	 * VerifyTitle(), VerifyUrl(), VerifyTotalPrice(), VerifyTotalPriceForDDT(),
	 * VerifyListOfStrings(), VerifyCountOfAllWebElements(),
	 * VerifyCountOfDisplayedWebElements(), VerifyImageCounts()
	 * VerifyListOfImageDimensions(), VerifyListOfImagePositions(),
	 * HighlightNewWindowOrPopup(), HandlingJSAlerts(), Flash_LoadFlashMovie(),
	 * Flash_SetPlaybackQuality(), Flash_SetVolume(), Flash_SeekTo(),
	 * Flash_VerifyValue(), Flash_StopVideo(), CloseBrowser(), QuitBrowser().
	 * 
	 * @parameter: Different parameters are passed as per the method declaration
	 * 
	 * @notes: Keyword Drives and Executes the framework interacting with the
	 * MasterTSModule xlsx file
	 * 
	 * @returns: All respective methods have there return types
	 * 
	 * @END
	 */

	@SuppressWarnings("rawtypes")
	public static Map getTextOrValues = new HashMap();
	// Generating Dynamic Log File
	public String FILE_NAME = System.setProperty("filename", tsName + tcName + " - " + getCurrentTime());
	public String PATH = System.setProperty("ROOTPATH", tempforderPath);
	public Logger APP_LOGS = Logger.getLogger("AutomationLog");
	public static long start;
	static Keywords keywords = null;
	public boolean Fail = false;
	public boolean highlight = false;
	public boolean captureScreenShot = false;
	public String failedResult = "";
	public static int count = 0;
	public static String scriptTableFirstRowData = "";
	static Properties props;
	public static Connection connection = null;
	public static Statement statement = null;
	public String parentWindowID;
	public String GTestName = null;
	String StrGet = null;
	String StrPost = null;
	public BrowserMobProxy proxy;
	public String interGlobal;
	StringBuilder sb = new StringBuilder(100);
	Pattern patternDigit = Pattern.compile("([0-9]+)");
	Matcher matcher;
	int allOfferCount = 0;
	BufferedWriter bw = null;
	FileWriter fw = null;
	BufferedReader br = null;
	FileReader fr = null;
	LogEntries logEntries;
	public String ActualText = null;

	private Keywords() throws IOException {
		props = new Properties();
		props.load(new FileInputStream(new File(orPath + "OR.properties/")));

		System.out.println("INFO=> Initializing keywords");
		APP_LOGS.debug("INFO=> Initializing keywords");
		// Initialize properties file
		try {
			// Config
			getConfigDetails();
			// OR
			OR = new Properties();
			fs = new FileInputStream(orPath + "OR.properties/");
			OR.load(fs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void executeKeywords(String testName, Hashtable<String, String> data) throws Exception {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: executeKeywords()
		 * 
		 * @parameter: String testName, Hashtable<String, String> data
		 * 
		 * @notes: Executes the Keywords as defined in the Master Xslx
		 * "Test Steps" Sheet and takes screenshots for any Test Step failure.
		 * The test case execution is asserted for any failure in actions and
		 * the script execution continues of at all there are some failures in
		 * verifications.
		 * 
		 * @returns: No return type
		 * 
		 * @END
		 */
		System.out.println(": =========================================================");
		APP_LOGS.debug(": =========================================================");
		System.out.println(": Executing---" + testName + " Test Case");
		APP_LOGS.debug(": Executing---" + testName + " Test Case");

		String keyword = null;
		String objectKeyFirst = null;
		String objectKeySecond = null;
		String dataColVal = null;
		GTestName = testName;
		String links_highlight_true = null;
		String links_highlight_false = null;
		String links_on_action = null;

		for (int rNum = 2; rNum <= xls.getRowCount("Test Steps"); rNum++) {
			if (testName.equals(xls.getCellData("Test Steps", "TCID", rNum))) {
				keyword = xls.getCellData("Test Steps", "Keyword", rNum);
				objectKeyFirst = xls.getCellData("Test Steps", "FirstObject", rNum);
				objectKeySecond = xls.getCellData("Test Steps", "SecondObject", rNum);
				dataColVal = xls.getCellData("Test Steps", "Data", rNum);
				String result = null;

				if (keyword.equals("OpenBrowser"))// It is not a keyword, it is
													// a supportive method
					result = OpenBrowser(dataColVal);

				else if (keyword.equals("Navigate"))
					result = Navigate(dataColVal);

				else if (keyword.equals("ClearTextField"))
					result = ClearTextField(objectKeyFirst);

				else if (keyword.equals("Click"))
					result = Click(objectKeyFirst);

				else if (keyword.equals("ClickOnEditOrDelete"))
					result = ClickOnEditOrDelete(objectKeyFirst, objectKeySecond, dataColVal);

				else if (keyword.equals("ClickOnElementIfPresent"))
					result = ClickOnElementIfPresent(objectKeyFirst);

				else if (keyword.equals("CloseBrowser"))
					result = CloseBrowser();

				else if (keyword.equals("CloseTheChildWindow"))
					result = CloseTheChildWindow();

				else if (keyword.equals("DeleteOrEditIconsExists"))
					result = DeleteOrEditIconsExists(objectKeyFirst, objectKeySecond, dataColVal);

				else if (keyword.equals("GetAllTheElementsFromDropdown"))
					result = GetAllTheElementsFromDropdown(objectKeyFirst, dataColVal);

				else if (keyword.equals("GetAvailableleaveCountBeforeApplyingLeave"))
					result = GetAvailableleaveCountBeforeApplyingLeave(objectKeyFirst);

				else if (keyword.equals("GetAvailableleaveCountAfterApplyingLeave"))
					result = GetAvailableleaveCountAfterApplyingLeave(objectKeyFirst);

				else if (keyword.equals("GetAvailableleaveCountAfterRejectionOfLeave"))
					result = GetAvailableleaveCountAfterRejectionOfLeave(objectKeyFirst);

				else if (keyword.equals("GetSelectedValueFromDropdown"))
					result = getSelectedValueFromDropdown(objectKeyFirst);

				else if (keyword.equals("GetText"))
					result = GetText(objectKeyFirst);

				else if (keyword.equals("HandlingJSAlerts"))
					result = HandlingJSAlerts();

				else if (keyword.equals("InputText"))
					result = InputText(objectKeyFirst, objectKeySecond, dataColVal);

				else if (keyword.equals("Login"))
					result = Login();

				else if (keyword.equals("MouseHover"))
					result = MouseHover(objectKeyFirst);

				else if (keyword.equals("MouseHoverAndClick"))
					result = MouseHoverAndClick(objectKeyFirst, objectKeySecond);

				else if (keyword.equals("MoveSliderHorizontallyRightSide"))
					result = MoveSliderHorizontallyRightSide(objectKeyFirst, dataColVal);

				else if (keyword.equals("MoveSliderHorizontallyLeftSide"))
					result = MoveSliderHorizontallyLeftSide(objectKeyFirst, dataColVal);

				else if (keyword.equals("QuitBrowser"))
					result = QuitBrowser();

				else if (keyword.equals("SelectDateFromCalendar"))
					result = SelectDateFromCalendar(objectKeyFirst, dataColVal);

				else if (keyword.equals("ScrollElementIntoView"))
					result = ScrollElementIntoView(objectKeyFirst);

				else if (keyword.equals("ScrollPageToBottom"))
					result = ScrollPageToBottom();

				else if (keyword.equals("ScrollPageToUp"))
					result = ScrollPageToUp();

				else if (keyword.equals("ScrollPageToEnd"))
					result = ScrollPageToEnd(objectKeyFirst);

				else if (keyword.equals("SelectRadioButton"))
					result = SelectRadioButton(objectKeyFirst);

				else if (keyword.equals("SwitchToNewWindow"))
					result = SwitchToNewWindow();

				else if (keyword.equals("SwitchToParentWindow"))
					result = SwitchToParentWindow();

				else if (keyword.equals("SelectUnselectCheckbox"))
					result = SelectUnselectCheckbox(objectKeyFirst, dataColVal);

				else if (keyword.equals("SelectValueFromDropDown"))
					result = SelectValueFromDropDown(objectKeyFirst, dataColVal);

				else if (keyword.equals("SelectValueFromDropDownWithAnchorTags"))
					result = SelectValueFromDropDownWithAnchorTags(objectKeyFirst, objectKeySecond);

				else if (keyword.equals("TestCaseEnds"))
					result = TestCaseEnds();

				else if (keyword.equals("VerifyElementPresent"))
					result = VerifyElementPresent(objectKeyFirst, dataColVal);

				else if (keyword.equals("VerifyPermalinkContent"))
					result = VerifyPermalinkContent(dataColVal);

				else if (keyword.equals("VerifyRowData"))
					result = VerifyRowData(objectKeyFirst, objectKeySecond, dataColVal);

				else if (keyword.equals("VerifyText"))
					result = VerifyText(objectKeyFirst, objectKeySecond, dataColVal);

				else if (keyword.equals("VerifyTitle"))
					result = VerifyTitle(actTitle, dataColVal);

				else if (keyword.equals("VerifyUrl"))
					result = VerifyUrl(actUrl, dataColVal);

				else if (keyword.equals("Wait"))
					result = Wait(dataColVal);

				else if (keyword.equals("WaitTillElementAppears"))
					result = WaitTillElementAppears(objectKeyFirst);

				else if (keyword.equals("WaitWhileElementPresent"))
					result = WaitWhileElementPresent();

				else if (keyword.equals("VerifyAppliedTicketsfromTable"))
					result = VerifyAppliedTicketsfromTable(objectKeyFirst, objectKeySecond);

				else if (keyword.equals("VerifyAppliedClaimsfromTable"))
					result = VerifyAppliedClaimsfromTable(objectKeyFirst, objectKeySecond);

				else if (keyword.equals("VerifyFileIsExportedAndSizeIsNotZero"))
					result = VerifyFileIsExportedAndSizeIsNotZero(dataColVal);

				else if (keyword.equals("DeleteFilesFromFolder"))
					result = DeleteFilesFromFolder(null);

								System.out.println(": " + result);
				APP_LOGS.debug(": " + result);
				File scrFile = null;
				String screeshotNameArray1[] = testName.split("_");
				String shortTcName = screeshotNameArray1[0];
				String screeshotNameArray2[] = screeshotNameArray1[1].split("_");
				shortTcName = shortTcName + "_" + screeshotNameArray2[0];

				//// ========================== FOR VERIFY
				//// KEYWORDS=======================
				if (keyword.contains("Verify")) {
					//// ============================ IF RESULT IS
					//// FAIL=======================
					if (!result.equals("PASS")) {
						if (highlight == true && captureScreenShot == true) // For
																			// UI
																			// Test
																			// cases
																			// Verification
																			// Fail
						{
							try {
								highlightElement(returnElementIfPresent(objectKeyFirst));
								scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
								scrFileName = shortTcName + "--Failed_AT-" + keyword + "-" + objectKeyFirst + "-"
										+ getCurrentTimeForScreenShot() + ".png";
								links_highlight_true = " , For Error Screenshot please refer to this link  : "
										+ "<a href=" + "'" + scrFileName + "'" + ">" + scrFileName + "</a>";
								String filename = SRC_FOLDER2 + Forwardslash + failedDataInText;
								FileWriter fw = new FileWriter(filename, true);
								String tempStr;
								tempStr = shortTcName + "__" + objectKeyFirst + "__" + actText + "__" + globalExpText
										+ "__" + scrFileName;
								fw.write(tempStr + "\r\n");
								fw.close();
								unhighlightElement(returnElementIfPresent(objectKeyFirst));
							} catch (Exception e) {
								Fail = true;
								failedResult = failedResult.concat(result + links_highlight_true + " && ");
							}
							try {
								FileUtils.copyFile(scrFile, new File(screenshotPath + scrFileName));
								System.out.println(": Verification failed. Please refer " + scrFileName);
								APP_LOGS.debug(": Verification failed. Please refer " + scrFileName);
								Fail = true;
								failedResult = failedResult.concat(result + links_highlight_true + " && ");
								System.out.println(": On Verification when highlight is True Failed");
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						else if (highlight == false && captureScreenShot == true) // For
																					// UI
																					// Test
																					// cases
																					// Verification
																					// Fail
																					// because
																					// of
																					// Element
																					// Not
																					// found
						{
							scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE); // error
																									// when
																									// highlight
																									// is
																									// not
																									// set
																									// in
																									// VerifyCompleteGetResponse
							scrFileName = shortTcName + "--Failed_AT-" + keyword + "-" + objectKeyFirst + "-"
									+ getCurrentTimeForScreenShot() + ".png";
							links_highlight_false = " , For Error Screenshot please refer to this link  : " + "<a href="
									+ "'" + scrFileName + "'" + ">" + scrFileName + "</a>";
							String filename = SRC_FOLDER2 + Forwardslash + failedDataInText;
							FileWriter fw = new FileWriter(filename, true);
							String tempStr;
							tempStr = testName + "__" + objectKeyFirst + "__" + objectKeyFirst
									+ " Not able to read text. Please check and modify Object Repository or  wait time"
									+ "__" + "" + "__" + scrFileName;
							fw.write(tempStr + "\r\n");
							fw.close();
							Thread.sleep(500);
							FileUtils.copyFile(scrFile, new File(screenshotPath + scrFileName));
							System.out.println(
									": Unable to Verify, as Web Element Not Found. Please refer " + scrFileName);
							APP_LOGS.debug(": Unable to Verify, as Web Element Not Found. Please refer " + scrFileName);
							Fail = true;
							failedResult = failedResult.concat(result + links_highlight_false + " && ");
						}

						else if (highlight == false && captureScreenShot == false) // For
																					// HAR,
																					// DB
																					// and
																					// API
																					// Test
																					// cases.
																					// We
																					// don't
																					// need
																					// to
																					// Highlight
																					// and
																					// take
																					// screenshot
						{
							String filename = SRC_FOLDER2 + Forwardslash + failedDataInText;
							FileWriter fw = new FileWriter(filename, true);
							String tempStr;
							tempStr = testName + "__" + objectKeyFirst + "__" + actText + "__" + globalExpText;
							fw.write(tempStr + "\r\n");
							fw.close();
							System.out.println(": VERIFICATION failed for HAR, DB or API call .");
							APP_LOGS.debug(": VERIFICATION failed for HAR, DB or API call.");
							Fail = true;
							failedResult = failedResult.concat(result + " && ");

						}
					}
					///// =============================== Creating HTML
					///// VERIFICATION NOTEPAD
					String filename = SRC_FOLDER2 + Forwardslash + verificationSummaryText;
					try {
						FileWriter fw = new FileWriter(filename, true);
						String tempStr = GTestName;
						if (result.equals("PASS")) {
							tempStr += " " + "__" + objectKeyFirst + "__" + keyword + "__" + "Y" + "__" + "-";
						} else {
							tempStr += " " + "__" + objectKeyFirst + "__" + keyword + "__" + "-" + "__" + "Y";
						}
						count++;

						fw.write(tempStr + "\r\n");
						fw.close();
					} catch (Exception e) {
						System.out.println("Error in count of the verification points..");
						e.printStackTrace();
					}
				}

				/////// ================================= FOR
				/////// ACTION=========================
				else {
					if (!result.equals("PASS")) {
						if (highlight == false && captureScreenShot == true) // UI
																				// Action
																				// Fail
						{
							scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

							scrFileName = shortTcName + "--Failed_AT-" + keyword + "-" + objectKeyFirst + "-"
									+ getCurrentTimeForScreenShot() + ".png";
							links_on_action = " , For Error Screenshot please refer to this link  : " + "<a href=" + "'"
									+ scrFileName + "'" + ">" + scrFileName + "</a>";
							String filename = SRC_FOLDER2 + Forwardslash + failedDataInText;
							FileWriter fw = new FileWriter(filename, true);
							String tempStr;
							tempStr = shortTcName + "__" + objectKeyFirst + "__" + objectKeyFirst
									+ " Did not appeared after waiting " + waitTime
									+ " seconds. Please check the application status or modify Object Repository, Wait time."
									+ "__" + "" + "__" + scrFileName;
							fw.write(tempStr + "\r\n");
							fw.close();
							try {
								FileUtils.copyFile(scrFile, new File(screenshotPath + scrFileName));
								System.out.println(
										": ACTION failed for UI because of Object Not Found Issue. Please refer "
												+ scrFileName);
								APP_LOGS.debug(": ACTION failed for UI because of Object Not Found Issue. Please refer "
										+ scrFileName);
								Fail = true;
								failedResult = failedResult.concat(result + links_on_action + " && ");
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.println(": TEST SCRIPT:=> " + GTestName + " Has FAILED!!!!!!!!!!!!");
							APP_LOGS.debug(": TEST SCRIPT:=> " + GTestName + " Has FAILED!!!!!!!!!!!!");
							Fail = false;
							QuitBrowser();
							driver = null;
							String failedResult1 = failedResult;
							failedResult = "";
							Assert.assertTrue(false, failedResult1);
						} else if (highlight == false && captureScreenShot == false) // DB,
																						// API
																						// Action
																						// Fail
						{
							String filename = SRC_FOLDER2 + Forwardslash + failedDataInText;
							FileWriter fw = new FileWriter(filename, true);
							String tempStr;
							tempStr = shortTcName + "__" + objectKeyFirst + "__" + actText + "__" + globalExpText;
							fw.write(tempStr + "\r\n");
							fw.close();
							System.out.println(": ACTION failed for DB or API call .");
							APP_LOGS.debug(": ACTION failed for DB or API call.");
							Fail = true;
							failedResult = failedResult.concat(result + " && ");
						}
					} // last if is closing
				} // first Else is closing. it is of inner IF's
			} // outer If loop is closing
		} // outer For loop is closing t
	}

	// **************************************************************************************************Keywords
	// Definitions******************************************************************************************************************************
	public String OpenBrowser(String browserType) throws Exception {
		/* @HELP
			@class:			Keywords
			@method:		OpenBrowser ()
			@parameter:	String browserType
			@notes:			Opens Browsers, Sets Timeout parameter and Maximize the Browser
			@returns:		("PASS" or "FAIL" with Exception in case if method not got executed because of some runtime exception) to executeKeywords method 
			@END
		 */		
		getConfigDetails();
		int WaitTime;
		NumberFormat nf = NumberFormat.getInstance();
		Number number = nf.parse(waitTime);
		WaitTime = number.intValue();
		CONFIG_IMPLICIT_WAIT_TIME=WaitTime;
		failedResult = "";
		System.out.println(": Opening: " + bType + " Browser");
		try {

			if(tBedType.equals("DESKTOP")){
				//***************** 1. For Desktop Browsers****************//
				if (bType.equals("Chrome")) {
					System.setProperty("webdriver.chrome.driver", chromedriverPath);
					if(GTestName.contains("LT")){
						//System.out.println("In Caps with GTestName"+GTestName);
						//set chromedriver system property
						DesiredCapabilities caps = DesiredCapabilities.chrome();
						ChromeOptions options = new ChromeOptions();
						options.addArguments("--allow-running-insecure-content");
						LoggingPreferences logPrefs = new LoggingPreferences();
						logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
						caps.setCapability(ChromeOptions.CAPABILITY, options);
						caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
						driver = new ChromeDriver(caps);
//						logEntries = driver.manage().logs().get(LogType.PERFORMANCE);
					
					}else{
						driver = new ChromeDriver();
					}
					getBrowserVersion();
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();

				}
				/*else if (bType.equals("Edge")) {
				     System.setProperty("webdriver.edge.driver", edgedriverPath);

				     driver = new EdgeDriver();
				     getBrowserVersion();
				     //System.out.println("______________ "+getBrowserVersion());
				     APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
				     driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
				     driver.manage().window().maximize();
				    }*/
				else if (bType.equals("Edge")) {
					//System.out.println("OPENING EDGE");
					System.setProperty("webdriver.edge.driver", edgedriverPath);
					//System.out.println("OPENING EDGE2");
					driver = new EdgeDriver();
					//System.out.println("OPENING EDGE3");
					getBrowserVersion();
					//System.out.println("______________ "+getBrowserVersion());
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();


				} else if (bType.equals("Mozilla")) {
					System.setProperty("webdriver.gecko.driver", geckodriverPath);

					driver = new FirefoxDriver();
					getBrowserVersion();
					//System.out.println("______________ "+getBrowserVersion());
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
				} else if (bType.equals("Safari")) {

					driver = new SafariDriver();
					getBrowserVersion();
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
				} else if (bType.equals("IE")) {
					System.setProperty("webdriver.ie.driver", iedriverPath);

					DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

					ieCapabilities.setCapability("nativeEvents", false);
					ieCapabilities.setCapability("unexpectedAlertBehaviour", "accept");
					ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
					ieCapabilities.setCapability("disable-popup-blocking", true);
					ieCapabilities.setCapability("enablePersistentHover", true);
					ieCapabilities.setCapability("ignoreZoomSetting", true);
					
					
					//InternetExplorerOptions capsIE = new InternetExplorerOptions();
					//capsIE.ignoreZoomSettings();//IgnoreZoomLevel = true;
					//capsIE.enableNativeEvents();//EnableNativeEvents = false;
					//capsIE.InitialBrowserUrl = "http://localhost";
					//capsIE.UnexpectedAlertBehavior = InternetExplorerUnexpectedAlertBehavior.Accept;
					//capsIE.introduceFlakinessByIgnoringSecurityDomains();//introduceInstabilityByIgnoringProtectedModeSettings = true;
					//capsIE.enablePersistentHovering();//EnablePersistentHover = true; 
					
					
					//driver = new InternetExplorerDriver(ieCapabilities);
					driver = new InternetExplorerDriver();
					getBrowserVersion();
					APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
					driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);
					driver.manage().window().maximize();
				} else if (bType.equals("Opera")) {
					driver = new OperaDriver();
				}	
				else if (bType.equals("HtmlUnit")) {
					driver = new HtmlUnitDriver(true);
				}
				if(captureVideoRecording.equals("Yes")){
					DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
					Date date = new Date();
					//Created object of ATUTestRecorder
					//Provide path to store videos and file name format.
					recorder = new ATUTestRecorder(suitrunvideoPath,"RecordedVideo-"+dateFormat.format(date),false);
					System.out.println(": Video Recording Started ");
					APP_LOGS.debug(": Video Recording Started ");
					//To start video recording.
					recorder.start();  
				}
				//APP_LOGS.debug(": Opening: " + bTypeVersion + " Browser");
				driver.manage().timeouts().implicitlyWait(WaitTime, TimeUnit.SECONDS);

			}else if(tBedType.equals("MOBILE_EMULATION")){

				if (bType.equals("Chrome")) {
					System.setProperty("webdriver.chrome.driver", chromedriverPath);
					if(GTestName.contains("LT")){
						mobileEmulation = new HashMap<String, String>();
						mobileEmulation.put("deviceName", deviceName);
						Map<String, Object> chromeOptions = new HashMap<String, Object>();
						chromeOptions.put("mobileEmulation", mobileEmulation);
						DesiredCapabilities capabilities = DesiredCapabilities.chrome();
						capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
						getBrowserVersion();
						System.out.println(": Opening Mobile Emulator : " + bTypeVersion + " in Chrome Browser");
						APP_LOGS.debug(": Opening Mobile Emulator : " + bTypeVersion + " in Chrome Browser");

						System.out.println(": Launching : " + capabilities.getBrowserName());
						APP_LOGS.debug(": Launching : " + capabilities.getBrowserName());

						driver = new ChromeDriver(capabilities);
					}
				}
				else{
					System.out.println(": The Browser Type: " + bType + " is not valid. Please Enter a valid Browser Type");
					APP_LOGS.debug(": The Browser Type: " + bType + " is not valid. Please Enter a valid Browser Type");
					return "FAIL - The Browser Type: " + bType + " is not valid. Please Enter a valid Browser Type";
				}
			}
			else {
				System.out.println(": The Browser Type: " + bType + " is not valid. Please Enter a valid Browser Type");
				return "FAIL - The Browser Type: " + bType + " is not valid. Please Enter a valid Browser Type";
			}


		} catch (Exception e) {
			return "FAIL - Not able to Open Browser";
		}
		return "PASS";
	}
	
	public String Navigate(String URLKey) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Navigate ()
		 * 
		 * @parameter: String URLKey
		 * 
		 * @notes: Navigate opened Browser to specific URL as metioned in the
		 * config details.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		getConfigDetails();
		/*
		 * System.out.println("deleting cookies");
		 * driver.manage().deleteAllCookies();
		 */
		failedResult = "";
		System.out.println(": Navigating to (" + SUTUrl + ") Site");
		APP_LOGS.debug(": Navigating to (" + SUTUrl + ") Site");
		try {
			if (captureVideoRecording.equals("Yes")) {
				DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
				Date date = new Date();
				recorder = new ATUTestRecorder(suitrunvideoPath,
						"RECVideo-" + Keywords.tcName + dateFormat.format(date), false);
				System.out.println(": Video Recording Started ");
				APP_LOGS.debug(": Video Recording Started ");
				recorder.start();
			}
			if (driver != null) {
				System.out.println(": Driver Handle: " + driver);
				String Windowhd = driver.getWindowHandle();
				System.out.println(": Browser is Already Opened and same will be used for this TestScript execution");
				APP_LOGS.debug(": Browser is Already Opened and same will be used for this TestScript execution");
				driver.get(SUTUrl);
				Thread.sleep(2000);
				WebDriverWait wait = new WebDriverWait(driver, 3);
				// System.out.println(":ExpectedConditions.alertIsPresent())->:
				// "+ExpectedConditions.alertIsPresent());
				if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
					System.out.println(": Alert Popup is persent");
					APP_LOGS.debug(": Alert Popup is persent");
					Alert alt = driver.switchTo().alert();
					alt.accept();
					System.out.println(": Alert Popup is Accepted");
					APP_LOGS.debug(": Alert Popup is Accepted");
					driver.switchTo().window(Windowhd);
				}
			} else {
				System.out.println(": Driver Handle: " + driver);
				System.out.println(": No Opened Browser Available, Opening New one");
				APP_LOGS.debug(": No Opened Browser Available, Opening New one");
				OpenBrowser(bType);
				driver.get(SUTUrl);
			}
			/*
			 * System.out.println("deleting cookies");
			 * driver.manage().deleteAllCookies();
			 */
		} catch (Exception e) {
			System.out.println(": Alert Exception getMessage: " + e.getMessage());
			if (e.getMessage().contains("Expected condition failed")) {
				System.out.println(": Alert hasn't Appeared");
				return "PASS";
			} else {
				System.out.println(": Alert Exception: " + e.getLocalizedMessage());
				return "FAIL - Not able to Navigate " + SUTUrl + " Site" + e.getMessage();
			}
		}
		return "PASS";
	}

	public String ClearTextField(String firstXpathKey) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: clearTextField ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Clearing Text Field.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		try {
			System.out.println(": Clearing Text Field");
			APP_LOGS.debug(": Clearing Text Field");
			Thread.sleep(1000);
			returnElementIfPresent(firstXpathKey).clear();
		} catch (InterruptedException e) {
			captureScreenShot = true;
			System.out.println("Not Able to perform clearTextField");
		}
		return "PASS";
	}

	public String Click(String firstXpathKey) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Click ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Performs Click action on link, Hyperlink, selections or
		 * buttons of a web page.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Performing Click action on " + firstXpathKey);
		APP_LOGS.debug(": Performing Click action on " + firstXpathKey);
		highlight = false;
		captureScreenShot = false;
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		try {
			Thread.sleep(1000);
			if (GTestName.contains("HLT")) {
				if (bType.equals("Edge")) {
					System.out.println(": In Edge if for Click ");
					returnElementIfPresent(firstXpathKey).click();
					Thread.sleep(1000);
				} else {
					wait = new WebDriverWait(driver, 20);
					// Wait for element to be clickable
					wait.until(ExpectedConditions.elementToBeClickable(returnElementIfPresent(firstXpathKey)));
					executor.executeScript("arguments[0].click();", returnElementIfPresent(firstXpathKey));
					Thread.sleep(1000);
				}
			} else {
				returnElementIfPresent(firstXpathKey).click();
				Thread.sleep(1000);
			}

		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to click on -- " + firstXpathKey + e.getMessage();
		}
		return "PASS";
	}

	@SuppressWarnings("unused")
	public String ClickOnEditOrDelete(String firstXpathKey, String SecondXpathKey, String inputData) {

		System.out.println(": Clicking on " + inputData + " button for the Leave applied by Employee");
		APP_LOGS.debug(": Clicking on " + inputData + " button for the Leave applied by Employee");
		highlight = false;
		captureScreenShot = false;
		JavascriptExecutor jse1 = (JavascriptExecutor) driver;
		String TestData = inputData;
		try {
			WebElement table = returnElementIfPresent(firstXpathKey);
			List<WebElement> rows = table.findElements(By.tagName("tr"));
			String expNarrationText = (String) getTextOrValues.get(SecondXpathKey);
			for (WebElement row : rows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				for (WebElement cell : cells) {
					if (cell.getText().contains(expNarrationText)) {
						Actions actions = new Actions(driver);
						actions.moveToElement(cell).perform();
						Thread.sleep(2000);
						jse1.executeScript("window.scrollBy(0,200)");
						Thread.sleep(2000);
						if (TestData.contains("Delete")) {
							cell.findElement(By.xpath("//span[2]/button")).click();
							break;
						}
						if (TestData.contains("Edit")) {
							cell.findElement(By.xpath("//span[3]/button[@title= 'Edit']")).click();
							break;
						} else {
							System.out.println("Unable to Click on " + inputData + " button");
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Unable to delete leaves " + e.getMessage());
			return "FAIL - Unable to delete leaves" + e.getMessage();
		}
		return "PASS";
	}

	public String ClickOnElementIfPresent(String firstXpathKey) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Click ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Performs Click action on link, Hyperlink, selections or
		 * buttons of a web page.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Performing Click action on " + firstXpathKey + " Element if it is Present in WebPage");
		APP_LOGS.debug(": Performing Click action on " + firstXpathKey + " Element if it is Present in WebPage");
		highlight = false;
		captureScreenShot = false;
		try {
			if (isElementPresent(firstXpathKey)) {
				System.out.println(": " + firstXpathKey + "Element is present. Performing Click Action on it.");
				APP_LOGS.debug(": " + firstXpathKey + "Element is present. Performing Click Action on it.");
				returnElementIfPresent(firstXpathKey).click();
			} else {
				System.out.println(": " + firstXpathKey + "Element is Not present in WebPage");
				APP_LOGS.debug(": " + firstXpathKey + "Element is Not present in WebPage");
				captureScreenShot = true;
			}
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to click on -- " + firstXpathKey;
		}
		return "PASS";
	}

	public String CloseBrowser() throws ATUTestRecorderException {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: CloseBrowser ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Closing the opened Browser after the Test Case Execution.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		getTextOrValues.clear();
		scriptTableFirstRowData = "";
		System.out.println(": Closing the Browser");
		APP_LOGS.debug(": Closing the Browser");
		try {
			driver.close();
			driver = null;
			if (captureVideoRecording.equals("Yes")) {
				System.out.println(": Video Recording Stopped ");
				APP_LOGS.debug(": Video Recording Stopped ");
				recorder.stop();
				Thread.sleep(SYNC_WAIT);
			}
		} catch (Exception e) {
			return "FAIL - Not able to Close Browser";
		}
		return "PASS";
	}

	public String CloseTheChildWindow() {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: dragAndDropByCoordinates (data)
		 * 
		 * @parameter: None
		 * 
		 * @notes: Makes POST request with attached data in form of file saved
		 * on HDD( in XMLForLT folder of the framework which contains JSON file)
		 * using apache apache library supported HttpRequest and HttpResponse.
		 * In dataColValue user must pass file path followed by URL e.g
		 * https://offers.dev.lendingtree.com/formstore/submit-lead.ashx,/
		 * XMLForLT/LT_02_Verify_POST_API_JSON.json
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */

		System.out.println(": Closing Child Window");
		APP_LOGS.debug(": Closing Child Window");
		highlight = false;
		captureScreenShot = false;
		try {
			String ParentWindow;
			String ChildWindow1;
			Set<String> set = driver.getWindowHandles();
			Iterator<String> it = set.iterator();
			ParentWindow = it.next();
			ChildWindow1 = it.next();
			driver.switchTo().window(ChildWindow1);
			Thread.sleep(2000);
			driver.close();
			driver.switchTo().window(ParentWindow);

		} catch (Exception e) {
			captureScreenShot = true;
			System.out.println(": " + e.getMessage());
			return "FAIL - Not Able to close Child Window";
		}
		return "PASS";
	}

	@SuppressWarnings("unused")
	public String DeleteOrEditIconsExists(String firstXpathKey, String SecondXpathKey, String inputData) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: DeleteOrEditIconsExists ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Will check if the Delete and Edit Icons are present on the
		 * page by comparing with inputData For Example(If inputData=Yes,Yes
		 * then Delete and Edit should get displayed).
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Verifying Edit and Delete icons for the leave applied by employee ");
		APP_LOGS.debug(": Verifying Edit and Delete icons for the leave applied by employee ");
		highlight = false;
		captureScreenShot = false;
		JavascriptExecutor jse1 = (JavascriptExecutor) driver;
		try {
			String[] testData = inputData.split(",");
			String DeleteIcon = testData[0];
			String EditIcon = testData[1];
			WebElement table = returnElementIfPresent(firstXpathKey);
			List<WebElement> rows = table.findElements(By.tagName("tr"));
			String expNarrationText = (String) getTextOrValues.get(SecondXpathKey);
			for (WebElement row : rows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				for (WebElement cell : cells) {
					if (cell.getText().contains(expNarrationText)) {
						Actions actions = new Actions(driver);
						actions.moveToElement(cell).perform();
						Thread.sleep(2000);
						jse1.executeScript("window.scrollBy(0,200)");
						Thread.sleep(2000);
						boolean Delete = cell.findElement(By.xpath("//span[2]/button")).isDisplayed();
						boolean Edit = cell.findElement(By.xpath("//span[3]/button[@title= 'Edit']")).isDisplayed();
						if (DeleteIcon.contains("Yes") && EditIcon.contains("Yes")) {
							if (Delete == true && Edit == true) {
								System.out.println(
										": Test Case PASS : Delete and Edit Icons Displayed for the leave applied by Employee");
								break;
							}
						}
						if (DeleteIcon.contains("No") && EditIcon.contains("No")) {
							if (Delete == false && Edit == false) {
								System.out.println(
										": Test Case PASS : As expected Delete and Edit Icons NOT Displayed for the leave applied by Employee");
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Unable to delete leaves " + e.getMessage());
			return "FAIL - Unable to delete leaves" + e.getMessage();
		}
		return "PASS";
	}

	public String GetAllTheElementsFromDropdown(String firstXpathKey, String inputData) throws IOException {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: GetAllTheElementsFromDropdown ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Get the text of all the dropdown data and compare with data
		 * from inputData Example: (Maternity,No) - Maternity should not be
		 * displayed in dropdown.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Verifying Data from Dropdown List");
		APP_LOGS.debug(": Verifying Data from Dropdown List");
		String sDropDownOptions = "";
		highlight = false;
		captureScreenShot = false;
		try {
			List<WebElement> drop = returnElementsIfPresent(firstXpathKey);
			java.util.Iterator<WebElement> i = drop.iterator();
			while (i.hasNext()) {
				WebElement row = i.next();
				System.out.println(": Dropdown Values are :" + row.getAttribute("innerText"));
				sDropDownOptions = sDropDownOptions + row.getAttribute("innerText");
			}
			String[] testData = inputData.split(",");
			String expText = testData[0];
			String YesorNo = testData[1];
			if (YesorNo.contains("Yes")) {
				if (sDropDownOptions.contains(expText)) {
					System.out.println(": Verifying " + expText + " Value in Dropdown -> Expected= " + YesorNo + ","
							+ " Actual= Yes");
				} else {
					System.out.println(": Verifying " + expText + " Value in Dropdown -> Expected= " + YesorNo + ","
							+ " Actual= No");
				}
			}
			if (YesorNo.contains("No")) {
				if (!sDropDownOptions.contains(expText)) {
					System.out.println(": Verifying " + expText + " Value in Dropdown -> Expected= " + YesorNo + ","
							+ " Actual= No");
				} else {
					System.out.println(": Verifying " + expText + " Value in Dropdown -> Expected= " + YesorNo + ","
							+ " Actual= Yes");
				}
			}
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to get the values from drop down" + e.getMessage();
		}
		return "PASS";
	}

	public String GetAvailableleaveCountAfterApplyingLeave(String firstXpathKey) throws IOException {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: GetAvailableleaveCountAfterApplyingLeave ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Get the count of leaves available after applying the leave.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Verifying Employee's Available Leave Balance After applying Leave");
		APP_LOGS.debug(": Verifying Employee's Available Leave Balance After applying Leave");
		highlight = false;
		captureScreenShot = false;
		try {
			String actText = returnElementIfPresent(firstXpathKey).getText().trim();
			System.out.println(": Total Leaves Available After Appying Leave is :> " + actText);
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to read Available Leaves from " + firstXpathKey;
		}
		return "PASS";
	}

	public String GetAvailableleaveCountBeforeApplyingLeave(String firstXpathKey) throws IOException {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: GetAvailableleaveCountBeforeApplyingLeave ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Get the count of leaves available Before applying the leave.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Verifying Employee's Available Leave Balance Before applying Leave");
		APP_LOGS.debug(": Verifying Employee's Available Leave Balance Before applying Leave");
		highlight = false;
		captureScreenShot = false;
		try {
			String actText = returnElementIfPresent(firstXpathKey).getText().trim();
			System.out.println(": Total Leaves Available Before Applying Leave is :> " + actText);
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to read Available Leaves from " + firstXpathKey;
		}
		return "PASS";
	}

	public String GetAvailableleaveCountAfterRejectionOfLeave(String firstXpathKey) throws IOException {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: GetAvailableleaveCountAfterApplyingLeave ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Get the count of leaves available after applying the leave.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Verifying Employee's Available Leave Balance After Rejection of Leave from Manager");
		APP_LOGS.debug(": Verifying Employee's Available Leave Balance After Rejection of Leave from Manager");
		highlight = false;
		captureScreenShot = false;
		try {
			String actText = returnElementIfPresent(firstXpathKey).getText().trim();
			System.out.println(": Total Leaves Available After Rejection of Leave from Manager :> " + actText);
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to read Available Leaves from " + firstXpathKey;
		}
		return "PASS";
	}

	public String getLastTestCaseName() {

		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: getLastTestCaseName ()
		 * 
		 * @returns: returns last test case name from Master.xlsx file which
		 * have runmode Y into any combination
		 * 
		 * @END
		 */
		Xls_Reader x = new Xls_Reader(mastertsmodulePath + "/MasterTSModule.xlsx");
		String suiteType = suitetype;

		if (!suiteType.contains("_") && !suiteType.equalsIgnoreCase("Regression")) {
			// System.out.println(": This suiteType don't contains UnderScore
			// and is: "+suiteType);
			int totalRows = x.getRowCount("Test Cases");
			String lastTestCaseName = null;
			String tcType = null;
			String runMode = null;
			for (int i = 1; i <= totalRows; i++) {
				tcType = x.getCellData("Test Cases", 1, i);
				if (tcType.contains(suiteType)) {
					runMode = x.getCellData("Test Cases", 2, i);
					if (runMode.contains("Y")) {
						lastTestCaseName = x.getCellData("Test Cases", 0, i);
					}
				}
			}
			System.out.println("INFO:=> Last Test Case Name is: " + lastTestCaseName);
			return lastTestCaseName;

		} else if (suiteType.contains("_")) {
			// System.out.println("INFO:=> This suiteType contains UnderScore
			// and is: "+suiteType);
			String splitArray[] = suiteType.split("_");
			int totalRows = x.getRowCount("Test Cases");
			String lastTestCaseName = null;
			String tcType = null;
			String runMode = null;
			for (int i = 1; i <= totalRows; i++) {
				tcType = x.getCellData("Test Cases", 1, i);
				if (tcType.contains(splitArray[0]) || tcType.contains(splitArray[1])) {
					runMode = x.getCellData("Test Cases", 2, i);
					if (runMode.contains("Y")) {
						lastTestCaseName = x.getCellData("Test Cases", 0, i);
					}
				}
			}
			System.out.println("INFO:=> Last Test Case Name is: " + lastTestCaseName);
			return lastTestCaseName;

		} else {
			System.out.println("INFO:=> This suiteType is " + suiteType);
			int totalRows = x.getRowCount("Test Cases");
			String lastTestCaseName = null;
			String runMode = null;
			for (int i = 1; i <= totalRows; i++) {
				runMode = x.getCellData("Test Cases", 2, i);
				if (runMode.equalsIgnoreCase("Y")) {
					lastTestCaseName = x.getCellData("Test Cases", 0, i);
				}
			}
			System.out.println("INFO:=> Last Test Case Name is: " + lastTestCaseName);
			return lastTestCaseName;
		}
	}

	public static Keywords getKeywordsInstance() throws IOException {
		if (keywords == null) {
			keywords = new Keywords();
		}
		return keywords;
	}

	public String getSelectedValueFromDropdown(String firstXpathKey) throws Exception {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: getSelectedValueFromDropdown ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: WebDriver Object focus should move to JavaScript Alerts
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": getting value from drop down");
		APP_LOGS.debug(": getting value from drop down");
		highlight = false;
		captureScreenShot = false;
		String value = "";
		try {

			Select sel = new Select(returnElementIfPresent(firstXpathKey));
			value = sel.getFirstSelectedOption().getText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			captureScreenShot = true;
			return "FAIL - Not able to getting value from drop down";
		}

		return value;
	}

	public String GetText(String firstXpathKey) throws IOException {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: GetText ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Get the text of the web element of the passed "firstXpathKey"
		 * and stores it into a global Hash map "getTextOrValues".
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Getting " + firstXpathKey + " Text from the Page");
		APP_LOGS.debug(": Getting " + firstXpathKey + " Text from the Page");
		highlight = false;
		captureScreenShot = false;
		try {
			getTextOrValues.put(firstXpathKey, returnElementIfPresent(firstXpathKey).getText().trim());
			// String ActText = returnElementIfPresent(firstXpathKey).getText();
			// System.out.println(ActText);

		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to read text from " + firstXpathKey;
		}
		return "PASS";
	}

	public String HandlingJSAlerts() throws Exception {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: HandlingJSAlerts ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: WebDriver Object focus should move to JavaScript Alerts
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Handling Java Scripts Alerts");
		APP_LOGS.debug(": Handling Java Scripts Alerts");
		highlight = false;
		captureScreenShot = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 3);
			if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				System.out.println(": Alert Popup is persent");
				APP_LOGS.debug(": Alert Popup is persent");
				Alert alt = driver.switchTo().alert();
				alt.accept();
			} else {
				System.out.println(": Alert Popup is NOT persent");
				APP_LOGS.debug(": Alert Popup is Not persent");
			}
			// alt.dismiss();
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to Switch to NewWindow or Popup";
		}
		return "PASS";
	}

	public String InputText(String firstXpathKey, String secondXpathKey, String inputData) throws Exception {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Input ()
		 * 
		 * @parameter: String firstXpathKey & String inputData
		 * 
		 * @notes: Inputs the value in any edit box. Value is defined in the
		 * master xlsx file and is assigned to "inputData" local variable. We
		 * cannot perform a data driven testing using the input keyword.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */

		if (inputData.isEmpty()) {
			System.out.println(": Test Data is Empty, taking this value from Hashmap");
			APP_LOGS.debug(": Test Data is Empty, taking this value from Hashmap");
			inputData = (String) getTextOrValues.get(secondXpathKey);
			System.out.println(": expText " + inputData);
			APP_LOGS.debug(": expText " + inputData);
			if (inputData == null) {
				System.out.println(
						": No Test Data present in Hashmap, taking this value from secondXpathKey object of Webpage");
				APP_LOGS.debug(
						": No Test data present in Hashmap, taking this value from secondXpathKey object of Webpage");
				inputData = returnElementIfPresent(secondXpathKey).getText().trim();
			}
		}

		else {
			if (inputData.equals("LeaveNarration")) {
				SimpleDateFormat tsdf = new SimpleDateFormat("ddMMMyyyyHHmmssz");
				java.util.Date tcurDate = new java.util.Date();
				String tstrDate = tsdf.format(tcurDate);
				String tstrActDate = tstrDate.toString();
				inputData = inputData + "_" + tstrActDate;
				System.out.println(": INPUTDATA FOR Leave Narration: " + inputData);
				getTextOrValues.put(firstXpathKey, inputData.trim());
			}

			else if (inputData.equals("TicketComment")) {
				SimpleDateFormat tsdf = new SimpleDateFormat("ddMMMyyyyHHmmssz");
				java.util.Date tcurDate = new java.util.Date();
				String tstrDate = tsdf.format(tcurDate);
				String tstrActDate = tstrDate.toString();
				inputData = inputData + "_" + tstrActDate;
				System.out.println(": INPUTDATA FOR Helpdesk Ticket Comment : " + inputData);
				getTextOrValues.put(firstXpathKey, inputData.trim());
			}

			else if (inputData.equals("ExpenseComment")) {
				SimpleDateFormat tsdf = new SimpleDateFormat("ddMMMyyyyHHmmssz");
				java.util.Date tcurDate = new java.util.Date();
				String tstrDate = tsdf.format(tcurDate);
				String tstrActDate = tstrDate.toString();
				inputData = inputData + "_" + tstrActDate;
				System.out.println(": INPUTDATA FOR Request for Expense Reimbursement Claim Comment : " + inputData);
				getTextOrValues.put(firstXpathKey, inputData.trim());
			}

			else if (inputData.equals("TestSubject")) {
				SimpleDateFormat tsdf = new SimpleDateFormat("ddMMMyyyyHHmmssz");
				java.util.Date tcurDate = new java.util.Date();
				String tstrDate = tsdf.format(tcurDate);
				String tstrActDate = tstrDate.toString();
				inputData = inputData + "_" + tstrActDate;
				System.out.println(": INPUTDATA FOR TestSubject : " + inputData);
				getTextOrValues.put(firstXpathKey, inputData.trim());
			}

		}
		System.out.println(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
		APP_LOGS.debug(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
		highlight = false;
		captureScreenShot = false;
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		try {
			if (GTestName.contains("HLT")) {
				Actions actions = new Actions(driver);
				if (bType.equals("Edge")) {
					System.out.println(": For edge browser");
					returnElementIfPresent(firstXpathKey).click();
					returnElementIfPresent(firstXpathKey).clear();
					for (int i = 0; i < inputData.length(); i++) {
						char eachInvidualCharacter = inputData.charAt(i);
						String indivualLetter = new StringBuilder().append(eachInvidualCharacter).toString();
						actions.sendKeys(indivualLetter);
						actions.build().perform();
						Thread.sleep(50);
					}
					Thread.sleep(100);
					actions.moveByOffset(300, 0).doubleClick();
					actions.build().perform();
					Thread.sleep(1000);
				} else {
					actions.moveToElement(returnElementIfPresent(firstXpathKey));
					Thread.sleep(500);
					actions.doubleClick();
					Thread.sleep(500);
					returnElementIfPresent(firstXpathKey).clear();
					for (int i = 0; i < inputData.length(); i++) {
						char eachInvidualCharacter = inputData.charAt(i);
						String indivualLetter = new StringBuilder().append(eachInvidualCharacter).toString();
						actions.sendKeys(indivualLetter);
						actions.build().perform();
						Thread.sleep(50);
					}
					Thread.sleep(100);
					actions.moveByOffset(300, 0).doubleClick();
					actions.build().perform();
					Thread.sleep(500);
				}
			} else {
				returnElementIfPresent(firstXpathKey).sendKeys(inputData);
			}
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to enter " + inputData + " in " + firstXpathKey + " Field" + e.getMessage();
		}
		return "PASS";
	}

	

	// =====================================================================================================================================

	public boolean isElementPresentBy(By by) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: isElementPresent ()
		 * 
		 * @parameter: By by
		 * 
		 * @notes: Supported method for finding an element.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		try {
			driver.findElement(by);
			return true;
		} catch (Exception e) {
			captureScreenShot = true;
			return false;
		}

	}

	public String Login() {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Login ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Inputs the default login details as mentioned in the
		 * "Config  Details" sheet of the master xlsx and performs click action
		 * on the login button.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		try {
			getConfigDetails();
			System.out.println(": Entering: " + username + " in USERNAME Field");
			APP_LOGS.debug(": Entering: " + username + " in USERNAME Field");
			returnElementIfPresent(GUSER_XPATH).sendKeys(username);
			System.out.println(": PASS");
			APP_LOGS.debug(": PASS");

			System.out.println(": Entering: " + password + " in PASSWORD Field");
			APP_LOGS.debug(": Entering: " + password + " in PASSWORD Field");
			returnElementIfPresent(GPASS_XPATH).sendKeys(password);
			System.out.println(": PASS");
			APP_LOGS.debug(": PASS");

			System.out.println(": Performing Click action on LOGIN");
			APP_LOGS.debug(": Performing Click action on LOGIN");
			returnElementIfPresent(GLOGIN).click();
		} catch (Exception e) {
			captureScreenShot = true;
			APP_LOGS.debug(
					": FAIL - Not able to Loging with " + username + " : Username and " + password + ": Password");
			return ("FAIL - Not able to Loging with " + username + " : Username and " + password + ": Password");
		}
		return "PASS";
	}

	public String MouseHover(String firstXpathKey) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: MouseHoverAndClick ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Hover mouse over given Object, link, Hyperlink, selections or
		 * buttons of a web page.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Performing Mouse hover on " + firstXpathKey);
		APP_LOGS.debug(": Performing Mouse hover on " + firstXpathKey);
		highlight = false;
		captureScreenShot = false;
		try {
			Thread.sleep(2000);
			Actions act = new Actions(driver);
			WebElement root = returnElementIfPresent(firstXpathKey);
			act.moveToElement(root).build().perform();
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to do mouse hover on -- " + firstXpathKey;
		}
		return "PASS";
	}

	public String MouseHoverAndClick(String firstXpathKey, String secondXpathKey) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: MouseHoverAndClick ()
		 * 
		 * @parameter: String firstXpathKey, String secondXpathKey
		 * 
		 * @notes: Performs Click action on link, Hyperlink, selections or
		 * buttons of a web page.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Performing Mouse hover and Click action on " + firstXpathKey);
		APP_LOGS.debug(": Performing Mouse hover and Click action on " + firstXpathKey);
		highlight = false;
		captureScreenShot = false;
		try {
			Thread.sleep(2000);
			Actions act = new Actions(driver);
			WebElement root = returnElementIfPresent(firstXpathKey);
			act.moveToElement(root).build().perform();
			Thread.sleep(1000);
			returnElementIfPresent(secondXpathKey).click();
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to do mouse hover and click on -- " + firstXpathKey;
		}
		return "PASS";
	}

	public String MoveSliderHorizontallyRightSide(String firstXpathKey, String xOffsetValue) {

		System.out.println(": Performing slider movement to right side " + firstXpathKey);
		APP_LOGS.debug(": Performing slider movement to right side " + firstXpathKey);
		highlight = false;
		captureScreenShot = false;
		try {
			Thread.sleep(500);

			Actions builder = new Actions(driver);
			builder.moveToElement(returnElementIfPresent(firstXpathKey)).doubleClick();
			Thread.sleep(1000);

			builder.moveByOffset(Integer.valueOf(xOffsetValue), 0).doubleClick();
			builder.build().perform();

			Thread.sleep(500);
			System.out.println(": Moved slider to right side by x offset value :" + xOffsetValue);
			APP_LOGS.debug(": Moved slider to right side by x offset value :" + xOffsetValue);

		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to move slider horizontally to right side  -- " + firstXpathKey;
		}
		return "PASS";
	}

	public String MoveSliderHorizontallyLeftSide(String firstXpathKey, String yOffsetValue) {

		System.out.println(": Performing slider movement to left side " + firstXpathKey);
		APP_LOGS.debug(": Performing slider movement to left side " + firstXpathKey);
		highlight = false;
		captureScreenShot = false;
		try {
			Thread.sleep(500);

			Actions builder = new Actions(driver);
			builder.moveToElement(returnElementIfPresent(firstXpathKey)).doubleClick();
			System.out.println("Sleep");
			Thread.sleep(1000);

			builder.moveByOffset(Integer.valueOf(yOffsetValue), 0).doubleClick();
			builder.build().perform();

			Thread.sleep(500);
			System.out.println("Moved slider to right side by x offset value :" + yOffsetValue);
			APP_LOGS.debug("Moved slider to right side by x offset value :" + yOffsetValue);
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to move slider horizontally to right side  -- " + firstXpathKey;
		}
		return "PASS";
	}

	public String QuitBrowser() throws ATUTestRecorderException {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: QuitBrowser ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Quits all opened Browsers or Brower instances after the test
		 * case Execution.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		getTextOrValues.clear();
		scriptTableFirstRowData = "";
		System.out.println(": Quiting all opened Browsers");
		APP_LOGS.debug(": Quiting all opened Browsers");
		try {
			driver.close();
			driver = null;
			if (captureVideoRecording.equals("Yes")) {
				recorder.stop();
				System.out.println(": Video Recording Stopped ");
				APP_LOGS.debug(": Video Recording Stopped ");
				Thread.sleep(SYNC_WAIT);
			}

		} catch (Exception e) {
			return "FAIL - Not able to Quit all opened Browsers";
		}
		return "PASS";
	}

	@SuppressWarnings("unused")
	public String SelectDateFromCalendar(String firstXpathKey, String inputData) throws Exception {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Input ()
		 * 
		 * @parameter: String firstXpathKey & String inputData
		 * 
		 * @notes: Inputs the value in any edit box. Value is defined in the
		 * master xlsx file and is assigned to "inputData" local variable. We
		 * cannot perform a data driven testing using the input keyword.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
		APP_LOGS.debug(": Entering: " + '"' + inputData + '"' + " text in " + firstXpathKey + " Field");
		highlight = false;
		captureScreenShot = false;
		try {
			Thread.sleep(1000);
			WebElement table = returnElementIfPresent(firstXpathKey);
			List<WebElement> tableRows = table.findElements(By.tagName("tr"));
			outerloop: for (WebElement row : tableRows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				for (WebElement cell : cells) {
					if (cell.getText().equals(inputData)) {
						cell.click();
						break outerloop;
					}
				}
			}
		}

		catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to enter date " + inputData + " in " + firstXpathKey + " Calendar Field"
					+ e.getMessage();
		}
		return "PASS";
	}

	public String ScrollElementIntoView(String firstXpathKey) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: ScrollElementIntoView ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Scroll page until element is visible on the page where
		 * element is passed in firstXpathKey.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		System.out.println(": Scrolling the page until element visible on the page ");
		APP_LOGS.debug(": Scrolling the page until element visible on the page ");
		try {
			WebElement element = returnElementIfPresent(firstXpathKey);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			Thread.sleep(500);
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,-250)", "");
			Thread.sleep(500);
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not Able to Scrol The Page to END Using END key";
		}
		return "PASS";
	}

	public String ScrollPageToBottom() {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: ScrollPageToBottom ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Scroll The Page to END in terms of what element is passed in
		 * firstXpathKey.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		System.out.println(": Scrolling The Page to END Using END key");
		APP_LOGS.debug(": Scrolling The Page to END Using END key");
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not Able to Scroll The Page to END Using END key";
		}
		return "PASS";
	}

	public String ScrollPageToUp() {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: ScrollPageToUp ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Scroll The Page to UP in terms of what element is passed in
		 * firstXpathKey.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */

		highlight = false;
		captureScreenShot = false;
		System.out.println(": Scrolling The Page UP ");
		APP_LOGS.debug(": Scrolling The Page UP ");
		try {
			((JavascriptExecutor) driver).executeScript("scroll(120, 0)");
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not Able to Scroll The Page Up";
		}
		return "PASS";
	}

	public String ScrollPageToEnd(String firstXpathKey) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: ScrollPageToEnd ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Scroll The Page to END in terms of what element is passed in
		 * firstXpathKey.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Scrolling The Page to END Using END key");
		APP_LOGS.debug(": Scrolling The Page to END Using END key");
		highlight = false;
		captureScreenShot = false;
		try {
			WebElement element = returnElementIfPresent(firstXpathKey);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not Able to Scrol The Page to END Using END key";
		}
		return "PASS";
	}

	public String SelectRadioButton(String firstXpathKey) throws Exception {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: SelectRadioButton ()
		 * 
		 * @parameter: String firstXpathKey
		 * 
		 * @notes: Select Radio Button
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Selecting Radio Button " + firstXpathKey);
		APP_LOGS.debug(": Selecting Radio Button " + firstXpathKey);
		highlight = false;
		captureScreenShot = false;
		try {
			Actions action = new Actions(driver);
			action.moveToElement(returnElementIfPresent(firstXpathKey)).click().build().perform();
			Thread.sleep(2000);
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to select " + firstXpathKey + " Radio button";
		}
		return "PASS";
	}

	// =====================================================================================================================================

	public String switchToDefaultContent() {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: switchToDefaultContent ()
		 * 
		 * @parameter: One
		 * 
		 * @notes: No parameter is needed for this method it will give control
		 * to the main page for switching form iFrame.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */

		System.out.println(": Switch default content from iframe");
		APP_LOGS.debug(": Switch default content from iframe");
		highlight = false;
		captureScreenShot = false;
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not Able to switch default content from iframe";
		}

		return "PASS";
	}

	public String SwitchToNewWindow() {

		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: SwitchToNewWindow ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Switches to new window and move the control of the driver to
		 * the newly opened window.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		try {
			System.out.println(": Switching to New Window");
			APP_LOGS.debug(": Switching to New Window");
			Set<String> set = driver.getWindowHandles();
			Iterator<String> itr = set.iterator();
			parentWindowID = itr.next();
			String ChID = itr.next();

			driver.switchTo().window(ChID);
		} catch (Exception e) {
			captureScreenShot = true;
			System.out.println("Not Able To Perform SwitchToNewWindow");
		}
		return "PASS";

	}

	public String SwitchToParentWindow() {

		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: SwitchToParentWindow ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Switches to parent window and move the control of the driver
		 * main window of the browser.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		try {
			System.out.println(": Switching to Parent Window");
			APP_LOGS.debug(": Switching to Parent Window");
			driver.switchTo().window(parentWindowID);
		} catch (Exception e) {
			captureScreenShot = true;
			System.out.println("Not Able To Perform SwitchToParentWindow");
		}

		return "PASS";

	}

	public String SelectUnselectCheckbox(String firstXpathKey, String checkBoxVal) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: SelectUnselectCheckbox ()
		 * 
		 * @parameter: String firstXpathKey, String checkBoxVal
		 * 
		 * @notes: Select or Unselect the checkbox of a webpage as per the value
		 * of local variable "chechBoxVal" mentioned in the "Test Steps" sheet
		 * in module excel.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Performing Select Unselect action on " + firstXpathKey);
		APP_LOGS.debug(": Setting " + firstXpathKey + " Checkbox Value As " + checkBoxVal);
		highlight = false;
		captureScreenShot = false;
		try {
			if (checkBoxVal.equals("TRUE")) {
				if (returnElementIfPresent(firstXpathKey).isSelected()) {
				} else {
					returnElementIfPresent(firstXpathKey).click();
				}
			} else {
				if (returnElementIfPresent(firstXpathKey).isSelected()) {
					returnElementIfPresent(firstXpathKey).click();
				}
			}
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to Select Unselect Checkbox-- " + firstXpathKey;
		}
		return "PASS";
	}

	public String SelectValueFromDropDown(String firstXpathKey, String inputData) throws Exception {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: SelectValueFromDropDown ()
		 * 
		 * @parameter: String firstXpathKey, String inputData
		 * 
		 * @notes: Selects the "inputData" as mentioned in the module xlsx from
		 * the DropDown in a webpage.firstXpathKey would be location of the
		 * Dropdown on webpage and dataColVal would be visible text of the
		 * dropdown.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Selecting : " + inputData + " from the Dropdown");
		APP_LOGS.debug(": Selecting : " + inputData + " from the Dropdown");
		highlight = false;
		captureScreenShot = false;
		try {
			Select sel = new Select(returnElementIfPresent(firstXpathKey));
			sel.selectByVisibleText(inputData);
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to select " + inputData + " from the Dropdown" + e.getMessage();
		}
		return "PASS";
	}

	public String SelectValueFromDropDownWithAnchorTags(String firstXpathKey, String secondXpathKey) throws Exception {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: SelectValueFromDropDownWithAnchorTags ()
		 * 
		 * @parameter: String firstXpathKey, String inputData
		 * 
		 * @notes: Click the dropdown and click the value from the List(Which
		 * contains anchor tags).
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": Selecting : " + secondXpathKey + " from the Dropdown");
		APP_LOGS.debug(": Selecting : " + secondXpathKey + " from the Dropdown");
		highlight = false;
		captureScreenShot = false;
		try {
			returnElementIfPresent(firstXpathKey).click();
			returnElementIfPresent(secondXpathKey).click();
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to select " + secondXpathKey + " from the Dropdown";
		}
		return "PASS";
	}

	public String TestCaseEnds() {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: TestCaseEnds ()
		 * 
		 * @parameter: None
		 * 
		 * @notes: Performs necessary actions before concluding the testcase
		 * like if testcase has anything fail it will declare by Assert.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		System.out.println(": TestCase is Ending");
		APP_LOGS.debug(": TestCase is Ending");
		getTextOrValues.clear();
		scriptTableFirstRowData = "";
		try {
			if (Fail == true) {
				System.out.println(": TEST SCRIPT:=> " + GTestName + " Has FAILED!!!!!!!!!!!!");
				APP_LOGS.debug(": TEST SCRIPT:=> " + GTestName + " Has FAILED!!!!!!!!!!!!");
				highlight = false;
				Fail = false;
				String failedResult1 = failedResult;
				failedResult = "";
				if (captureVideoRecording.equals("Yes")) {
					recorder.stop();
					System.out.println(": Video Recording Stopped As test case completed");
					APP_LOGS.debug(": Video Recording Stopped As test case completed");
					Thread.sleep(SYNC_WAIT);
				}
				Assert.assertTrue(false, failedResult1);
			} else {
				System.out.println(": TEST SCRIPT:=> " + GTestName + " Has PASSED************");
				APP_LOGS.debug(": TEST SCRIPT:=> " + GTestName + " Has PASSED************");
				Fail = true;
				String failedResult1 = failedResult;
				failedResult = "";
				if (captureVideoRecording.equals("Yes")) {
					recorder.stop();
					System.out.println(": Video Recording Stopped As test case completed");
					APP_LOGS.debug(": Video Recording Stopped As test case completed");
					Thread.sleep(SYNC_WAIT);
				}
				Assert.assertTrue(true, failedResult1);
				Fail = false;
			}
		} catch (Exception e) {
			return "FAIL - Not able to end TC";
		}
		return "PASS";
	}

	

	// =====================================================================================================================================

	public String VerifyElementPresent(String firstXpathKey, String expTEXT) throws ParseException {

		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: VerifyElementPresent ()
		 * 
		 * @parameter: String firstXpathKey, String expText
		 * 
		 * @notes: Performs the verification of the table data by getting column
		 * data from firstXpathKey and secondXpathKey and verify it against the
		 * expText or dataColVal.User can perform negative testing by passing
		 * boolean value in dataColVal.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */

		System.out.println(": Verifying " + firstXpathKey + " Element is Present on the page");
		APP_LOGS.debug(": Verifying " + firstXpathKey + " Element is Present on the page");
		highlight = false;
		captureScreenShot = false;
		String sElementText = null;

		try {
			String sFlag = "";
			if (isElementPresent(firstXpathKey)) {
				sFlag = "TRUE";
				sElementText = returnElementIfPresent(firstXpathKey).getText();
				if (expTEXT.equals(sFlag)) {
					System.out.println(
							": " + firstXpathKey + " Element is Present on the page and its Value is: " + sElementText);
					APP_LOGS.debug(
							": " + firstXpathKey + " Element is Present on the page and its Value is: " + sElementText);
				}
			} else {
				System.out.println(": " + firstXpathKey + " Element is NOT Present on Page");
				APP_LOGS.debug(": " + firstXpathKey + " Element is NOT Present on Page");
				captureScreenShot = true;
				return "FAIL -  " + firstXpathKey + " Element is NOT Present on the page";
			}
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not Able to verify " + firstXpathKey + " Element is Present on Page or Not--";
		}
		return "PASS";
	}

	@SuppressWarnings("unused")
	public String VerifyRowData(String firstXpathKey, String SecondXpathKey, String inputData) {
		highlight = false;
		captureScreenShot = false;
		String actRowData = "";
		try {
			String[] testData = inputData.split(",");
			String expFromDate = testData[0];
			System.out.println(expFromDate);

			String expToDate = testData[1];
			System.out.println(expToDate);

			String expNoOfLeaves = testData[2];
			System.out.println(expNoOfLeaves);

			String expLeaveType = testData[3];
			System.out.println(expLeaveType);

			WebElement table = returnElementIfPresent(firstXpathKey);
			List<WebElement> rows = table.findElements(By.tagName("tr"));
			String expNarrationText = (String) getTextOrValues.get(SecondXpathKey);
			for (WebElement row : rows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				for (WebElement cell : cells) {
					if (cell.getText().contains(expNarrationText)) {
						System.out.println(": Verifing Narration Text, Actual is-> " + cell.getText()
								+ " AND Expected is-> " + expNarrationText);
						APP_LOGS.debug(": Verifing Narration Text, Actual is-> " + cell.getText() + " AND Expected is->"
								+ expNarrationText);
						actRowData = row.getText();
						break;
					}
				}
			}

			if (actRowData.contains(expFromDate)) {
				System.out.println(": Verifing From Date: " + expFromDate + " is present in applied leave row data=> "
						+ actRowData);
				APP_LOGS.debug(": Verifing From Date: " + expFromDate + " is present in applied leave row data=> "
						+ actRowData);
			}
			if (actRowData.contains(expToDate)) {
				System.out.println(
						": Verifing To Date: " + expToDate + " is present in applied leave row data=> " + actRowData);
				APP_LOGS.debug(
						": Verifing To Date: " + expToDate + " is present in applied leave row data=> " + actRowData);
			}
			if (actRowData.contains(expNoOfLeaves)) {
				System.out.println(": Verifing No. of Leaves: " + expNoOfLeaves
						+ " is present in applied leave row data=> " + actRowData);
				APP_LOGS.debug(": Verifing No. of Leaves: " + expNoOfLeaves + " is present in applied leave row data=> "
						+ actRowData);
			}
			if (actRowData.contains(expLeaveType)) {
				System.out.println(": Verifing Leave Type: " + expLeaveType + " is present in applied leave row data=> "
						+ actRowData);
				APP_LOGS.debug(": Verifing Leave Type: " + expLeaveType + " is present in applied leave row data=> "
						+ actRowData);
			}
		} catch (Exception e) {
			System.out.println("Verification of row data failed" + e.getMessage());
			return "FAIL - Verification of row data failed" + e.getMessage();
		}
		return "PASS";
	}

	public String VerifyPermalinkContent(String expPermalinkContent) {

		highlight = false;
		captureScreenShot = false;
		try {
			String actURL = driver.getCurrentUrl();
			System.out.println(": Verifing Page URL:");
			APP_LOGS.debug(": Verifing Page URL:");

			String[] actURLContent = actURL.split("/");
			actPermalinkContent = actURLContent[actURLContent.length - 1];

			if (expPermalinkContent.equalsIgnoreCase(actPermalinkContent)) {

				System.out.println(": Actual Permalink value is-> " + actPermalinkContent + " AND Expected is-> "
						+ expPermalinkContent);
				APP_LOGS.debug(": Actual Permalink value is-> " + actPermalinkContent + " AND Expected is->"
						+ expPermalinkContent);

			} else {
				captureScreenShot = true;
				System.out.println("FAIL - Not Able to verify permalink content " + actPermalinkContent + "");
				APP_LOGS.debug("FAIL - Not Able to verify permalink content " + actPermalinkContent + "");
				return "FAIL - Actual permalink content is-> " + actPermalinkContent + " AND Expected is->"
						+ expPermalinkContent;
			}
		} catch (Exception e) {
			captureScreenShot = true;
			System.out.println("FAIL - Not Able to verify actual permalink content " + actPermalinkContent + "");
			APP_LOGS.debug("FAIL - Not Able to verify actual permalink content " + actPermalinkContent + "");

		}
		return "PASS";
	}

	@SuppressWarnings("unchecked")
	public String VerifyText(String firstXpathKey, String secondXpathKey, String expText)
			throws ParseException, InterruptedException {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: VerifyText ()
		 * 
		 * @parameter: String firstXpathKey, Optional=>String secondXpathKey,
		 * Optional=> String expText
		 * 
		 * @notes: Verifies the Actual Text as compared to the Expected Text.
		 * Verification can be performed on the same page or on different pages.
		 * User can perform two different webelement's text comparison by
		 * passing argument as objectKeySecond.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		System.out.println(": Verifying " + firstXpathKey + " Text on the Page");
		APP_LOGS.debug(": Verifying " + firstXpathKey + " Text on the Page");
		Thread.sleep(SYNC_WAIT);
		if (expText.equals("Transfer v8.4 Linux Standard")) {
			expText = "Transfer " + "[" + "v8.4" + "]" + " Linux Standard";
		} else {
			String regex = "[0-9].[0-9]";
			if (expText.matches(regex)) {
				NumberFormat nf = NumberFormat.getInstance();
				Number number = nf.parse(expText);
				long lnputValue = number.longValue();
				expText = String.valueOf(lnputValue);
				System.out.println(expText);
			}
		}

		if (expText.isEmpty()) {
			System.out.println(": Expected Data is Empty, taking this value from Hashmap");
			APP_LOGS.debug(": Expected Data is Empty, taking this value from Hashmap");
			expText = (String) getTextOrValues.get(secondXpathKey);
			// System.out.println(": expText "+expText);
			// APP_LOGS.debug(": expText "+expText);
			if (expText == null) {
				System.out.println(
						": No Expected Data present in Hashmap, taking this value from secondXpathKey object of Webpage");
				APP_LOGS.debug(
						": No Expected data present in Hashmap, taking this value from secondXpathKey object of Webpage");
				expText = returnElementIfPresent(secondXpathKey).getText().trim();
			}
		}
		try {
			actText = returnElementIfPresent(firstXpathKey).getText().trim();
			expText = expText.trim();

			if (actText.compareTo(expText) == 0) {
				System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
				APP_LOGS.debug(": Actual is-> " + actText + " AND Expected is->" + expText);
			} else {
				globalExpText = expText;
				highlight = true;
				captureScreenShot = true;
				System.out.println(": Actual is-> " + actText + " AND Expected is-> " + expText);
				return "FAIL - Actual is-> " + actText + " AND Expected is->" + expText;
			}

		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to read text-- " + firstXpathKey + "Exception " + e.getMessage();
		}
		return "PASS";
	}

	public String VerifyTitle(String actTitle, String expTitle) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: VerifyTitle ()
		 * 
		 * @parameter: String actTitle & String expTitle
		 * 
		 * @notes: Verifies the Actual Web Page Title as compared to the
		 * Expected Web Page title. Verification is performed on the same Web
		 * page.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */

		highlight = false;
		captureScreenShot = false;
		System.out.println(": Verifying Page Title");
		APP_LOGS.debug(": Verifying Page Title");
		try {
			expTitle = expTitle.replace("_", ",");
			actTitle = driver.getTitle();
			if (actTitle.compareTo(expTitle) == 0) {
				System.out.println(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
				APP_LOGS.debug(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
			} else {
				captureScreenShot = true;
				System.out.println(": Actual is-> " + actTitle + " AND Expected is->" + expTitle);
				return "FAIL - Actual is-> " + actTitle + " AND Expected is->" + expTitle;
			}
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to get title";
		}
		return "PASS";
	}

	public String VerifyUrl(String actUrl, String expUrl) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: VerifyUrl ()
		 * 
		 * @parameter: String actUrl, String expUrl
		 * 
		 * @notes: Verifies the Actual Web Page URL as compared to the Expected
		 * Web Page URL. Verification is performed on the same Web page.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		System.out.println(": Verifying Current URL");
		APP_LOGS.debug(": Verifying Current URL");
		try {
			actUrl = driver.getCurrentUrl();
			if (actUrl.compareTo(expUrl) == 0) {
				System.out.println(": Actual is-> " + actUrl + " AND Expected is->" + expUrl);
				APP_LOGS.debug(": Actual is-> " + actUrl + " AND Expected is->" + expUrl);
			} else {
				captureScreenShot = true;
				System.out.println(": Actual is-> " + actUrl + " AND Expected is->" + expUrl);
				return "FAIL - Actual is-> " + actUrl + " AND Expected is->" + expUrl;
			}
		} catch (Exception e) {
			captureScreenShot = true;
			return "FAIL - Not able to get URL";
		}
		return "PASS";
	}

	public String Wait(String stepWaitTime) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Wait ()
		 * 
		 * @parameter: String WaitTime
		 * 
		 * @notes: Wait for a user defined specific time to load the page for
		 * ex: 20 seconds. String "WaitTime" captures the value from the module
		 * xlsx file.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		try {

			System.out.println(": Waiting for Page to load.");
			APP_LOGS.debug(": Waiting for Page to load.");
			stepWaitTime = stepWaitTime.trim();
			if (stepWaitTime.equals("SYNC_WAIT") || stepWaitTime.equals("SMALL_WAIT") || stepWaitTime.equals("MID_WAIT")
					|| stepWaitTime.equals("LONG_WAIT")) {
				if (stepWaitTime.equals("SYNC_WAIT")) {
					Thread.sleep(Constants.SYNC_WAIT);
				} else if (stepWaitTime.equals("SMALL_WAIT")) {
					Thread.sleep(Constants.SMALL_WAIT);
				} else if (stepWaitTime.equals("MID_WAIT")) {
					Thread.sleep(Constants.MID_WAIT);
				} else if (stepWaitTime.equals("LONG_WAIT")) {
					Thread.sleep(Constants.LONG_WAIT);
				}
			} else {
				APP_LOGS.debug(
						": FAIL - Please check the Wait data in Test Case sheet. It can be SYNC_WAIT,SMALL_WAIT,MID_WAIT or LONG_WAIT BUT written as: "
								+ stepWaitTime);
				return (": FAIL - Please check the Wait data in Test Case sheet. It can be SYNC_WAIT,SMALL_WAIT,MID_WAIT or LONG_WAIT BUT written as: "
						+ stepWaitTime);
			}

		} catch (Exception e) {
			APP_LOGS.debug(": FAIL - Not able to wait for " + stepWaitTime + " Seconds to load the page");
			return ("FAIL - Not able to wait for " + stepWaitTime + " Seconds to load the page");
		}
		return "PASS";
	}

	public String WaitTillElementAppears(String ObjectIdentifier) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Wait ()
		 * 
		 * @parameter: String WaitTime
		 * 
		 * @notes: Wait for a user defined specific time to load the page for
		 * ex: 20 seconds. String "WaitTime" captures the value from the module
		 * xlsx file.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		try {
			highlight = false;
			captureScreenShot = false;
			int i = 0;
			int expWaitTime = 20;
			String objectIdentifierValue = "";
			String objectArray[] = null;
			String object = OR.getProperty(ObjectIdentifier);
			objectArray = object.split("__");
			objectIdentifierValue = objectArray[1].trim();
			System.out.println(": Waiting for Max " + expWaitTime + " seconds to Appear " + ObjectIdentifier
					+ " Element which may NOT Present on Page");
			APP_LOGS.debug(": Waiting for Max " + expWaitTime + " seconds to Disappear " + ObjectIdentifier
					+ " Element which may NOT Present on Page");
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			while (isElementPresentBy(By.xpath(objectIdentifierValue)) == false) {
				if (i <= expWaitTime) {
					System.out.println(": " + ObjectIdentifier
							+ " Element is currently NOT Present on Page. Going to check again after 1 second.");
					APP_LOGS.debug(": " + ObjectIdentifier
							+ " Element is currently NOT Present on Page. Going to check again after 1 second.");
					Thread.sleep(1000);
					i++;
				} else {
					System.out.println(": Element not loaded in " + expWaitTime
							+ " Seconds. So stopping this test script execution. Please see the screenshot for more details.");
					APP_LOGS.debug(": Element not loaded in " + expWaitTime
							+ " Seconds. So stopping this test script execution. Please see the screenshot for more details.");
					captureScreenShot = true;
					return ("FAIL - Element not loaded in " + expWaitTime
							+ " Seconds. So stopping this test script execution. Please see the screenshot for more details.");
				}
			}
			System.out.println(": " + ObjectIdentifier + " Element is Now Present on Page, Moving Ahead.");
			APP_LOGS.debug(": " + ObjectIdentifier + " Element is Now Present on Page, Moving Ahead");
			driver.manage().timeouts().implicitlyWait(CONFIG_IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
		} catch (Exception e) {
			captureScreenShot = true;
			APP_LOGS.debug(": FAIL - Not able to wait Till " + ObjectIdentifier
					+ " Element Appears on Page. Please see the screenshot for more details.");
			return ("FAIL - Not able to wait Till " + ObjectIdentifier
					+ " Element Appears on Page. Please see the screenshot for more details.");
		}
		return "PASS";
	}

	public String WaitWhileElementPresent() {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: Wait ()
		 * 
		 * @parameter: String WaitTime
		 * 
		 * @notes: Wait for a user defined specific time to load the page for
		 * ex: 20 seconds. String "WaitTime" captures the value from the module
		 * xlsx file.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		try {
			highlight = false;
			captureScreenShot = false;
			int i = 0;
			System.out.println(": Waiting for Page to load.");
			APP_LOGS.debug(": Waiting for Page to load.");
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			while (isElementPresentBy(By.xpath("//loader-component/div/div/div/div/span")) == true) {
				if (i <= 60) {
					System.out.println(": Element present. Checking again after 1 Second");
					Thread.sleep(1000);
					i++;
				} else {
					System.out.println(": Page not loaded in " + i
							+ " Seconds. So stopping this test script execution. Please see the screenshot for more details.");
					APP_LOGS.debug(": Page not loaded in " + i
							+ " Seconds. So stopping this test script execution. Please see the screenshot for more details.");
					captureScreenShot = true;
					return ("FAIL - Page not loaded in expected time. Please see the screenshot for more details.");
				}
			}
			Thread.sleep(3000); // This is becasue Edit panle elements are not
								// loading properly in give spitulated time
			// System.out.println(": " +ObjectIdentifier+" Element is Now NOT
			// Present on Page, Moving Ahead.");
			// APP_LOGS.debug(": " +ObjectIdentifier+" Element is Now NOT
			// Present on Page, Moving Ahead");
			driver.manage().timeouts().implicitlyWait(CONFIG_IMPLICIT_WAIT_TIME, TimeUnit.SECONDS);
			System.out.println(": CONFIG_IMPLICIT_WAIT_TIME: " + CONFIG_IMPLICIT_WAIT_TIME);
		} catch (Exception e) {
			captureScreenShot = true;
			APP_LOGS.debug(": FAIL - Not able to wait for Seconds to load the page");
			return ("FAIL - Not able to wait for Seconds to load the page");
		}
		return "PASS";
	}

	@SuppressWarnings("unused")
	public String VerifyAppliedTicketsfromTable(String firstXpathKey, String SecondXpathKey) {
		highlight = false;
		captureScreenShot = false;
		String actRowData = "";
		System.out.println(": Verifying if applied ticket is present in the Submitted tickets table");
		APP_LOGS.debug(": Verifying if applied ticket is present in the Submitted tickets table");
		try {
			WebElement table = returnElementIfPresent(firstXpathKey);
			List<WebElement> rows = table.findElements(By.tagName("tr"));
			String expTicketComment = (String) getTextOrValues.get(SecondXpathKey);
			for (WebElement row : rows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				for (WebElement cell : cells) {
					if (cell.getText().contains(expTicketComment)) {
						System.out.println(": Actual is-> " + cell.getText() + " AND Expected is->" + expTicketComment);
						APP_LOGS.debug(": Actual is-> " + cell.getText() + " AND Expected is->" + expTicketComment);
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Verification of applied ticket failed" + e.getMessage());
			return "FAIL - Verification of applied ticket failed" + e.getMessage();
		}
		return "PASS";
	}

	@SuppressWarnings("unused")
	public String VerifyAppliedClaimsfromTable(String firstXpathKey, String SecondXpathKey) {
		highlight = false;
		captureScreenShot = false;
		String actRowData = "";
		System.out.println(": Verifying if applied claim is present in the Submitted Claims table");
		APP_LOGS.debug(": Verifying if applied claim is present in the Submitted Claims table");
		try {
			WebElement table = returnElementIfPresent(firstXpathKey);
			List<WebElement> rows = table.findElements(By.tagName("tr"));
			String expClaimComment = (String) getTextOrValues.get(SecondXpathKey);
			for (WebElement row : rows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				for (WebElement cell : cells) {
					if (cell.getText().contains(expClaimComment)) {
						System.out.println(": Actual is-> " + cell.getText() + " AND Expected is->" + expClaimComment);
						APP_LOGS.debug(": Actual is-> " + cell.getText() + " AND Expected is->" + expClaimComment);
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Verification of applied claims failed" + e.getMessage());
			return "FAIL - Verification of applied Claims failed" + e.getMessage();
		}
		return "PASS";
	}

public String VerifyFileIsExportedAndSizeIsNotZero(String dataColValue) throws Exception {
/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: VerifyFileDownload (dataColValue)
		 * 
		 * @parameter: None
		 * 
		 * @notes: Verifies file mentions in parameter.
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */

		String tempFileName = "";
		String tempFileNameWithPath = "";
		String tempFileDownloadDirectory = "";
		try {
			Thread.sleep(5000);
			String user=System.getProperty("user.name");
			String downloadFolderPath= "C:\\Users\\"+user+"\\Downloads\\";
			// Add the code for deleting file at the downloads location.
			tempFileName = dataColValue;
			System.out.println(": Verifying '" + tempFileName + "' Exported File is Downloaded properly.");
			APP_LOGS.debug(": Verifying '" + tempFileName + "' Exported File is Downloaded properly.");
			tempFileDownloadDirectory = downloadFolderPath;
			tempFileNameWithPath = tempFileDownloadDirectory + tempFileName;
			tempFileNameWithPath.replaceAll("\\\\", "/");
			File file = new File(tempFileNameWithPath);
			double bytes = file.length();
			double kilobytes = (bytes / 1024);
			String str = String.format("%1.2f", kilobytes);
			kilobytes = Double.valueOf(str);
			if (file.exists() && bytes != 0) {
				System.out.println(": " + tempFileName + " file is Exported successfully at:-> '"
						+ tempFileDownloadDirectory + "' Directory and its Size is " + kilobytes + "KB");
				APP_LOGS.debug(": " + tempFileName + " file is Exported successfully at:-> '"
						+ tempFileDownloadDirectory + "' Directoryand and its Size is " + kilobytes + "KB");
			} else {
				System.out.println(": " + tempFileName + " file is NOT Exported as it is ont present at:-> '"
						+ tempFileDownloadDirectory + "' Directory.");
				APP_LOGS.debug(": " + tempFileName + " file is NOT Exported as it is not present at:-> '"
						+ tempFileDownloadDirectory + "' Directory.");
				return "FAIL - " + tempFileName + " file is NOT Exported as it is not present at:-> '"
						+ tempFileDownloadDirectory + "' Directory.";
			}

		} catch (Exception exception) {
			System.out.println("Error in saving a file: " + exception.getMessage());
			return "FAIL - " + tempFileName + " file is NOT Exported at:-> '" + tempFileDownloadDirectory
					+ "' Directory and got following error message=> " + exception.getMessage();
		}

		return "PASS";
	}

	public String DeleteFilesFromFolder(String filePath) {
		/*
		 * @HELP
		 * 
		 * @class: Keywords
		 * 
		 * @method: deleteFilesFromFolder (filePath)
		 * 
		 * @parameter: String filePath
		 * 
		 * @notes: Verifying able to delete files from mentioned folder
		 * 
		 * @returns: ("PASS" or "FAIL" with Exception in case if method not got
		 * executed because of some runtime exception) to executeKeywords method
		 * 
		 * @END
		 */
		highlight = false;
		captureScreenShot = false;
		String directoryName = "C:\\Users\\tanveer.patel\\Downloads";
		try {
			File directory = new File(directoryName);

			if (directory.isDirectory()) {

				for (int i = 0; i < directory.list().length; i++) {
					File file = new File(directory + "\\" + directory.list()[i]);
					file.delete();
				}

			} else {
				System.out.println("Parent Directory has not anything.");
			}
			System.out.println(": Successfully deleted directory : " + directoryName);
			APP_LOGS.debug(": Successfully deleted directory : " + directoryName);
		} catch (Exception ex) {
			System.out.println("Error in deleting contents of the directory : " + directoryName + " with exception "
					+ ex.getMessage());
			return "FAIL - Error in deleting contents of the directory : " + directoryName;
		}
		return "PASS";
	}
}