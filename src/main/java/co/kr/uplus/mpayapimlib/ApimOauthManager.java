/**
 * 
 */
package co.kr.uplus.mpayapimlib;

import java.util.Timer;
import java.util.TimerTask;

import co.kr.uplus.mpayapimlib.api.ApimApi_Oauth;
import co.kr.uplus.mpayapimlib.api.res.ApimApi_OauthRes;

/**
 * @author lbb1231
 *
 */
public class ApimOauthManager {

	/**
	 * instance
	 */
	private static ApimOauthManager _instance = null;
	
	/**
	 * 인증 받은 값
	 */
	private ApimApi_OauthRes oauth = null;
	
	/**
	 * timer
	 */
	private Timer timer = null;
	
	/**
	 * timer task
	 */
	private TimerTask task = null;
	
	/**
	 * 토큰 만료전에 토큰 값을 refresh 함. (만료시간 30분전) 
	 */
	private long token_expire_time = 1000 * 60 * 30;
	
	/**초기화
	 * @param isLoadRunner
	 */
	public static void init() {
		if(_instance == null) {
			_instance = new ApimOauthManager();
		}
	}

	/**
	 * @return
	 */
	public static ApimOauthManager getInstance() {
		return _instance;
	}
	
	/**
	 * constructor
	 */
	private ApimOauthManager() {
		startAccessTokenCheck();
	}
	
	/**
	 * task start
	 * 1분에 한번씩 토큰 만료 시간 체크후 만료시에 토큰 재발급 처리를 함. 
	 */
	private void startAccessTokenCheck() {
		
		//task 
		task = new TimerTask() {
			@Override
			public void run() {
				if(isExpiredToken() == true) {
					Apim apim = Apim.getInstance();
					ApimApi api = new ApimApi_Oauth();
					oauth = (ApimApi_OauthRes)apim.run(api);
					return;
				}
			}
		};

		//task period time (1분)
		final int cycleSec = 60 * 1000;
		timer = new Timer(true);
		timer.schedule(task, 1000, cycleSec);
	}
	
	/**token 만료시간 체크
	 * @return
	 */
	private boolean isExpiredToken() {
		if(oauth == null ) {
			return true;
		}
		//현재시간
		long now = System.currentTimeMillis();
		
		//인증받은 token 의 만료 시간
		long expire_in = oauth.getExpires_in_long();
		
		//만료 30분전 인지 체크
		if(expire_in - now < token_expire_time) {
			return true;
		}
		
		return false;
	}

	/**access token
	 * @param isLoadRunner
	 * @return
	 */
	public String getAccessToken() {
		if(oauth != null) {
			return oauth.getAccess_token();
		}
		return null;
	}
}
