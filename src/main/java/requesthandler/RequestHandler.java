/**
 * 
 */
package requesthandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author joshuasim
 *
 */
public class RequestHandler extends Thread{
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private Socket connection;
	
	public RequestHandler(Socket connectionSocket){
		this.connection = connectionSocket;
	}
	
	public void run(){
		log.debug("New Client Connect. \n Connected IP : {} , PORT : {} " , connection.getInetAddress() , connection.getPort());
		try(InputStream in = connection.getInputStream()){
			OutputStream out = connection.getOutputStream();
			
			
			//TODO 사용자 요청에 대한 처리는 이곳에서 구현한다. 
			DataOutputStream dos = new DataOutputStream(out);
		
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			
			String line = br.readLine(); 
			
			if(line == null){
				return; 
			}
			
			String getPath = line.split(" ")[1];
			/*
			while(!line.equals("")){
				log.info("header : {}",line);
				line = br.readLine();
			}
			*/
			
			while(!"".equals(line)){
				log.info("header : {}",line);
				line = br.readLine();
			}
		
			byte[] body = Files.readAllBytes(new File("./webapp"+getPath).toPath());
			
			response200header(dos, body.length);
			responseBody(dos, body);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void response200header(DataOutputStream dos, int lengthOfBodyContent){
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: "+lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		
	}
	
	private void responseBody(DataOutputStream dos, byte[] body){
		try {
			dos.write(body, 0 , body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		
	}
	
}
