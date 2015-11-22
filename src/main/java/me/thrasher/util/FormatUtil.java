package me.thrasher.util;

/**
 * Formatting utility. This is named, and located, for lack of a better place.
 * 
 * @author Jason Thrasher
 */
public class FormatUtil {
	private static final long kiloByte = 1000;
	private static final long megaByte = kiloByte * 1000;
	private static final long gigaByte = megaByte * 1000;
	private static final long teraByte = gigaByte * 1000;
	private static final long petaByte = teraByte * 1000;
	private static final long exaByte = petaByte * 1000;

	/**
	 * Format the given number as bytes. Formatting will result in the following
	 * conversion:
	 * 
	 * <pre>
	 * number: 0 formatted: 0 Bytes
	 * number: 1 formatted: 1 Byte
	 * number: 9 formatted: 9 Bytes
	 * number: 73 formatted: 73 Bytes
	 * number: 585 formatted: 585 Bytes
	 * number: 4681 formatted: 4681 Bytes
	 * number: 37449 formatted: 37.4 KB
	 * number: 299593 formatted: 299 KB
	 * number: 2396745 formatted: 2.39 MB
	 * number: 19173961 formatted: 19.1 MB
	 * number: 153391689 formatted: 153 MB
	 * number: 1227133513 formatted: 1.22 GB
	 * number: 9817068105 formatted: 9.81 GB
	 * number: 78536544841 formatted: 78.5 GB
	 * number: 628292358729 formatted: 628 GB
	 * number: 5026338869833 formatted: 5.02 TB
	 * number: 40210710958665 formatted: 40.2 TB
	 * number: 321685687669321 formatted: 321 TB
	 * number: 2573485501354569 formatted: 2.57 PB
	 * number: 20587884010836553 formatted: 20.5 PB
	 * number: 164703072086692425 formatted: 164 PB
	 * number: 1317624576693539401 formatted: 1.31 EB
	 * </pre>
	 * 
	 * @param count
	 *            value of number of bytes to format
	 * @return pretty printed value
	 */
	public static String formatBytes(long count) {
		if (count < 0) {
			// TODO: handle large negative long that will exceed Long.MAX_VALUE
			// return "-" + formatBytes(-1 * num);
			throw new IllegalArgumentException(
					"Sorry, you can't have negative bytes.");
		}
		if (count < kiloByte * 10) {
			return count == 1 ? "1 Byte" : count + " Bytes";
		}
		StringBuilder sb = new StringBuilder(4 + 3);
		sb.append(Long.toString(count).substring(0, 3));

		if (count < megaByte) {
			int offset = Long.toString(count / (megaByte / 1000)).length();
			if (offset < 3) {
				sb.insert(offset, '.');
			}
			sb.append(" KB");
		} else if (count < gigaByte) {
			int offset = Long.toString(count / (gigaByte / 1000)).length();
			if (offset < 3) {
				sb.insert(offset, '.');
			}
			sb.append(" MB");
		} else if (count < teraByte) {
			int offset = Long.toString(count / (teraByte / 1000)).length();
			if (offset < 3) {
				sb.insert(offset, '.');
			}
			sb.append(" GB");
		} else if (count < petaByte) {
			int offset = Long.toString(count / (petaByte / 1000)).length();
			if (offset < 3) {
				sb.insert(offset, '.');
			}
			sb.append(" TB");
		} else if (count < exaByte) {
			int offset = Long.toString(count / (exaByte / 1000)).length();
			if (offset < 3) {
				sb.insert(offset, '.');
			}
			sb.append(" PB");
		} else {
			int offset = Long.toString(count / exaByte).length();
			if (offset < 3) {
				sb.insert(offset, '.');
			}
			sb.append(" EB");
		}

		return sb.toString();
	}

	/**
	 * Round to a significant number of figures. This works for Base-10
	 * rounding.
	 * 
	 * @param number
	 *            to round
	 * @param significantDigits
	 *            count of digits to round to consider significant
	 * @return
	 */
	public static double roundToSignificantDigits(double number,
			int significantDigits) {
		if (number == 0) {
			return 0;
		}

		final double d = Math.ceil(Math.log10(number < 0 ? -number : number));
		final int power = significantDigits - (int) d;

		final double magnitude = Math.pow(10, power);
		final long shifted = Math.round(number * magnitude);
		return shifted / magnitude;
	}
}
