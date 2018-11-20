package li.tmj.model;

import java.util.Arrays;

public class FSItem {
	private static int criterionCount;
	private String name;
	private String path;
	private String type;
	private boolean[] matches;
	
	
	public FSItem(String name,String path,String type) {
		init(name,path,type);
		matches=new boolean[criterionCount];
	}
	
	public FSItem(String name,String path,String type,boolean[] matches) {
		init(name,path,type);
		this.matches=matches;
	}
	
	private void init(String name,String path,String type) {
		if(criterionCount<0) {
			throw new IllegalStateException("FSItem.criterionCount was not set!");
		}
		this.name=name;
		this.path=path;
		this.type=type;
	}

	public static int getCriterionCount() {
		return criterionCount;
	}

	public static void setCriterionCount(int criterionCount) {
		FSItem.criterionCount = criterionCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean[] getMatches() {
		return matches;
	}

	public void setMatches(boolean[] matches) {
		this.matches = matches;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(matches);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FSItem other = (FSItem) obj;
		if (!Arrays.equals(matches, other.matches))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FSItem [name=" + name + ", path=" + path + ", type=" + type + ", matches=" + Arrays.toString(matches)
				+ "]";
	}
	

	
}
