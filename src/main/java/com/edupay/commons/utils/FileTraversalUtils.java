package com.edupay.commons.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件遍历工具
 * com.bitzh.util
 * Created by yukewi on 2015/6/25 11:16.
 */
public class FileTraversalUtils {

    /**
     * 文件通用的分隔符替换策略
     *
     * @param rawPath
     * @return
     */
    public static String commonSeparator(String rawPath) {
        rawPath = StringUtils.trim(rawPath);
        rawPath = StringUtils.replace(rawPath, "\\", "/");
        while (StringUtils.indexOf(rawPath, "//") != -1) {
            rawPath = StringUtils.replace(rawPath, "//", "/");
        }

        if (StringUtils.endsWith(rawPath, "/")) {
            rawPath = StringUtils.removeEnd(rawPath, "/");
        }
        if (!StringUtils.startsWith(rawPath, "/")) {
            rawPath = "/" + rawPath;
        }

        return rawPath;
    }

    /**
     * 获取绝对路径
     *
     * @param file
     * @return
     */
    public static String getAbsolutePath(File file) {
        return FileTraversalUtils.commonSeparator(file.getAbsolutePath());
    }

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    public static FileType getFileType(File file) {
        if (file != null && file.isDirectory()) {
            return FileType.directory;
        }
        return FileType.file;
    }

    /**
     * 获取相对路径
     *
     * @param path
     * @param base
     * @return
     */
    public static String getRelative(String path, String base) {
        if (StringUtils.contains(path, base)) {
            return StringUtils.remove(path, base);
        } else {
            throw new RuntimeException("无法获取相对路径");
        }
    }

    /**
     * 获取相对路径
     *
     * @param applicationBaseDir
     * @param file
     * @return
     */
    public static String getRelativePath(String applicationBaseDir, File file) {
        final String absolutePath = file.getAbsolutePath();
        final String absolutePathTransfered = FileTraversalUtils.commonSeparator(absolutePath);
        final String applicationBaseDirTransfered = FileTraversalUtils.commonSeparator(applicationBaseDir);

        System.out.println("************************************************************************************");
        System.out.println("*");
        System.out.println("* absolutePath                      : " + absolutePath);
        System.out.println("* absolutePathTransfered            : " + absolutePathTransfered);
        System.out.println("* applicationBaseDirTransfered      : " + applicationBaseDirTransfered);
        System.out.println("*");
        System.out.println("************************************************************************************");

        return getRelative(absolutePathTransfered, applicationBaseDirTransfered);
    }

    public static String getSuffix(File file) {
        final String name = file.getName();
        String suffix = null;
        if (file.isFile() && StringUtils.contains(name, ".")) {
            suffix = StringUtils.substringAfterLast(name, ".");
        }
        return suffix;
    }

    /**
     * 列表展示当前路径下的文件
     *
     * @param base
     * @return
     */
    public static List<File> list(String base) {
        File baseDirectory = new File(base);
        return list(baseDirectory);
    }

    /**
     * 列表展示当前路径下的文件
     *
     * @param base
     * @return
     */
    public static List<File> list(File base) {
        if (!base.isDirectory()) {
            throw new RuntimeException("目标非文件目录! -> " + base.getName());
        }
        if (!base.exists()) {
            throw new RuntimeException("目标目录不存在! -> " + base.getName());
        }
        final File[] files = base.listFiles();
        List<File> fileList = new ArrayList<>();
        if (files != null) {
            fileList.addAll(Arrays.asList(files));
        }
        sort(fileList);
        return fileList;
    }

    /**
     * 主函数入口方法
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        File f = new File("d:/");
        final File parentFile = f.getParentFile();
        System.out.println(parentFile);
    }

    public static void sort(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1 * 10000;
                }
                if (o1.isFile() && o2.isDirectory()) {
                    return 1 * 10000;
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            }

            public boolean equals(Object obj) {
                return true;
            }
        });
    }

    /**
     * 遍历当前路径下的所有的文件
     *
     * @param base
     * @return
     */
    public static List<File> traversal(String base) {
        File baseDirectory = new File(base);
        return traversal(baseDirectory);
    }

    /**
     * 遍历当前路径下的所有的文件
     *
     * @param base
     * @return
     */
    public static List<File> traversal(File base) {
        Set<File> traversalResultSet = new LinkedHashSet<>();
        final List<File> traversalList = list(base);
        traversalResultSet.addAll(traversalList);
        for (File file : traversalList) {
            if (file.isDirectory()) {
                traversalResultSet.addAll(traversal(file));
            }
        }
        return new ArrayList<>(traversalResultSet);
    }

    public enum FileType {
        directory, file
    }
    
    /**
     * 根据当前日期获取上传文件的路径(路径不存在则自动创建)
     * 格式:basePath/upload/2018/01/01/filename.xxx
     * @param request
     * @param filename
     * @return
     */
    public static String getUploadPathByDate(HttpServletRequest request, String pathPrefix, String filename) {
    	String path = request.getSession().getServletContext().getRealPath("/upload");
		path += File.separator+"zbes"+File.separator+pathPrefix+File.separator+
				DateUtil.getYear()+File.separator+DateUtil.getMonth()+File.separator+DateUtil.getDay();
		File file = new File(path);
		filename = StringUtil.getUUID() + "_" +filename;
		if(file.exists()){
			if(file.isFile()){
				return null;
			}
			return path+File.separator+filename;
		}
		file.mkdirs();
		return path+File.separator+filename;
    }

}
