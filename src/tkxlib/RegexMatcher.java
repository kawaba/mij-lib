package tkxlib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatcher {
    public static void main(String[] args) {
        String s = "This is a test string with some patterns.";
        String m = " ".repeat(s.length());
        String r = "QQQ";

        Pattern pattern = Pattern.compile(r);
        Matcher matcher = pattern.matcher(s);
        
		if (!matcher.matches()) {
			System.out.println("No match found.");
			return;
		}
        
        while (matcher.find()) {
            int a = matcher.start();
            int b = matcher.end();
            System.out.println("Match found at index " + a + " - " + (b - 1));
            
            System.out.println(s);
            System.out.println(m.substring(0, a) + "^".repeat(b-a) + m.substring(b));
            
        }
    }
}
