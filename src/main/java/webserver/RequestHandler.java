package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import model.User;
import model.UserDatabase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(reader);
			log.debug("---- http request ----");
			String firstLine = br.readLine();			
			String headerLine = firstLine;
			int contentLength = 0;
			
			if(firstLine == null) {
				return;
			}
			
			while(!"".equals(headerLine)) {
				if(headerLine.startsWith("Content-Length")){
					contentLength = IOUtils.getContentLength(headerLine);
				}
				headerLine = br.readLine();
				//log.info(lastLine);
			}
			
			Map<String, String> map = IOUtils.getURI(firstLine);
			String method = IOUtils.getMethod(firstLine);
			String uri =  map.get("uri");
			String filename = map.get("filename");
			String params = map.get("params");
			log.debug("method: {}, uri: {}, filename: {}, params: {}", method, uri , filename, params);
			
			if(filename.equals("/")){
				log.debug("filename is empty.default filename is index.html");
				filename += "index.html";
			}
			
			else if(filename.equals("/create")){
				Map<String, String> parameters = null;
				if(method.equals("GET")){
					parameters = HttpRequestUtils.parseQueryString(params);
				}
				else if (method.equals("POST")){
					parameters = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
				}
				log.debug("userId: {}", parameters.get("userId"));
				User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
				log.debug(user.toString());
				UserDatabase.putUser(user);
				filename = "/index.html";
			}	
	
			byte[] body = Files.readAllBytes(new File("./webapp" + filename).toPath());
			DataOutputStream dos = new DataOutputStream(out);
			//byte[] body = "Hello World".getBytes();
			response200Header(dos, body.length);
			responseBody(dos, body);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 Document Follows \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
