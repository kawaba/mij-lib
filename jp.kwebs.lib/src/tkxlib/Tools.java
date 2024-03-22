package tkxlib;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Tools {
	
	/**
	 * タイムスタンプを日付に直した文字列を返す
	 * @param timestamp
	 * @return　yyyy-MM-dd hh:mm の文字列
	 */
    public static String timestamp2date(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        return dateTime.toLocalDate() + " " + dateTime.toLocalTime();
    }

    
    
    
    
    // テスト
    public static void main(String[] args) {
		System.out.println(timestamp2date(1710939600L));
	}
}
