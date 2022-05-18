/**
 * 
 */
package co.kr.uplus.mpayapimlib;

import lombok.Getter;

/**
 * @author lbb1231
 *
 */
public class ApimiException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * failback
	 */
	@Getter
	private boolean isFailBack;
	
	/**
	 * @param isFailBack
	 * @param e
	 */
	public ApimiException(boolean isFailBack, Exception e) {
		super(e);
		this.isFailBack = isFailBack;
	}
}
