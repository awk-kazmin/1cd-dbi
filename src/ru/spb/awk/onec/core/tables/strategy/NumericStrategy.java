package ru.spb.awk.onec.core.tables.strategy;

import java.nio.ByteBuffer;

import ru.spb.awk.onec.dbi.Field;
import ru.spb.awk.onec.dbi.FieldStrategy;

public class NumericStrategy implements FieldStrategy {

	@Override
	public int getSize(Field pF) {
		int pLenght = pF.getLenght();
		int len = (pLenght + 1) / 2 + (pLenght + 1) % 2;
		if(pF.isNullable()) return len + 1;
		return len;
	}

	@Override
	public Object getValue(ByteBuffer pRecord, Field pF) {
		
		int pLenght = pF.getLenght();
		int pPrec = pF.getPrecision();
		int len = (pLenght + 1) / 2 + (pLenght + 1) % 2;
		byte[] dst = new byte[len];
		pRecord.get(dst);
		byte s = (byte) (dst[0]>>>4);
		StringBuilder sb = new StringBuilder();
		if(s==0) {
			s=-1;
		} else {
			s= 1;
		}
		int i = 1;
		int i1 = (byte) ((byte)(dst[0]<<4)>>>4);
		int i2 =0;
		sb.append(i1<0?-i1:i1);
		while(i<len-1) {
			i1 = (byte) (dst[i]>>4);
			i2 = (byte) ((byte)(dst[i]<<4)>>4);
			sb.append(i1<0?-i1:i1);
			sb.append(i2<0?-i2:i2);
			i++;
		}
		i1 = (byte) (dst[i]>>>4);
		sb.append(i1<0?-i1:i1);
		if(pLenght%2!=0) {
			i2 = (byte) ((byte)(dst[i]<<4)>>>4);
			sb.append(i2<0?-i2:i2);
		}
		if(pPrec==0) return Integer.parseInt(sb.toString())*s;
		else {
			sb.insert(pLenght - pPrec, ".");
			return Double.parseDouble(sb.toString())*s;
		}	
	}

}
