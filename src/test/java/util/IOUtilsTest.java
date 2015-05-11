package util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtilsTest {
	private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);
	
	@Test
	public void readData() throws Exception {
		String data = "abcd123";
		StringReader sr = new StringReader(data);
		BufferedReader br = new BufferedReader(sr);
		
		logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
	}
	
	@Test
	public void getUri() {
		String line = "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
		Map<String, String> map = IOUtils.getURI(line);
		logger.debug("uri: {}, filename: {}, params: {}", map.get("uri"),map.get("filename"), map.get("params"));
	}
	
	@Test 
	public void getUriWithNoParams() {
		String line = "GET /create HTTP/1.1";
		Map<String, String> map = IOUtils.getURI(line);
		logger.debug("uri: {}, filename: {}, params: {}", map.get("uri"),map.get("filename"), map.get("params"));
	}
}
