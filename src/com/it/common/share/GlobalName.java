package com.it.common.share;

import java.util.ResourceBundle;

import com.it.common.component.security.RoleManager;
import com.it.common.component.security.RoleManagerImpl;

public class GlobalName {
	
	private static ResourceBundle resources;
	
	private static ResourceBundle appContextResources = ResourceBundle.getBundle("com/it/context/appContext");
	
	public static ResourceBundle appResources;
	
	public static ResourceBundle dbResources = ResourceBundle.getBundle("com/it/context/dbContext");
	
	public static RoleManager roleManager;
	
	////////////////////////////////////////////CODE/////////////////////////////////////
	
	public static String CODE = "UTF-8";
	
	public static String JSON_CODE = "UTF-8";
	
	public static String ISO_8895_CODE = "ISO-8859-1";
	
	public static String SHIT_JIS_CODE = "Shift_JIS";
	
	public static String SJIS_CODE = "SJIS";
	
	public static String UTF8_CODE = "UTF-8"; 
	
	public static String APP_CONTEXT_PATH = "";
	
	///////////////////////////////////////////PATH///////////////////////////////////////
	
	public static String BASEDAO_FROM = "";
	
	public static String PROJECT_PATH = "";
	
	//public static String UPLOAD_PATH = "";
	
	public static String INACTIVE_PATH = "";
	
	////////////////////////////////////////SOCKET////////////////////////////////////////
	
	public static int BUFFER_SIZE = 1024;
	
	public static int CONNECTION_TIMEOUT = 5000;
	
	public static int SO_TIMEOUT = 5000;
	
	////////////////////////////////////////calculate/////////////////////////////////////
	
	public static String FLOOR = "1";
	
	public static String HALF_UP = "2";
	
	public static String CEILING = "3";
	
	///////////////////////////////////////////PARAM///////////////////////////////////////
	
	public static final String SESSION_TOKEN = "com.it.SESSION_TOKEN";
	
	public static final String PARAM_TOKEN = "com.it.TOKEN";
	
	public static final String SYSTEMDTO_NAME = "com.it.SYSTEMDTO";
	
	public static final String ERRLISTDTO_NAME = "com.it.ERRLISTDTO";
	
	public static final String PAGE_NAME = "com.it.page";
	
	public static final String NUM_FLAG = ",";
	
	public static final String DATE_FLAG = "-";
	
	public static final String TIME_FLAG = ":";
	
	public static final String PRE_YEAR = "20";
	
	public static final String DAY_NAMES[] = { "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日" };
	
	public static final String S_DAY_NAMES[] = { "日曜", "月曜", "火曜", "水曜", "木曜", "金曜", "土曜" };	
	
	////////////////////////////////////////////app-config////////////////////////////////
	
	public static String AJAX_CHECK = "false";
	
	public static String DOC_FOCUS = "false";
	
	static{
		resources = ResourceBundle.getBundle("com/it/context/base/paramContext");
		CODE = resources.getString("code");
		JSON_CODE = resources.getString("json_code");
		CONNECTION_TIMEOUT = Integer.parseInt(resources.getString("httpclient_conn_timeout").trim());
		SO_TIMEOUT = Integer.parseInt(resources.getString("httpclient_so_timeout").trim());
		AJAX_CHECK = resources.getString("ajax_check");
		DOC_FOCUS = resources.getString("doc_focus");
		BASEDAO_FROM = resources.getString("basedao_from");
		appResources = ResourceBundle.getBundle(appContextResources.getString("appContext"));
		try {
			roleManager = (RoleManager)Class.forName(resources.getString("role_manager_impl")).newInstance();
		} catch (Exception e) {
			roleManager = new RoleManagerImpl();
		}
		
		//UPLOAD_PATH = resources.getString("upload_path");
		//INACTIVE_PATH = resources.getString("inactive_path");
	}

}
