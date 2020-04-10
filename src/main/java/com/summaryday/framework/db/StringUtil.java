package com.summaryday.framework.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static final Logger log = LoggerFactory.getLogger(StringUtil.class);
	

	
	//接收中文参数乱码处理
	public static String UTF8ToString(String str){
		String ss=null;
		try {
				ss = new String(str.getBytes("ISO8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				//e1.printStackTrace();
				log.error("接收中文参数乱码处理-异常："+e1);
			}
		return ss;
	}
	/**
	 * dateFormat :"yyyy/MM/dd HH:mm:ss"
	 */
	public static String TimestampToString(String dateFormat){
		String tsStr = "";
		String dateFormat1="yyyy/MM/dd HH:mm:ss";
		
		if(StringUtil.isNull(dateFormat)) 
			dateFormat1="yyyy-MM-dd HH:mm:ss";
		else
			dateFormat1=dateFormat;
		
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		DateFormat sdf = new SimpleDateFormat(dateFormat1);
		try {
			//方法一
			tsStr = sdf.format(ts);
			//System.out.println(tsStr);
			//方法二
			//tsStr = ts.toString();
			//System.out.println(tsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tsStr;
	}
	/**
	 * 时间字符串转换时间
	 * @param date :"2011-05-09 11:49:45"
	 * @return
	 */
	public static Timestamp StringToTimestamp(String date){
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		try {
			ts = Timestamp.valueOf(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}
	/**
	 * 直接获取当前Timestamp时间
	 * @return
	 */
	public static Timestamp getTimestamp(){
		Timestamp ts =null;
		try {
			ts = new Timestamp(new Date().getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}
	//去掉后带.123时间的毫秒数方法
	public static String getTimestampStringAbortMillisecond(){
		Timestamp ts=getTimestamp();
		 SimpleDateFormat dateformatAll= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义返回的日期格式
		 if(ts==null){//如果时间为空返回当前时间
		  return dateformatAll.format(new Timestamp(new Date().getTime()));
		 }
	 return dateformatAll.format(ts);//格式化传过来的时间就可以去掉毫秒数
	}
	/**
	 * 判断空值
	 * @param obj
	 * @return
	 */
	public static boolean isNull(String... obj){
		for(String s : obj){
			if(s == null || "".equals(s)){
				return true;
			}
		}		
		return false;
	}
	
	/**
	 * 判断空值
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s){
			if(s == null || "".equals(s)){
				return true;
			}
		return false;
	}
	
	//将byte数组置空
	public static Object[] resetArrayOfObject(Object[] a){
		Object[] b2 = new Object[a.length];
		for(int i=0;i<a.length;i++)
		{
		  a[i] = b2[i];
		}
		return a;
	}
	/**
	 * 过滤特殊字符
	 * @param src
	 * @return
	 */
	public static String encoding(String src) {
		if (src == null)
			return "";
		StringBuilder result = new StringBuilder();
		if (src != null) {
			src = src.trim();
			for (int pos = 0; pos < src.length(); pos++) {
				switch (src.charAt(pos)) {
				case '\"':
					result.append("&quot;");
					break;
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '\'':
					result.append("&apos;");
					break;
				/*case '&':
					result.append("&amp;");
					break;*/
				case '%':
					result.append("&pc;");
					break;
				case '_':
					result.append("&ul;");
					break;
				case '#':
					result.append("&shap;");
					break;
				case '?':
					result.append("&ques;");
					break;
				default:
					result.append(src.charAt(pos));
					break;
				}
			}
		}
		return result.toString();
	}
	
	
	
	/**
	 * 判断是否是Email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if(StringUtil.isNull(email)){
			return false;
		}
		Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		Matcher matcher = emailPattern.matcher(email);
		if(matcher.find()){
			return true;
		}
		return false;		
	}
	
	/**
	 * 判断是否是电话
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if(StringUtil.isNull(phone)){
			return false;
		}
		Pattern phonePattern = Pattern.compile("^1\\d{10}$");
		Matcher matcher = phonePattern.matcher(phone);
		if(matcher.find()){
			return true;
		}		
		return false;
	}	
	

	
	/***
	 * 验证中文名字
	 * @param name
	 * @return
	 */
	public static boolean isChineseName(String name) {
		if(StringUtil.isNull(name)){
			return false;
		}
		Pattern pattern = Pattern.compile("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]){2,5}$");
		Matcher matcher = pattern.matcher(name);
		if(matcher.find()){
			return true;
		}		
		return false;
	}
	
	/**
	 * 字符转换
	 * @param str
	 * @return
	 * 
	 */
	public static String changeChar(String str){
		String s=null;		
		try {
			s =  new String(str.getBytes("iso-8859-1"),"utf-8").toString();
		} catch (UnsupportedEncodingException e) {
			s="";
			log.info(e.getMessage());
		}
		
		return s;
	}
	
	/**
	 * 获取图片数量
	 * @return
	 */
	public static int getImageCount(String content) {
		if(content == null){
			return 0;
		}
		String regex = "<img[^>]*>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		int count = 0;
		while(matcher.find()){
			count++;
		}
		return count;
	}
	/**
	 * @param s
	 * @return
	 */
	public static String StringTrim(String s){
		String str =null;
		try{
		str = new String(s.trim());
		}catch(Exception e){
			log.info(e.getMessage());
		}
		return str;
	}
	/**
	 * 字符串截断
	 */
	@SuppressWarnings("unused")
	public static String SubStringVal(String s,int begin,int end){
		if(!s.equals("")||s!=null){
			if(s.length()>end){
			String s2=s.substring(begin, end);
			return s2;
			}
			return s;
		}else{
			return "字符串截取错误";
		}
		
	}
	/**
	 * 过滤html标签
	 * 
	 * */
	
	@SuppressWarnings("unused")
	public static String guoHtml(String s){
		if(!s.equals("")||s!=null){
			String str=s.replaceAll("<[.[^<]]*>","");
			return str;
		}else{
			return s;
		}
		
	}
	
	/**
	   * 获取现在时间
	   * 
	   * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	   */
	public static Date getNowDate() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

		java.util.Date time=null;
		try {
		   time= sdf.parse(sdf.format(new Date()));

		} catch (ParseException e) {

		   e.printStackTrace();
		}
	   return time;
	}
	
	/**
     * 将“yyyy-MM-dd”格式的日期字符串转换为日期对象。
     * 
     * @param source
     *                日期字符串。
     * @return Date 日期对象。
     */
    public static Date getParseHyphenDate(String source) {
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	 return sdf.parse(source, new ParsePosition(0));
    }

    /**
     * 将指定格式的日期字符串转换为日期对象。
     * 
     * @param source
     *                日期字符串。
     * @param pattern
     *                模式。
     * @return Date 日期对象。
     */
    public static Date getParseDate(String source, String pattern) {
	 SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	 return sdf.parse(source, new ParsePosition(0));
    }
	/**
	 * 获取当前时间，可以指定格式
	 * @param str
	 * @return
	 */
	public static Date StringToDate(String str) {  
		String ff=null;
		if(StringUtil.isNull(str)) 
			ff="yyyy-MM-dd"; 
		else
			ff=str;
        DateFormat format = new SimpleDateFormat(ff);  
        Date date = null;  
        try {  
            // Fri Feb 24 00:00:00 CST 2012  
            date = format.parse(str);   
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        // 2012-02-24  
        date = java.sql.Date.valueOf(str);  
                                              
        return date;  
    }  
	
	/**
	   * 获取现在时间
	   * 
	   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	   */
	public static String getStringDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	
	/**
	   * 获取现在时间
	   * 
	   * @return 返回短时间字符串格式yyyy-MM-dd
	   */
	public static String getStringDateShort() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	
	/**
	   * 获取时间 小时:分;秒 HH:mm:ss
	   * 
	   * @return
	   */
	public static String getTimeShort() {
	   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	   Date currentTime = new Date();
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	
	/**
	 * 要获得两个日期差，差的形式为：XX天XX小时XX分XX秒
	 * @param start  2004-03-26 13:31:40
	 * @param end   2004-01-02 11:30:24
	 * @return
	 * @throws ParseException 
	 */
	public static String start_endTime(String start,String end) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   java.util.Date now = null;
		   java.util.Date date=null;
		try {
			now = df.parse(end);
			date=df.parse(start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   long l=now.getTime()-date.getTime();
		   long day=l/(24*60*60*1000);
		   long hour=(l/(60*60*1000)-day*24);
		   long min=((l/(60*1000))-day*24*60-hour*60);
		   long s=(l/1000-day*24*60*60-hour*60*60-min*60);
		  // System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
		   return ""+day+"天"+hour+"小时"+min+"分"+s+"秒";
	}
	/**
	* @Title: StringToLong 
	* @Description: TODO(字符串转为long型) 
	* @param @param s
	* @param @return    设定文件 
	* @return Long    返回类型 
	* @throws
	 */
	public static Long StringToLong(String s) throws Exception{
		Long id=Long.parseLong(s);
		return id;
	}
	/**
	 * 字符串转整数stringToInt
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static Integer StringToInteger(String s) throws Exception{
		Integer id=Integer.parseInt(s);
		return id;
	}
	/**
	 * 字符串转为boolean类型
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static boolean StringToBoolean(String s) throws Exception{
		Boolean id=Boolean.parseBoolean(s);
		return id;
	}
	
	/*public static void main(String[] args) {
		try {
		//	Integer id=StringUtil.StringToInteger("2");
			Boolean id=Boolean.parseBoolean("true");
			System.out.println(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 取得系统时间
	 * 
	 * @return String 返回系统时间(YYYYMMDDHHMMSS)
	 */
	public static String getSysDateTime() {
		String strDate = new String();
		try {
			GregorianCalendar sysDate = new GregorianCalendar();
			strDate = String.valueOf(sysDate.get(Calendar.YEAR));
			strDate += toStringAdd(String
					.valueOf(sysDate.get(Calendar.MONTH) + 1), 2);
			strDate += toStringAdd(String.valueOf(sysDate.get(Calendar.DATE)),
					2);
			strDate += toStringAdd(String.valueOf(sysDate
					.get(Calendar.HOUR_OF_DAY)), 2);
			strDate += toStringAdd(
					String.valueOf(sysDate.get(Calendar.MINUTE)), 2);
			strDate += toStringAdd(
					String.valueOf(sysDate.get(Calendar.SECOND)), 2);
		} catch (Exception ex) {
			return "";
		}
		return strDate;
	}
	
	/**
	 * 数字字符串指定长度，左侧补0
	 * 
	 * @param text
	 *            处理的数字字符串
	 * @param length
	 *            指定长度
	 * @return 处理后的数字字符串

	 */
	public static java.lang.String toStringAdd(java.lang.String text, int length) {
		String OutString = "";
		try {
			if (text.length() == length) {
				OutString = text;
			} else {
				OutString = "";
				while (length > text.length()) {
					OutString = OutString + "0";
					length--;
				}
				OutString = OutString + text;
			}
		} catch (Exception ex) {
			return "";
		}
		return OutString;
	}
	/**
	 * int型数字转换

	 * 
	 * @param strNumber
	 *            要求转换的数字

	 * @return 转换为int型的数字
	 */
	public static int toInteger(String strNumber) {
		int retInt = 0;
		try {
			if (strNumber == null || strNumber.equals("") || "null".equals(strNumber) || "NULL".equals(strNumber)) {
				return 0;
			}
			retInt = Integer.parseInt(strNumber);
		} catch (Exception e) {
			return 0;
		}
		return retInt;
	}
	
	/**
	 * 字符串split处理
	 * 
	 * @return fieldArr
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] split(String splitString, String splitFlag) {
		String[] fieldArr;
		try {
			if (splitString == null) {
				return null;
			} else if (splitFlag == null) {
				fieldArr = new String[1];
				fieldArr[0] = splitString;
				return fieldArr;
			}
			Vector tempVector = new Vector();
			String tempString;
			int currPos = splitString.indexOf(splitFlag);
			int lastPos = 0;
			while (currPos >= 0) {
				tempString = splitString.substring(lastPos, currPos);
				tempVector.addElement(tempString);
				lastPos = currPos + splitFlag.length();
				currPos = splitString.indexOf(splitFlag, lastPos);
			}
			tempString = splitString.substring(lastPos, splitString.length());
			tempVector.addElement(tempString);

			fieldArr = new String[tempVector.size()];
			for (int i = 0; i < tempVector.size(); i++) {
				fieldArr[i] = (String) tempVector.elementAt(i);
			}
			return fieldArr;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	/**
	 * null 空字符串的替换处理<BR>
	 * 
	 * @param s
	 *            处理的字符串
	 * @return 处理后的字符串

	 */
	public static String nullToBlank(String s) {
		if (null == s || "null".equals(s) ||"NULL".equals(s)) {
			return "";
		} else {
			return s.trim();
		}
	}
	
	
	/**
	 * 字符串指定位数变换，前边补0
	 * 
	 * @param str
	 *            要求转换的字符串
	 * @param len
	 *            指定的字符串位数
	 * @return 转换后的字符串

	 */
	public static String formatStringByZero(String str, int len) {
		String tempString = "";
		if (str == null || str.equals("")) {
			for (int i = 0; i < len; i++) {
				tempString += "0";
			}
			return tempString;
		}
		if (str.length() >= len) {
			return str;
		}
		for (int i = 0; i < len - str.length(); i++) {
			tempString = "0" + tempString;
		}
		return tempString + str;
	}
	
	public static String haoAddOne(String liuShuiHao){
	    Integer intHao = Integer.parseInt(liuShuiHao);
	    intHao++;
	    String strHao = intHao.toString();
	    while (strHao.length() < liuShuiHao.length())
	        strHao = "0" + strHao;
	    return strHao;
	}

	
//	通过HashSet剔除 
//  删除ArrayList中重复元素
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public   static   List  removeDuplicate(List list)   { 
		    HashSet h  =   new  HashSet(list); 
		    list.clear(); 
		    list.addAll(h); 
		   //System.out.println(list); 
		    return list;
		} 
	 
	 
	// 删除ArrayList中重复元素，保持顺序 
	// 删除ArrayList中重复元素，保持顺序 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public   static   List  removeDuplicateWithOrder(List list)   { 
	      Set set  =   new  HashSet(); 
	      List newList  =   new  ArrayList(); 
	   for  (Iterator iter  =  list.iterator(); iter.hasNext();)   { 
	         Object element  =  iter.next(); 
	         if  (set.add(element)) 
	            newList.add(element); 
	     } 
	     list.clear(); 
	     list.addAll(newList); 
	    // System.out.println( " remove duplicate "   +  list); 
	     return list;
	} 
/*//可以直接利用set得唯一性来解决
  public static List<Power> deleteDupDate(List<Power> listPower1){
	  List newList  =   new  ArrayList(); 
	  Set hs = new HashSet();  
	  for(int i=0;i<listPower1.size();i++){  
		  hs.add(listPower1.get(i));    
		  System.out.print(" <===拆==>"+listPower1.get(i).getPowerName()+"\n");   
		  } 
	  Iterator it = hs.iterator();  
	  while(it.hasNext()){    
		  Power s = (Power) it.next();   
	  System.out.print("\n"+s.getPowerName()+" <==组===");   
		//  listPower1.clear();
		  newList.add(s);
		  }
	  return newList;
  }
  */
//删除ArrayList中重复元素 
/*  public   static   List<Power>  delDuplicate(List<Power> list)   { 
	  //List newList  =   new  ArrayList(); 
     for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )   { 
      for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )   { 
        if  (list.get(j).getPowerName().equals(list.get(i).getPowerName()))   { 
          list.remove(j); 
        } 
      } 
    } 
    //System.out.println(list); 
    return list;
  }*/
  
  
    //是否是数字加字符的格式
	public static boolean isNumber(String str){
		   for(int i=0;i<str.length();i++){//循环遍历字符串
		    if(Character.isDigit(str.charAt(i))&&Pattern.compile("(?i)[a-z]").matcher(str).find()){//用char包装类中的判断数字的方法判断每一个字符
		     return true;
		    }
		   }
		   return false;
	}
	//判断是否是单个字符格式的
	public static boolean isCract(String str){
		 String reg = "[a-zA-Z]";

		   boolean isCract = str.matches(reg);
		   if(isCract){
			   return true;
		   }
		   return false;
	}
/**
 * 
* @Title: readTxtByCityId 
* @Description: TODO(读取城市id与缩写) 
* @param @param cityid
* @param @param file
* @param @return    返回城市缩写 
* @return String    返回类型 
* @throws
 */  
 @SuppressWarnings("null")
public static String readTxtByCityId(Integer cityid,String file){
	       String cityNO="error";
	       String line = null;
	       BufferedReader bw=null;
	     
	   try {
			bw = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			 try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.error("读取文件"+file+"异常"+e2);
		}
	       
	   try {
	    	   while((line = bw.readLine()) != null){
	    	 
	    		   if(line.indexOf(cityid.toString())>-1){
	    			   //System.out.println(line);
	    			   String[] arr=line.split("=");
	    			   if(arr[0].equals(cityid.toString())){
	    				   cityNO=arr[1];
	    			   }
	    		   }
	    	   }
	    	   bw.close();
	    	  } catch (IOException e) {
	    		try {
					bw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					log.error("BufferedReader关闭异常:"+e1);
				}
				log.error("关闭"+file+"异常"+e);
	    	  }
	        return cityNO;
	    }
	 
/*	public static void main(String[] args) {
		       String file="d:\\city.txt";
			 System.out.println("所在城市编号:"+StringUtil.readTxtByCityId(3,file));
		
	} */
	 
	 
	 	/**
     * 检验输入是否为正确的日期格式(不含秒的任何情况),严格要求日期正确性,格式:yyyy-MM-dd HH:mm
     * @param sourceDate
     * @return
     */
    public static boolean checkDate(String sourceDate){
        if(sourceDate==null){
            return false;
        }
        try {
               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
               dateFormat.setLenient(false);
               dateFormat.parse(sourceDate);
               return true;
        } catch (Exception e) {
        }
         return false;
    }
    
    /**
	 * 上传图片
	 * @param request
	 * @param response
	 * @throws IOException
     * @throws FileUploadException 
	 */
/*	private String[] upLoadCommon(HttpServletRequest request) throws IOException, FileUploadException {
		File file0 = null;
		File file1 = null;
		String []tmp_file_name={};
		String filePath =ConnectionFactory.FILE_UP_LOAD;
		if(null!=filePath){
			DiskFileUpload disFileUpload = new DiskFileUpload();
			//String filePath =getServletConfig().getServletContext().getRealPath("");
			List<FileItem> list = disFileUpload.parseRequest(request);
			for (FileItem fileItem : list) {
				if (fileItem.getName() != null) {
					String name = new String(fileItem.getName().getBytes(), "UTF-8");
					log.info("上传文件原名:"+name);
					if(flieformat(name, "2")){
						String file_date=DateUtil.getCurDate();
						String file_name=DateUtil.dateToDateString(new Date(), "yyyy_MM_dd_HH_mm_ss_SSS")+".jpg";
						filePath = filePath + File.separator+file_date;
						tmp_file_name[0]=name;
						// System.out.println("Name=" + fileItem.getName());
						// 写入的位置,从相册中选择的图片没有后缀，需要自己处理下
						file0 = new File(filePath);
						if (!file0.exists())
							file0.mkdirs();// 生成文件夹
							filePath = filePath + File.separator + name+file_name;
							file1 = new File(filePath);
							InputStream ins = fileItem.getInputStream();
							OutputStream ous = new FileOutputStream(file1);
							try {
								byte[] buffer = new byte[1024 * 64];
								int len = 0;
								while ((len = ins.read(buffer)) > -1) {
									ous.write(buffer, 0, len);
								}
								
							} finally {
								ous.close();
								ins.close();
								tmp_file_name[1]=file_name;
							}
							
					  }else{
						  log.info(name+"图片格式不符合要求，上传失败.");
					  }
							
				}
			  }
		}else{
			 log.info(filePath+"图片路径不正确,上传失败.");
		}
		
		return tmp_file_name;
	}
	*/
	/**
	 * 资料扩展名判断
	 * @param name
	 * @param filetype:1为图片(jpg,png,gif)；2为头像(jpg,png,gif)；3为文档资料(doc,docx,zip,xls,ppt,pptx);
	 * @return
	 */
	public boolean  flieformat(String name,String filetype){
		boolean flag=false;
		String[] file=null;
		String[] ffimg=new String[]{".jpg",".png",".gif",".jpeg"};
		String[] ffdoc=new String[]{".doc",".docx",".ppt",".pptx",".zip",".rar",".pdf"};
		if("1".equals(filetype)||"2".equals(filetype)){
			file=ffimg;
			for(int i=0;i<file.length;i++){
				flag=name.toLowerCase().indexOf(file[i])>0 ? true:false;
				if(flag) break;
			}
		}else
		if("3".equals(filetype)){
			file=ffdoc;
			for(int i=0;i<file.length;i++){
				flag=name.toLowerCase().indexOf(file[i])>0 ? true:false;
				if(flag) break;
			}
		}
		//log.info("flag:"+flag);
		return flag;
	} 
	
 /**
  * 
 * @Title: listSort 
 * @Description: TODO(对list进行排序) 
 * @param @param list
 * @param @return    设定文件 
 * @return List<?>    返回类型 
 * @throws
  */
/*public static List<Power> listSort(List<Power> list,Power p){
	  List<Power> listPower=new ArrayList<Power>();
		 Collections.sort(list,p);
		 for(int i=0;i<list.size();i++){
			 p=list.get(i);
			// System.out.println(p.getPowerName());
			 listPower.add(p);
		 }
	  return listPower;
  }*/
  
/*	 public static void main(String[] args) {
		 List listPower=new ArrayList();
		 listPower.add(new PowerVo(1,0,"数据中心","/adddrole.jhtml","阿萨","2012-03-22",2));
		 listPower.add(new PowerVo(3,0,"任务中心","/adddrole.jhtml","阿萨","2012-03-22",3));
		 listPower.add(new PowerVo(2,0,"系统中心","/adddrole.jhtml","阿萨","2012-03-22",1));
		 PowerVo pvo=new PowerVo();
		 Collections.sort(listPower,pvo);
		 
		 for(int i=0;i<listPower.size();i++){
			 pvo=(PowerVo)listPower.get(i);
			 System.out.println(pvo.getPowerName());
		 }
	}*/
	public static void main(String[] args) {
		String d=DateUtil.getCurDate();
		System.out.println(d);
	}
 
}
