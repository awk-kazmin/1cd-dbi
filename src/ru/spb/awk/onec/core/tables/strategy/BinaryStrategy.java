package ru.spb.awk.onec.core.tables.strategy;

import java.nio.ByteBuffer;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;

public class BinaryStrategy implements FieldStrategy {

	@Override
	public int getSize(Field pF) {
		if(pF.isNullable()) {
			return 1 + pF.getLenght();
		}
		return pF.getLenght();
	}

	@Override
	public byte[] getValue(ByteBuffer pRecord, Field pF) {
		byte[] result = new byte[pF.getLenght()];
		pRecord.get(result);
		return result;
	}

}
