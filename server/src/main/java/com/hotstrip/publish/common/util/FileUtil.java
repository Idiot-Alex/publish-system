package com.seestech.sell.common.utils;

import com.seestech.sell.domain.model.enums.FileTypeEnums;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by idiot on 2016/12/28.
 */
public class FileUtils {
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * @description 拷贝文件流到文件
     * @param multipartFile
     * @param tempPartFile
     */
    public static void copyInputStreamToFile(MultipartFile multipartFile, File tempPartFile) {
        File file = null;
        try {
            file = convert(multipartFile);  //multipartFile 转 file
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(tempPartFile);
            byte[] b = new byte[102400];
            int n;
            while ((n = fis.read(b)) != -1) {
                fos.write(b, 0, n);
            }
            fis.close();
            fos.close();
        }catch (Exception e){
            logger.error("fileInputStream to file......message: [{}]...caused: [{}]", e.getMessage(), e.getCause());
        }finally {
            //用上面的 multipartFile 转 file方法  会在磁盘里面生成一个文件   干掉它
            if(file != null && file.exists()){
                file.delete();
            }
        }
    }

    /**
     * @description 合并分片文件到新文件里面
     * @param partFile  分片文件
     * @param destTempFile  新文件
     */
    public static void copyFile(File partFile, File destTempFile) {
        try {
            FileInputStream fis = new FileInputStream(partFile);
            FileOutputStream fos = new FileOutputStream(destTempFile, true);
            byte[] b = new byte[1024000];
            int n;
            while ((n = fis.read(b)) != -1) {
                fos.write(b, 0, n);
            }
            fis.close();
            fos.close();
        } catch (IOException e) {
            logger.error("old file to new file......message: [{}]...caused: [{}]", e.getMessage(), e.getCause());
        }
    }

    /**
     * @description 删除目录以及目录下的文件
     * @param tempFileDir
     */
    public static void deleteDirectory(File tempFileDir) {
        if(tempFileDir.exists()){
            deleteFile(tempFileDir);
        }
    }

    public static void deleteFile(File file){
        if(file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }
    }

    /**
     * @description multipartFile 转 file   使用该方法会在磁盘里生成一个文件(通常是在项目根路径下)
     * @param file
     * @return
     */
    public static File convert(MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());
        try {
            if(convertFile.createNewFile()){
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
                fos.close();
            }else logger.error("convert file failed......");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return convertFile;
    }

    /**
     * @description 判断是否存在文件（目录）
     * @param file
     * @return
     */
    public static boolean existsFile(File file){
        if(file != null && file.exists()){
            return true;
        }
        return false;
    }


    /**
     * @description 创建文件（目录）
     * @param file
     * @return
     */
    public static boolean createFile(File file) {
        boolean flag = true;
        if(!file.exists()){
            flag = file.mkdirs();
        }
        return flag;
    }

    /**
     * @description 反转数组元素
     * @param array
     * @return
     */
    public static String reverseArray(String[] array){
        StringBuffer buffer = new StringBuffer();
        int length = array.length;
        for(int i = 0; i < length; i++){
            buffer.append(array[length - i - 1]);
            if(i < length - 1){
                buffer.append("/");
            }
        }
        return buffer.toString();
    }

    /**
     * @description 读取zip文件里面的信息
     * @param path
     * @throws Exception
     */
    public static String readZipFileName(String path) throws Exception {
        // ZipFile zf = new ZipFile(path);
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        String fileName = null;
        String temp = null;
        while ((ze = zin.getNextEntry()) != null) {
            //获取第一个目录名字 如果是目录 就继续查找以该名字命名的文件
            if(ze.isDirectory()){
                temp = temp == null ? ze.getName() : temp;
                continue;
            }else {     //该目录是文件  如果此时temp为空  返回该文件名  反之  继续比较temp和文件名
                if(temp == null){
                    if(ze.getName().contains("/") || ze.getName().contains("url.ini"))
                        continue;
                    fileName = ze.getName();
                    break;
                }else {
                    if('.' == ze.getName().charAt(temp.lastIndexOf('/'))){
                        fileName = ze.getName();
                        break;
                    }
                }
            }
        }
        zin.closeEntry();
        logger.info("this compressed index file name: [{}]", fileName);
        return fileName;
    }

    /**
     * @description 根据路径获取指定文件的绝对路径
     * @param newFilePath
     * @param fileName
     * @return
     */
    public static String getFilePathByFileName(String newFilePath, String fileName) {
        String filePath = new String();
        File file = new File(newFilePath);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            //遍历所有文件  匹配文件名字
            for (File f : files){
                if(f.isDirectory()){
                    filePath = getFilePathByFileName(f.getAbsolutePath(), fileName);
                }
                if(f.getName().equals(fileName)){
                    filePath = f.getAbsolutePath();
                    break;
                }
            }
        }
        return filePath;
    }

    /**
     * @description 生成文件
     * @param filePath      路径
     * @param content       内容
     * @param isAppend      是否追加
     */
    public static void generateIniFile(String filePath, String content, boolean isAppend) {
        logger.debug("file path: [{}]...content: [{}]", filePath, content);
        FileUtils.createParentsFile(filePath);
        File file = new File(filePath);
        try {
            // 是否追加内容的方式重写文件内容
            FileOutputStream fos = new FileOutputStream(file, isAppend);
            if(createFile(file)){
                logger.debug("create new file....");
                //写入内容到文件
                fos.write(content.getBytes());
                fos.flush();
            }
            fos.close();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    //根据路径  创建父级目录
    private static void createParentsFile(String filePath) {
        int index = filePath.lastIndexOf("/");
        // 若系统是 windows 目录分隔符是 \
        if (SystemUtils.isWindows()) {
            index = filePath.lastIndexOf("\\");
        }
        String result = filePath.substring(0, index);
        File f = new File(result);
        if(!f.exists()){
            f.mkdirs();
        }
    }


    /**
     * @descitption 获取文件名  不带后缀
     * @param path
     * @return
     */
    public static String getFileName(String path){
        int end = path.lastIndexOf('.');
        String result = path.substring(0, end);
        logger.info("file name is: [{}]", result);
        return result;
    }

    /**
     * @description 替换反斜杠\ 为正斜杠 /  通常windows中才会存在反斜杠
     * @param filePath
     * @return
     */
    public static String getTrulyPath(String filePath){
        if(SystemUtils.isWindows()){
            logger.debug("==============windows==============");
            filePath = filePath.replaceAll("\\\\", "/");
        }
        return filePath;
    }

    /**
     * @description  根据当前系统构建存储目录
     * @param path
     * @return
     */
    public static File buildFileByPath(String path){
        path = getWindowsPath(path);
        //保证 文件夹存在
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        logger.info("directory path......filePath: [{}]", path);
        return fileDir;
    }

    /**
     * @description  获取windows下的path
     * @param path
     * @return
     */
    public static String getWindowsPath(String path){
        if(SystemUtils.isWindows() && !path.contains(Constants.Regular.base_dir)){
            logger.debug("==============windows==============");
            path = (Constants.Regular.base_dir + removePrefix(path)).replaceAll("/", "\\\\");
        }
        return path;
    }

    //移除以/开始的路径前缀
    public static String removePrefix(String path){
        if(path.startsWith("/")){
            return path.substring(1);
        }
        return path;
    }

    /**
     * @description 移除windows系统下的根目录
     * @param uploadUrl
     * @param path
     * @return
     */
    public static String removeBaseDir(String uploadUrl, String path){
        if(SystemUtils.isWindows()){
            logger.debug("==============windows==============");
            path = path.substring(Constants.Regular.base_dir.length()-1);
        }
        return uploadUrl + path;
    }

    /**
     * @description 给目录加上"/"分隔符
     * @param path
     * @return
     */
    public static String addPathSeparate(String path){
        if(!path.endsWith("/")){
            path += "/";
        }
        return path;
    }

    /**
     * @description 移除目录最后一个"/"分隔符
     * @param path
     * @return
     */
    public static String removeLastPathSeparate(String path){
        if(path.endsWith("/")){
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path;
    }

    public static String addPathSeparate(String ... paths) {
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (String path : paths){
            if(i > 0 && i < paths.length && path.startsWith("/"))
                path = path.replaceFirst("^/", "");
            if(i == paths.length-1)
                buffer.append(path);
            else
                buffer.append(addPathSeparate(path));
            i++;
        }
        return buffer.toString();
    }


    /**
     * @description  根据文件名  获取文件的拓展名
     * @param myFileName
     */
    public static String getExtName(String myFileName) {
        if(myFileName == null || "".equals(myFileName))
            return null;
        int index = myFileName.lastIndexOf(".");
        //文件没有拓展名
        if(index == -1) {
            return null;
        }
        //获取文件拓展名
        return myFileName.substring(index+1, myFileName.length());
    }

    //根据文件路径   获取文件集合
    public static List<String> getFilesByPath(String uploadUrl, String path){
        List<String> files = new ArrayList<>();
        File file = new File(path);
        if(FileUtils.existsFile(file)){
            files.addAll(getFilePath(uploadUrl, file));
        }
        return files;
    }

    //递归获取文件
    public static List<String> getFilePath(String uploadUrl, File file){
        List<String> files = new ArrayList<>();
        if(file.isDirectory()){
            for (File item : file.listFiles()){
                files.addAll(getFilePath(uploadUrl, item));
            }
        }else {
            files.add(FileUtils.getTrulyPath(FileUtils.removeBaseDir(uploadUrl, file.getPath())));
        }
        return files;
    }

    /**
     * @description   根据文件拓展名  获取文件类型
     * @param extName
     * @return
     */
    public static int getFileTypeByExtName(String extName){
        for (FileTypeEnums item : FileTypeEnums.values()){
            if(item.getName().equalsIgnoreCase(extName)){
                return item.getValue();
            }
        }
        return -1;
    }

    /**
     * 获取视频的播放时长  单位 毫秒
     * @param file
     */
    public static long getVedioDuration(File file) throws EncoderException {
        Encoder encoder = new Encoder();
        MultimediaInfo m = encoder.getInfo(file);
        return m.getDuration();
    }

    public static void main(String[] args){
        /*try {
            String text = readTextFile(new File("C:\\Users\\Administrator\\Desktop\\bs通信接口整理.txt"));
            logger.info(text);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // generateIniFile("D:\\people\\storage\\myLeds\\20170603\\test44.txt", "asdfasd asfas f  s  ");
        logger.info(getExtName("text.jpg"));
        logger.info(getExtName("text.jpg.txt"));
        logger.info(getExtName("text"));
    }

    /**
     * 获取文件大小
     * @param file
     * @return
     */
    public static long getFileSize(File file){
        if (file.exists())
            return file.length();
        else return 0L;
    }

}
