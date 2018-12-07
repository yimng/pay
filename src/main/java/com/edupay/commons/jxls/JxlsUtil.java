/**
 *
 * @Title JxlsUtil.java
 * @author wanglu
 * @date 2018年4月25日
 */
package com.edupay.commons.jxls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;

/**  
 *
 * Title JxlsUtil 
 * @author wanglu  
 * @date 2018年4月25日  
 */
public class JxlsUtil {
	static{
        //添加自定义指令（可覆盖jxls原指令）
        XlsCommentAreaBuilder.addCommandMapping("image", ImageCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("each", EachCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("merge", MergeCommand.class);
        XlsCommentAreaBuilder.addCommandMapping("link", LinkCommand.class);
    }
    /** jxls模版文件目录 */
    private final static String TEMPLATE_PATH = "jxlsTemplate";
    /**
     * 导出excel
     * @param is - excel文件流
     * @param os - 生成模版输出流
     * @param beans - 模版中填充的数据
     * @throws IOException 
     */
    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> beans) throws IOException {
        Context context = new Context();
        if (beans != null) {
            for (String key : beans.keySet()) {
                context.putVar(key, beans.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer  = jxlsHelper.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        funcs.put("jx", new JxlsUtil());    //添加自定义功能
        evaluator.getJexlEngine().setFunctions(funcs);
        jxlsHelper.processTemplate(context, transformer);
    }

    /**
     * 导出excel
     * @param xlsPath excel文件
     * @param outPath 输出文件
     * @param beans 模版中填充的数据
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static void exportExcel(String xlsPath, String outPath, Map<String, Object> beans) throws FileNotFoundException, IOException {
            exportExcel(new FileInputStream(xlsPath), new FileOutputStream(outPath), beans);
    }
    
    /**
     * 导出excel
     * @param xlsPath 模板路径
     * @param response 返回请求
     * @param beans 数据封装map
     * @param downloadAttachBaseName 下载文件名字
     */
    public static void exportExcel(String xlsPath, HttpServletResponse response, Map<String, Object> beans,String downloadAttachBaseName){
    	FileInputStream is = null;
    	try{
    		response.setContentType("application/vnd.ms-excel");
    		String attachmentFileName = URLEncoder.encode(downloadAttachBaseName, "UTF-8");
    		if(xlsPath.endsWith("xlsx")){
    			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.xlsx\"", attachmentFileName));
    		}else{
    			response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.xls\"", attachmentFileName));
    		}
    		response.setHeader("Content-Transfer-Encoding", "binary");
    		File template = JxlsUtil.getTemplate(xlsPath);
    		is = new FileInputStream(template);
    		ServletOutputStream outputStream = response.getOutputStream();   
    		exportExcel(is,outputStream, beans);
    		outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error encountered while exporting excel file for CLASSNUS.", e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
    }


    /**
     * 导出excel
     * @param xls excel文件
     * @param out 输出文件
     * @param beans 模版中填充的数据
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static void exportExcel(File xls, File out, Map<String, Object> beans) throws FileNotFoundException, IOException {
            exportExcel(new FileInputStream(xls), new FileOutputStream(out), beans);
    }
    /**
     * 获取jxls模版文件
     */
    public static File getTemplate(String name){
        String templatePath = JxlsUtil.class.getClassLoader().getResource(TEMPLATE_PATH).getPath();
        File template = new File(templatePath, name);
        if(template.exists()){
            return template;
        }
        return null;
    }

    // 日期格式化
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return null;
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 返回第一个不为空的对象
    public Object defaultIfNull(Object... objs) {
        for (Object o : objs) {
            if (o != null)
                return o;
        }
        return null;
    }

    // if判断
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }
}
