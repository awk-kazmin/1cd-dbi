package ru.spb.awk.onec.core;

public class Version {

	private int mV;
	private int mV2;
	private int mV3;
	private int mV4;

	public Version(int pV, int pV2, int pV3, int pV4) {
		mV = pV;
		mV2 = pV2;
		mV3 = pV3;
		mV4 = pV4;
	}

	@Override
	public String toString() {
		return "" + mV + "." + mV2 + "." + mV3 + "." + mV4;
	}
}
