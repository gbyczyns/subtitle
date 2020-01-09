package fr.noop.subtitle.util;

import java.io.BufferedReader;
import java.io.IOException;

public class BomUtils {
    private static final int BOM = 0xFEFF;

    public static void skipByteOrderMark(BufferedReader br) throws IOException {
        br.mark(1);
        if (br.read() != BOM) {
            br.reset();
        }
    }
}
