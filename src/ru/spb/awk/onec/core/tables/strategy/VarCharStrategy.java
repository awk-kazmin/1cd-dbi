package ru.spb.awk.onec.core.tables.strategy;

import java.nio.ByteBuffer;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;

public class VarCharStrategy implements FieldStrategy {

	@Override
	public int getSize(Field pF) {
		int pr = pF.isNullable() ? 1 : 0;
		pr += pF.getLenght() * 2 + 2;
		return pr;
	}

	@Override
	public String getValue(ByteBuffer pRecord, Field pF) {
		int len = pRecord.getShort();
		len = len<0?-len:len;
		len = Math.min(len, pF.getLenght());
		StringBuilder sb = new StringBuilder();
		for(int i =0; i<len; i++) {
			sb.appendCodePoint(pRecord.getShort());
		}
		pRecord.position(pRecord.position() + (pF.getLenght()-len)*2);
		return sb.toString().trim();
	}

}
