package model.criteria;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FSCompareCriterion;
import model.FSCompareCriterionElement;
import model.criteria.FileContent;
import model.criteria.FileName;
import model.criteria.FilePath;

/*
 * TODO Dieses Konzept auf alle Comboboxen anwenden
 */
public class FSCompareCriteriaPlaceholder {
	private FSCompareCriteria type;
	private String displayName;

	public FSCompareCriteriaPlaceholder(FSCompareCriteria type, String displayName) {
		this.type=type;
		this.displayName=displayName;
	}
		
	public FSCompareCriterion createFSCompareCriterion(int count) { 
		FSCompareCriterionElement[] elements=new FSCompareCriterionElement[count];
		switch (type) {
			case CRITERION_FILENAME: for(int i=0; i<elements.length; i++) elements[i]=new FileName(); break;
			case CRITERION_FILECONTENT: for(int i=0; i<elements.length; i++) elements[i]=new FileContent(); break;
			case CRITERION_FILEPATH: for(int i=0; i<elements.length; i++) elements[i]=new FilePath(); break;
			case CRITERION_DIRSIZE: for(int i=0; i<elements.length; i++) elements[i]=new DirSize(false); break;
			case CRITERION_DIRSIZEREC: for(int i=0; i<elements.length; i++) elements[i]=new DirSize(true); break;
		default: throw new RuntimeException("FSCompareCriteriaPlaceholder: Unknown FSCompareCriterion \""+type+"\"!");
		}
		return new FSCompareCriterion(elements);
	}
	
	public static ObservableList<FSCompareCriteriaPlaceholder> criteriaPlaceholderList(){
		ObservableList<FSCompareCriteriaPlaceholder> list=FXCollections.observableArrayList();
		for(FSCompareCriteria entry:FSCompareCriteria.values()) {
			list.add(new FSCompareCriteriaPlaceholder(entry, entry.displayName));
		}
		return list;
	}

	@Override
	public String toString() { // this is the visible output if used in a ComboBox
		return displayName;
	}
		
}