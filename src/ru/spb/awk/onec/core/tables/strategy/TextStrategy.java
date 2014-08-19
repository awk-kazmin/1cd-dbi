package ru.spb.awk.onec.core.tables.strategy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ru.spb.awk.onec.dbi.BlobAddr;
import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;

public class TextStrategy implements FieldStrategy {


	
	@Override
	public int getSize(Field pF) {
		if(pF.isNullable()) return 9;
		return 8;
	}

	@Override
	public BlobAddr getValue(ByteBuffer pRecord, Field pF) {
		AddrImpl result = new AddrImpl();
		pRecord.order(ByteOrder.LITTLE_ENDIAN);
		result.setIndx(pRecord.getInt());
		result.setLen(pRecord.getInt());
		return result;
	}

}
