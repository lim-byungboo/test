package co.kr.uplus.mpayapimlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import co.kr.uplus.mpayapimlib.api.ApimApi_Sample;
import co.kr.uplus.mpayapimlib.api.res.ApimApi_SampleRes;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lbb1231
 *
 */
//@Slf4j
public class MpayApimLibApplication {

	/**사용예
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//------------------------
			//branch prj 에서 수정함......
			//------------------------
			
			
//			//********************************************************************************
//			//Apim 초기화(init) 처리는 Secrets Manager 로 부터 설정값을 취득한후 한번만 처리하면됨.
//			//********************************************************************************
//			String privateHost = "https://dev-openapi-pv.lguplus.co.kr";
//			int privatePort = 443;
//			String publicHost = "https://dev-openapi-pb.lguplus.co.kr";
//			int publicPort = 443;
//			boolean isLoadRunner = false;
//			Apim.init(privateHost, privatePort, publicHost, publicPort, isLoadRunner);
//			//********************************************************************************
//
//			
//			
//			//********************************************************************************
//			//각 로직단에서 하기와 같이 사용하면됨.
//			//********************************************************************************
//			ApimApi_Sample api = new ApimApi_Sample();
//			api.setParam1("param1");
//			api.setParam2("param2");
//			
//			ApimResponse rtn = Apim.getInstance().run(api);
//			
//			ApimApi_SampleRes res = (ApimApi_SampleRes) rtn;
//			log.info("return = {}", res.toString());
			
			edpFileRead();
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void edpFileRead() throws Exception {
		
		String file = "E:/001.Project/008.LG소액결젬마케팅/000.자료/L1BAT_APPSTORE_TADV_TXN_202202.csv";
		
		File f = new File(file);
		int cnt = 0;
		
		
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"))) {
			
			while(true) {
				System.out.println(br.readLine());
				cnt++;
				
				if(cnt > 10) {
					return;
				}
			}
			
		}
		
	}
}
