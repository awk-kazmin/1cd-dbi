package ru.spb.awk.onec.dbi;

public interface Index extends Iterable<Field> {

	String getName();

	int getPosition();

}
