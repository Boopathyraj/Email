package com.example.Recruitment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.mail.Flags.Flag;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import javax.transaction.SystemException;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/Registration")
@RestController
public class CandidateController {

	@Autowired

	CandidateService service;
	

	Candidate bean = new Candidate();
	Employees emp = new Employees();

	public String fileName;
	public String destinationDirectory;
	public String filePath;
	public String existingFolderFilePath;
	public String existingFolderNewFileName;
	public Long existFolderName;

	String agencyName,status,name;
	// private static final int BUFFER_SIZE = 4096;
	// Declaration Part of property Details for connection 
	private static final String MAIL_POP_HOST = "webmail.kgfsl.com";
	private static final String MAIL_STORE_TYPE = "imaps";
	private static final String POP_USER = "baraneetharan.ramasamy@kgfsl.com";
	private static final String POP_PASSWORD = "Aarbarani@12";
	private static final String POP_PORT = "465";
	private static final String saveDirectory = "D:/boopathy/Attachments";

	// public void setSaveDirectory(String dir) {
	//    this.saveDirectory = dir;
	// }

	//Fetch the Subject Keywords from the application.properties file
	@Value("#{'${my.list.of.subjectkeywords}'.split(',')}")
	private List<String> myList;

	public void getMails(String user, String password, String saveDirectory) {
		try {

			// create properties field
			Properties properties = new Properties();
			properties.put("mail.pop3.host", MAIL_POP_HOST);
			properties.put("mail.pop3.port", POP_PORT);
			properties.put("mail.pop3.starttls.enable", "true");
			properties.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

			// Login Authentication 
			Session emailSession = Session.getDefaultInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(POP_USER, POP_PASSWORD);
				}
			});

			// create the POP3 store object and connect with the pop server
			Store store = emailSession.getStore(MAIL_STORE_TYPE);
			store.connect(MAIL_POP_HOST, user, password);

			// create the folder object and open it
			Folder emailFolder = store.getFolder("recruitment");
			emailFolder.open(Folder.READ_WRITE);

			// Get and Display the Unread Message Count
			System.out.println("unread Messages count :" + emailFolder.getUnreadMessageCount());

			// Find the Unread Messages
			Message messages[] = emailFolder.search(new FlagTerm(new Flags(Flag.SEEN), false));

			/* Use a suitable FetchProfile&nbsp;&nbsp;&nbsp; */
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.CONTENT_INFO);
			emailFolder.fetch(messages, fp);

			System.out.println("property keywords are:" + myList.get(0) + "== " + myList.get(1));
			// System.out.println("Total messages length---" + messages.length);

			// retrieve the messages from the folder in an array and print it
			ArrayList<String> list = new ArrayList<>();
			for (int i = 0, n = messages.length; i < n; i++) { //processing unread mesaages one by one 
				Message message = messages[i];
				for (int z = 0; z < myList.size(); z++) //subject keyword size
				{
					if (message.getSubject().contains(myList.get(z))) { //filter method for Subject
						System.out.println("keyword ="+myList.get(z));
						System.out.println("---------------------------------");
						System.out.println("Email Number " + (i + 1));
						System.out.println("Subject: " + message.getSubject());
						System.out.println("From: " + message.getFrom()[0]);
						// System.out.println("Body: " + message.getContent().toString());

						//get the from Address
						String fromAddress = message.getFrom()[0].toString();

						// System.out.println("from :"+foldername);

						// Using the split operation to get the Mailid 
						String[] splited = fromAddress.split("<");
						String[] email = splited[1].split(">");
						System.out.println("split :" + email[0]);
						String emailId = email[0];
						// System.out.println("gggg" +emailId);
						String contentType = message.getContentType();
						// System.out.println("content  " + contentType);
						String messageContent = "";

						// store attachment file name, separated by comma
						String attachFiles = "";

						// Validate the emailId wheather is already exist or not
						boolean result = validateEmailId(emailId);
						if (result == false) {

							list.add(emailId);
							//  bean.setEmailId(emailId);
							service.addDetails(list);
							Candidate obj = service.getId(emailId);
							// bean.setuniqueId(obj.getuniqueId());
							Long foldername = obj.getuniqueId();
							System.out.println("Unique id is =" + obj.getuniqueId());

							// save Attachements in respective folder
							File file = new File(saveDirectory + File.separator + foldername);
							System.out.println(file);

							// create folder 
							if (!file.exists()) {
								if (file.mkdir()) {
									System.out.println("Directory is created!");
									// part.saveFile(file + File.separator + fileName);
									// System.out.println("Attachment Saved successfully");
								} else {
									System.out.println("Failed to create directory!");
								}
							} else {
								System.out.println("folder already exist");
							}

							//checking for Attachments
							if (contentType.contains("multipart")) {
								// content may contain attachments
								Multipart multiPart = (Multipart) message.getContent();
								int numberOfParts = multiPart.getCount();
								for (int partCount = 0; partCount < numberOfParts; partCount++) {
									MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
									if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
										// this part is attachment
										fileName = part.getFileName();

										attachFiles += fileName + ", ";
										destinationDirectory = saveDirectory + File.separator + foldername;
										filePath = file + File.separator + fileName;
										System.out.println("path :" + filePath);
										part.saveFile(filePath);
										System.out.println("filname :" + fileName);
										if (fileName.endsWith("zip")) {
											// ZipInputStream zipIn = new ZipInputStream(new FileInputStream(filePath));
											System.out.println("zip");
											extractFile(filePath);
											Employees eobj =readFile(filePath);
											service.addResumeDetails(eobj);
										} else {
											String savefile=file + File.separator + fileName;
											part.saveFile(savefile);
											Employees eobj =readFile(savefile);
											System.out.println(eobj.getEmailId());
											service.addResumeDetails(eobj);
										}

										// part.saveFile(file + File.separator + fileName);
									} else {
										// this part may be the message content
										messageContent = part.getContent().toString();
									}
								}

								if (attachFiles.length() > 1) {
									attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
								}
							} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
								Object content = message.getContent();
								if (content != null) {
									messageContent = content.toString();
								}
							}

						} else {

							System.out.println("your EmailId already Exists");
							Candidate obj = service.getId(emailId);
							existFolderName = obj.getuniqueId();
							System.out.println("exist foldername is :" + existFolderName);

							if (contentType.contains("multipart")) {
								// content may contain attachments
								Multipart multiPart = (Multipart) message.getContent();
								int numberOfParts = multiPart.getCount();
								for (int partCount = 0; partCount < numberOfParts; partCount++) {
									MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
									if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
										// this part is attachment
										existingFolderNewFileName = part.getFileName();
										System.out.println("filename :" + existingFolderNewFileName);
										String folderPath = saveDirectory + File.separator + existFolderName;
										System.out.println("folderpath" + folderPath);
										attachFiles += existingFolderNewFileName + ", ";
										// String pathForNewFile = saveDirectory + File.separator + existFolderName
										// 		+ File.separator + existingFolderNewFileName;
										// 		part.saveFile(pathForNewFile);
										// 		File newFile = new File(pathForNewFile);

										File folder = new File(folderPath);
										File[] listOfFiles = folder.listFiles();

										for (int j = 0; j < listOfFiles.length; j++) {
											if (listOfFiles[j].isFile()) {

												// if (listOfFiles[j].getName().contentEquals(existingFolderNewFileName)) {
												// System.out.println("same" + listOfFiles[j]);
												System.out.println("Filename isss :" + listOfFiles[j].getName());

												// File existfile=listOfFiles[j];
												String oldFile = listOfFiles[j].toString();
												File oldfilePath = new File(oldFile);

												String pathForNewFile = saveDirectory + File.separator + existFolderName
														+ File.separator + existingFolderNewFileName;
												part.saveFile(pathForNewFile);
												File newFile = new File(pathForNewFile);

												System.out.println("qqqqq :" + oldfilePath + "rrrr :" + newFile);
												comparingWithExistingFile(oldfilePath, newFile);

												// } else {
												// 	System.out.println("Filenammmmee" + listOfFiles[j].getName());
												// 	System.out.println("Not Same");
												// }

											} else if (listOfFiles[j].isDirectory()) {
												System.out.println("Directory " + listOfFiles[j].getName());
											}
										}
										// }	
										System.out.println("filname :" + existingFolderNewFileName);
										existingFolderFilePath = saveDirectory + File.separator + existFolderName
												+ File.separator + existingFolderNewFileName;
										if (existingFolderNewFileName.endsWith("zip")) {
											// ZipInputStream zipIn = new ZipInputStream(new FileInputStream(filePath));
											System.out.println("zip");
											extractFile(existingFolderFilePath);

										} else {
											String existingFolderSaveFile=saveDirectory + File.separator + existFolderName
											+ File.separator + existingFolderNewFileName;
											part.saveFile(existingFolderSaveFile);
													readFile(existingFolderSaveFile);
										}
										// part.saveFile(
										// 		saveDirectory + File.separator + existFolderName + File.separator + fileName);
									} else {
										// this part may be the message content
										messageContent = part.getContent().toString();
									}
								}

								if (attachFiles.length() > 1) {
									attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
								}
							} else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
								Object content = message.getContent();
								if (content != null) {
									messageContent = content.toString();
								}
							}

						}

						System.out.println("\t Attachments: " + attachFiles);
						emailFolder.setFlags(messages, new Flags(Flags.Flag.SEEN), true);

						// } 
						// else {

						// 	System.out.println("your EmailId already Exists");
						// 	Candidate obj=service.getId(emailId);
						// 	System.out.println("exist no is :"+obj.getuniqueId());
						// }

					}
				}
			}

			// close the store and folder objects
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {

	//		String saveDirectory = "D:/Attachment";

	//		GetPOPMails receiver = new GetPOPMails();
	//        receiver.setSaveDirectory(saveDirectory);
	// getMails(POP_USER, POP_PASSWORD,saveDirectory);

	// private void createFolder(Long foldername, String fileName) {

	// }

	private Employees readFile(String savefile) throws IOException {
		System.out.println("read file ");
		InputStream ExcelFileToRead = new FileInputStream(savefile);
		XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
		
		XSSFWorkbook test = new XSSFWorkbook(); 
		
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFRow row; 
		XSSFCell cell;

		Iterator rows = sheet.rowIterator();
		// String[] a;
		ArrayList<String> details=new ArrayList<String>();
		ArrayList<Long> noDetails=new ArrayList<Long>();
		// Map<String, Employees> recordsMap = new <String, Employees>HashMap();
		
		while (rows.hasNext())
		{
			row=(XSSFRow) rows.next();
			Iterator cells = row.cellIterator();
			while (cells.hasNext())
			{
				cell=(XSSFCell) cells.next();
				// String[] cellss = new String[row.getPhysicalNumberOfCells()];
				// int i = 0;

				

				// HashMap<String, Employees> mapList = new HashMap<>();
				// System.out.println("no :"+cellss.toString());

				



				if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
				{
					// System.out.println("index :"+cell.getColumnIndex());
					// agencyName = cell.getStringCellValue();
					// status = cell.getStringCellValue();
					// name = cell.getStringCellValue();
					// System.out.print(cell.getStringCellValue()+" ");

					//  agencyName = cell.getStringCellValue();

					// mapList.putAll(cell.getStringCellValue());

					// String[] a =agencyName.split(":");
					// String q=a[0];

					// String w=a[1];
					// recordsMap.get(cell.getStringCellValue());

					details.add(cell.getStringCellValue());

					// System.out.println("q :" + q);
					//  System.out.println(agencyName );


					
					// System.out.println(emp.emailId);
				}

				
				else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
				{	
					// recordsMap.get(cell.getNumericCellValue());

					noDetails.add((long) cell.getNumericCellValue());

					// System.out.print(cell.getNumericCellValue()+" ");
				}
				else
				{
					//U Can Handel Boolean, Formula, Errors
				}
			}
			 
			System.out.println();
		}

		// System.out.println("map --"+recordsMap.get(name));
		emp.setAgencyName(details.get(1));
					emp.setStatus(details.get(3));
					emp.setName(details.get(5));
					emp.setQualification(details.get(7));
					emp.setPostForApply(details.get(9));
					// emp.setYearOfExperience(details.get(11));
					emp.setTechnologiesKnown(details.get(12));
					emp.setEmailId(details.get(14));
					  emp.setContactNo(noDetails.get(1));
					  emp.setYearOfExperience(noDetails.get(0));
	
		System.out.println("details -" +noDetails.get(0));
		System.out.println("emp "+emp.getQualification()+"--"+emp.getPostForApply()+"--"+emp.getStatus());			
		// service.addResumeDetails(emp);
				return emp;
		
		
	}

	private void comparingWithExistingFile(File file1, File file2) throws IOException {
		BufferedReader reader1 = new BufferedReader(new FileReader(file1));
		System.out.println("pathForNewFile" + file2);
		BufferedReader reader2 = new BufferedReader(new FileReader(file2));
		String line1 = reader1.readLine();

		String line2 = reader2.readLine();

		boolean areEqual = true;

		int lineNum = 1;

		while (line1 != null || line2 != null) {
			if (line1 == null || line2 == null) {
				areEqual = false;

				break;
			} else if (!line1.equalsIgnoreCase(line2)) {
				areEqual = false;

				break;
			}

			line1 = reader1.readLine();

			line2 = reader2.readLine();

			lineNum++;
		}

		if (areEqual) {
			System.out.println("Two files have same content.");
			System.out.println("file1 =" + file1);
			System.out.println("file2 =" + file2);
			FileUtils.forceDeleteOnExit(file2);
			
		} else {

			System.out.println("Two files have different content. They differ at line " + lineNum);

			System.out.println("File1 has " + line1 + " and File2 has " + line2 + " at line " + lineNum);
			System.out.println("file1 =" + file1);
			System.out.println("file2 =" + file2);
			// newFile.delete();
			String filePathToRead=file2.toString();
			Employees eobj=readFile(filePathToRead);
			service.updateResumeDetails(eobj);
			FileUtils.forceDeleteOnExit(file1);
			
		}

		reader1.close();

		reader2.close();

	}

	private void extractFile(String filePath) throws ZipException, IOException {

		int BUFFER = 2048;
		File files = new File(filePath);

		ZipFile zip = new ZipFile(files);
		String newPath = filePath.substring(0, filePath.length() - 4);

		new File(newPath).mkdir();
		Enumeration zipFileEntries = zip.entries();

		// Process each entry
		while (zipFileEntries.hasMoreElements()) {
			// grab a zip file entry
			ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
			String currentEntry = entry.getName();
			File destFile = new File(newPath, currentEntry);
			//destFile = new File(newPath, destFile.getName());
			File destinationParent = destFile.getParentFile();

			// create the parent directory structure if needed
			destinationParent.mkdirs();

			if (!entry.isDirectory()) {
				BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
				int currentByte;
				// establish buffer for writing file
				byte data[] = new byte[BUFFER];

				// write the current file to disk
				FileOutputStream fos = new FileOutputStream(destFile);
				BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

				// read and write until last byte is encountered
				while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, currentByte);
				}
				dest.flush();
				dest.close();
				is.close();

			}

			// if (currentEntry.endsWith(".zip"))
			// {
			// 	// found a zip file, try to open
			// 	extractFolder(destFile.getAbsolutePath());
			// }
			FileUtils.forceDeleteOnExit(files);
			// boolean deleted = files.delete();
			// System.out.println("result "+deleted);
		}

		// files.exists();

	}

	private boolean validateEmailId(String emailId) {
		return service.validateEmail(emailId);
	}

	@GetMapping("/get")
	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	@ResponseBody
	public void get() {
		getMails(POP_USER, POP_PASSWORD, saveDirectory);
	}

	// }

}