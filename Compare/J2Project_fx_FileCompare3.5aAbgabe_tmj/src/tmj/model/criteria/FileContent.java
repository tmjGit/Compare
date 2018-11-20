package tmj.model.criteria;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.pmw.tinylog.Logger;
import tmj.model.FSCompareCriterion;
import tmj.model.FSCompareFileValue;

public class FileContent extends FSCompareCriterion {
	public FileContent(FSCompareFileValue[] criterionValues) {
		super(criterionValues,"Inhalt",false,true,false, f->readDisplayValueStatic(f) );
	}

	@Override
	public boolean compareConcreteIntern(File[] files,boolean updateValue) { // make flexible
		Logger.trace("compareConcreteIntern...");
//		if(files.length<2) // super class checked this
//			return false;
//		if (files[0]==null ^ files[1]==null)
//			return false;
//		if(files[0]==null)
//			return true;
		byte[][] fileBytes=new byte[files.length][];
		for (int i=0; i<files.length; i++) {
			fileBytes[i]= readFileAsBytes(files[i]);
			if (updateValue) {
				setFileValue(i,files[i]);
			}
		}
		if (fileBytes[0]==null || fileBytes[1]==null) {
			return false;
		}

		return Arrays.equals(fileBytes[0], fileBytes[1]);
	}
	
	private static byte[] readFileAsBytes(File file) {
		return readFileAsBytesIntern(file, 0); // 0 means full file.
	}

	protected static String readDisplayValueStatic(File file) {
		byte[] fileBytes=readFileAsBytesIntern(file,30);
		if(fileBytes==null)
			return null;
		return new String(fileBytes, StandardCharsets.UTF_8);
	}

//	@Override
//	protected String readDisplayValue(File file) {
//		return readDisplayValueStatic(file);
//	}
	
	private static byte[] readFileAsBytesIntern(File file,int maxBytes) {
		if (file==null) {
			Logger.warn("file=null!");
			return null;
		}
		try (InputStream input = new BufferedInputStream(new FileInputStream(file));) {
			if(maxBytes==0)
				maxBytes=(int) file.length();//// TODO works with small files, only?
			byte[] fileBytes = new byte[maxBytes]; 
			input.read(fileBytes);
			return fileBytes;
		} catch (FileNotFoundException e) {
			Logger.error("File \"{}\" could not be found!",file);
		} catch (IOException e) {
			Logger.error("Error: Could not access file \"{}\"!",file);
		}
		return null;
	}
}
