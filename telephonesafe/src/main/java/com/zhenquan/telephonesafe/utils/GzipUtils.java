package com.zhenquan.telephonesafe.utils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtils {
	public static void zip(File sourceFile, File targetFile) {
		FileInputStream fis = null;
		GZIPOutputStream gos = null;
		try {
			fis = new FileInputStream(sourceFile);
			gos = new GZIPOutputStream(new FileOutputStream(targetFile));
			int b;
			byte[] buffer = new byte[1024];
			while ((b = fis.read(buffer)) != -1) {
				gos.write(buffer, 0, b);
			}
			gos.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeIO(fis, gos);
		}

	}
	
	public static void unZip(File sourceFile, File targetFile) {
		FileOutputStream fos = null;
		GZIPInputStream gis = null;
		try {
			fos = new FileOutputStream(sourceFile);
			gis = new GZIPInputStream(new FileInputStream(targetFile));
			int b;
			byte[] buffer = new byte[1024];
			while ((b = gis.read(buffer)) != -1) {
				fos.write(buffer, 0, b);
			}
			fos.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeIO(fos, gis);
		}

	}
	public static void unZipByStream(InputStream inputStream, File targetFile) {
		FileOutputStream fos = null;
		GZIPInputStream gis = null;
		try {
			fos = new FileOutputStream(targetFile);
			gis = new GZIPInputStream(inputStream);
			int b;
			byte[] buffer = new byte[1024];
			while ((b = gis.read(buffer)) != -1) {
				fos.write(buffer, 0, b);
			}
			fos.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeIO(fos, gis);
		}

	}
	private static void closeIO(Closeable... io) {
		if (io != null) {
			for (Closeable closeable : io) {
				if (closeable != null) {
					try {
						closeable.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
