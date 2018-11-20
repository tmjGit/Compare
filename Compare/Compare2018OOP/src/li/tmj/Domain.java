package li.tmj;

import java.net.URL;

public class Domain {
	public static String basePath() {
		return getResource("").getPath();
	}
	
	public static URL getResource(String relativePath) {
//		Project/bin/ = ...getResource("/")...
//		Project/bin/<Pakete / statt .>/Class/ = ...getResource(".")... = ...getResource("")...
		return Domain.class.getResource(relativePath);
	}
}
