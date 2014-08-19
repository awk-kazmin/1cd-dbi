package ru.spb.awk.onec.dbi;

import java.nio.ByteBuffer;

public interface Field {

	boolean isVersionField();

	boolean isNullable();

	int getSize();

	String getName();

	Object getValue(ByteBuffer pRecord);

	int getLenght();

	int getPrecision();
	
	boolean isIgnoreCase();

}
