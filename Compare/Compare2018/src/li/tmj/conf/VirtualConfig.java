package li.tmj.conf;

import java.util.Properties;
import java.util.function.Supplier;

public class VirtualConfig extends Config {
	public VirtualConfig(Supplier<Properties> defaultFactory) {
		super("main");
		this.defaultFactory=defaultFactory;
		hasFile=false;
	}

	@Override
	protected boolean read() {
		// Do nothing because it's virtual.
		return false;
	}	
}

