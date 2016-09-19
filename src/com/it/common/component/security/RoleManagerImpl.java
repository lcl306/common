package com.it.common.component.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.it.common.component.check.base.DateCheck;
import com.it.common.component.lang.date.DBTime;
import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

public class RoleManagerImpl implements RoleManager {
	
	protected static String encrySeed = "3e2-473So6lE^0wn";
	
	public static final String EXPIRE_VALID = "roleManageImpl_valid";

	@Override
	public boolean pass(HttpServletRequest request) {
		return passValid(request, encrySeed);
	}
	
	public static boolean isValid(String expireDay, String encrySeed){
		String day = DBTime.getInstance().getDay();
		boolean rtn = true;
		try {
			expireDay = decryKey(expireDay, encrySeed);
		} catch (Exception e) {
			e.printStackTrace();
			LogPrint.error(e.getMessage());
			rtn = false;
		}
		if(!DateCheck.chkDate(expireDay, false)){
			rtn = false;
		}else if(day.compareTo(expireDay)>0){
			rtn = false;
		}
		return rtn;
	}
	
	protected static boolean passValid(HttpServletRequest request, String encrySeed){
		boolean rtn = true;
		String expireDay = "";
		try {
			expireDay = readKey(request);
		} catch (IOException e) {
			e.printStackTrace();
			LogPrint.error(e.getMessage());
			rtn = false;
		}
		rtn = isValid(expireDay, encrySeed);
		if(!rtn){
			request.setAttribute(EXPIRE_VALID, "false");
		}
		return rtn;
	}
	
	protected static String getFileName(HttpServletRequest request){
		return request.getServletContext().getRealPath("/")+"WEB-INF/classes/com/it/context/encryptkey";
	}
	
	protected static String decryKey(String key, String encrySeed) throws Exception{
		EncryptUtil eu = new EncryptUtil(encrySeed);
		return eu.decrypt(key);
	}
	
	private static String readKey(HttpServletRequest request) throws IOException{
		File f = new File(getFileName(request));
		if(!f.exists()){
			f.createNewFile();
			return "";
		}
		byte[] content = new byte[(int)f.length()];
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			in.read(content);
		}finally{
			in.close();
		}
		return new String(content, GlobalName.CODE);
	}
	
	public static void writeKey(HttpServletRequest request, String key) throws IOException{
		File f = new File(getFileName(request));
		if(!f.exists()){
			f.createNewFile();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(f);
			out.write(key.getBytes(GlobalName.CODE));
		}finally{
			out.close();
		}
	}

}
