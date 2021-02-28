package com.study.core.resource;

import com.study.core.net.URLUtil;
import com.study.core.io.FileUtil;
import com.study.core.util.StrUtil;

import java.io.File;


/**
 * 文件资源访问对象
 * 
 * @author looly
 *
 */
public class FileResource extends UrlResource{

	// ----------------------------------------------------------------------- Constructor start
	/**
	 * 构造
	 * 
	 * @param file 文件
	 */
	public FileResource(File file) {
		this(file, file.getName());
	}
	
	/**
	 * 构造
	 * 
	 * @param file 文件
	 * @param fileName 文件名，如果为null获取文件本身的文件名
	 */
	public FileResource(File file, String fileName) {
		super(URLUtil.getURL(file), StrUtil.isBlank(fileName) ? file.getName() : fileName);
	}
	
	/**
	 * 构造
	 * 
	 * @param path 文件绝对路径或相对ClassPath路径，但是这个路径不能指向一个jar包中的文件
	 */
	public FileResource(String path) {
		this(FileUtil.file(path));
	}
	// ----------------------------------------------------------------------- Constructor end

}
