package core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageFile {
	public static String GenerateFileName(int id, String filename) {
		String extension = filename.substring(filename.lastIndexOf("."));
		System.out.println("拡張子:" + extension);
		Date date = new Date();
		return String.valueOf(id) + String.valueOf(date.getTime()) + extension;
	}

	public static boolean CheckExtension(String filename) {
		String extension = filename.substring(filename.lastIndexOf(".")).toUpperCase();
		//許可する拡張子リスト
		List<String> ExtensionList = Arrays.asList(".JPG", ".JPEG", ".PNG", ".GIF", ".BMP", ".TIFF");
		return ExtensionList.contains(extension);
	}

	public static boolean CheckImage(String path) {
		File file = new File(path);
		if (file != null && file.isFile()) {
			String filename = file.getName();

			try {
				BufferedImage bi = ImageIO.read(file);
				if (CheckExtension(filename) && bi != null) {
					return true;
				}
			} catch (Exception e) {
				e.getStackTrace();
			}
		}

		return false;
	}

	public static boolean DeleteImage(String path) {
		File file = new File(path);
		//ファイルが存在すれば削除
		if (file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}
}