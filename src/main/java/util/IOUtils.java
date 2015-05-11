package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IOUtils {
	/**
	 * @param BufferedReader는 Request Body를 시작하는 시점이어야 
	 * @param contentLength는 Request Header의 Content-Length 값이다.
	 * @return
	 * @throws IOException
	 */
	public static String readData(BufferedReader br, int contentLength) throws IOException {
		char[] body = new char[contentLength];
		br.read(body, 0, contentLength);
		return String.copyValueOf(body);
	}
	
	public static String getMethod(String line) {
		String[] tokens = line.split(" ");
		return tokens[0];
	}
	
	public static Map<String, String> getURI(String line) {
		Map<String, String> res = new HashMap<String, String>();
		String[] tokens = line.split(" ");
		
		String[] tokens2 = tokens[1].split("\\?");
		
		res.put("uri", tokens[1]);
		res.put("filename", tokens2[0]);
		if(tokens2.length > 1) {
			res.put("params", tokens2[1]);
		}
		return res;
	}
}
