package ru.spb.awk.onec.text;
import java.io.IOException;
import java.io.Reader;


public class OneCParser {
	private Reader r;
	private OneCHandler p;
	private int first;
	private int next;

	public OneCParser(Reader r, OneCHandler p) {
		this.r = r;
		this.p = p;
	}
	
	public void read() throws IOException {
		first = r.read();
		next  = r.read();
		StringBuilder sb=new StringBuilder();
		while (first != -1) {
			switch (first) {
			case '{':
				p.start();
				sb=new StringBuilder();
				break;
			case ',':
				if(sb.length() > 0)
					p.value(sb.toString());
				p.br();
				sb=new StringBuilder();
				break;
			case '}':
				if(sb.length() > 0)
					p.value(sb.toString());
				sb=new StringBuilder();
				p.end();
				break;
			case '"':
				first = next;
				next = r.read();
				sb = readString();
				break;
			case '\n':
			case ' ':
			case '\r':
			case '\t':
				break;
			default:
				if(!Character.isSpaceChar(first)) {
					sb.appendCodePoint(first);
				}
				break;
			}
			first = next;
			next = r.read();
			
		}
	}

	private StringBuilder readString() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (first != -1) {
			if(first == '"' && next == '"') {
				sb.append('"');
				first = next;
				next = r.read();
			} else if (first == '"') {
				return sb;
			} else {
				sb.appendCodePoint(first);
			}
			first = next;
			next = r.read();
			
		}
		return sb;
	}
}
