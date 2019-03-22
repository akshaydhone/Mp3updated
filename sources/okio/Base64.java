package okio;

import com.google.appinventor.components.runtime.util.Ev3Constants.Opcode;
import java.io.UnsupportedEncodingException;

final class Base64 {
    private static final byte[] MAP = new byte[]{Opcode.JR_FALSE, Opcode.JR_TRUE, Opcode.JR_NAN, Opcode.CP_LT8, Opcode.CP_LT16, Opcode.CP_LT32, Opcode.CP_LTF, Opcode.CP_GT8, Opcode.CP_GT16, Opcode.CP_GT32, Opcode.CP_GTF, Opcode.CP_EQ8, Opcode.CP_EQ16, Opcode.CP_EQ32, Opcode.CP_EQF, Opcode.CP_NEQ8, Opcode.CP_NEQ16, Opcode.CP_NEQ32, Opcode.CP_NEQF, Opcode.CP_LTEQ8, Opcode.CP_LTEQ16, Opcode.CP_LTEQ32, Opcode.CP_LTEQF, Opcode.CP_GTEQ8, Opcode.CP_GTEQ16, Opcode.CP_GTEQ32, Opcode.PORT_CNV_OUTPUT, Opcode.PORT_CNV_INPUT, Opcode.NOTE_TO_FREQ, Opcode.JR_LT8, Opcode.JR_LT16, Opcode.JR_LT32, Opcode.JR_LTF, Opcode.JR_GT8, Opcode.JR_GT16, Opcode.JR_GT32, Opcode.JR_GTF, Opcode.JR_EQ8, Opcode.JR_EQ16, Opcode.JR_EQ32, Opcode.JR_EQF, Opcode.JR_NEQ8, Opcode.JR_NEQ16, Opcode.JR_NEQ32, Opcode.JR_NEQF, Opcode.JR_LTEQ8, Opcode.JR_LTEQ16, Opcode.JR_LTEQ32, Opcode.JR_LTEQF, Opcode.JR_GTEQ8, Opcode.JR_GTEQ16, Opcode.JR_GTEQ32, Opcode.MOVE8_8, Opcode.MOVE8_16, Opcode.MOVE8_32, Opcode.MOVE8_F, Opcode.MOVE16_8, Opcode.MOVE16_16, Opcode.MOVE16_32, Opcode.MOVE16_F, Opcode.MOVE32_8, Opcode.MOVE32_16, (byte) 43, Opcode.INIT_BYTES};
    private static final byte[] URL_MAP = new byte[]{Opcode.JR_FALSE, Opcode.JR_TRUE, Opcode.JR_NAN, Opcode.CP_LT8, Opcode.CP_LT16, Opcode.CP_LT32, Opcode.CP_LTF, Opcode.CP_GT8, Opcode.CP_GT16, Opcode.CP_GT32, Opcode.CP_GTF, Opcode.CP_EQ8, Opcode.CP_EQ16, Opcode.CP_EQ32, Opcode.CP_EQF, Opcode.CP_NEQ8, Opcode.CP_NEQ16, Opcode.CP_NEQ32, Opcode.CP_NEQF, Opcode.CP_LTEQ8, Opcode.CP_LTEQ16, Opcode.CP_LTEQ32, Opcode.CP_LTEQF, Opcode.CP_GTEQ8, Opcode.CP_GTEQ16, Opcode.CP_GTEQ32, Opcode.PORT_CNV_OUTPUT, Opcode.PORT_CNV_INPUT, Opcode.NOTE_TO_FREQ, Opcode.JR_LT8, Opcode.JR_LT16, Opcode.JR_LT32, Opcode.JR_LTF, Opcode.JR_GT8, Opcode.JR_GT16, Opcode.JR_GT32, Opcode.JR_GTF, Opcode.JR_EQ8, Opcode.JR_EQ16, Opcode.JR_EQ32, Opcode.JR_EQF, Opcode.JR_NEQ8, Opcode.JR_NEQ16, Opcode.JR_NEQ32, Opcode.JR_NEQF, Opcode.JR_LTEQ8, Opcode.JR_LTEQ16, Opcode.JR_LTEQ32, Opcode.JR_LTEQF, Opcode.JR_GTEQ8, Opcode.JR_GTEQ16, Opcode.JR_GTEQ32, Opcode.MOVE8_8, Opcode.MOVE8_16, Opcode.MOVE8_32, Opcode.MOVE8_F, Opcode.MOVE16_8, Opcode.MOVE16_16, Opcode.MOVE16_32, Opcode.MOVE16_F, Opcode.MOVE32_8, Opcode.MOVE32_16, Opcode.RL16, Opcode.SELECTF};

    private Base64() {
    }

    public static byte[] decode(String in) {
        int outCount;
        int limit = in.length();
        while (limit > 0) {
            char c = in.charAt(limit - 1);
            if (c != '=' && c != '\n' && c != '\r' && c != ' ' && c != '\t') {
                break;
            }
            limit--;
        }
        byte[] out = new byte[((int) ((((long) limit) * 6) / 8))];
        int inCount = 0;
        int word = 0;
        int pos = 0;
        int outCount2 = 0;
        while (pos < limit) {
            int bits;
            c = in.charAt(pos);
            if (c >= 'A' && c <= 'Z') {
                bits = c - 65;
            } else if (c >= 'a' && c <= 'z') {
                bits = c - 71;
            } else if (c >= '0' && c <= '9') {
                bits = c + 4;
            } else if (c == '+' || c == '-') {
                bits = 62;
            } else if (c == '/' || c == '_') {
                bits = 63;
            } else {
                if (!(c == '\n' || c == '\r' || c == ' ')) {
                    if (c == '\t') {
                        outCount = outCount2;
                        pos++;
                        outCount2 = outCount;
                    } else {
                        outCount = outCount2;
                        return null;
                    }
                }
                outCount = outCount2;
                pos++;
                outCount2 = outCount;
            }
            word = (word << 6) | ((byte) bits);
            inCount++;
            if (inCount % 4 == 0) {
                outCount = outCount2 + 1;
                out[outCount2] = (byte) (word >> 16);
                outCount2 = outCount + 1;
                out[outCount] = (byte) (word >> 8);
                outCount = outCount2 + 1;
                out[outCount2] = (byte) word;
                pos++;
                outCount2 = outCount;
            }
            outCount = outCount2;
            pos++;
            outCount2 = outCount;
        }
        int lastWordChars = inCount % 4;
        if (lastWordChars == 1) {
            outCount = outCount2;
            return null;
        }
        if (lastWordChars == 2) {
            outCount = outCount2 + 1;
            out[outCount2] = (byte) ((word << 12) >> 16);
        } else {
            if (lastWordChars == 3) {
                word <<= 6;
                outCount = outCount2 + 1;
                out[outCount2] = (byte) (word >> 16);
                outCount2 = outCount + 1;
                out[outCount] = (byte) (word >> 8);
            }
            outCount = outCount2;
        }
        if (outCount == out.length) {
            return out;
        }
        byte[] prefix = new byte[outCount];
        System.arraycopy(out, 0, prefix, 0, outCount);
        return prefix;
    }

    public static String encode(byte[] in) {
        return encode(in, MAP);
    }

    public static String encodeUrl(byte[] in) {
        return encode(in, URL_MAP);
    }

    private static String encode(byte[] in, byte[] map) {
        int i;
        byte[] out = new byte[(((in.length + 2) / 3) * 4)];
        int end = in.length - (in.length % 3);
        int index = 0;
        for (int i2 = 0; i2 < end; i2 += 3) {
            i = index + 1;
            out[index] = map[(in[i2] & 255) >> 2];
            index = i + 1;
            out[i] = map[((in[i2] & 3) << 4) | ((in[i2 + 1] & 255) >> 4)];
            i = index + 1;
            out[index] = map[((in[i2 + 1] & 15) << 2) | ((in[i2 + 2] & 255) >> 6)];
            index = i + 1;
            out[i] = map[in[i2 + 2] & 63];
        }
        switch (in.length % 3) {
            case 1:
                i = index + 1;
                out[index] = map[(in[end] & 255) >> 2];
                index = i + 1;
                out[i] = map[(in[end] & 3) << 4];
                i = index + 1;
                out[index] = Opcode.MOVEF_16;
                index = i + 1;
                out[i] = Opcode.MOVEF_16;
                i = index;
                break;
            case 2:
                i = index + 1;
                out[index] = map[(in[end] & 255) >> 2];
                index = i + 1;
                out[i] = map[((in[end] & 3) << 4) | ((in[end + 1] & 255) >> 4)];
                i = index + 1;
                out[index] = map[(in[end + 1] & 15) << 2];
                index = i + 1;
                out[i] = Opcode.MOVEF_16;
                break;
        }
        i = index;
        try {
            return new String(out, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
