package com.it.common.component.socket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class HttpClientUtil {
	
	private static final String CODE = GlobalName.CODE;
	private static final String JSON_TYPE = "application/json";
	private static final String TEXT_TYPE = "text/plain";
	
	/**
	 * create a httpClient
	 * */
	public static HttpClient getHttpClient(){
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, GlobalName.CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, GlobalName.SO_TIMEOUT);
		HttpClientParams.setCookiePolicy(params, CookiePolicy.BROWSER_COMPATIBILITY);
		return client;
	}
	
	static byte[] readStream(InputStream in)throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[GlobalName.BUFFER_SIZE];
		int len = 0;
		while((len=in.read(buffer))!=-1){
			out.write(buffer, 0, len);
		}
		out.close();
		return out.toByteArray();
	}
	
	static InputStream doResponse(HttpResponse response)throws IOException{
		InputStream in = null;
		if(response.getStatusLine().getStatusCode()==200){
			HttpEntity entity = response.getEntity();
			if(entity!=null){
				in = entity.getContent();
			}
		}
		return in;
	}
	
	/**
	 * post json return input stream
	 * */
	public static InputStream postReturnStream(HttpClient client, String url, String jStr){
		HttpPost post = new HttpPost(url);
		InputStream in = null;
		try{
			if(jStr!=null){
				StringEntity s = new StringEntity(jStr, JSON_TYPE, CODE);
				//StringEntity s = new StringEntity(jStr, TEXT_TYPE, CODE);
				post.setEntity(s);
			}
			HttpResponse response = client.execute(post);
			in = doResponse(response);
		}catch(Exception e){
			e.printStackTrace();
		}
		return in;
	}
	
	/**
	 * post json return object
	 * */
	public static Object postReturnObject(HttpClient client, String url, String json){
		InputStream in = HttpClientUtil.postReturnStream(client, url, json);
		Object o = null;
		if(in!=null){
			try{
				o = StreamUtil.in2Object(in);
			}finally{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return o;
	}
	
	/**
	 * post json return byte[]
	 * */
	public static byte[] postReturnByte(HttpClient client, String url, String json){
		InputStream in = postReturnStream(client, url, json);
		try {
			return readStream(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * post json return json
	 * */
	public static String postReturnJson(HttpClient client, String url, String json){
		InputStream in = postReturnStream(client, url, json);
		String rtn = inputStream2json(in);
		try{
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return rtn;
	}
	
	public static InputStream postObject(HttpClient client, String url, Object object){
		ByteArrayOutputStream  bos = null;
		InputStream in = null;
		try{
			HttpPost post = new HttpPost(url);	
			bos = new ByteArrayOutputStream();
			StreamUtil.object2Out(object, bos);
			ByteArrayEntity entity = new ByteArrayEntity(bos.toByteArray());
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			in = doResponse(response);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bos!=null) try{bos.close();}catch(Exception e){}
		}
		return in;
	}
	
	
	
	static String inputStream2json(InputStream in){
		InputStreamReader reader = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try{
			reader = new InputStreamReader(in, CODE);
			br = new BufferedReader(reader, 8192);
			String line = null;
			while((line=br.readLine())!=null){
				sb.append(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
			br.close();
			reader.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
