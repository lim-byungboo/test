import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @author lbb1231
 *
 */
public class EdpRead {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		String s = "LGXXXX0002_20220205051321785102525818861|02|202202|OR000659|358원보|과금|88|원스토어|0188|통합스토어폰빌|72F08636699152CA9C7A53B1F1D8CAD48EF368BE|E90ED161DF987CC6AAF3971B6E8BD493FD436876|6E6B64F6E1038F720A093B6B88C0C1CC9F1104AA|9562BA72F3021D2F6BA079A25F15DDE3996341B7|8A1DEAD106B01879A166228C5EEF5CE0694F995A||LZP0000001|LPZ0000409|5G 프리미어 에센셜|20190930|SM-G998N|40|FEMALE|311326|주식회사 골드스타|0|5900|0|5310|2022-02-05 05:13:40|2022-02-05 05:13:42|Hong Kong Chujian East Asia Network Technology Company Limited||||ONE pay 내통장결제|0910258555|Y2|2022-02-06 03:21:31|BDPADM";
//		//s = "a|^|b|^|c|^|d|^|e";
//
//		System.out.println("separator = "+ args[0]);
//		
//		//String[] ary = s.split("[" + args[0] + "]");
//		char[] c = args[0].toCharArray();
//		String sep = "";
//		for(char c1 : c) {
//			sep = sep + "\\" + c1;
//		}
//		
//		System.out.println("sep = " + sep);
//		
//		
//		
//		String[] ary = s.split(sep);
//		System.out.println("size = " + ary.length);
//		
//		for(String a : ary) {
//			System.out.println(a);
//		}
		
		
		if(args == null || args.length < 4) {
			System.out.println("------------------------------------------------------------");
			System.out.println("-- USAGE ---------------------------------------------------");
			System.out.println("ex] java EdpRead [filepath] [separator] [readcount] [charset]");
			System.out.println("[filepath] = /efs/edp/yyyy/mm/xxxxx.csv");
			System.out.println("[separator] = |^|");
			System.out.println("[readcount] = 0 --> no limit");
			System.out.println("[charset] = utf-8 , euc-kr");
			System.out.println("java EdpRead /efs/edp/2022/2/L1BAT_APPSTORE_TADV_TXN_202202.csv '|^|' 0 utf-8");
			System.out.println("------------------------------------------------------------");
			System.exit(1);
		}

		long start = System.currentTimeMillis();
		
		//------------------------------------------
		//input parameter
		//------------------------------------------
		String filepath = args[0];
		
		char[] c = args[1].toCharArray();
		String separator = "";
		for(char c1 : c) {
			separator = separator + "\\" + c1;
		}

		int readcount = Integer.parseInt(args[2]);
		String charset = args[3];
		//------------------------------------------
		
		System.out.println("[INPUT] filepath = " + filepath);
		System.out.println("[INPUT] separator = " + args[1]);
		System.out.println("[INPUT] readcount = " + readcount);
		System.out.println("[INPUT] charset = " + charset);
		System.out.println("--------------------------------------------------------------");
		
		File f = new File(filepath);
		int cnt = 0;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), charset))) {
			
			int first_columnSize = 0;
			
			while(true) {
				String data = br.readLine();
				if(data == null) {
					break;
				}
				cnt++;
				
				String[] splitData = data.split(separator);
				
				//첫번째 행의 컬럼 사이즈
				if(cnt == 1) {
					first_columnSize = splitData.length;
					System.out.println(">> Column Size = " + first_columnSize);
				}
				else if(first_columnSize != splitData.length) {
					System.out.println(String.format("Column Size Error [%d, %d] : line [%03d] : %s", first_columnSize, splitData.length, cnt, data));
				}
				
				if(readcount > 0 && cnt >= readcount) {
					break;
				}
			}
			long end = System.currentTimeMillis();
			System.out.println(String.format("============================= END File Data Output [row count = %d] [time = %d] =============================", cnt, (end - start)));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
}
