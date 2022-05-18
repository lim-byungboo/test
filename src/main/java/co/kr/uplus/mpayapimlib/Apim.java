/**
 * 
 */
package co.kr.uplus.mpayapimlib;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * apim 연동 모듈
 * @author lbb1231
 *
 */
@Slf4j
public class Apim {

	/**
	 * singleton instance
	 */
	private static Apim _instance = null;

	/**
	 * ObjectMapper 
	 */
	private ObjectMapper om = new ObjectMapper();

	/**
	 * connectionManager
	 */
	private PoolingHttpClientConnectionManager connectionManager = null;
	
	/**
	 * timer
	 */
	private Timer timer = null;
	
	/**
	 * timer task
	 */
	private TimerTask task = null;	

	/**
	 * Apim Host
	 */
	private String privateHost = "https://dev-openapi-pv.lguplus.co.kr";
	
	/**
	 * Apim Port
	 */
	private int privatePort = 443;
	
	/**
	 * Apim Host
	 */
	private String publicHost = "https://dev-openapi-pb.lguplus.co.kr";
	
	/**
	 * Apim Port
	 */
	private int publicPort = 443;
	
	/**
	 * loadrunner 테스중?
	 */
	private boolean isLoadRunner = false;
	
	/**Apim 초기화 (app 기동시 최초 한번만 실행)
	 * @param host
	 * @param port
	 */
	public static void init(String privateHost, int privatePort, String publicHost, int publicPort, boolean isLoadRunner) {
		if(_instance == null) {
			_instance = new Apim(privateHost, privatePort, publicHost, publicPort, isLoadRunner);
			
			
			//------------------------
			//branch prj 에서 수정함......
			//------------------------

			
			//인증 토크 관리 객체 init
			ApimOauthManager.init();
		}
	}
	
	/**
	 * get instance
	 * 
	 * @return
	 */
	public static Apim getInstance() {
		return _instance;
	}

	/**constructor (singleton)
	 * @param privateHost
	 * @param privatePort
	 * @param publicHost
	 * @param publicPort
	 * @param isLoadRunner
	 */
	private Apim(String privateHost, int privatePort, String publicHost, int publicPort, boolean isLoadRunner) {
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(20);
		connectionManager.setDefaultMaxPerRoute(10);
		
		this.privateHost = privateHost;
		this.privatePort = privatePort;
		this.publicHost = publicHost;
		this.publicPort = publicPort;
		this.isLoadRunner = isLoadRunner;
		
		connPoolCleanTask();
	}

	/**
	 * task start
	 * 30초에 한번씩 사용하지 않는 connection을 정리한다 
	 */
	private void connPoolCleanTask() {
		
		//idle connection close time
		final int IDLE_TIMEOUT = 30 * 1000;
		
		//task period time
		final int cycleSec = 30 * 1000;
		
		//task 
		task = new TimerTask() {
			@Override
			public void run() {
				connectionManager.closeExpiredConnections();
				connectionManager.closeIdleConnections(IDLE_TIMEOUT, TimeUnit.MILLISECONDS);
			}
		};

		//30초 주기
		timer = new Timer(true);
		timer.schedule(task, cycleSec, cycleSec);
	}

	/**
	 * httpClient 생성
	 * @return HttpClient
	 */
	private synchronized HttpClient getHttpClient() {
		return HttpClientBuilder.create().setConnectionManager(connectionManager).build();
	}
	
	/**
	 * @param api
	 * @param isLoadRunner
	 * @return
	 */
	public ApimResponse run(ApimApi api) {
		
		//loadrunner 테스트시 
		if(isLoadRunner == true) {
			return api.loadrunnerResult();
		}
		
		ApimResponse res = null;
		try {
			res = execute(privateHost, privatePort, api);
		}
		catch(ApimiException e) {
			if(e.isFailBack() == true) {
				try {
					res = execute(publicHost, publicPort, api);
				}
				catch(ApimiException apimE) {}
			}
		}
		
		return res;
	}
	
	/**
	 * @param host
	 * @param port
	 * @param api
	 * @return
	 * @throws Exception
	 */
	private ApimResponse execute(String host, int port, ApimApi api) throws ApimiException {
		
		HttpRequestBase req = null;
		CloseableHttpResponse res = null;
		ApimResponse result = null;

		try {
			req = api.getHttpRequest();
			
			String url = String.format("%s:%d%s", host, port, api.getSubUrl());
			log.debug(">>> apim url : {}", url);
			req.setURI(new URI(url));
			
			HttpClient httpClient = this.getHttpClient();
			res = (CloseableHttpResponse) httpClient.execute(req);
			
			int statusCode = res.getStatusLine().getStatusCode();
			log.debug(">>> apim statusCode : {}", statusCode);
			
			//http_status == 200 의 경우  
			if(statusCode == HttpStatus.SC_OK) {
				ResponseHandler<String> handler = new BasicResponseHandler();
		        String jsonResult = handler.handleResponse(res);
		        log.debug(">>> apim response : {}", jsonResult);
		        result = (ApimResponse)om.readValue(jsonResult, api.getResultClass());
			}
			else {
				throw new Exception(String.format("HTTP STATUS ERROR [%d]", statusCode));
			}
			
			return result;
		}
		//json mapping 에러 (서버 처리는 성공)
		catch(JsonProcessingException e) {
			log.error("occured JsonProcessingException", e);
			throw new ApimiException(false, e);
		}
		//서버 에러
		catch(Exception e) {
			log.error("occured Exception", e);
			if (req != null && req.isAborted() == false) {
				req.abort();
			}
			throw new ApimiException(true, e);
		}
		finally {
			//connection pool 리소스 반납
			if (res != null && res.getEntity() != null) {
				EntityUtils.consumeQuietly(res.getEntity());
			}
		}
	}
	
}
