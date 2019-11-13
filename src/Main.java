import java.io.*;

public class Main {

    public static final int MIN_LEN_MATCH = 2;
    public static final int BUFFER_SIZE_LOOKAHEAD = 9;
    public static final int BUFFER_SIZE_SEARCH = 16;



    public static String shiftByOne(String s) {
        String res = s.substring(1);
        return res;
    }
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    public static byte mapOffsetToCodedOffset(byte offset) {
        return (byte)(offset-MIN_LEN_MATCH);
    }

    public static byte mapLengthToCodedLength(byte length) {
        return (byte)(length-MIN_LEN_MATCH);
    }

    public static int mapCodedOffsetToOffset(int offset) {
        return (offset+MIN_LEN_MATCH);
    }

    public static int mapCodedLengthToLenght(int length) {
        return (length+MIN_LEN_MATCH);
    }


    public static byte get_length_byte_3_low_bits (byte length) {

        byte result = (byte)(length);
        return result;
    }

    public static byte get_offset_byte_3_4_5_6_high_bits(byte offset) {

        byte result = (byte)(offset << 3);
        return result;
    }

    public static byte codify_offset_length_one_byte_with_flag(byte offset, byte length) {
        //resto 3
        offset = mapOffsetToCodedOffset(offset);
        length = mapLengthToCodedLength(length);

        byte o = get_offset_byte_3_4_5_6_high_bits(offset);
        byte l = get_length_byte_3_low_bits(length);

        byte result = (byte)(o | l);
        result = (byte)(result | 0x80); //higher bit flag=1
        return result;
    }

    public static int[] decodify_offset_length_one_byte(byte off_len) {
        int off_len_int = unsignedByteToInt(off_len);
        int length = off_len_int & 0x07;
        off_len_int = off_len_int >> 3;
        //off_len_int = unsignedByteToInt(off_len);
        int offset = off_len_int & 0x0F;

        //sumo 3
        offset = mapCodedOffsetToOffset(offset);
        length = mapCodedLengthToLenght(length);

        return new int[] {offset, length};
    }



    public static void main(String[] args) throws IOException {
        String input = "";
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream("entrada.txt"));
            int c;
            while ( (c = bis.read() ) != -1) {
                input += (char)c;
            }
        }
        finally {
            bis.close();
        }

        //System.out.println(input);

        File f;
        f = new File("comprimido.txt");
        FileOutputStream fos;
        fos = new FileOutputStream(f);
        OutputStream bos;
        bos = new BufferedOutputStream(fos);


        WindowBuffer w = new WindowBuffer((short)BUFFER_SIZE_SEARCH,(short)BUFFER_SIZE_LOOKAHEAD,input);
        w.fillLookAheadBuffer();

        while (!w.lookAheadIsEmpty()) {
            EncodedString es = w.findMatch();
            //es.print();
            if (es.getLength() >= MIN_LEN_MATCH) {
                //es.print();
                byte offset = (byte)es.getOffset();
                byte length = (byte)es.getLength();
                byte symbol = (byte)es.getC();
                byte off_len = codify_offset_length_one_byte_with_flag(offset, length);
                //bos.write(offset);
                //bos.write(length);
                bos.write(off_len);
                bos.write(symbol);
                //bos.flush();
                w.shiftLeft(es.getLength()+1);
            } else {
                //String out = "0" + w.getFirstCharLookAheadBuffer();
                //System.out.print(out);
                byte flag_literal = (byte)0;
                //only ASCII
                byte symbol = (byte)w.getFirstCharLookAheadBuffer();
                //bos.write(flag_literal);
                bos.write(symbol);
                //bos.flush();

                //System.out.printf("00%c", w.getFirstCharLookAheadBuffer());
                w.shiftLeft(1);
            }

        }
        bos.close();


        //decode bytes


        //output txt
        FileWriter fw = new FileWriter("descomprimido.txt");
        PrintWriter pw = new PrintWriter(fw);

        //decode
        DecodeWindow dw = new DecodeWindow(BUFFER_SIZE_SEARCH);
        BufferedInputStream bis2 = null;
        try {
            bis2 = new BufferedInputStream(new FileInputStream("comprimido.txt"));
            int b;
            while ( (b = bis2.read() ) != -1) {
                byte byte_read = (byte)b;
                if (byte_read < 128 && byte_read > 8) {
                //if (byte_read > 8) { //flag literal or coded token
                    //b = bis2.read(); //literal
                    //dw.addChar((char)b);

                    //no flag byte, directly char
                    dw.addChar((char)byte_read);
                }
                else {
                    //b was offset_length
                    //int len = bis2.read(); //length
                    int[] off_len = decodify_offset_length_one_byte((byte)b);
                    int len = off_len[1];
                    System.out.println(len);
                    int off = off_len[0];
                    int symbol = bis2.read(); //last symbol
                    dw.copyCharsSince(len,off,(char)symbol);

                }
            }
        }
        finally {
            bis2.close();
            pw.print(dw.getBuffer());
            pw.flush();
            pw.close();
        }

        }



    }
