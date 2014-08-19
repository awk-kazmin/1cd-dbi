package ru.spb.awk.onec.text;


public interface OneCHandler {
	
	void start();
	void end();
	void br();
	void value(String s);

}
