package com.edupay.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * 日期操作的工具类
 */
public class DateUtil {

	public static int getDaysNumByYearAndMonth(Integer year, Integer month) {
		int daysNum = 31;// 1,3,5,7,8,10,12
		if(month==4 || month==6 || month==9 || month==11){
			daysNum = 30;
		}
		else if( month==2 ){
			if( (year%4==0&&year%100!=0) || year%400==0 ){
				daysNum = 29;
			}
			else{
				daysNum = 28;
			} 
		}
		return daysNum;
	}
    /**
     * 根据date,判断和今天是否否同一天
     *
     * @param date Date
     * @return Boolean
     */
    public static boolean isSameDay(Date date) {
        return DateUtils.isSameDay(date, new Date());
    }

    /**
     * 得到今年的年份
     *
     * @return Integer
     */
    public static Integer getYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }


    /**
     * 获得当年月 Description:
     *
     * @return
     * @Version1.0 2014年12月24日 下午8:57:56 by 于科为 创建
     */
    public static String getMonth() {
        Integer month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String monthStr = "";
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = String.valueOf(month);
        }
        return monthStr;
    }

    /**
     * 获得当前天 Description:
     *
     * @return
     * @Version1.0 2014年12月24日 下午8:58:07 by 于科为 创建
     */
    public static String getDay() {
        Integer day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        String dayStr = "";
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = String.valueOf(day);
        }
        return dayStr;
    }

    /**
     * 和现在比相差多少天
     *
     * @param date Date
     * @return Integer
     */
    public static Integer getDistanceDays(Date date) {
    	Long day = (new Date().getTime() - date.getTime()) / 86400000;
        return day.intValue();
    }
    
    /**
     * 查询任意一天，前/后N天的日期
     * @param date   任意一天
     * @param numDays  前/后N天（负数表示前）
     * @return
     * @author yangzhenlin
     */
    public static Date getAnyDateOfNumDays(Date date, int numDays)
    {
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DATE, numDays);
    	date = calendar.getTime();
    	return date;
    }
    

    public static String getFormatDate(Date date, String Format) {
        SimpleDateFormat df = new SimpleDateFormat(Format);
        return df.format(date);
    }

    /**
     * @param date Calendar
     * @param fild Calendar.MONDAY~Calendar.SUNDAY
     * @return
     * @Title: getWeekDay
     * @Description: 根据给定的日期获一周内指定的日期
     */
    public static Date getWeekDay(Date date, int fild) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setFirstDayOfWeek(Calendar.MONDAY); // 以周1为首日
        // 当前时间，貌似多余，其实是为了所有可能的系统一致
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, fild);
        return calendar.getTime();
    }

    /**
     * @param date   时间字符串
     * @param format 格式化字符串
     * @return
     * @throws java.text.ParseException
     * @Title: strToDate
     * @Description: 字符串转换成时间类型
     */
    public static Date strToDate(String date, String format)
            throws java.text.ParseException {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        return formatDate.parse(date);
    }

    /**
     * @param date
     * @param format
     * @return
     * @throws java.text.ParseException
     * @Title: dateToString
     * @Description:转换时间格式
     */
    public static String dateToString(Date date, String format)
            throws java.text.ParseException {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        return formatDate.format(date);
    }
    
    /**
     * 比较时间是否再两个时间段内
     *
     * @param nowTime	当前时间
     * @param beginTime	时间段开始时间
     * @param endTime	时间段结束时间
     * @return
     * @throws ParseException 
     */
    public static boolean belongCalendar(Date nowDate, Date beginDate, Date endDate) throws ParseException {
    	if(nowDate == null || beginDate == null || endDate == null){
    		return false;
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:zzzz");
		nowDate = sdf.parse(sdf.format(nowDate));
    	beginDate = sdf.parse(sdf.format(beginDate));
    	endDate = sdf.parse(sdf.format(endDate));
    	
        Calendar date = Calendar.getInstance();
        date.setTime(nowDate);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

}
