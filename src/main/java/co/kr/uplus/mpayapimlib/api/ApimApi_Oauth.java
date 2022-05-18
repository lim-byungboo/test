/**
 * 
 */
package co.kr.uplus.mpayapimlib.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;

import co.kr.uplus.mpayapimlib.ApimApi;
import co.kr.uplus.mpayapimlib.ApimResponse;
import co.kr.uplus.mpayapimlib.api.res.ApimApi_OauthRes;
import lombok.Getter;

/**oauth2.0 인증 (client credeantials)
 * https://도메인정보/사용자카타로그/oauth2/token
 * 
 * [도메인]
 * 개발 
 * --- 내부 public  :dev-openapi-pb.lguplus.co.kr
 * --- 내부 private :dev-openapi-pv.lguplus.co.kr
 * 검수(통시) 
 * --- 내부 public  :tst-openapi-pb.lguplus.co.kr
 * --- 내부 private :tst-openapi-pv.lguplus.co.kr
 * 운영
 * --- 내부 public  :openapi-pb.lguplus.co.kr
 * --- 내부 private :openapi-pv.lguplus.co.kr
 * 
 * [사용자카타로그]
 * 내부사용자 : uplus/intuser
 * 외부사용자 : uplus/extuser
 *  
 * @author lbb1231
 *
 */
public class ApimApi_Oauth extends ApimApi{
	
	/**
	 * sub url
	 */
	private static final String SUB_URL = "/uplus/intuser/oauth2/token";

	/**apim oauth 인증 헤더
	 * @author lbb1231
	 *
	 */
	public enum OAUTH_PARAM {
		 GRANT_TYPE("client_credentials")
		,CLIENT_ID("XXXX")
		,CLIENT_CREDENTIALS("XXX")
		,SCOPE("CM")
		;

		@Getter
		private String value;

		private OAUTH_PARAM(String value) {
			this.value = value;
		}
	}
	
	/**
	 *
	 */
	@Override
	public HttpRequestBase getHttpRequest() throws Exception {
		HttpPost request = new HttpPost();
		
		//헤더 설정
		request.addHeader("Content_Type", "application/x-www-form-urlencoded");
		
		List<NameValuePair> param = new ArrayList<>();
		param.add(new BasicNameValuePair("grant_type", OAUTH_PARAM.GRANT_TYPE.getValue()));
		param.add(new BasicNameValuePair("client_id", OAUTH_PARAM.CLIENT_ID.getValue()));
		param.add(new BasicNameValuePair("client_secret", OAUTH_PARAM.CLIENT_CREDENTIALS.getValue()));
		param.add(new BasicNameValuePair("scope", OAUTH_PARAM.SCOPE.getValue()));
		
		//post 파라미터 설정
		request.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
		return request;
	}
	
	/**
	 *
	 */
	@Override
	public ApimResponse loadrunnerResult() {
		ApimApi_OauthRes res = new ApimApi_OauthRes();
		res.setToken_type("Bearer");
		res.setAccess_token("");
		res.setExpires_in("");
		res.setConsented_on("");
		res.setScope("");
		res.setGrant_type(OAUTH_PARAM.GRANT_TYPE.getValue());
		res.setRefresh_token("");
		res.setRefresh_token_expires_in("");
		return res;
	}
	
	/**
	 *
	 */
	@Override
	public String getSubUrl() {
		return ApimApi_Oauth.SUB_URL;
	}

	/**
	 *
	 */
	@Override
	public Class<?> getResultClass() {
		return ApimApi_OauthRes.class;
	}
	
}
