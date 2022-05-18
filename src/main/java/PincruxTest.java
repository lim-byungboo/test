import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */

/**
 * @author lbb1231
 *
 */
public class PincruxTest {

	/**
	 * AES256 key (32byte)
	 */
	private static final String SECRET_KEY = "MPAY_PINCRUX_SECRET_KEY_!@#$1234"; 
	
	/**
	 * AES256 IV (16byte)
	 */
	private static final String IV = "MPAY_PINCRUX_IV_";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Map<String, Object> m = new HashMap<>();
			
			m.put("usrkey", "1234567890");
			m.put("transid_pub", UUID.randomUUID().toString());

			List<Map<String, String>> list = new ArrayList<>();
			m.put("item_list", list);
			
			for(int i=0; i<5; i++) {
				Map<String, String> item = new HashMap<>();
				item.put("appkey", String.format("%06d", i+1));
				list.add(item);
			}
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = mapper.writeValueAsString(m);
			
			System.out.println("json string = " + jsonStr);
			
			String encryptString = encrypt(jsonStr); 
			System.out.println("encrypt = " + encryptString);
			
			String decryptString = decrypt(encryptString);
			System.out.println("decrypt = " + decryptString);
			
			//----------------------------------------------------------------------
			//----------------------------------------------------------------------
			String url = "https://randing.pincrux.com/lgu/mobile.pin";
			
			String pubkey = "123456";
			String ed = encryptString;

			System.out.println("------------------------------------------------------------");
			System.out.println(String.format("Redirect URL : %s?pubkey=%s&ed=%s", url, pubkey, ed));
			System.out.println("------------------------------------------------------------");
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**AES256 암호화 --> base64 인코딩 --> url 인코딩 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String str) throws Exception {

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		String baseStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
		return URLEncoder.encode(baseStr, "utf-8");
	}

	/**url 디코딩 --> base64 디코딩 --> AES256 복호화
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String aesstr) throws Exception {
		String str = URLDecoder.decode(aesstr, "utf-8");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

		return new String(cipher.doFinal(Base64.getDecoder().decode(str)), "UTF-8");
	}
}
