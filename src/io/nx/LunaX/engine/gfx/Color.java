package io.nx.LunaX.engine.gfx;

import java.math.BigInteger;

public class Color {

	public static int RGBtoHex(int r, int g, int b) {
		java.awt.Color color = new java.awt.Color(r, g, b);
		String hex = "0xff" + Integer.toHexString(color.getRGB()).substring(2);
		BigInteger bigInt = new BigInteger(hex.substring(2), 16);
		int endHex = bigInt.intValue();
		return endHex;
	}

}
