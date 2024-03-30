package mejai.mejaigg.common;

import java.util.Map;
import java.util.TreeMap;

public class RomanNumber {
	private static final TreeMap<Integer, String> romanMap1 = new TreeMap<Integer, String>();

	static {
		romanMap1.put(1000, "M");
		romanMap1.put(900, "CM");
		romanMap1.put(500, "D");
		romanMap1.put(400, "CD");
		romanMap1.put(100, "C");
		romanMap1.put(90, "XC");
		romanMap1.put(50, "L");
		romanMap1.put(40, "XL");
		romanMap1.put(10, "X");
		romanMap1.put(9, "IX");
		romanMap1.put(5, "V");
		romanMap1.put(4, "IV");
		romanMap1.put(1, "I");
	}

	private static final Map<Character, Integer> romanMap2 = Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D',
		500,
		'M', 1000);

	public static int romanToInt(String roman) {
		int result = 0;
		int prev = 0;
		for (int i = roman.length() - 1; i >= 0; i--) {
			int curr = romanMap2.get(roman.charAt(i));
			if (curr < prev) {
				result -= curr;
			} else {
				result += curr;
			}
			prev = curr;
		}
		return result;
	}

	public static String toRoman(int number) {
		int num = romanMap1.floorKey(number);
		if (number == num) {
			return romanMap1.get(number);
		}
		return romanMap1.get(num) + toRoman(number - num);
	}
}
