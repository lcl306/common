package com.it.common.component.updown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author liu
 * */
public class ZipUtil {
	
	/**
	 * This seperator is used for ZipEntr.
	 */
	private static final String SEPERATOR = "/";

	// this Method zip files to the same Stream
	protected static void zip(ZipOutputStream out, File f, String base)
			throws Exception {
		System.out.println("   ...   Now   Zipping   :   " + f.getPath());
		// one folder
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			// put folder entry in out stream
			out.putNextEntry(new ZipEntry(base + SEPERATOR));
			base = base.length() == 0 ? "" : base + SEPERATOR;
			// zip all files in folder
			for (int i = 0; i < fl.length; i++) {
				// Pass and return
				zip(out, fl[i], base + fl[i].getName());
			}
		}
		// one file
		else {
			out.putNextEntry(new ZipEntry(base));
			// put file entry in out stream
			FileInputStream in = new FileInputStream(f);
			int buf;
			// write to zipfile by outStream
			while ((buf = in.read()) != -1) {
				out.write(buf);
			}
			in.close();
		}
	}

	public static void zip(String fromFileName, String zipFileName)
			throws Exception {
		File fromFile = new File(fromFileName);
		// create outStream
		FileOutputStream fout = new FileOutputStream(zipFileName);
		// CheckedOutputStream csum = new CheckedOutputStream(fout, new
		// CRC32());
		// ZipOutputStream out = new ZipOutputStream(csum);
		ZipOutputStream out = new ZipOutputStream(fout);
		System.out.println("---   zip   start!!   ---");
		String folder = "";
		// file or folder's name by java.lang.String folder we save the route
		int index=fromFileName.lastIndexOf(SEPERATOR);
		if(index!=-1){
			folder = fromFileName.substring(fromFileName.lastIndexOf(SEPERATOR),
					fromFileName.length());
		}else{
			folder = fromFileName.substring(fromFileName.lastIndexOf("\\"),
					fromFileName.length());
		}
		zip(out, fromFile, folder);
		System.out.println("---   zip   done!!   ---");
		// update Stream
		out.flush();
		// close Stream
		out.close();
	}

	public static void unzip(String zipFileName, String outputDirectory)
			throws Exception {
		FileInputStream fin = new FileInputStream(zipFileName);
		// CheckedInputStream csum = new CheckedInputStream(fin, new CRC32());
		// ZipInputStream in = new ZipInputStream(csum);
		// read zipfile to inputStream
		ZipInputStream in = new ZipInputStream(fin);
		ZipEntry z;
		while ((z = in.getNextEntry()) != null) {
			System.out.println("   ...   Now   unziping   :   " + z.getName());
			// the entry is one folder.
			if (z.isDirectory()) {
				String name = z.getName();
				name = name.substring(0, name.length() - 1);
				// you must create folder for the file before unzip the file
				File f = new File(outputDirectory + File.separator + name);
				f.mkdir();
			}
			// the entry is one file
			else {
				File f = new File(outputDirectory + File.separator
						+ z.getName());
				f.createNewFile();
				FileOutputStream out = new FileOutputStream(f);
				int buf;
				// write to purpose file
				while ((buf = in.read()) != -1) {
					out.write(buf);
				}
				out.close();
			}
		}
		in.close();
		FileUtil.delFile(zipFileName);
	}

}
