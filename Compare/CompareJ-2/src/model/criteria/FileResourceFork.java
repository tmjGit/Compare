package model.criteria;

import java.io.File;
import model.FSCompareCriterionElement;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileResourceFork extends FSCompareCriterionElement {
	public FileResourceFork() {
		super("ResourceFork", false, true, false);
	}
	
	public static File rfFile(File f) {
		File file=new File( f.getAbsolutePath() );
		return file;//TOO
	}

	@Override
	public boolean compareTo(File f) {
		return compare(file, f);
	}

	@Override
	public boolean compare(File f1, File f2) {
		byte[] file1Bytes, file2Bytes;
		try {
			file1Bytes = readFileAsBytes(f1);
			file2Bytes = readFileAsBytes(f2);
			if (file1Bytes == null || file2Bytes == null) {
				return false;
			}

			return Arrays.equals(file1Bytes, file2Bytes);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	private byte[] readFileAsBytes(File file) throws FileNotFoundException, IOException {
		if (file == null) {
			System.err.println("FileContent.readFileAsBytes file=null!");
			return null;
		}
		byte[] fileBytes = new byte[(int) file.length()]; // TODO works with small files, only?
		try (InputStream input = new BufferedInputStream(new FileInputStream(file));) {
			input.read(fileBytes);
		}
		return fileBytes;
	}

	@Override
	protected String determineValueContrete(File file) {
		if (file == null) {
			System.err.println("FileContent.determineValueContrete file=null!");
			return null;
		}
		byte[] fileBytes = new byte[30];
		try (InputStream input = new BufferedInputStream(new FileInputStream(file));) {
			input.read(fileBytes);
			return new String(fileBytes, StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "FileContent [file=" + file + "]";
	}
}
