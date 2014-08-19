package ru.spb.awk.onec.core.tables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.Index;

public class IndexImpl implements Index {

	private static class KeyValue {

		private Field field;
		private int length;

		public KeyValue(Field pIndField, int pParseInt) {
			field = pIndField;
			length = pParseInt;
		}

	}

	private boolean primaryKey;
	private String name;
	private List<KeyValue> fields = new ArrayList<>();
	private List<Field> sortedFields= new ArrayList<>();
	private int mPosition;

	public IndexImpl(String pIndexName, boolean pPk) {
		name = pIndexName;
		primaryKey = pPk;
	}

	public IndexImpl(int pos) {
		mPosition = pos;
	}

	public void addField(Field pIndField, int pParseInt) {
		fields .add(new KeyValue(pIndField, pParseInt));
		sortedFields.add(pIndField);
	}

	public void setName(String pS) {
		name = pS;
	}

	public void setPrimaryKey(boolean pEquals) {
		primaryKey = pEquals;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Iterator<Field> iterator() {
		return sortedFields.iterator();
	}

	@Override
	public int getPosition() {
		return mPosition;
	}

}
