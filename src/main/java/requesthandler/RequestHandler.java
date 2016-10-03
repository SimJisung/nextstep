/**
 * 
 */
package requesthandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
			byte[] body =  "HelloWorld".getBytes();
			
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
