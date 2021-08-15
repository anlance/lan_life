package club.anlan.lanlife.storage;

import club.anlan.lanlife.storage.file.FileHandler;
import club.anlan.lanlife.storage.file.FileHandlerFactory;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 */
public class FileUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 将文件转成字节流
	 * @param fileName 文件路径
	 */
	public static byte[] getBytes(String fileName) {

        if(!(new File(fileName).exists())){
            return null;
        }
        InputStream inputStream = null;
        BufferedInputStream buf = null;
        ByteArrayOutputStream outByte = null;
		try {
			inputStream = new FileInputStream(fileName);
			buf = new BufferedInputStream(inputStream);
			outByte = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = buf.read(b, 0, 1024)) != -1) {
				outByte.write(b, 0, i);
			}
			outByte.flush();
			return outByte.toByteArray();
		} catch (IOException e) {
			logger.error("Read file error! ", e);
			return null;
		}finally {
			if(buf != null) {
				try {
					buf.close();
				} catch (IOException e) {
					logger.error("buf close error! ", e);
				}
			}
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error("inputStream close error! ", e);
				}
			}
			if(outByte != null) {
				try {
					outByte.close();
				} catch (IOException e) {
					logger.error("outByte close error! ", e);
				}
			}
		}
	}
    /**
     * input转成file
     * @param inputStream
     * @param file
     */
	public static void inputStreamToFile(InputStream inputStream, File file){
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int byteLen = 0;
			byte[] buf = new byte[inputStream.available()];
			while((byteLen = inputStream.read(buf, 0, inputStream.available())) > 0){
				os.write(buf, 0, byteLen);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(os != null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void bytesToFile(byte[] bytes, File file) {
		OutputStream os = null;
		try {
			int len = bytes.length;
			if(len > 0) {
				os = new FileOutputStream(file);
				os.write(bytes, 0, len);
			}
		} catch (Exception e) {
			logger.debug("bytesToFile is failed", e);
			e.printStackTrace();
		}finally{
			try {
				if(os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static InputStream bytesToInputStream(byte[] bytes){
		return new ByteArrayInputStream(bytes);
	}
	
	/**
	 * 将文件转成字节流
	 * @param folder 文件所属文件夹
	 * @param fileName 文件名
	 * @return
	 */
	public static byte[] getBytes(String folder, String fileName) {

		return getBytes(getFolder(folder) + fileName);
	}

	/**
	 * 将字节流转成文件
	 * @param sourceBytes
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void writeBytes(byte[] sourceBytes, String targetFile) throws IOException {
		FileOutputStream fileOutputStream = null;
		try {
			int index = targetFile.lastIndexOf(File.separator);
			if (index != -1) {
				FileUtil.makedirs(targetFile.substring(0, index));
			}
			fileOutputStream = new FileOutputStream(targetFile);
			fileOutputStream.write(sourceBytes);
		} catch (IOException e) {
			logger.error("Save file error! ", e);
		} finally {
			if(fileOutputStream != null){
				fileOutputStream.close();
			}
		}
	}

	/**
	 * 创建文件夹，若文件夹已存在，则不创建
	 * @param path
	 */
	public static void makedirs(String path) {

		if (!StringUtils.isEmpty(File.separator)) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}

	/**
	 * 将字节流转成文件
	 * @param sourceBytes
	 * @param targetFolder
	 * @param targetFile
	 * @throws IOException 
	 */
	public static void writeBytes(byte[] sourceBytes, String targetFolder, String targetFile) throws IOException {
		writeBytes(sourceBytes, getFolder(targetFolder) + targetFile);
	}

	
	private static String getFolder(String folder) {

		if (StringUtils.isEmpty(folder)) {
			folder = File.separator;
		}

		if (!File.separator.equals(folder.substring(folder.length() - 1))) {
			folder = folder + File.separator;
		}
		return folder;
	}

	/**
	 * 读取属性文件
	 * @param fileName 属性文件名称
	 * @return
	 */
	public static Properties getProperties(String fileName) {
		// 属性集合对象
		Properties result = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
			// 将属性文件流装载到Properties对象中
			result.load(fis);

		} catch (Exception e) {
			logger.error("Save file error! " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				logger.error("Save file error! " + e.getMessage());
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 
	 *保存属性文件
	 *
	 * @param savedProp 要保存的属性
	 * @param targetFile 属性文件
	 */
	public static void writeProperties(Properties savedProp, String targetFile) {

		writeProperties(savedProp, null, targetFile);
	}

	/**
	 * 
	 *保存属性文件
	 *
	 * @param savedProp 要保存的属性
	 * @param comments 属性注释
	 * @param targetFile 属性文件
	 */
	public static void writeProperties(Properties savedProp, String comments,
			String targetFile) {

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(targetFile);
			savedProp.store(fout, comments);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fout != null) {
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 *删除文件
	 *
	 * @param pathname
	 */
	public static void deleteFile(String pathname){
		
		File file = new File(pathname);
		if(file.isFile() && file.exists()){
			file.delete();
		}
		else{
			logger.error("File["+ pathname +"] not exists!");
		}
		
	}
	
	/**
	 * 
	 * 拷贝文件
	 *
	 * @param src
	 * @param dest
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFile(String src, String dest) throws FileNotFoundException, IOException{
		if(StringUtils.hasLength(src) && StringUtils.hasLength(dest)){
			FileCopyUtils.copy(new File(src), new File(dest));
		}
	}
	
	/**
	 * 
	 * 拷贝文件
	 *
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFile(File src, File dest) throws IOException{
		FileCopyUtils.copy(src, dest);
	}
	
	/**
	 * 
	 * 文件重命名
	 *
	 * @param path
	 * @param oldName
	 * @param newName
	 */
	public static void renameFile(String path, String oldName, String newName){
		if(StringUtils.hasLength(path) && StringUtils.hasLength(oldName) && StringUtils.hasLength(newName)){
			renameFile(new File(path+oldName), new File(path+newName));
		}
	}
	
	/**
	 * 
	 * 文件重命名
	 *
	 * @param oldFile
	 * @param newFile
	 */
	public static void renameFile(File oldFile, File newFile){
		if(oldFile!=null && newFile!=null){
			if(newFile.exists()){
				logger.info(newFile.getName()+"已经存在！");
			}else{
                oldFile.renameTo(newFile);
			}
		}
	}

	/**
	 * 
	 * 获取文件扩展名
	 *
	 * @param fileName
	 * @return
	 */
	public static String getExtention(String fileName) {
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(pos + 1);
	}
	
	/**
	 * 
	 * 删除文件
	 *
	 * @param path
	 * @param fileName
	 */
	public static boolean deleteFile(String path, String fileName){
		return deleteFile(new File(path+fileName));
	}
	
	/**
	 * 
	 * 删除文件
	 *
	 * @param file
	 */
	public static boolean deleteFile(File file){
		if(file != null){
			if(file.exists() && file.isFile()){
				return file.delete();
			}
		}
		return false;
	}

	/**
	 * 获取文件分隔符
	 * @return
	 */
	public static String getFileSeparator() {
		return File.separator;
	}

	/**
	 * 把一个字符串写到指定文件中
	 * @param str
	 * @param path
	 * @param fileName
	 * @throws IOException 
	 */
    public static void writeStringToFile(String str,String path,String fileName) throws IOException{
    	FileWriter fw = null;
    	try {
    		File fileDir = new File(path);
        	if(!fileDir.exists()){
        		fileDir.mkdirs();
        	}
        	File file = new File(path+fileName);
    		if(!file.exists()){
        		file.createNewFile();
        	}
			fw = new FileWriter(file,true);
			fw.write(str);
			fw.flush();
		} catch (IOException e) {
			logger.error("load in file error", e);
		} finally {
			if(fw != null){
				fw.close();
			}
		}
    }
    
    /**
     * 从文件中读取所有字符串
     * @param path
     * @param fileName
     * @return
     */
  /*  public static String readStringFromFile(String path,String fileName){
    	StringBuffer fileInString = null;
    	File fileDir = new File(path);
    	if(!fileDir.exists()){
    		return null;
    	}
    	File file = new File(path+fileName);
    	if(!file.exists()){
    		return null;
    	}
    	try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = "";
			while((str = br.readLine()) != null){
				fileInString = fileInString.append(str);
			}
		} catch (Exception e) {
			logger.error("read file error");
			return null;
		}
    	return fileInString.toString();
    }*/
    
    /**
     * 获取去掉扩展名的文件名称
     * @param fileName
     * @return      
     */
    public static String getFileNameWithoutExtname(String fileName){
        //获取扩展名
        if(fileName.lastIndexOf(".") >= 0 ){
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        return null;
    }
    
    /**
     * 获取指定路径的磁盘剩余空间
     */
    public static Long getFreeSpace(String path) {
        Long freeSpace = 0L;
        File file = new File(path);
        if( file.exists() ){
            freeSpace = file.getUsableSpace();
        }else{
            if( file.mkdirs() ){
                freeSpace = file.getUsableSpace();
            }else{
                return null;
            }
        }
        return freeSpace;
    }
    
    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return      
     * @since  Ver 3.0
     */
    public static String getExtName(String fileName){
        int extSeperatorIndex = fileName.lastIndexOf(".");
        if (extSeperatorIndex >= 0) {
            return fileName.substring(extSeperatorIndex);
        }
        return null;
    }
    
    public static String createNewFileName( String extName){
        //通过时间产生一个随机数种子
        Random random = new Random(System.currentTimeMillis());
        return random.nextLong() + extName;
    }
    
    public static boolean deleteDir(String path){
    	File file = new File(path);
		if (file.isDirectory()) {
			String[] children = file.list();
			if(children != null){
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteDir(file + "/" + children[i]);
					if (!success)
						return false;
				}
			}
		}
    	return file.delete();
    }
    

    /**
     * 删除指定文件夹下所有文件及文件夹
     * 
     * @param path
     *            文件夹完整绝对路径
     */
    public static void deleteFiles(String path) {
        try {
        	if(path == null){
        		return ;
        	}
            File file = new File(path);
            if (file.exists()) {
                deleteDir(file); // 如果没有自动创建一个
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
    static private void deleteDir(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if(files != null){
            	 for (int i = 0; i < files.length; i++) {
                     if (files[i].isDirectory()) {
                         deleteDir(files[i]);
                     } else {
                         deleteFile(files[i]);
                     }
                 }
            }
            file.delete();
        } else {
            deleteFile(file);
        }
    }

    public static boolean deleteFileByName(String fileName) {
        boolean is = false;
        File file = new File(fileName);
        if (file.isFile()) {
            is = file.delete();
        }
        return is;
    }
    
    public static void createFileDirectory(String path) {
        try {
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs(); // 如果没有自动创建一个
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }
    /**
     * @Description:修改文件中指定的区域
     */
    public static void modifyFile(String filePath,String oldStr,String newStr) throws IOException{
    	FileInputStream in = null;
    	PrintWriter out = null;
    	try {
			in = new FileInputStream(filePath);
			byte[] bytes = new byte[in.available()];
			int len = in.read(bytes);
			while(len != -1) {
    			len = in.read(bytes);
    		}
			String str = new String(bytes);
			str = str.replace(oldStr, newStr);
			out = new PrintWriter(filePath);
			out.write(str.toCharArray());
			out.flush();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if(in != null){
				in.close();
			}
			if(out != null){
				out.close();
			}
		}
    }
    /**
     * @Description:读取文件的内容
     */
    public static String getFileContent(String path) throws IOException{
    	StringBuffer sb = new StringBuffer();
    	BufferedReader bufferedReader = null;
    	FileInputStream fileInputStream = null;
        try {
			fileInputStream = new FileInputStream(path);
			bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			String s = null;
			while ((s = bufferedReader.readLine()) != null) {
				sb.append(s);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if(bufferedReader != null){
				bufferedReader.close();
			}
			if(fileInputStream != null){
				fileInputStream.close();
			}
			
		}
		return sb.toString();
    }
    
    /**
     * @descript:解压ZIP文件或者RAR文件
    
     * @param file
     * @return
     * @throws BusinessException:
     */
    /*public static List<FileModel> decompressZipOrRar(MultipartFile file) throws BusinessException {
    	//判断文件是否为zip文件
		String filename = file.getOriginalFilename();
		if (filename.toLowerCase().endsWith("zip")) {
			return unzip(file);
		}else if (filename.toLowerCase().endsWith("rar")) {
			return unrar(file);
		}
		return null;
    }*/
    

	
     /**
      * @descript:判断是否是中文
     
      * @param str
      * @return:
      */
     public static boolean existZH(String str){
    	 String regEx = "[\\u4e00-\\u9fa5]";
    	 Pattern p = Pattern.compile(regEx);
    	 Matcher matcher = p.matcher(str);
    	 while(matcher.find()){
    		return true; 
    	 }
    	 return false;
     }

	/**
	 * 返回文件url接口地址
	 * @param fileHandlerFactory
	 * @param convertUrl
	 * @param fileId
	 * @return
	 */
	public static String getFileUrl(FileHandlerFactory fileHandlerFactory, String convertUrl, String fileId) {
   		FileHandler handler = fileHandlerFactory.getFileHandler();
   		if (handler != null) {
   			StringBuilder imgUrl = new StringBuilder(convertUrl).append(handler.getFilePrefix());
   			return new StringBuilder(imgUrl).append(Constants.SLASH).append(fileId).toString();
   		}
   		return null;
   	}
   	
    //获取文件的字节数组
    public static byte[] getFileBytes(InputStream inputStream) {
        ByteArrayOutputStream outByte = null;
        try {
            outByte = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = inputStream.read(buf, 0, 1024)) != -1) {
                outByte.write(buf, 0, i);
            }
            outByte.flush();
            return outByte.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
                if(outByte != null) {
                    outByte.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 将文件下载到指定路径
     * @param urlString 下载文件url
     * @param filename  下载文件名称
     * @param savePath  保存路径
     * @throws Exception
     */
	public static void download(String urlString, String filename,String savePath) throws Exception {
		InputStream is = null;
		OutputStream os = null;
		try {
			// 构造URL  
	        URL url = new URL(urlString);  
	        // 打开连接  
	        URLConnection con = url.openConnection();  
	        //设置请求超时为5s  
	        con.setConnectTimeout(5*1000);  
	        // 输入流  
	        is = con.getInputStream();  
	        // 1K的数据缓冲  
	        byte[] bs = new byte[1024];  
	        // 读取到的数据长度  
	        int len;  
	        // 输出的文件流  
	        File sf=new File(savePath);  
	        if(!sf.exists()){
	        	sf.mkdirs();  
	        }  
	        os = new FileOutputStream(sf.getPath() + "/" + filename);  
	        // 开始读取  
	        while ((len = is.read(bs)) != -1) {
	        	os.write(bs, 0, len);  
	        }  
		} catch (Exception e) {
			logger.error("将文件下载到指定路径失败", e);
		} finally {
			// 完毕，关闭所有链接  
			if(os != null){
                try {
                	os.close();
                } catch (IOException e) {
                    logger.error("关闭IO流异常", e);
                }
            }
			if(is != null){
                try {
                	is.close();
                } catch (IOException e) {
                    logger.error("关闭IO流异常", e);
                }
            }
		}
	}
	/**
	 * 文件、文件夹压缩为zip,并导出
	 * @param srcDir 源文件路径
	 * @param out	 
	 * @param KeepDirStructure 是否保留源文件文件目录格式
	 * @throws RuntimeException
	 */
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException{
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    logger.error("【FileUtil】 method toZip error");
                }
            }
            if(out != null){
                try {
                	out.close();
                } catch (IOException e) {
                    logger.error("【FileUtil】 method toZip error");
                }
            }
        }
    }

	private static void compress(File sourceFile, ZipOutputStream zos, String name,boolean KeepDirStructure) throws Exception{
		FileInputStream in = null;
		try {
			byte[] buf = new byte[1024];
	        if(sourceFile.isFile()){
	            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
	            zos.putNextEntry(new ZipEntry(name));
	            // copy文件到zip输出流中
	            int len;
	            in = new FileInputStream(sourceFile);
	            while ((len = in.read(buf)) != -1){
	                zos.write(buf, 0, len);
	            }
	            // Complete the entry
	            zos.closeEntry();
	        } else {
	            File[] listFiles = sourceFile.listFiles();
	            if(listFiles == null || listFiles.length == 0){
	                // 需要保留原来的文件结构时,需要对空文件夹进行处理
	                if(KeepDirStructure){
	                    // 空文件夹的处理
	                    zos.putNextEntry(new ZipEntry(name + "/"));
	                    // 没有文件，不需要文件的copy
	                    zos.closeEntry();
	                }
	            }else {
	                for (File file : listFiles) {
	                    // 判断是否需要保留原来的文件结构
	                    if (KeepDirStructure) {
	                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
	                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
	                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
	                    } else {
	                        compress(file, zos, file.getName(),KeepDirStructure);
	                    }
	                }
	            }
	        }
		} catch (Exception e) {
			logger.error("zip导出异常", e);
		} finally {
			if(in != null){
                try {
                	in.close();
                } catch (IOException e) {
                    logger.error("关闭IO流异常", e);
                }
            }
		}
	}
}
