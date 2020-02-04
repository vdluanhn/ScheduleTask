package com.example.scheduletask.FilesUtils;

//import com.github.ffpojo.util.StringUtil;
import org.apache.commons.io.filefilter.WildcardFilter;

import java.io.*;
import java.io.FileWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;

public class FileUtil {
    public static final int BUFFER_SIZE = 65536;
    public static final int MAX_SMALL_FILE_SIZE = 16777216;

    public FileUtil() {
    }

    public static boolean writeFile(String pathFile, String data){
        FileWriter fw = null;
        try {
            //Bước 1: Tạo đối tượng luồng và liên kết nguồn dữ liệu
            File f = new File(pathFile);
            fw = new FileWriter(f);
            //Bước 2: Ghi dữ liệu
            fw.write(data);
            //Bước 3: Đóng luồng
            fw.close();
            return true;
        }catch (Exception ex){
            System.out.println(ex.toString());
        }finally {
            if (fw!=null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static String readFile(String pathFile){
        FileReader fr =null;
        BufferedReader br = null;
        try {
            //Bước 1: Tạo đối tượng luồng và liên kết nguồn dữ liệu
            File f = new File(pathFile);
            fr = new FileReader(f);
            //Bước 2: Đọc dữ liệu
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null){
                line+=line+"\n";
                System.out.println(line);
            }
            //Bước 3: Đóng luồng
            fr.close();
            br.close();
            System.out.println("Đọc file thành công: "+pathFile);
            return line;
        }catch (Exception ex){
            System.out.println(ex.toString());
        }finally {
            if (fr!=null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String getAbsolutePath(String strCurrenDir, String strFileName) {
        if (!strFileName.startsWith("/") && !strFileName.startsWith("\\")) {
            return !strCurrenDir.endsWith("/") && !strCurrenDir.endsWith("\\") ? strCurrenDir + "/" + strFileName : strCurrenDir + strFileName;
        } else {
            return strFileName;
        }
    }

    public static void forceFolderExist(String strFolder) throws IOException {
        File flTemp = new File(strFolder);
        if (!flTemp.exists()) {
            if (!flTemp.mkdirs()) {
                throw new IOException("Could not create folder " + strFolder);
            }
        } else if (!flTemp.isDirectory()) {
            throw new IOException("A file with name" + strFolder + " already exist");
        }

    }

    public static boolean renameFile(String strSrc, String strDest, boolean deleteIfExist) throws IOException {
        File flSrc = new File(strSrc);
        File flDest = new File(strDest);
        if (flSrc.getAbsolutePath().equals(flDest.getAbsolutePath())) {
            return false;
        } else {
            if (flDest.exists()) {
                if (!deleteIfExist) {
                    throw new IOException("File '" + strDest + "' already exist");
                }

                flDest.delete();
            }

            return flSrc.renameTo(flDest);
        }
    }

    public static boolean renameFile(String strSrc, String strDest) {
        File flSrc = new File(strSrc);
        File flDest = new File(strDest);
        if (flSrc.getAbsolutePath().equals(flDest.getAbsolutePath())) {
            return true;
        } else {
            if (flDest.exists()) {
                flDest.delete();
            }

            return flSrc.renameTo(flDest);
        }
    }

    public static boolean copyFile(String strSrc, String strDest) {
        FileInputStream isSrc = null;
        FileOutputStream osDest = null;

        try {
            File flDest = new File(strDest);
            if (flDest.exists()) {
                flDest.delete();
            }

            File flSrc = new File(strSrc);
            if (flSrc.exists()) {
                isSrc = new FileInputStream(flSrc);
                osDest = new FileOutputStream(flDest);
                byte[] btData = new byte[65536];

                int iLength;
                while((iLength = isSrc.read(btData)) != -1) {
                    osDest.write(btData, 0, iLength);
                }

                return true;
            }
        } catch (Exception var11) {
            var11.printStackTrace();
            return false;
        } finally {
            safeClose((InputStream)isSrc);
            safeClose((OutputStream)osDest);
        }

        return false;
    }

    public static boolean deleteFile(String strSrc) {
        File flSrc = new File(strSrc);
        return flSrc.delete();
    }

    public static boolean copyResource(Class cls, String strResSource, String strFile) {
        InputStream isSrc = null;
        FileOutputStream osDest = null;

        try {
            strResSource = strResSource.replace('\\', '/');
            isSrc = cls.getResourceAsStream(strResSource);
            if (isSrc == null) {
                throw new IOException("Resource " + strResSource + " not found");
            }

            osDest = new FileOutputStream(strFile);
            byte[] btData = new byte[65536];

            int iLength;
            while((iLength = isSrc.read(btData)) != -1) {
                osDest.write(btData, 0, iLength);
            }

            return true;
        } catch (IOException var10) {
            var10.printStackTrace();
        } finally {
            safeClose(isSrc);
            safeClose((OutputStream)osDest);
        }

        return false;
    }

    public static void deleteOldFile(String strPath, String strWildcard, int iOffset) {
        if (!strPath.endsWith("/")) {
            strPath = strPath + "/";
        }

        File flFolder = new File(strPath);
        if (flFolder.exists()) {
            String[] strFileList = flFolder.list(new WildcardFilter(strWildcard));
            if (strFileList != null && strFileList.length > 0) {
                long lCurrentTime = (new Date()).getTime();

                for(int iFileIndex = 0; iFileIndex < strFileList.length; ++iFileIndex) {
                    File fl = new File(strPath + strFileList[iFileIndex]);
                    if (lCurrentTime - fl.lastModified() >= (long)iOffset) {
                        fl.delete();
                    }
                }
            }

        }
    }

    public static void backup(String strFileName, int iMaxSize, int iRemainSize) throws Exception {
        if (iMaxSize <= iRemainSize) {
            throw new IllegalArgumentException();
        } else {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
            File flSource = new File(strFileName);
            if (flSource.length() > (long)iMaxSize) {
                String strNewName = strFileName + "." + fmt.format(new Date());
                renameFile(strFileName, strNewName);
                RandomAccessFile fl = null;
                FileOutputStream os = null;

                try {
                    os = new FileOutputStream(strFileName);
                    fl = new RandomAccessFile(strNewName, "rw");
                    fl.seek(fl.length() - (long)iRemainSize);
                    byte[] bt = new byte[iRemainSize];
                    int iByteRead = fl.read(bt);
                    if (iByteRead != iRemainSize) {
                        throw new IOException();
                    }

                    os.write(bt, 0, iByteRead);
                    fl.setLength(fl.length() - (long)iRemainSize);
                } finally {
                    safeClose(fl);
                    safeClose((OutputStream)os);
                }
            }

        }
    }

    public static void backup(String strFileName, int iMaxSize) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        File flSource = new File(strFileName);
        if (flSource.length() > (long)iMaxSize) {
            String strNewName = strFileName + "." + fmt.format(new Date());
            renameFile(strFileName, strNewName);
        }

    }
//
//    public static String backup(String strSourcePath, String strBackupPath, String strSourceFile, String strBackupFile, String strBackupStyle) throws Exception {
//        return backup(strSourcePath, strBackupPath, strSourceFile, strBackupFile, strBackupStyle, true);
//    }
//
//    public static String backup(String strSourcePath, String strBackupPath, String strSourceFile, String strBackupFile, String strBackupStyle, boolean bReplace) throws Exception {
//        return backup(strSourcePath, strBackupPath, strSourceFile, strBackupFile, strBackupStyle, "", bReplace);
//    }
//
//    public static String backup(String strSourcePath, String strBackupPath, String strSourceFile, String strBackupFile, String strBackupStyle, String strAdditionPath) throws Exception {
//        return backup(strSourcePath, strBackupPath, strSourceFile, strBackupFile, strBackupStyle, strAdditionPath, true);
//    }

//    public static String backup(String strSourcePath, String strBackupPath, String strSourceFile, String strBackupFile, String strBackupStyle, String strAdditionPath, boolean bReplace) throws Exception {
//        if (strBackupStyle.equals("Delete file")) {
//            if (!deleteFile(strSourcePath + strSourceFile)) {
//                throw new Exception("Cannot delete file " + strSourcePath + strSourceFile);
//            }
//        } else if (strBackupPath.length() > 0) {
//            String strCurrentDate = "";
//            if (strBackupStyle.equals("Daily")) {
//                strCurrentDate = StringUtil.format(new Date(), "yyyyMMdd") + "/";
//            } else if (strBackupStyle.equals("Monthly")) {
//                strCurrentDate = StringUtil.format(new Date(), "yyyyMM") + "/";
//            } else if (strBackupStyle.equals("Yearly")) {
//                strCurrentDate = StringUtil.format(new Date(), "yyyy") + "/";
//            }
//
//            forceFolderExist(strBackupPath + strCurrentDate + strAdditionPath);
//            if (!renameFile(strSourcePath + strSourceFile, strBackupPath + strCurrentDate + strAdditionPath + strBackupFile, bReplace)) {
//                throw new Exception("Cannot rename file " + strSourcePath + strSourceFile + " to " + strBackupPath + strCurrentDate + strBackupFile);
//            }
//
//            return strBackupPath + strCurrentDate + strBackupFile;
//        }
//
//        return "";
//    }

    public static void safeClose(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void safeClose(OutputStream os) {
        try {
            if (os != null) {
                os.close();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public static void safeClose(RandomAccessFile fl) {
        try {
            if (fl != null) {
                fl.close();
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

//    public static long getSequenceValue(String strFileName) throws Exception {
//        return getSequenceValue(new File(strFileName));
//    }

//    public static long getSequenceValue(File fl) throws Exception {
//        return getSequenceValue(fl, 1L);
//    }

//    public static long getSequenceValue(File fl, long lIncrementValue) throws Exception {
//        String str = readSmallFile(fl);
//        long l = Long.parseLong(str);
//        writeSmallFile(fl, String.valueOf(l + lIncrementValue));
//        return l;
//    }
//
//    public static byte[] readSmallFileToByteArray(String strFileName) throws Exception {
//        return readSmallFileToByteArray(new File(strFileName));
//    }

//    public static byte[] readSmallFileToByteArray(File fl) throws Exception {
//        if (fl.length() > 16777216L) {
//            throw new Exception("File content too large");
//        } else {
//            FileInputStream is = null;
//
//            byte[] var3;
//            try {
//                is = new FileInputStream(fl);
//                var3 = StreamUtil.readStreamToByteArray(is);
//            } finally {
//                safeClose((InputStream)is);
//            }
//
//            return var3;
//        }
//    }

//    public static String readSmallFile(String strFileName) throws Exception {
//        return readSmallFile(new File(strFileName));
//    }

//    public static String readSmallFile(File fl) throws Exception {
//        if (fl.length() > 16777216L) {
//            throw new Exception("File content too large");
//        } else {
//            FileInputStream is = null;
//
//            String var3;
//            try {
//                is = new FileInputStream(fl);
//                var3 = StreamUtil.readStream(is);
//            } finally {
//                safeClose((InputStream)is);
//            }
//
//            return var3;
//        }
//    }

    public static void writeSmallFile(String strFileName, String strContent) throws Exception {
        writeSmallFile(new File(strFileName), strContent);
    }

    public static void writeSmallFile(File fl, String strContent) throws Exception {
        if (strContent.length() > 16777216) {
            throw new Exception("Content too large");
        } else {
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(fl);
                os.write(strContent.getBytes());
            } finally {
                safeClose((OutputStream)os);
            }

        }
    }

//    public static String formatFileName(String strFileName, String strFileFormat) throws AppException {
//        if (strFileName != null && strFileName.length() != 0 && strFileFormat != null && strFileFormat.length() != 0) {
//            int iExtIndex = strFileName.lastIndexOf(46);
//            if (iExtIndex < 0) {
//                iExtIndex = strFileName.length();
//            }
//
//            int iBaseIndex = strFileName.lastIndexOf(47);
//            if (iBaseIndex < 0) {
//                iBaseIndex = strFileName.lastIndexOf(92);
//            }
//
//            if (iBaseIndex < 0) {
//                iBaseIndex = 0;
//            }
//
//            String strBaseFileName = strFileName.substring(iBaseIndex, iExtIndex);
//            String strFileExtension = "";
//            if (iExtIndex < strFileName.length() - 1) {
//                strFileExtension = strFileName.substring(iExtIndex + 1, strFileName.length());
//            }
//
//            strFileFormat = StringUtil.replaceAll(strFileFormat, "$FileName", strFileName);
//            strFileFormat = StringUtil.replaceAll(strFileFormat, "$BaseFileName", strBaseFileName);
//            strFileFormat = StringUtil.replaceAll(strFileFormat, "$FileExtension", strFileExtension);
//            return strFileFormat;
//        } else {
//            return strFileName;
//        }
//    }

    public static URL getResource(String strName) {
        try {
            File fl = new File(strName);
            if (fl.exists() && fl.isFile()) {
                return fl.toURI().toURL();
            }
        } catch (Exception var2) {
        }

        return !strName.startsWith("/") ? FileUtil.class.getResource("/" + strName) : FileUtil.class.getResource(strName);
    }
}
