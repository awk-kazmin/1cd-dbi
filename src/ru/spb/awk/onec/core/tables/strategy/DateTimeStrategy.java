package ru.spb.awk.onec.core.tables.strategy;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;

public class DateTimeStrategy implements FieldStrategy {
	SimpleDateFormat formater = new SimpleDateFormat("y-M-d h:m:s");

	@Override
	public int getSize(Field pF) {
		if(pF.isNullable()) return 8;
		return 7;
	}

	@Override
	public Date getValue(ByteBuffer pRecord, Field pF) {
		byte ch1 = pRecord.get();
		byte ch2 = pRecord.get();
		byte m = pRecord.get();
		byte d = pRecord.get();
		byte h = pRecord.get();
		byte mm = pRecord.get();
		byte ss = pRecord.get();
		String res = String.format("%02x%02x-%02x-%02x %02x:%02x:%02x", ch1,ch2,m,d,h,mm,ss);
		try {
			return formater.parse(res);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}	
	}

}
