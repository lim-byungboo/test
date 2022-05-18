/**
 * 
 */
package co.kr.uplus.mpayapimlib.api.res;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.kr.uplus.mpayapimlib.ApimResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**oauth2.0 인증 Response
 * @author lbb1231
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString
public class ApimApi_OauthRes extends ApimResponse {

	/**
	 * 토큰유형 (일반적인경우 Bearer)
	 */
	@JsonProperty("token_type")
	private String token_type;
	
	/**
	 * 권한부여 서버에서 발급한 리소스 서버에 접근 권한 확인용 문자열
	 */
	@JsonProperty("access_token")
	private String access_token;
	
	/**
	 * access_token 만료 시간
	 */
	@JsonProperty("expires_in")
	private String expires_in;
	
	/**만료시간 long type
	 * @return
	 */
	public long getExpires_in_long() {
		if(StringUtils.isBlank(expires_in) == true) {
			return 0;
		}
		try {
			return Long.parseLong(expires_in);
		}
		catch(Exception e) {
			return 0;
		}
	}
	
	/**
	 * 선택동의 시간 (토큰발행시간, timestame 정수형)
	 */
	@JsonProperty("consented_on")
	private String consented_on;
	
	/**
	 * 사용하고자 하는 api 의 scope
	 */
	@JsonProperty("scope")
	private String scope;
	
	/**
	 * oauth2.0 유형
	 */
	@JsonProperty("grant_type")
	private String grant_type;
	
	/**
	 * access_token 이 만료 되었을 때 새로운 token 을 발급받기위한 token
	 */
	@JsonProperty("refresh_token")
	private String refresh_token;
	
	/**
	 * refresh_token 만료 시간
	 */
	@JsonProperty("refresh_token_expires_in")
	private String refresh_token_expires_in;
	
}
