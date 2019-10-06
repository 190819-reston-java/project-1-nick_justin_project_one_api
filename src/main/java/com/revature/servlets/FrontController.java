package com.revature.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.model.Employee;
import com.revature.model.EmployeeId;
import com.revature.model.LoginInfo;
import com.revature.model.Reimbursement;
import com.revature.model.UpdateInfo;
import com.revature.model.UpdateReimbursement;
import com.revature.repository.EmployeeDAOImplPJDBC;
import com.revature.repository.ReimbursementDAOImplPJDBC;
import com.revature.service.EmployeeService;
import com.revature.service.ReimbursementService;

@MultipartConfig
public class FrontController extends HttpServlet {
	
	private EmployeeService employeeService;
	private ReimbursementService reimbursementService;
	
	@Override
	public void init() throws ServletException {
		employeeService = new EmployeeService(new EmployeeDAOImplPJDBC());
		reimbursementService = new ReimbursementService(new ReimbursementDAOImplPJDBC());
		super.init();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String[] splitURI = req.getRequestURI().split("/");
		String[] tokens = Arrays.copyOfRange(splitURI, 3, splitURI.length);
		switch(tokens[0]) {
		case "employee-verification":
			employeeVerification(req, resp);
			break;
		case "employee-view-info":
			employeeViewInfo(req, resp);
			break;
		case "employee-update-info":
			employeeUpdateInfo(req, resp);
			break;
		case "reimbursement":
			handleReimbursement(req, resp, tokens);
			break;
		case "view-employees":
			viewAllEmployees(req, resp);
			break;
		case "update-reimbursement":
			updateReimbursement(req, resp);
			break;
		case "manager-verification":
			managerVerification(req, resp);
			break;
		case "employee-reimbursements":
			employeeReimbursements(req, resp);
			break;
		case "upload-receipt":
			manageReceipts(req, resp);
		case "logout":
			logout(req, resp);
			break;
		}
	}

	private void employeeVerification(HttpServletRequest req, HttpServletResponse resp)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		LoginInfo loginInfo = om.readValue(req.getReader(), LoginInfo.class);
		HttpSession session = req.getSession();
		
		if (employeeService.getEmployee(loginInfo.getUsername()).getPassword().equals(loginInfo.getPassword())) {
			Employee employee = employeeService.getEmployee(loginInfo.getUsername());
			session.setAttribute("employee", employee);
		} else {
			resp.sendError(400, "Incorrect username or password");
		}
	}

	private void employeeViewInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ObjectMapper om = new ObjectMapper();
		PrintWriter pw = resp.getWriter();
		HttpSession session = req.getSession();
		String employeeInfo = om.writeValueAsString(session.getAttribute("employee"));
		pw.write(employeeInfo);
	}
	
	private void employeeUpdateInfo(HttpServletRequest req, HttpServletResponse resp)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		HttpSession session = req.getSession();
		UpdateInfo updateInfo = om.readValue(req.getReader(), UpdateInfo.class);
		Employee oldEmployee = (Employee) session.getAttribute("employee");
		Employee newEmployee = new Employee(
				oldEmployee.getId(),
				oldEmployee.getUsername(),
				oldEmployee.getPassword(),
				updateInfo.getFirstName(),
				updateInfo.getLastName(),
				updateInfo.getEmail(),
				updateInfo.getAddress(),
				oldEmployee.getCompanyPosition());
		
		if(employeeService.updateEmployee(newEmployee)) {
			session.setAttribute("employee", newEmployee);
		} else {
			resp.sendError(400, "Failed to create player");
		}
	}
	
	private void handleReimbursement(HttpServletRequest req, HttpServletResponse resp, String[] tokens) throws IOException {
		ObjectMapper om = new ObjectMapper();
		PrintWriter pw = resp.getWriter();
		HttpSession session = req.getSession();
		
		Reimbursement reimbursement = null;
		Employee employee = null;
				
		switch (req.getMethod()) {
		case "GET":
			employee = (Employee) session.getAttribute("employee");

			if (tokens.length == 1) {
				String jsonReimbursements = om.writeValueAsString(reimbursementService.getReimbursements());
				pw.write(jsonReimbursements);
			} else {
				String jsonReimbursements = om.writeValueAsString(reimbursementService.getReimbursementsForEmployee(employeeService.getEmployee(tokens[(int) employee.getId()])));
				pw.write(jsonReimbursements);
				
			}
			break;
		case "POST":
			reimbursement = om.readValue(req.getReader(), Reimbursement.class);
			employee = om.readValue(req.getReader(), Employee.class);
			
			if (!reimbursementService.createReimbursement(employee, reimbursement)) {
				resp.sendError(400, "Failed to create reimbursement");
			} else {
				pw.write("Successful submitted reimbursement!");
			}
			break;
		default:
			break;
		}
		
	}

	private void logout(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		session.invalidate();
	}

	private void managerVerification(HttpServletRequest req, HttpServletResponse resp)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		LoginInfo loginInfo = om.readValue(req.getReader(), LoginInfo.class);
		HttpSession session = req.getSession();
		
		if (employeeService.getEmployee(loginInfo.getUsername()).getCompanyPosition().equals("manager")) {
			if (employeeService.getEmployee(loginInfo.getUsername()).getPassword().equals(loginInfo.getPassword())) {
				Employee employee = employeeService.getEmployee(loginInfo.getUsername());
				session.setAttribute("employee", employee);
			} else {
				resp.sendError(400, "Incorrect username or password");
			}
		} else {
			resp.sendError(403, "You are not a manager. You do not have access to this content");
		}
	}

	private void viewAllEmployees(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ObjectMapper om = new ObjectMapper();
		PrintWriter pw = resp.getWriter();
		String jsonEmployees = om.writeValueAsString(employeeService.getEmployees());
		pw.write(jsonEmployees);
	}

	private void updateReimbursement(HttpServletRequest req, HttpServletResponse resp)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		HttpSession session = req.getSession();
		Employee manager = (Employee) session.getAttribute("employee");
		UpdateReimbursement updateReim = om.readValue(req.getReader(), UpdateReimbursement.class);
		Reimbursement oldReim = reimbursementService.getReimbursement(updateReim.getId());
		
		if (oldReim.getStatus().equals("PENDING")) {
			Reimbursement newReim = new Reimbursement(
					oldReim.getId(),
					oldReim.getEmployeeId(),
					oldReim.getAmount(),
					oldReim.getSqlDate(),
					updateReim.getStatus(),
					manager.getFirstName() + " " + manager.getLastName()
					);
			
			if (reimbursementService.updateReimbursement(newReim)) {
				// do nothing?
			} else {
				resp.sendError(400, "Failed to update reimbursement request");
			}
		} else {
			resp.sendError(400, "Reimbursement is not pending");
		}
	}

	private void employeeReimbursements(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ObjectMapper om = new ObjectMapper();
		ObjectMapper mapper = new ObjectMapper();
		EmployeeId employeeId = om.readValue(req.getReader(), EmployeeId.class);
		PrintWriter pw = resp.getWriter();
		Employee employee = employeeService.getEmployee(Long.toString(employeeId.getId()));
		String jsonEmployeeReimbursements = mapper.writeValueAsString(reimbursementService.getReimbursementsForEmployee(employee));
		pw.write(jsonEmployeeReimbursements);
	}

	private void manageReceipts(HttpServletRequest req, HttpServletResponse resp)
			throws JsonParseException, JsonMappingException, IOException, ServletException {
		HttpSession session = req.getSession();
		Employee e = (Employee) session.getAttribute("employee");
		Date date= new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		
		String amount = req.getParameter("amount");
		Part filePart = req.getPart("receipt");
		InputStream fileContent = filePart.getInputStream();
		String fileName = Long.toString(e.getId()) + ts;
//		File receipt = new File("");
//		FileUtils.copyInputStreamToFile(fileContent, receipt);
		
//		BasicAWSCredentials credentials = new BasicAWSCredentials("SCREW YOU JOHN");
//		Amazon s3client = new AmazonS3Client(credentials);
		
//		AWSCredentials credentials = new BasicAWSCredentials("JOHN SUCKS");
//		AmazonS3 s3client = AmazonS3ClientBuilder
//				.standard()
//				.withCredentials(new AWSStaticCredentialsProvider(credentials))
//				.withRegion(Regions.US_EAST_1)
//				.build();
//		
//		String bucketName = "project-1-receipts";
		
//		
//		ObjectMetadata omd = new ObjectMetadata();
//		s3client.putObject(new PutObjectRequest(bucketName, fileName, receipt)
//				.withCannedAcl(CannedAccessControlList.PublicRead));
	}
}
