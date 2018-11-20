package li.tmj.model.criteria;

import org.pmw.tinylog.Logger;
import li.tmj.model.FSCompareCriterion;
import li.tmj.model.FSCompareFileValue;
import li.tmj.ui.controls.ComboBoxable;
import li.tmj.ui.lang.ErrorSet;
import li.tmj.ui.lang.Localization;

public enum FSCompareCriteria implements ComboBoxable {
	FILENAME,
	FILECONTENT,
	FILEPATH,
	DIRSIZE,
	DIRSIZEREC ;
//  Brainstorming für weitere Kriterienklassen:
//	CreateDate, ChangeDate, Permissions (Mode), Owner, Group, Mac ResourceFork, Charset, FileSystemType des Objekts

	public FSCompareCriterion create(int count) {
		FSCompareCriterion criterion;
		FSCompareFileValue[] values = new FSCompareFileValue[count];
		switch (this) {
			case FILENAME: criterion=new FileName(values); break;
			case FILECONTENT: criterion=new FileContent(values); break;
			case FILEPATH: criterion=new FilePath(values); break;
			case DIRSIZE: criterion=new DirSize(values,false); break;
			case DIRSIZEREC: criterion=new DirSize(values,true); break;
//			default: throw new RuntimeException("FSCompareCriteriaPlaceholder: Unknown FSCompareCriterion \""+type+"\"!");
			default: {
				Logger.error(ErrorSet.couldNotIdentifyFSCompareCriteriaType(),this);
				criterion=null;
			}
		}
		return criterion;
	}

	@Override
	public String getDisplayName() {
		return Localization.get(name());
	}
	
}
