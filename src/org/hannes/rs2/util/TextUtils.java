package org.hannes.rs2.util;

/**
 * utils to do with text lol
 * @author red
 *
 */
public class TextUtils {

	/**
	 * Encode a string
	 * 
	 * @param text
	 * @return
	 */
	public static long encode(String text) {
		long hash = 0L;
		for (int i = 0; i < text.length() && i < 12; i++) {
			char c = text.charAt(i);
			hash *= 37L;
			if (c >= 'A' && c <= 'Z')
				hash += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				hash += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				hash += (27 + c) - 48;
		}
		while (hash % 37L == 0L && hash != 0L)
			hash /= 37L;
		return hash;
	}

}