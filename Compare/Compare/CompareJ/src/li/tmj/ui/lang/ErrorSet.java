package li.tmj.ui.lang;

// TODO automatisch generieren?
public final class ErrorSet {//implements ComboBoxable { 			
	public static String configurationContainsObscureValues() { return "Internal error, the configuration seems to contain obscure values.";}//get("ConfigurationContainsObscureValues"); }
	public static String configurationErrorFileValuesButFilesToCompairInThisCriterion() { return "Configuration error: {} fileValues (display fields) but {} files to compair in this criterion ({})! This will probably cause an error, later.";}//get("ConfigurationErrorFileValuesButFilesToCompairInCriterion"); }
	public static String configurationDoesNotExist() { return "Configuration does not exist!";}//get("ConfigurationDoesNotExist"); }
	public static String configurationFileXdoesNotExist() { return "Configuration file \"{}\" does not exist!";}//get("ConfigurationFileXdoesNotExist"); }
	public static String configurationXdoesNotExist() { return "Configuration \"{}\" does not exist!";}//get("ConfigurationXdoesNotExist"); }
	public static String configurationHasNoDefault() { return "Configuration \"{}\" has no default!";}//get("ConfigurationHasNoDefault"); }
	public static String configurationXhasNoDefault() { return "Configuration \"%s\" has no default!";}//get("ConfigurationXhasNoDefault"); }
	public static String couldNotAccessFile() { return "Error: Could not access file \"{}\"!";}
	public static String couldNotGetWindowsPathLOCALAPPDATAforLogfile() { return "Could not get Windows' path %LOCALAPPDATA% for the log file.";}//get("CouldnotgetWindowspathLOCALAPPDATAforthelogfile"); }
	public static String couldNotIdentifyFSCompareCriteriaType() { return "Internal error: Could not identify the FSCompareCriteria type \"{}\"!";}//get("CouldNotIdentifyFSCompareCriteriaType"); }
	public static String errorGettingConfigurationFileX() { return "Error getting configuration file \"{}\"!";}//get("ErrorGettingConfigurationFileX"); }
	public static String fileCouldNotBeTouchedX() { return "File could not be touched: \"{}\"!";}//get("FileCouldNotBeTouchedX"); }
	public static String fileNotFoundX() { return "File not found: \"{}\"!";}//get("FileNotFoundX"); }
	public static String keyXdoesNotExistInConfiguration() { return "Key \"{}\" does not exist in the configuration \"{}\"!";}//get("KeyXdoesNotExistInConfiguration"); }
	public static String keyNotFoundInConfiguration() { return "Key \"{}\" not found in configuration \"{}\"";}//get("KeyNotFoundInConfiguration"); }
	public static String noRecursionType() { return "\"%s\" is no recursion type!";}//get("noRecursionType"); }
	public static String missingLanguageResource() { return "Missing language resource!";}//get("MissingLanguageResource"); }
	public static String missingResource() { return "<missing resource>";}//get("missingResource"); }
	public static String missingValue() { return "<missing value>";}//get("missingValue"); }
	public static String toomanyfilesdroppedWetakefirst2() { return "Too many files dropped! We take the first two, only.";}//get("ToomanyfilesdroppedWetakefirst2"); }

	private static String get(String key) { return Localization.get(key); }
	
}
