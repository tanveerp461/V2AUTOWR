package in.v2solutions.hybrid.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CopyReportsAndZip extends Constants {
	
	/* @HELP
	@class:				CopyReportsAndZip extending Constants Class (Single Level of Inheritance)
	@methods:			getSuiteRunTimeStamp(), copyFolder(), newestFile(), replacingHTMLTagsInSuiteExecutionReports(), getResultFolderPath(), zippingFolder()
	@parameter:		Different parameters are passed as per the method declaration
	@notes:				The class file consist of different methods which gets different values and parameters as follows,
										1. Gets the Suite Run time stamp from the log file generated when the test case starts execution.
										2. Copies all the latest Screenshots (if any), log of the suite execution and XSLT report output on to a new folder with the above time stamp.
										3. Selects the path of the newest folder from the set of  above copied files.
										4. Edit and Replace the html tags in Output reports
										5. Zipping (i.e. creating a .zip file) the latest folder as per the newest folder name. 
	@returns:			All respective methods have there return types
	@END
	 */	
	  static String[] temp  = new String[20];
	  static  int count = 0;
	  static String verifivationSummaryData = "";
	public static void getSuiteRunTimeStamp()  {
		/* @HELP
		@class:				CopyReportsAndZip	
		@method:			getSuiteRunTimeStamp()
		@parameter:		No parameters
		@notes:				Gets the Suite run time stamp from the Log file generated at the time of test suite execution
		@returns:			Not return type
		@END
		 */
		File dir = new File(Keywords.tempforderPath + "/logs");
		File[] files = dir.listFiles();
	
	    for (int i = 0; i < files.length; i++) {
		      if (files[i].isFile()) {
		    	  if(files[i].getName().contains(".log")){
		    		  filename=files[i].getName();
		    	  }
		      } 
		    }
	    	//System.out.println(filename);
			String[] FirstSplit = filename.split(Dot+"log");
			String[] SecondSplit =FirstSplit[0].split(Space + Hyphen + Space);
			SuiteRunTimeStamp = SecondSplit[1];
			//System.out.println(SuiteRunTimeStamp);
	}

	public static void copyReports() throws ParseException {
		/* @HELP
		@class:				CopyReportsAndZip	
		@method:			copyReports()
		@parameter:		No parameters
		@notes:				Copies the latest log file, XSLT report output and Screenshots if any to a new folder named "SuiteExecution_Reports_Logs" using copyFolder() Method with the suite run time stamp
		@returns:			No return type
		@END
		 */
				
		srcFolder[0] = new File(Keywords.SRC_FOLDER2);
		srcFolder[1] = new File(Keywords.SRC_FOLDER1);
		srcFolder[2] = new File(Keywords.SRC_FOLDER3);
		destFolder = new File(Keywords.suiteExecution_Reports_LogsPath+suiteName +" "+ Keywords.strActDate);
		
		// Code to get Suit Execution Time
		 String startAfter = SuiteRunTimeStamp;
	     SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy_hh-mm-ss_aaa(zzz)");
	     Date date = dateFormat.parse(startAfter);
	     suiteStartTimeInSeconds=(date.getTime()/1000);
	     //System.out.println(date.getTime());
	    
	    java.util.Date endDate = new java.util.Date();
		suiteEndTimeInSeconds =(endDate.getTime()/1000);
		
		suiteExecutionTimeInSeconds=suiteEndTimeInSeconds-suiteStartTimeInSeconds;
	    int hours = (int) suiteExecutionTimeInSeconds / 3600;
	    int remainder = (int) suiteExecutionTimeInSeconds - hours * 3600;
	    int mins = remainder / 60;
	    remainder = remainder - mins * 60;
	    int secs = remainder;
	    int[] Time = {hours , mins , secs};
	    //System.out.println(Time[0]);
	    //System.out.println(Time[1]);
	   //System.out.println(Time[2]);
	    totalExecutionTime="Total Execution Time: "+Time[0]+" hour(s) "+Time[1]+" minute(s) "+Time[2]+" second(s)";
	    System.out.println("INFO:=> Total Execution Time: "+totalExecutionTime);
				
		for (int i = 0; i <= srcFolder.length - 1; i++) {
			if (!srcFolder[i].exists()) {
				System.out.println(srcFolder[i]+"Directory does not exist.");
				// just exit
				// System.exit(0);
			} else {
				try {
					copyFolder(srcFolder[i], destFolder);
					//System.out.println(srcFolder[i]+"Directory got copied.");
				} catch (IOException e) {
					//e.printStackTrace();
					// error, just exit
					//System.exit(0);
				}
			}
		}
	}

	public static void copyFolder(File src, File dest) throws IOException {
		 /*@HELP
		@class:				CopyReportsAndZip	
		@method:			copyFolder()
		@parameter:		File src, File dest
		@notes:				This method is called in copyReports() method
		@returns:			Not return type
		@END
		*/ 
		
		if (src.isDirectory()) {
			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				//System.out.println("Directory copied from " + src + "  to "+ dest);
			}
			// list all the directory contents
			String files[] = src.list();
			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}
		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
			//System.out.println("File copied from " + src + " to " + dest);
		}
	}

	// ********************************************************************************
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void newestFile() {
		 /*@HELP
		@class:				CopyReportsAndZip	
		@method:			newestFile()
		@parameter:		No Parameters are passed
		@notes:				Gets the folder path of the latest copied file containing logs, latest xslt reports and screenshots.
		@returns:			No return type
		@END
		*/ 
		
		File dir = new File(Keywords.suiteExecution_Reports_LogsPath);
		File[] files = dir.listFiles();
		Arrays.sort(files, new Comparator() {
			public int compare(Object o1, Object o2) {
				return compare((File) o1, (File) o2);
			}
			private int compare(File f1, File f2) {
				long result = f2.lastModified() - f1.lastModified();
				if (result > 0) {
					return 1;
				} else if (result < 0) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		Latestfile = (files[0].getAbsolutePath());
	}
	
	public static void replacingHTMLTagsInSuiteExecutionReports() throws Exception{
		 /*@HELP
		@class:				CopyReportsAndZip	
		@method:			replacingHTMLTagsInSuiteExecutionReports()
		@parameter:		No Parameters are passed
		@notes:				Replaces the "&lt" & "&gt' tags with "<" & ">" symbols on the XSLT output html files.
		@returns:			No return type
		@END
		*/ 
		
		File dir = new File(Latestfile);
	      FilenameFilter filter = new FilenameFilter() {
	         public boolean accept(File dir, String name) {
	            return name.endsWith(".html");
	        }
	      };
	      String[] list = dir.list(filter);
	      if (list == null) {
	         System.out.println("Either dir does not exist or is not a directory");
	      } 
	      else {
	         for (int i=0; i< list.length; i++) {
	            String filename = list[i];
	                   
	            if(filename.contains(suitetype+".html") || filename.contains("Verify")){
	            	filename.trim();
	            	File htmlfile = new File(Latestfile+"//"+filename);
	            	
	            	//System.out.println(htmlfile);
	            	
	            	BufferedReader reader = new BufferedReader(new FileReader(htmlfile));
		              String line = "", oldtext = "";
		             while((line = reader.readLine()) != null)
		                 {
		                 oldtext += line + "\n";
		             }
		             reader.close();	                  
		             String gttext = oldtext.replaceAll("&gt;", ">");
		             String lttext = gttext.replaceAll("&lt;", "<");
		             FileWriter writer = new FileWriter(Latestfile+"//"+filename);
		             writer.write(lttext);
		             writer.close();	              
	            	//System.out.println("Replaced");
              }
           }
      }
}
		
	public static void getResultFolderPath(){
	 /*@HELP
	@class:				CopyReportsAndZip	
	@method:			getResultFolderPath()
	@parameter:		No Parameters are passed
	@notes:				Gets the latest results folder name which is used in CreateDashboard class file. 
	@returns:			No return type
	@END
	 */
	
		String Latest1=Latestfile;
		String First = Latest1;
		String[] FirstSplit = First.split(DotZip);
		Latestresultsfolder = FirstSplit[0];
		//System.out.println("LatestResultFolderPath :"+Latestresultsfolder);

 }
	
 	public static void zippingFolder() {
 		/*@HELP
 		@class:				CopyReportsAndZip	
 		@method:			zippingFolder()
 		@parameter:		No Parameters are passed
 		@notes:				Gets the latest results folder from the output folder and creates a ".zip" file. 
 		@returns:			No return type
 		@END
 		 */
 		
		try {
			// Source Folder == > Path to Zip a particular folder
			File inFolder = new File(Latestfile);
			// Destination Folder == > Output zip file name
			File outFolder = new File(Latestfile + ".zip");
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(outFolder)));
			BufferedInputStream in = null;
			byte[] data = new byte[5000];
			String files[] = inFolder.list();
			for (int i = 0; i < files.length; i++) {
				in = new BufferedInputStream(new FileInputStream(
						inFolder.getPath() + "/" + files[i]), 1000);
				out.putNextEntry(new ZipEntry(files[i]));
				int count;
				while ((count = in.read(data, 0, 1000)) != -1) {
					out.write(data, 0, count);
				}
				out.closeEntry();
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
 	public static void createHtmlVerificationReport() throws IOException{
		getConfigDetails();
		String verifivationSummaryData = "";
		int passCount=0;
		int failCount=0;

		File file = new File(SRC_FOLDER2+Forwardslash+verificationSummaryText);
		  FileReader fileReader = new FileReader(file);
		  BufferedReader bufferedReader = new BufferedReader(fileReader);
		  String line;
		  String dataArr[];
		  while ((line = bufferedReader.readLine()) != null) {
		 
		   dataArr=line.split("__");
		   if(dataArr[3].equals("Y")){
		   	passCount++;
		   }
		   if(dataArr[4].equals("Y")){
		   	failCount++;
		   }
		   
		   verifivationSummaryData = verifivationSummaryData+"<tr> <td>"
		+ dataArr[0]
		+ "</td><td>"
		+ dataArr[1]
		+ "</td><td>"
		+ dataArr[2]
		+ "</td><td>"
		+ dataArr[3]
		+ "</td><td>"
		+ dataArr[4]
		+ "</td>"
		+ "</tr>";
		  }
		  fileReader.close();
		  verifivationSummaryData = verifivationSummaryData+"<tr><td colspan=3 align='center'>Total</td><td>"
		+ passCount
		+ "</td><td>"
		+failCount
		+ "</td>"
		+ "</tr>";

		  BufferedWriter bw = new BufferedWriter(new FileWriter(verificationReportPath));
			bw.write("<html><title>v2autoW4.5 Verification Summary</title></head><body><table align='center' border='0' cellpadding='0' cellspacing='0'	width='1200'><div align='left'><b><font size='4'><font color='#000080' face='Calibri'>"
			//+ "<pre>Project Name	        : "+projectName+"</br>SUT Version Number	: "+SUTBuildVersion+"</br>Module			: "+suiteName+"</br>SuiteType			: "+suitetype+"</br>Browser	                        : "+bTypeVersion+" </br></br>Execution Summary</br></b></font></b><b><font size='6'><font color='#000080' face='Calibri'></pre>"
			+ "<pre>Project Name		: "+projectName+"</br>SUT Server			: "+SUTServer+"</br>SUT Build Number	: "+SUTBuildVersion+"</br>Test Bed				: "+tBedType+"</br>OS					: "+osType+"</br>BROWSER/API		: "+bType+"</br>Device Name		: "+deviceName+"</br>SuiteType			: "+suitetype+"</br>Module				: "+module+"</br>Browser Version		: "+bTypeVersion+" </br></br>Execution Summary</br></b></font></b><b><font size='6'><font color='#000080' face='Calibri'></pre>"
			
			+ "</font></b></div><table border='5' style='font-family: Calibri;'><tr><th>S.No</th><th>Object Name</th><th> Verification Performed</th><th> Pass</font></th><th>Fail</font></th></tr>" +
			verifivationSummaryData+
			"</table></body></html>");
			bw.close();


		}
 	
 	@SuppressWarnings({ "rawtypes", "finally" })
 	public static Vector readResponseDataFromTextFile(String filename)
			throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String templine1 = "";
		String templine2 = "";
		try {
			String line = br.readLine();
			while (line != null) {

				if (line.startsWith("Verify") && line.contains(".png"))
					templine2 = line;
				else {
					templine1 = line;
					templine2 = templine2 + templine1;
					templine1 = "";
				}

				if (templine2.startsWith("Verify")
						|| templine2.contains(".png")) {
					verificationData.add(templine2);
					templine2 = "";
				}
				line = br.readLine();

			}
		} finally {
			br.close();
			return verificationData;
		}
	}

	public static void writeLogDataExcel(Vector<String> vectorData,
			String sheetName) throws IOException {
		//System.out.println(": Inside writeLogDataExcel");
		Workbook wb = new XSSFWorkbook();
		Row row;
		Cell cell;
		String[] cellvalue = null;
		int t = 0;
		Sheet sh = wb.createSheet(sheetName);
		FileOutputStream fos = new FileOutputStream(SRC_FOLDER2+"\\"+failedDataInXlsx);
		row = sh.createRow((short) t);
		//System.out.println(": Inside writeLogDataExcel 1");
		t++;
		for (int k = 0; k <= 4; k++) {
			cell = row.createCell((short) k);
			switch (k) {
			case 0:
				cell.setCellValue("TCID");
				break;
			case 1:
				cell.setCellValue("Field Name ");
				break;
			case 2:
				cell.setCellValue("Act Value");
				break;
			case 3:
				cell.setCellValue("Exp Value");
				break;
			case 4:
				cell.setCellValue("Screenshot");
				break;
			}
		}
		//System.out.println(": Inside writeLogDataExcel befor for loop");
		for (int i = 0; i < vectorData.size(); i++) {
			row = sh.createRow((short) t);
			t++;
			String str1 = vectorData.get(i).replaceAll("\\r\\n|\\r|\\n", " ");
			cellvalue = str1.split("__");
			for (int j = 0; j < cellvalue.length; j++) {
				cell = row.createCell((short) j);
				cell.setCellValue(cellvalue[j]);
				if (j == cellvalue.length - 1) {
					CreationHelper createHelper = wb.getCreationHelper();
					Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
					CellStyle hlink_style = wb.createCellStyle();
					Font hlink_font = wb.createFont();
					hlink_font.setUnderline(Font.U_SINGLE);
					hlink_font.setColor(IndexedColors.BLUE.getIndex());
					hlink_style.setFont(hlink_font);
					String FileAddress = screenshotPath + cellvalue[j];
					FileAddress = FileAddress.replace("\\", "/");
					link.setAddress(FileAddress);
					cell.setHyperlink(link);
					cell.setCellStyle(hlink_style);
				}
			}
		}
		wb.write(fos);
		fos.close();
		System.out.println("Log Excel file has been generated!");
	}
  
}

 
