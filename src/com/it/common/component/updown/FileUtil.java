package com.it.common.component.updown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liu
 * */
public class FileUtil {

	public static void deleteDirectory(File dir) throws IOException {
		if ((dir == null) || !dir.isDirectory()) {
			throw new IllegalArgumentException("Argument " + dir+ " is not a directory. ");
		}
		File[] entries = dir.listFiles();
		int sz = entries.length;
		for (int i = 0; i < sz; i++) {
			if (entries[i].isDirectory()) {
				deleteDirectory(entries[i]);
			} else {
				entries[i].delete();
			}
		}
		dir.delete();
	}

	public static File[] getSubfile(File dir) throws IOException {
		if ((dir == null || !dir.isDirectory())) {
			throw new IllegalArgumentException("Argument " + dir
					+ "is not a directory or isn't a validate directory!");
		}
		File[] entries = dir.listFiles();
		return entries;
	}

	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // if file has not exist,it will create a new
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ File.separator + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// if it is a file
					copyFolder(oldPath + File.separator + file[i], newPath+ File.separator + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("it appears error when system copys all files");
			e.printStackTrace();
		}

	}

	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
				System.out.println("successfull to delete file under " + path);
			}
			if (temp.isDirectory()) {
				delAllFile(path + File.separator + tempList[i]);// first ,delte file
				delFolder(path + File.separator + tempList[i]);// second delete empty folder
				System.out.println("successfull to delete folder under " + path);
			}
		}
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // delete all file of this folder
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // delete empty folder

		} catch (Exception e) {
			System.out.println("it appears error when delete file or folder!");
			e.printStackTrace();
		}
	}

	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();
			System.out.println("delelte succesful!");
		} catch (Exception e) {
			System.out.println("it appears error when delete file or file!");
			e.printStackTrace();
		}
	}

}
