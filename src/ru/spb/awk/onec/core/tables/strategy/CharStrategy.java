package ru.spb.awk.onec.core.tables.strategy;

import java.nio.ByteBuffer;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;

public class CharStrategy implements FieldStrategy {

	@Override
	public int getSize(Field pF) {
		int pr = pF.isNullable() ? 1 : 0;
		pr += pF.getLenght() * 2;
		return pr;
	}

	@Override
	public String getValue(ByteBuffer pRecord, Field pF) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<pF.getLenght(); i++) {
			sb.appendCodePoint(pRecord.getShort());
		}
		return sb.toString();
	}

}
