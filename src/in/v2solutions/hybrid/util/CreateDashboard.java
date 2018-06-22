package in.v2solutions.hybrid.util;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CreateDashboard extends CopyReportsAndZip{
	/* @HELP
	@class:				CreateDashboard extends CopyReportsAndZip (Multi Level of Inheritance)
	@methods:			fileNamesWithPath(), readXmlFiles(), dataTestReport(), masterDashboardData(), creatingDashbaordHtml(), creatingDashboardDatabase()
	@parameter:		Different parameters are passed as per the method declaration
	@notes:				Makes a local dashboard data for test results and stores the same in .html format in "Dashboard' Folder and stores the test results in Database (Central Repository)
	@returns:			All respective methods have there return types
	@END
	 */	

	public static String[] fileNamesWithPath(String directoryPath) {
		/* @HELP
		@class:				CreateDashboard	
		@method:			fileNamesWithPath()
		@parameter:		String directoryPath
		@notes:				Gets the file path of the directory passed as the parameter to this method in an array list filesPath.
		@returns:			No return type
		@END
		 */
		File dir = new File(directoryPath);
		if (dir.isDirectory()) {
			listFiles = dir.listFiles();
			for (File file : listFiles) {
				if (file.isFile()) {
					filesPath.add(file.getAbsolutePath());
					//System.out.println("In fileNamesWithPath Method and path is : --- "+file.getAbsolutePath());
				}
			}
		}
		return filesPath.toArray(new String[] {});
	}

	public static String[] fileNames(String directoryPath) {
		/* @HELP
		@class:				CreateDashboard	
		@method:			fileNamesWithPath()
		@parameter:		String directoryPath
		@notes:				Gets the file names of the directory passed as the parameter to this method in an array list fileNames.
		@returns:			Returns the filenames
		@END	
		 */
		File dir = new File(directoryPath);

		if (dir.isDirectory()) {
			listFileNames = dir.listFiles();
			for (File file : listFileNames) {
				if (file.isFile()) {
					fileNames.add(file.getName());
					//System.out.println("In fileNames Methods and File Name is : --- "+file.getName());
					}
			}
		}
		return fileNames.toArray(new String[] {});
	}

	public static void readXmlFiles() throws ParserConfigurationException, SAXException, IOException{
		/* @HELP
		@class:				CreateDashboard	
		@method:			fileNamesWithPath()
		@parameter:		No Parameters
		@notes:			
					1.Gets the list of ".xml" files from junitreports folder inside testng-output folder and reads the node <tests>, <failures> & <errors>
					2. Calculates the Total Number of Test Cases, Total Test Cases Skipped & Total Test Cases failed.
		@returns:			No return type
		@END
		 */
		for (int i = 0; i < listFiles.length; i++) {
		// IMP===> [No of Total Tests: ] [No. of Tests Skipped : ] [No. of Tests Failed :] // [tests] [failures] [errors]
			//System.out.println("In readXmlFiles Methods and files are : --- "+listFiles[i].getName());
			TestSummary = new ArrayList<String>();
			File fXmlFile = new File(CreateDashboard.listFiles[i].getAbsolutePath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("testsuite");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					TestSummary.add(eElement.getAttribute("tests"));
					TestSummary.add(eElement.getAttribute("skipped"));
					TestSummary.add(eElement.getAttribute("failures"));
				}
			}
			TOTAL_TC = TOTAL_TC + Integer.parseInt(TestSummary.get(0));
			TC_SKIP = TC_SKIP + Integer.parseInt(TestSummary.get(1));
			TC_FAIL = TC_FAIL + Integer.parseInt(TestSummary.get(2));
			//System.out.println("-Test Summary : "+TestSummary);
			TestSummary.clear();
		}
	}
	
	public static void dataTestReport(){
		/* @HELP
		@class:				CreateDashboard	
		@method:			dataTestReport()
		@parameter:		No Parameters
		@notes:			
					1. Calculates the Total Number of Test Cases Executed, Total Test Cases Passed, % Automation Coverage, % Pass & % Fail
		@returns:		No return type
		@END
		 */
		
		

		System.out.println("TOTAL_TC: "+TOTAL_TC);
		System.out.println("TC_SKIP: "+TC_SKIP);
		System.out.println("TC_FAIL: "+TC_FAIL);
		
		TC_EXECUTED = TOTAL_TC - TC_SKIP;
		System.out.println("TC_EXECUTED: "+TC_EXECUTED);
		TC_PASS = TC_EXECUTED - TC_FAIL;
		System.out.println("TC_PASS: "+TC_PASS);
		AUTO_COVERAGE = (TC_EXECUTED / TOTAL_TC) * 100;
		System.out.println("AUTO_COVERAGE: "+AUTO_COVERAGE);
		PERCENT_PASS = ((TC_PASS / TC_EXECUTED) * 100);
		System.out.println("PERCENT_PASS: "+PERCENT_PASS);
		PERCENT_FAIL = (TC_FAIL / TC_EXECUTED) * 100;
		System.out.println("PERCENT_FAIL: "+PERCENT_FAIL);
		PERCENT_SKIP = (TC_SKIP / TOTAL_TC) * 100;
		System.out.println("PERCENT_SKIP: "+PERCENT_SKIP);
	}
		
	@SuppressWarnings("unchecked")
	public static void masterDashboardData() throws IOException{
		/* @HELP
		@class:				CreateDashboard	
		@method:			masterDashboardData()
		@parameter:		No Parameters
		@notes:			
					1. Stores all the Test Execution Results in Dashboard Array List for creating a local Dasboard with following parameters as follows,
						 a. SuiteType
						 b. SuiterunTimeStamp
						 c. Environment
						 d. Browser
						 e. Operating System Type
						 f. Automation Coverage
						 g. Percentage Pass
						 h. Percentage Fail
						 i. Total Test Cases
						 j. Test Cases Executed
						 k. Test Cases Passed
						 l. Test Cases Failed
						 m. Test Cases Skipped
						 n. Links
		@returns:			No return type
		@END
		 */
		String env = SUTServer;
		String ostype= osType;
		String tstype= tBedType;
		String browser;
		
		if (bType.equalsIgnoreCase("API")){browser="API";}
		else {browser=bTypeVersion;}
		System.out.println("BROWSER/EMULATOR: "+browser);
		String Links = "<a href='file:///"+Latestresultsfolder+"'>Result Folder</a>/<a href='file:///"+Latestresultsfolder+"/index.html'>XSLT Report</a>";
		DecimalFormat autoCoverage = new DecimalFormat("0.0");
		Double A_COVERAGE = Double.parseDouble(autoCoverage.format(AUTO_COVERAGE));
		System.out.println("A_COVERAGE :  "+A_COVERAGE);
		DecimalFormat perPass = new DecimalFormat("0.0");
		Double PER_PASS = Double.parseDouble(perPass.format(PERCENT_PASS));
		System.out.println("PER_PASS :  "+PER_PASS);
		DecimalFormat perFail = new DecimalFormat("0.0");
		Double PER_FAIL = Double.parseDouble(perFail.format(PERCENT_FAIL));
		System.out.println("PER_FAIL :  "+PER_FAIL);
		DecimalFormat perSkip = new DecimalFormat("0.0");
		Double PER_SKIP = Double.parseDouble(perSkip.format(PERCENT_SKIP));
		System.out.println("PERCENT_SKIP : "+PERCENT_SKIP);
		
		Dashboard.add(0, suitetype);
		Dashboard.add(1, SuiteRunTimeStamp);
		Dashboard.add(2, env);
		Dashboard.add(3, tstype);
		Dashboard.add(4, ostype);
		Dashboard.add(5, browser);
		Dashboard.add(6, A_COVERAGE);
		Dashboard.add(7, PER_PASS);
		Dashboard.add(8, PER_FAIL);
		Dashboard.add(9, PER_SKIP);
		Dashboard.add(10, Math.round(TOTAL_TC));
		Dashboard.add(11, Math.round(TC_EXECUTED));
		Dashboard.add(12, Math.round(TC_PASS));
		Dashboard.add(13, Math.round(TC_FAIL));
		Dashboard.add(14, Math.round(TC_SKIP));
		//System.out.println("Links :=> "+Links.replaceAll("\\\\", "/"));
		Dashboard.add(15, Links.replaceAll("\\\\", "/"));
		}

	public static void creatingDashbaordHtml() throws IOException{
		/* @HELP
		@class:				CreateDashboard	
		@method:			creatingDashbaordHtml()
		@parameter:		No Parameters
		@notes:				Creates a ".html" file with data stored in Dashboard array list 
		@returns:			No return type
		@END
		 */
		String DashboardFilePath = dashboardPath+"DASHBOARD-"+suiteName+".html";
		String DashboardData = "<tr>"
				+"<td>"+ Dashboard.get(0)+"</td>"
				+"<td>"+ Dashboard.get(1)+"</td>"
				+"<td>"+ Dashboard.get(2)+"</td>"
				+"<td>"+ Dashboard.get(3)+"</td>"
				+"<td>"+ Dashboard.get(4)+"</td>"
				+"<td>"+ Dashboard.get(5)+"</td>"				
				+"<td>"+ Dashboard.get(6)+"</td>"
				+"<td>"+ Dashboard.get(7)+"</td>"
				+"<td>"+ Dashboard.get(8)+"</td>"
				+"<td>"+ Dashboard.get(9)+"</td>"
				+"<td>"+ Dashboard.get(10)+"</td>"
				+"<td>"+ Dashboard.get(11)+"</td>"
				+"<td>"+ Dashboard.get(12)+"</td>"
				+"<td>"+ Dashboard.get(13)+"</td>"
				+"<td>"+ Dashboard.get(14)+"</td>"
				+"<td>"+ Dashboard.get(15)+"</td>"
				+ "</tr>";
		
		String DashboadFileName = "DASHBOARD-"+suiteName+".html";
		// If the Dashboard File Already Exists
		if (!fileNames.isEmpty()){   				 // this if block will continue if there is more than one file in desh board folder and it contains deshboard file name. it will simply add the deshboard data to <th>Execution Result</th>
			//System.out.println("filenames: "+fileNames);
			if(fileNames.contains(DashboadFileName)){
				try    {
	             File file = new File(DashboardFilePath);
	             BufferedReader reader = new BufferedReader(new FileReader(file));
	              String line = "", oldtext = "";
	             while((line = reader.readLine()) != null)
	                 {
	                 oldtext += line;
	             }
	             reader.close();	                  
	             String newtext = oldtext.replaceFirst("<th>Execution Result</th></tr></thead><tbody>","<th>Execution Result</th></tr></thead><tbody>"+DashboardData);
	             FileWriter writer = new FileWriter(DashboardFilePath);
	            writer.write(newtext);writer.close();
	         }
	         catch (IOException ioe) {
	             ioe.printStackTrace();
	         }
		}else { // this else block is for if there is more than one file in folder and it does not contains deshboard name. it will create new file from html and will append data.
			BufferedWriter bw = new BufferedWriter(new FileWriter(DashboardFilePath));
			bw.write("<!DOCTYPE html><html><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'><meta name='viewport' content='width=device-width, initial-scale=1'><link rel='stylesheet' type='text/css' href='../../sysfiles/DashBoardSysFiles/css/style.css'><title>Index</title><script type='text/javascript' src='../../sysfiles/DashBoardSysFiles/js/jquery-1.10.2.js'></script><script type='text/javascript' src='../../sysfiles/DashBoardSysFiles/js/DashboardChart.js'></script></head>"
					+"<body><header><div class='title'>SUITE EXECUTION DASHBOARD</div><div class='logo'><img src='../../sysfiles/DashBoardSysFiles/images/v-2-solutions-logo-standard.png'/></div><div class='clearBoth'></div></header>"
					+"<div class='main'><ul class='description'><li><span>Project Name : </span><span>"+projectName+"</span></li><li><span>SUT Build Version : </span><span>"+SUTBuildVersion+"</span></li><li><span>Module : </span><span>"+module+"</span></li><li>"
					+"<span>Suit Type : </span><span>"+suitetype+"</span></li></ul><div class='graphContainer'><div class='commonContainer'><p>Execution Result Overview</p><div id='piechart' style='height: 450px;'></div></div>"
					+"<div class='commonContainer'><p>Result As Per Browser/Iteration</p><div id='chart_div' style='height: 400px;'></div></div><div class='clearBoth'></div></div>"
					+"<div class='reportContainer'><table class='executionTable'><thead><tr><th>Suit type</th><th>Execution Time</th><th>SUT Server</th><th>Testbed</th><th>OS</th><th>Browser/API</th><th>% of Automation Coverage</th><th>%TCs Passed</th><th>%TCs Failed</th><th>%TCs Skipped</th><th>Total TCs</th><th>TCs Executed</th><th>TCs Pass</th><th>TCs Failed</th><th>TCs Skipped</th><th>Execution Result</th></tr></thead><tbody>"
					+DashboardData+
					"</tbody></table></div></div><div class='footer'></div></body></html>");
			bw.close();
				System.out.println(Dashboard);
				}
		}
		// Create a New Dashboard (this block is for complete new file and folder have no file.)
		else {  
			BufferedWriter bw = new BufferedWriter(new FileWriter(DashboardFilePath));
			bw.write("<!DOCTYPE html><html><head><meta charset='utf-8'><meta http-equiv='X-UA-Compatible' content='IE=edge'><meta name='viewport' content='width=device-width, initial-scale=1'><link rel='stylesheet' type='text/css' href='../../sysfiles/DashBoardSysFiles/css/style.css'><title>Index</title><script type='text/javascript' src='../../sysfiles/DashBoardSysFiles/js/jquery-1.10.2.js'></script><script type='text/javascript' src='../../sysfiles/DashBoardSysFiles/js/DashboardChart.js'></script></head>"
					+"<body><header><div class='title'>SUITE EXECUTION DASHBOARD</div><div class='logo'><img src='../../sysfiles/DashBoardSysFiles/images/v-2-solutions-logo-standard.png'/></div><div class='clearBoth'></div></header>"
					+"<div class='main'><ul class='description'><li><span>Project Name : </span><span>"+projectName+"</span></li><li><span>SUT Build Version : </span><span>"+SUTBuildVersion+"</span></li><li><span>Module : </span><span>"+module+"</span></li><li>"
					+"<span>Suit Type : </span><span>"+suitetype+"</span></li></ul><div class='graphContainer'><div class='commonContainer'><p>Execution Result Overview</p><div id='piechart' style='height: 450px;'></div></div>"
					+"<div class='commonContainer'><p>Result As Per Browser/Iteration</p><div id='chart_div' style='height: 400px;'></div></div><div class='clearBoth'></div></div>"
					+"<div class='reportContainer'><table class='executionTable'><thead><tr><th>Suit type</th><th>Execution Time</th><th>SUT Server</th><th>Testbed</th><th>OS</th><th>Browser/API</th><th>% of Automation Coverage</th><th>%TCs Passed</th><th>%TCs Failed</th><th>%TCs Skipped</th><th>Total TCs</th><th>TCs Executed</th><th>TCs Pass</th><th>TCs Failed</th><th>TCs Skipped</th><th>Execution Result</th></tr></thead><tbody>"
					+DashboardData+
					"</tbody></table></div></div><div class='footer'></div></body></html>");
			bw.close();
		
			}
		String graphData=getLineChartData(DashboardFilePath);
		createFinalDashboard(graphData,DashboardFilePath);
		
		}

	private  static String getLineChartData(String DashboardFilePath){
			
				ArrayList<org.jsoup.nodes.Element> tableArray = new ArrayList<org.jsoup.nodes.Element>();

				ArrayList<String> passPer = new ArrayList<String>();
				ArrayList<String> failPer = new ArrayList<String>();
				ArrayList<String> skipPer = new ArrayList<String>();
				ArrayList<String> browserType = new ArrayList<String>();
	 			ArrayList<String> itr = new ArrayList<String>();
				ArrayList<String> allItr = new ArrayList<String>();
				ArrayList<Integer> TotalTcs = new ArrayList<Integer>();
				ArrayList<Integer> TcsPass = new ArrayList<Integer>();
				ArrayList<Integer> TcsFail = new ArrayList<Integer>();
				ArrayList<Integer> TcsSkipped = new ArrayList<Integer>();
				
				
				StringBuilder contentBuilder = new StringBuilder();
				try{
				BufferedReader in = new BufferedReader(new FileReader(DashboardFilePath));
				String str;
				while ((str = in.readLine()) != null) {
					contentBuilder.append(str);
		
				}
				in.close();
				}catch(IOException e){
					e.printStackTrace();
				}
				
				try {
					String html = contentBuilder.toString();
					org.jsoup.nodes.Document doc = Jsoup.parse(html);
					org.jsoup.select.Elements trlinks = doc.select("tr");
					for (int i = 1; i < trlinks.size(); i++) {											// ignore first tr tags
						tableArray.add(trlinks.get(i));
					}
				} catch (Exception e) {
					System.out.println("Following exception while parshing html file data with 'tr' tag "+ e.getMessage());
				}
				
				try {
					for (int i = 0; i < tableArray.size(); i++) {
						itr.add("Iteration - "+(i+1));
						String tdData=tableArray.get(i).toString();
						String onlyTD[]=tdData.split("<td");
						
						for (int j = 0; j < onlyTD.length; j++) {
							if(j==6){
								String brwArr[]=onlyTD[j].split(">");
								String brwName=brwArr[1].replace("</td", "");
								browserType.add(brwName);
							}
							else if(j==8){
								String passArr[]=onlyTD[j].split(">");
								String perPass=passArr[1].replace("</td", "");
								passPer.add(perPass);
							}else 	if(j==9){
								String failArr[]=onlyTD[j].split(">");
								String perFail=failArr[1].replace("</td", "");
								failPer.add(perFail);
							}else 	if(j==10){
								String skipArr[]=onlyTD[j].split(">");
								String perSkip=skipArr[1].replace("</td", "");
								skipPer.add(perSkip);
							}
							
							//it is used for Piechart report on dashboard
							else 	if(j==11){
								String totalTCArr[]=onlyTD[j].split(">");
								String totalTCNum=totalTCArr[1].replace("</td", "");
								Integer temp = new Integer(totalTCNum);
								TotalTcs.add(temp);
							}
							else 	if(j==13){
								String TCsPassArr[]=onlyTD[j].split(">");
								String TCsPassNum=TCsPassArr[1].replace("</td", "");
								Integer temp = new Integer(TCsPassNum);
								TcsPass.add(temp);
							}
							else 	if(j==14){
								String TCsFailArr[]=onlyTD[j].split(">");
								String TCsFailNum=TCsFailArr[1].replace("</td", "");
								Integer temp = new Integer(TCsFailNum);
								TcsFail.add(temp);
							}
							else 	if(j==15){
								String TCsSkippedArr[]=onlyTD[j].split(">");
								String TCsSkippedNum=TCsSkippedArr[1].replace("</td", "");
								Integer temp = new Integer(TCsSkippedNum);
								TcsSkipped.add(temp);
							}
							
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("Following exception while parshing html file data with '/td' tag for browsertype, no. of pass/fail/skip test cases "+ e.getMessage());
				}
				// it is used for counting total number of test cases executed under one dashboard (Html) file
				int TOTALTC = 0;
				for(int i=0 ; i<TotalTcs.size();i++)
				{
					 TOTALTC += TotalTcs.get(i);
				}
				System.out.println("TOTALTC__________________ : "+TOTALTC);
				
				int TOTALTCsPASS = 0;
				for(int i=0 ; i<TcsPass.size();i++)
				{
					TOTALTCsPASS += TcsPass.get(i);
				}
				System.out.println("TOTALTCsPASS__________________ : "+TOTALTCsPASS);
				
				int TOTALTCsFAIL = 0;
				for(int i=0 ; i<TcsFail.size();i++)
				{
					TOTALTCsFAIL += TcsFail.get(i);
				}
				System.out.println("TOTALTCsFAIL__________________ : "+TOTALTCsFAIL);
				
				int TOTALTCsSKIPPED = 0;
				for(int i=0 ; i<TcsSkipped.size();i++)
				{
					TOTALTCsSKIPPED += TcsSkipped.get(i);
				}
				System.out.println("TOTALTCsSKIPPED__________________ : "+TOTALTCsSKIPPED);
				
				int iterationCount=0;
				for(int i=itr.size()-1; i>=0; i--){
					String data = "";data="[\'"+browserType.get(i)+"\',"+TcsPass.get(i)+","+TcsFail.get(i)+"]";
					
					// it is used for depict skip percentage in line chart.
					allItr.add(iterationCount, data);
					iterationCount++;
					
				}
				String iterationData=allItr.toString().replace("[[", "[");
				String graphData="<script type='text/javascript'>google.charts.load('current', {'packages':['corechart']});google.charts.setOnLoadCallback(drawPieChart);function drawPieChart() {var data =google.visualization.arrayToDataTable([['Data', 'Hours per Day'],['%Fail',  "+TOTALTCsFAIL+"],['%Skip', "+TOTALTCsSKIPPED+"],['%Pass', "+TOTALTCsPASS+"],]);var options = {colors: ['#af2f10','#fbb03b','#8da408'],legend:{position: 'bottom'},backgroundColor: '#f5f5f5',fontName: 'lato',height: 400,fontSize: 12};var chart = new google.visualization.PieChart(document.getElementById('piechart'));chart.draw(data, options);}google.charts.setOnLoadCallback(drawStacked);function drawStacked() {var data = new google.visualization.DataTable();data.addColumn('timeofday', 'Status');data.addColumn('number', '%Pass');data.addColumn('number', '%Fail');data.addRows([[{v: [8, 0, 0]}, 50, 50],[{v: [9, 0, 0]}, 75, 25],[{v: [10, 0, 0]}, 30, 70],]);var options_fullStacked = {isStacked: 'percent',height: 400,fontSize: 12,fontName: 'lato',colors:['#8da408','#af2f10'],backgroundColor: '#f5f5f5',legend: {position: 'bottom', maxLines: 3},vAxis: {minValue: 0,ticks: [0, .25, .50, .75, 1],gridlines: {color: '#979797'}},hAxis: {gridlines: {color: 'transparent'}},bar: {groupWidth: '20'}};var data = new google.visualization.arrayToDataTable([['Browser', '%Pass', '%Fail'],"+iterationData+");var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));chart.draw(data, options_fullStacked);}</script>";
				return graphData;
			}

	private  static void createFinalDashboard(String graphData,String DashboardFilePath){
		
		String newDashboardData="";
		
			try {
				File file = new File(DashboardFilePath);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String line = "", oldtext = "",withoutChartDataText="";
				while ((line = reader.readLine()) != null) {
					oldtext += line;
				}
				String detailedData[]=oldtext.split("div");
				for (int i = 0; i < detailedData.length; i++) {
				if(detailedData.length==1){	// If we are creating  the line chart for first time
					withoutChartDataText=withoutChartDataText+detailedData[i];
					}else{
						if(i==0){
							//withoutChartDataText=withoutChartDataText+detailedData[i].replace("</table></font><br><", "</table></font><br>");
						}else if(i==6){
							//withoutChartDataText=withoutChartDataText+detailedData[i].replaceFirst("><table", "<table");
						}else{
							
						}
					}
				}
		
				reader.close();
				 newDashboardData=newDashboardData+"<tr></tr>";
				String newtext = oldtext.replaceFirst("DashboardChart.js'></script>","DashboardChart.js'></script>"+graphData);
				newtext=newtext.replaceAll("\\\\", "/");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				FileWriter writer = new FileWriter(DashboardFilePath);
				writer.write(newtext);
				writer.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
	}

	public static void main(String args[]) throws ParserConfigurationException, SAXException, IOException, SQLException {
		/* @HELP
		@class:				CreateDashboard	
		@method:			main()
		@parameter:		String[] args
		@notes:				Calls all the methods of the CreateDashbaord Class
		@returns:			No return type
		@END
		 */
		getConfigDetails();
		getBrowserVersionfromTextFile();
		fileNamesWithPath(tcresultxmlfolderPath);
		fileNames(dashboardPath);
		readXmlFiles();
		dataTestReport();
		getSuiteRunTimeStamp();
		newestFile();
		getResultFolderPath();		
		masterDashboardData();
		creatingDashbaordHtml();
		//creatingDashboardDatabase();
		}
}