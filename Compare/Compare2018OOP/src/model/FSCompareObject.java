package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import model.RuntimeException.IllegalRecursionTypeException;

public class FSCompareObject extends FSCompare {
	private ArrayList<FSCompareCriterion> criteria=new ArrayList<>();
	private File[] files=null;
	
	public FSCompareObject() {
		super();
		files=new File[2];//TODO make flexibel!
	}
	
	public File getFile(int Index) {
		return files[Index];
	}

	public void setFile(int Index,File file) {
		files[Index] = file;
		setChildFile(Index,file);
	}
    
	private void setChildFile(int Index,File file) {
		for (FSCompareCriterion c:criteria)
			if(c!=null)
				c.get(Index).setFile(file);
	}
    
	// returns the Listindex of the new criterion
	public int addCriterion(FSCompareCriterion c) {
		initCriterion(c);
		criteria.add(c);
		return criteria.size()-1;
	}
	
	public void setCriterion(int Index,FSCompareCriterion c) {
		criteria.set(Index, c);
		initCriterion(c);
	}

	public void initCriterion(FSCompareCriterion c) {
		if (c!=null) {
			for(int i=0; i<files.length; i++)
				c.get(i).setFile(files[i]);
		}
	}
	
	public boolean compare(boolean compareFull) throws FileNotFoundException {
		boolean b;
		if(compareFull)
			b=compareFullIntern();
		else
			b=compareIntern();
		setValue(compareResult(b));
		return b;
	}

	private boolean compareIntern() throws FileNotFoundException{
		switch(recursion) {
			case RECURSION_OFF: return compareInternSingle();
			case RECURSION_ALL: {
				boolean b=compareRecursive(files);
//				setChildFile( 0, files[0]);// TODO bessere L�sung finden. Aktuell werden die Dateizuordnungen bei
//				setChildFile( 1, files[1]);//der Rekursion ersetzt, so dass sie hier wieder hergestellt werden m�ssen.
				return b;
			}
			case RECURSION_FILES: throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion);//TODO count files, only
			case RECURSION_DIRS: throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); //TODO count dirs, only
			default: throw IllegalRecursionTypeException.createIllegalRecursionTypeException(recursion); // return false; // we use enums, now, so this can be removed
		}
	}

	private boolean compareInternSingle() throws FileNotFoundException { 
		for (FSCompareCriterion c:criteria) {
			if (c!=null && !c.compare()) // if we find one false, than the result is false; ignoring undefined criteria
				return false;
		}
		return true;
	}

	private boolean compareRecursive(File[] files) throws FileNotFoundException{ //TODO for better performance use java.nio.file.Files, java.nio.file.Path?
//		System.out.println("FSCompareObject.compareRecursive: files[0]="+files[0]+", files[1]="+files[1]);
//		Path path=file.toPath();
//		Stream<Path> str=Files.list(path);
//		result=str.count();
		
		File[][] childs=new File[files.length][]; 
		for (int i=0; i<2; i++) {
			childs[i]=files[i].listFiles();
		}
		for (File f1:childs[0]) { //TODO make flexibel
			File f2=new File(files[1],f1.getName()); // virtual file from the one folder within the other.
			if(!f2.exists()) // if one file does not exist in both folders, the folders can't be same.
				return false;
			if(!comparePair(f1, f2))
				return false;
		}
		for (File f2:childs[1]) {
			File f1=new File(files[0],f2.getName()); // virtual file from the one folder within the other.
			if(!f1.exists())
				return false;
			//if exists, we compared it already.
		}
		return true;
	}
	
	private boolean comparePair(File file1,File file2) throws FileNotFoundException {
		System.out.println("FSCompareObject.comparePair: file1="+file1+", file2="+file2);
		if(file1.isDirectory() && file2.isDirectory())  // next recursion level
			return compareRecursive(new File[] {file1,file2});
		if(file1.isDirectory() || file2.isDirectory()) // only one of them
			return false;
		
		// now compare the two files...	
//		setChildFile( 0, file1) ;//TODO bessere L�sung f�r die rekursive Dateizuordnung finden,
//		setChildFile( 1, file2) ;//zus�tzliche statische methoden Varianten in den CompareElement-Klassen
//		return compareInternSingle();
		
		for (FSCompareCriterion c:criteria) {
			if (c!=null && !c.compare(file1, file2)) // if we find one false, than the result is false; ignoring undefined criteria
				return false;
		}
		return true;

	}

	private boolean compareFullIntern() throws FileNotFoundException { 
		boolean compared=true;
		boolean[] matches=new boolean[criteria.size()];
		for (int i=0; i < matches.length; i++) { // we check all criteria true or false; ignoring undefined criteria
			FSCompareCriterion c=criteria.get(i);
			if(c==null)
				matches[i]=true;
			else {
				matches[i]=c.compare();
				if(!matches[i])
					compared=false;	
			}
		}
		return compared;

	}

	@Override
	public String toString() {
		return "FSCompareObject [criteria=" + criteria + ", files=" + Arrays.toString(files) + "]";
	}

}
