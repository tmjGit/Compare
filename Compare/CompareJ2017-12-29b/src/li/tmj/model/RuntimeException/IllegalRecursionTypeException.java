package li.tmj.model.RuntimeException;

import li.tmj.model.RecursionType;
import li.tmj.ui.lang.Localization;

public class IllegalRecursionTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
//	public static final String STD_MSG_Completion= " is no recursion type!";

	public IllegalRecursionTypeException(String message) {
		super(message);
	}

//	public static IllegalRecursionTypeException createIllegalRecursionTypeException(String message,boolean useStdMsgCompletion) {
//		String s=message;
//		if(useStdMsgCompletion)
//			s="\""+s+"\""+STD_MSG_Completion;
//		return new IllegalRecursionTypeException(s);
//	}

	public static IllegalRecursionTypeException createIllegalRecursionTypeException(RecursionType recursion) {
		return new IllegalRecursionTypeException(String.format( Localization.get("noRecursionType"), recursion.name() ));
		//return new IllegalRecursionTypeException("\""+recursion.name()+"\""+STD_MSG_Completion);
	}
}
