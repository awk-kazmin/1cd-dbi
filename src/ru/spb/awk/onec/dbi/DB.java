package ru.spb.awk.onec.dbi;


public interface DB extends Iterable<Table> {

	void close();

	Table getTable(String pString);

	Records getTableScaner(Table pT);

	Records getTableScaner(Table pT, Index pI);


}
