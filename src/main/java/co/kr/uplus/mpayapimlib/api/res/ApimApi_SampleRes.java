/**
 * 
 */
package co.kr.uplus.mpayapimlib.api.res;

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
public class ApimApi_SampleRes extends ApimResponse {

	/**
	 * result
	 */
	@JsonProperty("result")
	private String result;
	
	/**
	 * res_data
	 */
	@JsonProperty("res_data1")
	private String res_data1;
	
}
