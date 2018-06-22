package in.v2solutions.hybrid.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import atu.testrecorder.exceptions.ATUTestRecorderException;


@SuppressWarnings("rawtypes")
public class CreateTestCases extends Constants {

	/* @HELP 
	  @class: 		CreateTestCases extends Constants
	  @methods:		readMasterExcel(),writeEmptyJavaFiles(),creatingTCJavaFiles(),main()
	  @parameter: 	Different parameters are passed as per the method declaration
	  @constructor(if any): No.
	  @notes: 		Creates testcase java files by reading TCID of master excel.	 
	  @returns: 	All respective methods have there respective return types
	  @END*/

	private static Vector<Vector<String>> vectorDataExcelXLSX = new Vector<Vector<String>>(); 
	static XSSFSheet xssfSheet;
	static Sheet sh;
	static File tcase;
	static Vector<Vector> ParentVector = new Vector<Vector>();
	static FileOutputStream fos = null;

	public static Vector<Vector<String>> readMasterExcel(String fileName, int SheetNumber) {
		/*@HELP
		@class:				CreateTestCases	
		@method:			readMasterExcel()
		@parameter:		    String fileName, int SheetNumber
		@notes:				This method is called from  creatingTCJavaFiles() method.
		 					This method reads the contents of TCID of master excel and collects in a vector.
		@returns:			 Vector<Vector<String>>
		@END
		 */ 
		String xssfCellval = null;
		Vector<Vector<String>> vectorData = new Vector<Vector<String>>();
		try { 
			FileInputStream fileInputStream = new FileInputStream(fileName);
			XSSFWorkbook xssfWorkBook = new XSSFWorkbook(fileInputStream);
			XSSFSheet xssfSheet = xssfWorkBook.getSheetAt(SheetNumber);
			Iterator<?> rowIteration = xssfSheet.rowIterator();
			while (rowIteration.hasNext()) {
				XSSFRow xssfRow = (XSSFRow) rowIteration.next();		
				Iterator<?> cellIteration = xssfRow.cellIterator();
				Vector<String> vectorCellEachRowData = new Vector<String>();
				while (cellIteration.hasNext()) {
					XSSFCell xssfCell = (XSSFCell) cellIteration.next();			
					int row=xssfRow.getRowNum();
					int col=xssfCell.getColumnIndex();

					if(col==0 && row!=0)
					{	
						if(xssfCell.toString().contains(","))
							xssfCellval=xssfCell.toString().replace(",", "__");
						else
							xssfCellval=xssfCell.toString();
						vectorCellEachRowData.addElement(xssfCellval);
					}
					vectorData.addElement(vectorCellEachRowData);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vectorData;
	}

	public static void writeEmptyJavaFiles( Vector<Vector> vectorData) throws IOException {
		/*@HELP
		@class:				CreateTestCases	
		@method:			writeEmptyJavaFiles()
		@parameter:		    Vector<Vector> vectorData
		@notes:				This method is called from creatingTCJavaFiles() method.
		 							This method creates blank testcase java files in testcase folder. 
		@returns:			 Vector
		@END
		 */ 
		FileOutputStream fos=null;
		for (int i = 0; i < vectorData.size(); i++) {
			Vector<?> vectorCellEachRowData = vectorData.get(i);
			for (int j = 0; j < vectorCellEachRowData.size(); j++) {
				String str1 = vectorCellEachRowData.get(j).toString().replace("[", "");
				String str2=str1.replace("]", "");
				if(str2.isEmpty()){}
				else{
					fos  =  new FileOutputStream(tcPath+str2+".java");				 
					fos.close(); 					
				}						
			}
		}
	}

	public static void openAndWriteJavaFiles(File src) throws IOException {
		/*@HELP
	@class:				CreateTestCases	
	@method:			openAndWriteJavaFiles()
	@parameter:		    File src
	@notes:				This method is called from  main() method.
	 					This method opens blank testcase java files from testcase folder and writes common code into it.
	@returns:			 No return type.
	@END
		 */ 
		Keywords k = Keywords.getKeywordsInstance();
		String files[] = src.list();
		PrintWriter out;
		String lastTestCase =k.getLastTestCaseName();
		for (String file : files) {

			out = new PrintWriter(new BufferedWriter(new FileWriter(tcPath+file, true)));
			out.println("package in.v2solutions.hybrid.testcases;\n");

			out.println("import in.v2solutions.hybrid.util.Keywords;");
			out.println("import in.v2solutions.hybrid.util.TestUtil;");
			out.println("import in.v2solutions.hybrid.util.Constants;");
			out.println("import java.util.Hashtable;");
			out.println("import org.testng.SkipException;");
			out.println("import org.testng.annotations.AfterTest;");
			out.println("import org.testng.annotations.BeforeTest;");
			out.println("import org.testng.annotations.DataProvider;");
			out.println("import org.testng.annotations.Optional;");
			out.println("import org.testng.annotations.Parameters;");
			out.println("import org.testng.annotations.Test;\n");
			out.println("import atu.testrecorder.exceptions.ATUTestRecorderException;\n");
			String testcase=file.replace(".java", "");  		   
			out.println("\n public class "+ testcase +" extends Constants {");
			out.print("\n String TCName = "+"\""+testcase+"\";\n"); 
			out.println("\n String lastTestCaseName = "+"\""+lastTestCase+"\";\n"); 
			out.println(" int runModecounter = Keywords.xls.getCellRowNum(\"Test Data\",\"DDTCIDWithRunMode\",TCName)+2;");
			out.println("\n @Parameters({ \"Suite-Name\" })");
			out.println("@BeforeTest");
			out.println("public void beforeTest(@Optional String Suitename) {");
			out.println("");		   
			out.println("String Actsuitename = Suitename;");
			out.println("	if (Actsuitename != null) ");
			out.println("	{");
			out.println("		Keywords.tsName = Actsuitename;");
			out.println("		Keywords.tcName = TCName;");
			out.println("	}");
			out.println("	else ");
			out.println("	{");
			out.println("		Keywords.tcName = TCName;");
			out.println("	}");
			out.println("}\n");		   
			out.println("\n @Test(dataProvider = "+"\"getTestData\")");
			String testMethod="";
			String tcName[]=testcase.split("Verify");
			testMethod="verify"+tcName[1];
			out.println("public void "+testMethod+"(Hashtable<String, String> data)throws Exception {");	
			out.println("if (!TestUtil.isTestCaseExecutable(TCName,Keywords.xls))");
			out.println("	throw new SkipException("+"\"Skipping the test as runmode is NO\");");
			out.println("	{\n");
			out.println("if(getTestData().length > 1) {");
			out.println("	String YorN = Keywords.xls.getCellData("+"\"Test Data"+"\",0,runModecounter);");
			out.println("// System.out.println(YorN) => Please uncomment it to debug in case there are some issue in identifying DDT Test case instences;");
			out.println("if (YorN.equals(\""+"N"+"\")){");
			out.println("runModecounter = runModecounter+1;");
			out.println("	throw new SkipException("+"\"Skipping the test as runmode is NO DDT\");");
			out.println("}");	
			out.println("	runModecounter = runModecounter+1;");
			out.println("	}\n");
			out.println("Keywords k = Keywords.getKeywordsInstance();");
			out.println("	k.executeKeywords(TCName, data);");
			out.println("	}\n");
			out.println("	}\n");
			out.println("@AfterTest");
			out.println("public void afterTest() throws ATUTestRecorderException {");
			out.println(" if (TCName.equals(lastTestCaseName))");
			out.println("	  { System.out.println(\" Last Test Case Quit \");");
			out.println("	if(captureVideoRecording.equals(\"Yes\")){  ");
			out.println("	Constants.recorder.stop();  ");
			out.println("	System.out.println(\": Video Recording Stopped \"); }  ");
			out.println("	try{  ");
			out.println("Constants.driver.close();");
			out.println("}catch(Exception e){");
			out.println("  Constants.driver = null;");		  
			out.println("}");			
			out.println("Constants.driver = null;");
			out.println("	}\n");
			out.println("	}");
			out.println("\n @DataProvider");
			out.println("public Object[][] getTestData() {");
			out.println("return TestUtil.getData(TCName, Keywords.xls);");
			out.println("}");
			out.println("}");
			out.close();
			//System.out.println("Data is written into "+file);
		}
		System.out.println("INFO:=> Test Script are Created");
	}

	public static void createTCJavaFiles() throws Exception {	
		/*@HELP
	@class:				CreateTestCases	
	@method:			createTCJavaFiles()
	@parameter:		 No.
	@notes:				This method is called from  main() method.
								This method calls :-> 
										1.getConfigDetails() to get tBedType(i.e.either Desktop or Mobile) from Constants.java file 
			 							2.readMasterExcel() to read TCID of master excel and 
			 							3.writeEmptyJavaFiles() to create blank testcases in testcase folder.
	@returns:			No return type.
	@END
		 */ 
		getConfigDetails();
		tcase=new File(tcPath);
		File file = new File(mastertsmodulePath);
		String[] str = file.list();
		for (String st : str) {
			vectorDataExcelXLSX =readMasterExcel(mastertsmodulePath + st, 0);
			ParentVector.add(vectorDataExcelXLSX);
		}
		writeEmptyJavaFiles(ParentVector);
		ParentVector.clear();		
		openAndWriteJavaFiles(tcase);
	}

	public static void main(String[] args) throws Exception {
		createTCJavaFiles();	
	}

}
