package ru.spb.awk.onec.dbi;

import java.util.Collection;


public interface Table extends Iterable<Field> {

	int getRecordPage();

	boolean haveVersionField();

	boolean haveRecordLock();

	int getRecordLenght();

	String getName();

	int getIndexPage();

	int getBlobPage();

	Collection<Index> indexes();

	Index getIndex(String pString);

}
