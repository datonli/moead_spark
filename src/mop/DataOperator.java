package mop;

import utilities.WrongRemindException;

public interface DataOperator {
	public String mop2Line(int i);
	public void line2mop(String line)  throws WrongRemindException;
}
