package mejai.mejaigg.common;

import java.util.Map;
import java.util.TreeMap;

public class RomanNumber {
	private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

	static {

		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");

	}

	private static final Map<Character, Integer> romanMap = Map.of(
		'I', 1,
		'V', 5,
		'X', 10,
		'L', 50,
		'C', 100,
		'D', 500,
		'M', 1000
	);

	public static int romanToInt(String s) {
		int result = 0;
		int prev = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			int curr = romanMap.get(s.charAt(i));
			if (curr < prev) {
				result -= curr;
			} else {
				result += curr;
			}
			prev = curr;
		}
		return result;
	}

	public final static String toRoman(int number) {
		int l = map.floorKey(number);
		if (number == l) {
			return map.get(number);
		}
		return map.get(l) + toRoman(number - l);
	}
}
