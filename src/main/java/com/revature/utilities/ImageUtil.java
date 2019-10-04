package com.revature.utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
	public static byte [] ImageToByte(File file) throws FileNotFoundException{

        FileInputStream fis = new FileInputStream(file);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] byteArray = new byte[1024];

        try {

            for (int readNum; (readNum = fis.read(byteArray)) != -1;) {

                bos.write(byteArray, 0, readNum);     

                System.out.println("read " + readNum + " bytes,");

            }

        } catch (IOException ex) {

        }

        byte[] bytes = bos.toByteArray();

     return bytes;

    }
	
	public static File ByteToImage(byte[] byteArray, String fileName) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
		BufferedImage newImage = ImageIO.read(bis);
		File imageFile = new File(fileName + ".jpg");
		ImageIO.write(newImage, "jpg", imageFile);
		return imageFile;
	}
}