package HackAssembler;

import java.util.Dictionary;
import java.util.Hashtable;

public class Code {

    public static String dest(String dest) {
        String A = dest.contains("A") ? "1" : "0";
        String D = dest.contains("D") ? "1" : "0";
        String M = dest.contains("M") ? "1" : "0";
        return A + D + M;
    }

    public static String comp(String comp) {
        Dictionary<String, String> compSpec = new Hashtable<String, String>();
        compSpec.put("0", "0" + "101010");
        compSpec.put("1", "0" + "111111");
        compSpec.put("-1", "0" + "111010");
        compSpec.put("D", "0" + "001100");
        compSpec.put("A", "0" + "110000");
        compSpec.put("M", "1" + "110000");
        compSpec.put("!D", "0" + "001101");
        compSpec.put("!A", "0" + "110001");
        compSpec.put("!M", "1" + "110001");
        compSpec.put("-D", "0" + "001111");
        compSpec.put("-A", "0" + "110011");
        compSpec.put("-M", "1" + "110011");
        compSpec.put("D+1", "0" + "011111");
        compSpec.put("A+1", "0" + "110111");
        compSpec.put("M+1", "1" + "110111");
        compSpec.put("D-1", "0" + "001110");
        compSpec.put("A-1", "0" + "110010");
        compSpec.put("M-1", "1" + "110010");
        compSpec.put("D+A", "0" + "000010");
        compSpec.put("D+M", "1" + "000010");
        compSpec.put("D-A", "0" + "010011");
        compSpec.put("D-M", "1" + "010011");
        compSpec.put("A-D", "0" + "000111");
        compSpec.put("M-D", "1" + "000111");
        compSpec.put("D&A", "0" + "000000");
        compSpec.put("D&M", "1" + "000000");
        compSpec.put("D|A", "0" + "010101");
        compSpec.put("D|M", "1" + "010101");
        return compSpec.get(comp);
    }

    public static String jump(String jump) {
        if (jump.isEmpty()) return "000";
        Dictionary<String, String> jumpSpec = new Hashtable<String, String>();
        jumpSpec.put("JGT", "001");
        jumpSpec.put("JEQ", "010");
        jumpSpec.put("JGE", "011");
        jumpSpec.put("JLT", "100");
        jumpSpec.put("JNE", "101");
        jumpSpec.put("JLE", "110");
        jumpSpec.put("JMP", "111");
        return jumpSpec.get(jump);
    }

}
