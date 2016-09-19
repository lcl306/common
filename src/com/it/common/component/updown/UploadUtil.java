package com.it.common.component.updown;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class UploadUtil {
	

	/**
	 * get array of string defined by intArray
	 * @param  code: Shift_JIS  GBK  UTF-8
	 * */
	public static String[] getText(String line, int[] intArray, String code) {
		try {
			int arrLen = intArray.length;
			String[] dataArray = new String[arrLen];
			byte[] lineByte = line.getBytes(code);
			int start = 0;
			for (int i = 0; i < arrLen; i++) {
				dataArray[i] = new String(lineByte, start, intArray[i],code);
				start += intArray[i];
			}
			return dataArray;
		} catch (Exception e) {
			return null;
		}
	}
	public static String[] getText(String line, int[] intArray) {
		return getText(line,intArray,GlobalName.CODE);
	}
	/**
	 * change a line string to csv data array
	 * @param  code: Shift_JIS  GBK  UTF-8
	 * @return
	 */
	public static String[] getCsvData(String lineStr,String code){
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(lineStr.getBytes(code)); 
			CsvReader reader = new CsvReader(is, ',', Charset.forName(code));
			while (reader.readRecord()) { 
				return reader.getValues(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String[] getCsvData(String lineStr){
		return getCsvData(lineStr,GlobalName.CODE);
	}
	
	
	/**
	 * get list from a inputStream
	 * @param  code: Shift_JIS  GBK  UTF-8
	 * */
	public static List<String> getTextData(InputStream in, String code) {
		List<String> datas = new ArrayList<String>();
		BufferedReader rf = null;
		try {
			InputStreamReader ISR = new InputStreamReader(in, code);
			rf = new BufferedReader(ISR);
			String strSource = new String();
			while ((strSource = rf.readLine()) != null) {
				if (strSource.trim().equals("")) {
					continue;
				}
				if (strSource.length() == 0) {
					break;
				}
				datas.add(strSource);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogPrint.error(e.getMessage());
		} finally {
			try {
				if (rf != null) {
					rf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return datas;
	}
	
	/**
	 * get list from a inputStream, list contains array of string defined by intArray
	 * @param  code: Shift_JIS  GBK  UTF-8
	 * */
	public static List<String[]> getTextData(InputStream in, int[] intArray, String code) {
		List<String[]> datas = new ArrayList<String[]>();
		BufferedReader rf = null;
		try {
			InputStreamReader ISR = new InputStreamReader(in, code);
			rf = new BufferedReader(ISR);
			String strArray[] = null;
			String strSource = new String();
			while ((strSource = rf.readLine()) != null) {
				if (strSource.trim().equals("")) {
					continue;
				}
				if (strSource.length() == 0) {
					break;
				}
				strArray = getText(strSource, intArray, code);
				datas.add(strArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogPrint.error(e.getMessage());
		} finally {
			try {
				if (rf != null) {
					rf.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return datas;
	}
	/**
	 * get list from a inputStream, list contains array of string
	 * */
	public static List<String[]> getCsvTextData(InputStream in, boolean hasHeader){
		List<String[]> rtn = new ArrayList<String[]>();
		try {
			CsvReader reader = new CsvReader(in, ',', Charset.forName(GlobalName.CODE));
			if(hasHeader) reader.readHeaders();
			while (reader.readRecord()) { 
				rtn.add(reader.getValues()); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	/*public static String[] getCSV(String src) throws Exception{
		if (src == null || src.equals("")) {
			return new String[0];
		}
		StringBuffer st = new StringBuffer();
		List<String> result = new ArrayList<String>();
		boolean beginWithQuote = false;
		int size=0;
		for (int i = 0; i < src.length(); i++) {
			char ch = src.charAt(i);
			if (ch == '\"') {
				if (beginWithQuote) {
					i++;
					if (i >= src.length()) {
						result.add(st.toString());
						st = new StringBuffer();
						beginWithQuote = false;
					} else {
						ch = src.charAt(i);
						if (ch == '\"') {
							st.append(ch);
						} else if (ch == ',') {
							result.add(st.toString());
							st = new StringBuffer();
							beginWithQuote = false;
						}
					}
				}
				st.append('\"');
			} else if (ch == ',') {
				size++;
				if (beginWithQuote) {
					st.append(ch);
				} else {
					result.add(st.toString());
					st = new StringBuffer();
					beginWithQuote = false;
				}
				if((i+1) == src.length()){
					result.add(" ");
				}
			} else {
				st.append(ch);
			}
		}
		if (st.length() != 0) {
			if (beginWithQuote) {
			} else {
				result.add(st.toString());
			}
		}

		if(size==14 && result.size()==14){
			String rs[] = new String[result.size()+1];
			try {
				for (int i = 0; i < rs.length-1; i++) {
					rs[i] = new String(result.get(i).toString().getBytes(GlobalName.SHORT_CODE),
							GlobalName.CODE);
				}
				rs[rs.length-1]=" ";
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return rs;

		}

		String rs[] = new String[result.size()];
		try {
			for (int i = 0; i < rs.length; i++) {
				rs[i] = new String(result.get(i).toString().getBytes(GlobalName.SHORT_CODE),GlobalName.CODE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rs;
	}*/

}
