package model.criteria;

import java.io.Serializable;

public enum FSCompareCriteria implements Serializable { 
	CRITERION_FILENAME("Datei-/Ordnername"), 
	CRITERION_FILECONTENT("Dateiinhalt"), 
	CRITERION_FILEPATH("Datei-/Ordnerpfad"), 
	CRITERION_DIRSIZE("Ordnerobjektzahl"), 
	CRITERION_DIRSIZEREC("Ordnerobjektzahl rekursiv");

//	CRITERION_FILENAME("FileName"), 
//	CRITERION_FILECONTENT("FileContent"), 
//	CRITERION_FILEPATH("FilePath"), 
//	CRITERION_DIRSIZE("DirSize"), 
//	CRITERION_DIRSIZEREC("DirSizeRecursive");

	  public final String displayName; 
    
    FSCompareCriteria(String displayName) {
        this.displayName = displayName;
    }
	
	public static FSCompareCriteria parse(String importText) {
		for(FSCompareCriteria entry:FSCompareCriteria.values())
			if(entry.name().equals(importText))
				return entry;
		return null;
	}
	
}