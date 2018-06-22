package in.v2solutions.hybrid.util;

import java.io.File;
import java.io.IOException;

public class DeleteReportsAndLogs extends Constants {

	/* @HELP
	@class:			DeleteReportsAndLogs extends Constants
	@method:		deleteOldReports (), delete(File file)
	@parameter:	Different parameters are passed as per the method declaration
	@notes:			
							1. Deletes all Screenshots inside Screenshot Folder, Delete logs file inside Log Folder, Previous Execution Output folder, Previous Created testng.xml & Deletes the "MasterTSModule.xslx" file
	@returns:		All respective methods have there return types
	@END
	 */

	public static void deleteOldReports() {
		/* @HELP
		@class:				DeleteReportsAndLogs	
		@method:			deleteOldReports()
		@parameter:		No Parameter
		@notes:				Deletes Screenshots inside Screenshot Folder, Delete log file inside Log Folder, Previous Execution Output folder, Previous Created testng.xml & Deletes the "MasterTSModule.xslx" file
		@returns:			No return type
		@END
		 */
		files[0] = new File(SRC_FOLDER1);
		files[1] = new File(SRC_FOLDER2);
		files[2] = new File(SRC_FOLDER3);
		files[3] = new File(SRC_FOLDER4);
		files[4] = new File(SRC_FOLDER5);
		files[5] = new File(SRC_FOLDER6);

		for (int i = 0; i <= files.length-1; i++) {
			// make sure directory exists
			if (!files[i].exists()) {
			} else {
				try {
					delete(files[i]);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
		}
		System.out.println("Delete Done");
	}

	public static void delete(File file) throws IOException {
		/* @HELP
		@class:				DeleteReportsAndLogs	
		@method:			delete()
		@parameter:		File file
		@notes:				This method is called in deleteOldReports() method to delete all the folders passed in deletedeleteOldReports() method.
		@returns:			No return type
		@END
		 */

		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}
				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}
		} else {
			// if file, then delete it
			file.delete();
		}
	}

	public static void main(String args[]) {
		/* @HELP
		@class:				DeleteReportsAndLogs	
		@method:			main()
		@parameter:		String args[]
		@notes:				Calls all the methods of the DeleteReportsAndLogs Class
		@returns:			No Return type
		@END
		 */
		deleteOldReports();
	}

}
