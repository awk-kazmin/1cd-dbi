package ru.spb.awk.onec.core.tables.strategy;

import java.nio.ByteBuffer;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;

public class BooleanStrategy implements FieldStrategy {

	@Override
	public int getSize(Field pF) {
		if(pF.isNullable()) {
			return 2;
		}
		return 1;
	}

	@Override
	public Boolean getValue(ByteBuffer pRecord, Field pF) {
		if(pRecord.get()==1) {
			return true;
		}
		return false;
	}

}
