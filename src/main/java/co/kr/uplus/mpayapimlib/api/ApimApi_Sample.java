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
import co.kr.uplus.mpayapimlib.ApimOauthManager;
import co.kr.uplus.mpayapimlib.ApimResponse;
import co.kr.uplus.mpayapimlib.api.res.ApimApi_SampleRes;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**sample api
 * @author lbb1231
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ApimApi_Sample extends ApimApi{
	
	/**
	 * sub url
	 */
	private static final String SUB_URL = "/uplus/intuser/sample/api1";
	
	/**
	 * param1
	 */
	private String param1;
	
	/**
	 * param2
	 */
	private String param2;

	/**
	 *
	 */
	@Override
	public HttpRequestBase getHttpRequest() throws Exception {
		HttpPost request = new HttpPost();
		
		//헤더 설정
		request.addHeader("Content_Type", "application/x-www-form-urlencoded");
		
		List<NameValuePair> param = new ArrayList<>();
		
		//oauth2.0 access token 설정
		param.add(new BasicNameValuePair("access_token", ApimOauthManager.getInstance().getAccessToken()));
		param.add(new BasicNameValuePair("param1", param1));
		param.add(new BasicNameValuePair("param2", param2));
		
		//post 파라미터 설정
		request.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
		return request;
	}
	
	/**
	 *
	 */
	@Override
	public ApimResponse loadrunnerResult() {
		ApimApi_SampleRes res = new ApimApi_SampleRes();
		res.setResult("test");
		res.setRes_data1("data");
		return res;
	}
	
	/**
	 *
	 */
	@Override
	public String getSubUrl() {
		return ApimApi_Sample.SUB_URL;
	}

	/**
	 *
	 */
	@Override
	public Class<?> getResultClass() {
		return ApimApi_SampleRes.class;
	}
	
}
