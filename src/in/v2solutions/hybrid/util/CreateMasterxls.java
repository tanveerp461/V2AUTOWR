package in.v2solutions.hybrid.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateMasterxls extends Constants {
	/* @HELP
	@class:				CreateMasterxls extends Constants (Multi Level of Inheritance)
	@methods:			fileNames(), readDataExcelXLSX(), writeDataExcelXLSX(), creatingMasterXlsxForAllModules(),creatingMasterXlsxForIndividualModule, main()
	@parameter:		Different parameters are passed as per the method declaration
	@notes:				Creates a "MasterTSModule.xlsx" as per the selected Modules from Configuration Excel File "Config.xlsx"
	@returns:			All respective methods have there return types
	@END
	 */	

	@SuppressWarnings("rawtypes")
	private static Vector vectorDataExcelXLSX = new Vector(); 
	static XSSFSheet xssfSheet;
	static Sheet sh;
	@SuppressWarnings("rawtypes")
	static Vector<Vector> ParentVector = new Vector<Vector>();
	static FileOutputStream fos = null;

	public static String[] fileNames(String directoryPath) {
		/* @HELP
		@class:				CreateMasterxls	
		@method:			fileNames()
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
				}
			}
		}
		return fileNames.toArray(new String[] {});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector readDataExcelXLSX(String fileName, int SheetNumber) {
		/* @HELP
		@class:				CreateMasterxls	
		@method:			readDataExcelXLSX()
		@parameter:		String fileName, int SheetNumber
		@notes:				Reads all the Excel file as per the Module Details mentioned in "Config.xlslx" and stores the data in a vector list.
		@returns:			Returns the data stored in the vector array list
		@END
		 */
		Vector vectorData = new Vector();
		String xssfCellval=null;
		try {
			FileInputStream fileInputStream = new FileInputStream(fileName);
			XSSFWorkbook xssfWorkBook = new XSSFWorkbook(fileInputStream);
			XSSFSheet xssfSheet = xssfWorkBook.getSheetAt(SheetNumber);
			Iterator rowIteration = xssfSheet.rowIterator();
			while (rowIteration.hasNext()) {
				XSSFRow xssfRow = (XSSFRow) rowIteration.next();
				Iterator cellIteration = xssfRow.cellIterator();
				Vector vectorCellEachRowData = new Vector();
				while (cellIteration.hasNext()) {
					XSSFCell xssfCell = (XSSFCell) cellIteration.next();		
					if(xssfCell.toString().contains(","))
						xssfCellval=xssfCell.toString().replace(",", "__");
					else
						xssfCellval=xssfCell.toString();
					vectorCellEachRowData.addElement(xssfCellval);
				}
				vectorData.addElement(vectorCellEachRowData);
			}
		} catch (Exception ex) {
		}
		return vectorData;
	}

	@SuppressWarnings("rawtypes")
	public static void writeDataExcelXLSX(Vector<Vector> vectorData, Workbook wb, String sheetName) throws IOException {
		/* @HELP
		@class:				CreateMasterxls	
		@method:			writeDataExcelXLSX()
		@parameter:		Vector<Vector> vectorData, Workbook wb, String sheetName
		@notes:				Creates a new sheet and writes all the data as per the data stored in the vectorData
		@returns:			Returns the data stored in the vector array list
		@END
		 */
		Row row;
		Cell cell;
		String[] cellvalue = null;
		int t = 0, p = 0;
		sh = wb.createSheet(sheetName);
		for (int i = 0; i < vectorData.size(); i++) {
			Vector vectorCellEachRowData = (Vector) vectorData.get(i);
			if (sheetName.equals("Test Cases") || sheetName.equals("Test Steps"))
				if (i != 0)	p = 1;
			for (int j = p; j < vectorCellEachRowData.size(); j++) {
				//System.out.print("------->" + vectorCellEachRowData.get(j).toString() + "\t\t ");
				String str1 = vectorCellEachRowData.get(j).toString().replace("[", "");
				cellvalue = str1.split(",");
				row = sh.createRow((short) t);
				t++;
				for (int k = 0; k < cellvalue.length; k++) {
					cell = row.createCell((short) k);		
					if (cellvalue[k].contains("__")){					
						cellvalue[k] = cellvalue[k].replace("__", ",");}
					else{}
					cell.setCellValue(cellvalue[k].replace("]", "").trim());
				}
			}
		}
	}

	public static void creatingMasterXlsxForAllModules() throws Exception {
		/* @HELP
		@class:				CreateMasterxls	
		@method:			creatingMasterXlsxForAllModules()
		@parameter:		No Parameters 
		@notes:				Creates a "Master.xslx" file by collating the data from all different workbooks and maintains the sheet integrity.
		@returns:			No Return type
		@END
		 */
		File file;
		Workbook wb = new XSSFWorkbook();
		fos = new FileOutputStream(mastertsmodulePath + "/MasterTSModule.xlsx");
		file = new File(tsmodulesPath);
		String[] str = file.list();
		String[] tabName = { "Test Cases", "Test Steps", "Test Data" };
		for (int var = 0; var < 3; var++) {
			for (String st : str) {
				vectorDataExcelXLSX = CreateMasterxls.readDataExcelXLSX(tsmodulesPath + st, var);
				ParentVector.add(vectorDataExcelXLSX);
			}
			CreateMasterxls.writeDataExcelXLSX(ParentVector, wb, tabName[var]);
			ParentVector.clear();
		}
		wb.write(fos);
		fos.close();
		System.out.println("INFO:=> MASTER TESTSUITE file has been generated.");
	}

	public static void creatingMasterXlsxForIndividualModule(String filepath) {
		/* @HELP
		@class:				CreateMasterxls	
		@method:			creatingMasterXlsxForIndividualModule()
		@parameter:		String filepath
		@notes:				Creates a "Master.xslx" file by collating the data from a single workbook as per the Module detail passed in "config.xlsx" file.
		@returns:			No Return type
		@END
		 */
		try {
			FileInputStream file = new FileInputStream(new File(filepath));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						break;
					case Cell.CELL_TYPE_NUMERIC:
						break;
					case Cell.CELL_TYPE_STRING:
						break;
					}
				}
			}
			file.close();
			FileOutputStream out = new FileOutputStream(new File(mastertsmodulePath + "\\MasterTSModule.xlsx"));
			workbook.write(out);
			out.close();
			System.out.println("INFO:=> MASTER TESTSUITE file has been generated.");
		} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
	}
	
	public static void deleteJavaFiles(){
		File file;
		 file = new File(tcPath);     
	     String[] myFiles;    
	         if(file.isDirectory()){
	             myFiles = file.list();
	             for (int i=0; i<myFiles.length; i++) {
	                 File myFile = new File(file, myFiles[i]); 
	                 myFile.delete();
	             }
	          }
	}
	
	@SuppressWarnings("resource")
	public static void createObjectRepository() throws Exception{
		ArrayList<String> list = new ArrayList<String>();
		File Folder = new File(inORpath);
		File files[];
		files = Folder.listFiles();
		try {
			if (files.length >= 1) {
				for (int i = 0; i < files.length; i++) {
					BufferedReader br = new BufferedReader(new FileReader(files[i]));
					String s1 = null;
					while ((s1 = br.readLine()) != null) {
						list.add(s1);
					}
				}
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(outORPath));
		String listWord;
		for (int i = 0; i < list.size(); i++) {
			listWord = list.get(i);
			writer.write(listWord);
			writer.write("\r\n");
		}
		System.out.println("INFO:=> MASTER OBJECT REPOSITORY file has been generated");
		writer.close();
	}
	
	public static void main(String[] args) throws Exception {
		/* @HELP
		@class:				CreateMasterxls	
		@method:			main()
		@parameter:		String[] args
		@notes:				Calls all the methods of the CreateMasterxls Class
		@returns:			No Return type
		@END
		 */
		getConfigDetails();
		deleteJavaFiles();
		createObjectRepository();
		fileNames(tsmodulesPath);	
		if (module.equalsIgnoreCase("all")) {
			creatingMasterXlsxForAllModules();
			System.out.println("INFO:=> Preparing to Run all Modules");
		} else {
			for (int i = 0; i < fileNames.size(); i++) {
				if (fileNames.get(i).contentEquals(module+excelxExtention))
				{
					System.out.println("INFO:=> Preparing to Run '"+module+"' Module");
					String filePath = fileNames.get(i).toString();
					creatingMasterXlsxForIndividualModule(tsmodulesPath + filePath);
				}
			}
		}
	}
}
