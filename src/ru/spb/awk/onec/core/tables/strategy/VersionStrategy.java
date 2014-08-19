package ru.spb.awk.onec.core.tables.strategy;

import java.nio.ByteBuffer;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;

public class VersionStrategy implements FieldStrategy {

	@Override
	public int getSize(Field pF) {
		if(pF.isNullable()) return 17;
		return 16;
	}

	@Override
	public Object getValue(ByteBuffer pRecord, Field pF) {
		byte[] result = new byte[16];
		pRecord.get(result);
		return result;
	}

}
