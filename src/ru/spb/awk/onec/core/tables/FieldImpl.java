package ru.spb.awk.onec.core.tables;
import java.nio.ByteBuffer;

import ru.spb.awk.onec.dbi.Field;


public class FieldImpl implements Field {

	private String mName;
	private FieldType mType;
	private boolean mNullable;
	private int mLenght;
	private int mPrec;
	private boolean mIgnoreCase;
	private boolean rv;

	public FieldImpl(String fName, String fType, String fNullable, String fLen,
			String fPrec, String s) {
		mName = fName;
		mType = FieldType.get(fType);
		mNullable = fNullable.equalsIgnoreCase("1");
		mLenght = Integer.parseInt(fLen);
		mPrec = Integer.parseInt(fPrec);
		mIgnoreCase = ! s.equalsIgnoreCase("CS");
		rv = fType.equalsIgnoreCase("RV");
	}

	public FieldImpl() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return mName;
	}

	public int getSize() {
		return mType.getSize(this);
	}

	public boolean isNullable() {
		return mNullable;
	}

	public Object getValue(ByteBuffer pRecord) {
		return mType.getValue(pRecord, this);
	}

	@Override
	public String toString() {
		return mName;
	}

	@Override
	public boolean isVersionField() {
		return rv;
	}

	@Override
	public int getLenght() {
		return mLenght;
	}

	@Override
	public int getPrecision() {
		return mPrec;
	}

	@Override
	public boolean isIgnoreCase() {
		return mIgnoreCase;
	}

	public void setName(String pS) {
		mName = pS;
	}

	public void setType(FieldType pFieldType) {
		mType = pFieldType;
		rv = pFieldType.equals("RV");
	}

	public void setNullable(boolean pEquals) {
		mNullable = pEquals;
	}

	public void setLength(int pParseInt) {
		mLenght = pParseInt;
	}

	public void setPrecision(int pParseInt) {
		mPrec = pParseInt;
	}

	public void setIgnoreCase(boolean pEquals) {
		mIgnoreCase = pEquals;
	}
}
