package com.it.common.component.updown;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.it.common.share.GlobalName;

/**
 * @author liu
 * */
public class DownloadUtil {
	
	protected static final int READ_SIZE=1024*1024;  //1MB
	
	private static void setMultiResponse(HttpServletResponse response, String filename){
//		 response.setContentType("application/octet-stream; charset="+GlobalName.SHIT_JIS_CODE);
		try {
			filename= java.net.URLEncoder.encode(filename,GlobalName.CODE);
			filename=filename.replaceAll("\\+", " ");
		} catch (UnsupportedEncodingException e) {			
		}
		 response.setContentType("application/octet-stream; charset="+GlobalName.CODE);
		 response.setHeader("Content-Disposition", "attachment;filename=" + filename);
	}
	
	/**
	 * read the file, then download
	 * */
	public static final void downloadFile(HttpServletResponse response, String globalFileName){
		//response.setHeader("Pragma", "No-chache");
		//response.setHeader("Chache-Control", "no-chache");
		//response.setDateHeader("Expires", 0);
		String fileName = globalFileName.substring(globalFileName.lastIndexOf(File.separator)).substring(1);
		setMultiResponse(response, fileName);

		FileInputStream fis = null;
		ServletOutputStream out = null;
		try{
			fis = new FileInputStream(globalFileName);
			out = response.getOutputStream();
			byte[] buffer = new byte[READ_SIZE];
			int len = 0;
			while((len=fis.read(buffer, 0, READ_SIZE))!=-1){
				out.write(buffer, 0, len);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fis!=null) try{fis.close();}catch(Exception e){}
			if(out!=null) try{out.close();}catch(Exception e){}
		}
	}
	
	/**
	 * read the list of string, then download
	 * */
	public static final void downloadList(HttpServletResponse response, String fileName, List<String> context){
		setMultiResponse(response, fileName);
		ServletOutputStream out = null;
		ByteArrayOutputStream bos = null;
		try{
			out = response.getOutputStream();			
			bos = new ByteArrayOutputStream();
			
			/*
			 * BOM付き処理、ただし不安要素があるため、今回見送り
			out.write( 0xef );
			out.write( 0xbb );
			out.write( 0xbf );
			*/
			for(String line : context){
				bos.write(line.getBytes(GlobalName.CODE));
				if(bos.size()>=READ_SIZE){
					bos.writeTo(out);
					bos.reset();
				}
			}
			bos.writeTo(out);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bos!=null) try{bos.close();}catch(Exception e){}
			if(out!=null) try{out.close();}catch(Exception e){}
		}
	}
	
	/**
	 * read the input stream, then download
	 * */
	public static final void downloadStream(HttpServletResponse response, String fileName, InputStream is){
		downloadStream(response, fileName, is, true);
	}
	
	/**
	 * read the input stream, then download
	 * @param closeStream means: true=close input stream  false=does not close input stream
	 * */
	public static final void downloadStream(HttpServletResponse response, String fileName, InputStream is, boolean closeStream){
		setMultiResponse(response, fileName);
		ServletOutputStream out = null;
		try{
			out = response.getOutputStream();			
			byte[] buffer = new byte[READ_SIZE];
			int len = 0;
			while((len=is.read(buffer, 0, READ_SIZE))!=-1){
				out.write(buffer, 0, len);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(closeStream && is!=null) try{is.close();}catch(Exception e){}
			if(out!=null) try{out.close();}catch(Exception e){}
		}
	}

}
