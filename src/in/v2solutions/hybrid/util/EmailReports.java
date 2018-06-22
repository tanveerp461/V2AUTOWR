package in.v2solutions.hybrid.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;


public class EmailReports extends CopyReportsAndZip{
	/* @HELP
	@class:				EmailReports extends CopyReportsAndZip
	@method:			send() 
	@parameter:		Different parameters are passed as per the method declaration
	@notes:			
			1. Email the reports as per the defined format with the Test Execution Results as an attachment as a ".zip" file
			2. To Address and CC address and all Email related Data are read from "confg.xslx" file.
	@returns:			All respective methods have there return types
	@END
	 */
	
	private void send(String from, String[] toAddress2, String[] ccAddress2,String subject, String body, ArrayList<String> attachments)
			throws MessagingException {
		/* @HELP
		@class:				EmailReports	
		@method:			send()
		@parameter:		String from, String[] toAddress2, String[] ccAddress2,String subject, String body, ArrayList<String> attachments
		@notes:				This Method send the email to respective users as per the parameters passed.
		@returns:			No return type
		@END
		 */

		Message message = new MimeMessage(getSession());
		for (int i = 0; i < toAddress2.length; i++) {
			message.addRecipient(RecipientType.TO, new InternetAddress(toAddress2[i]));
			System.out.println("Sending Mail to TO:" + toAddress2[i]);
		}
		for (int i = 0; i < ccAddress2.length; i++) {
			message.addRecipient(RecipientType.TO, new InternetAddress(ccAddress2[i]));
			System.out.println("Sending Mail to CC:" + ccAddress2[i]);
		}
		message.addFrom(new InternetAddress[] { new InternetAddress(from) });
		message.setSubject(subject);
		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText(body);
		mbp1.setContent(body, contentHtml);
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);

		for (String fileName : attachments) {
			MimeBodyPart mbp = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(fileName);
			mbp.setDataHandler(new DataHandler(fds));
			mbp.setFileName(fds.getName());
			mp.addBodyPart(mbp);
		}
		message.setContent(mp);
		message.setSentDate(new Date());
		Transport.send(message);
	}

	private Session getSession() {
		Authenticator authenticator = new Authenticator();
		Properties properties = new Properties();
		
		properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
		properties.setProperty("mail.smtp.auth", "false");
		properties.setProperty("mail.smtp.host", contentHost);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		return Session.getInstance(properties, authenticator);
	}

	private class Authenticator extends javax.mail.Authenticator {
		private PasswordAuthentication authentication;

		public Authenticator() {
			authentication = new PasswordAuthentication(fromEmailAddress, fromEmailPwd);
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return authentication;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception {
		/* @HELP
		@class:				EmailReports	
		@method:			main()
		@parameter:		String args[]
		@notes:				Calls all the methods of the EmailReports & CopyReportsAndZip Class
		@returns:			No Return type
		@END
		 */
		getConfigDetails();
		getBrowserVersionfromTextFile();
		getSuiteRunTimeStamp();
		
		File scrShot=new File(screenshotPath);
		  boolean exists = scrShot.exists();
		  if (exists) {
				String 	filenameOne=SRC_FOLDER2+Forwardslash+failedDataInText;
				verificationData = readResponseDataFromTextFile(filenameOne);
				writeLogDataExcel(verificationData, "Failed  Data");
		  }else{
		  }
		  
		  File fileaVerification = new File(SRC_FOLDER2+Forwardslash+verificationSummaryText);
		  exists = fileaVerification.exists();
		  if (exists) {
			  createHtmlVerificationReport();
			  //System.out.println("Inside createHtmlVerificationReport");
		  }else{
			 //System.out.println("VerificationReport is not available ");
		  }
		  
		  
		
		copyReports();
		newestFile();
		replacingHTMLTagsInSuiteExecutionReports();
		zippingFolder();
		
		String mailSubjectLine="v2autoW: Execution Result=> Suite Name: "+ suiteName+ ", Executed At: "+ SuiteRunTimeStamp;
		EmailReports sender = new EmailReports ();
		String projName="Project Name: <b>"+projectName+"</b>";
		String testingSite = "SUT URL: <b>"+SUTUrl+"</b>";
		String autbuildversion= "SUT Build Version: <b>"+SUTBuildVersion+"</b>";
		String env = "SUT Server: <b>"+SUTServer+"</b>";
		String tstype= "Testbed: <b>"+tBedType+"</b>";
		String ostype= "OS: <b>"+osType+"</b>";
		String browser = null;
		
		if(bType.equalsIgnoreCase("API")){
			 browser= " Browser/API: <b>"+bType+"</b>";
		}else{
			 browser= " Browser/API: <b>"+bTypeVersion+"</b>";
		};
		
		String suitedetails="Suite Name: <b>"+suiteName+"</b>";
		String mailBody = "This is an automated message. ***Do not reply.***<br /><br />" + 
				"1. You have executed ("+ suiteName+") Suite at: "+ SuiteRunTimeStamp+ " with below details:<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"a) "+ projName+"<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"b) "+ testingSite+"<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"c) "+ autbuildversion+"<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"d) "+ env+"<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"e) "+ tstype+"<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"f) "+ ostype+"<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"g) "+ browser+"<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"h) "+ suitedetails+"<br />"+
				"&nbsp;&nbsp;&nbsp;&nbsp;"+"i) " +totalExecutionTime+ "<br />"+
				"2. For Execution details, please find the	 attached XSLT Report. <br /> "+
				"3. Although you can find this report in our result repository which is @("+ Keywords.suiteExecution_Reports_LogsPath+")<br /><br /><br />"+
				" Thanks, <br /> V2 Automation Team-QA";
		// sending another mail, with attachments.
		@SuppressWarnings("rawtypes")
		ArrayList fileName = new ArrayList();
		//System.out.println(Latestfile + ".zip");
		fileName.add(Latestfile+ ".zip");
		//System.out.println(SuiteRunTimeStamp);
		//sender.send(fromEmailAddress, toAddr, ccAddr,mailSubjectLine, mailBody, fileName);
	}
}
