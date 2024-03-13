package mejai.mejaigg.common;

import java.util.Map;

public class RomanToInt {
	private static final Map<Character,Integer> romanMap = Map.of(
			'I',1,
			'V',5,
			'X',10,
			'L',50,
			'C',100,
			'D',500,
			'M',1000
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
	public static void main(String[] args) {
		System.out.println(romanToInt("III")); // Output: 3
		System.out.println(romanToInt("IV"));  // Output: 4
		System.out.println(romanToInt("IX"));  // Output: 9
		System.out.println(romanToInt("LVIII")); // Output: 58
		System.out.println(romanToInt("MCMXCIV")); // Output: 1994
	}
}
