package com.xdz.dsa.string;

import java.io.IOException;
import java.nio.file.Paths;

public class Counter {

    public static void main(String[] args) {

        String raw = "import java.io.BufferedReader;\n" +
                "import java.io.FileReader;\n" +
                "import java.io.IOException;\n" +
                "import java.nio.file.Paths;\n" +
                "\n" +
                "public class ReadSelf {\n" +
                "    public static void main(String[] args) {\n" +
                "        // 获取当前文件的路径\n" +
                "        String filePath = Paths.get(\"ReadSelf.java\").toAbsolutePath().toString();\n" +
                "        \n" +
                "        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {\n" +
                "            String line;\n" +
                "            while ((line = reader.readLine()) != null) {\n" +
                "                System.out.println(line);\n" +
                "            }\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "    }\n" +
                "}\n";

        Alphabet alphabet = Alphabet.ASCII;

        int R = alphabet.R();
        int[] count = new int[R];
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (alphabet.contains(c)) {
                count[alphabet.toIndex(c)]++;
            }
        }
        for (int i = 0; i < R; i++) {
            if (count[i] > 0) {
                System.out.println(alphabet.toChar(i) + ":\t" + count[i]);
            }
        }
    }
}
