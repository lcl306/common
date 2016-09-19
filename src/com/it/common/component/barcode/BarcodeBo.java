package com.it.common.component.barcode;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.it.common.component.log.LogPrint;

/**
 * @author liu
 * */
public class BarcodeBo {

	/**
	 * if barcode bean is got, the method print the java.awt.Image of the barcode
	 * @param bracode4j.BarcodeBean
	 * @return java.awt.Image
	 * */
	public static Image getCodeImage(BarcodeDto bean){
		BitmapCanvasProvider canvas = new BitmapCanvasProvider( bean.getDpi(), BufferedImage.TYPE_BYTE_BINARY, false, 0);
		try{
			bean.getBean().generateBarcode(canvas, bean.getCode());
		}catch(Exception e){
			e.printStackTrace();
			LogPrint.error(e.getMessage());
		}
        BufferedImage image = canvas.getBufferedImage();
        try{if(canvas!=null)canvas.finish();}catch(IOException e){};
        return image;
	}

	/**
	 * @param width is image's width
	 * @param height is image's height
	 * */
	public static Image cutImage(Image src, int width, int height){
		BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		target.getGraphics().drawImage(src, 0, 0, width, height, null);
		return target;
	}

	/**
	 * @param height is image's height
	 * */
	public static Image cutImage(Image src, int height){
		int width = ((BufferedImage)src).getWidth();
		return cutImage(src, width, height);
	}

}
