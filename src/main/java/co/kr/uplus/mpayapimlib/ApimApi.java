/**
 * 
 */
package co.kr.uplus.mpayapimlib;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author lbb1231
 *
 */
public abstract class ApimApi {

	/**sub url
	 * @return
	 */
	public abstract String getSubUrl();
	
	/**request HttpGet, HttpPost
	 * @return
	 * @throws Exception
	 */
	public abstract HttpRequestBase getHttpRequest() throws Exception;
	
	/**처리 결과
	 * @return
	 */
	public abstract Class<?> getResultClass();
	
	/**test 용
	 * @return
	 */
	public abstract ApimResponse loadrunnerResult(); 
}
