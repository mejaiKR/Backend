import java.sql.Date;

import mejai.mejaigg.common.YearMonthToEpochUtil;

public class test {
	public static void main(String[] args) {

		Date date = new Date(YearMonthToEpochUtil.addDayEpochSecond("2024-03", 1) * 1000L);
		System.out.println(date);
	}
}
