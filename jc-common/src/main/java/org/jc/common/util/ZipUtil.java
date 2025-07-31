package org.jc.common.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    public static void unzip(File zipFile, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdirs(); // 创建目标目录
        }

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry = zipIn.getNextEntry();

            // 遍历ZIP文件中的所有条目
            while (entry != null) {
                String filePath = destDir.getAbsolutePath() + File.separator + entry.getName();

                // 如果是目录，创建目录
                if (entry.isDirectory()) {
                    File dir = new File(filePath);
                    dir.mkdirs();
                } else {
                    // 如果是文件，写入到目标目录
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
                        byte[] bytesIn = new byte[4096];
                        int read;
                        while ((read = zipIn.read(bytesIn)) != -1) {
                            bos.write(bytesIn, 0, read);
                        }
                    }
                }

                zipIn.closeEntry(); // 关闭当前条目
                entry = zipIn.getNextEntry(); // 获取下一个条目
            }
        }
    }
}
