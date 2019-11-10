import java.io.*;
import java.lang.Integer;
import java.sql.SQLOutput;

public class Main {
    public static String shiftByOne(String s) {
        String res = s.substring(1);
        return res;
    }

    public static byte get_length_byte_4_low_bits (byte offset) {

        byte result = (byte)(offset & 0x07);
        return result;
    }

    public static byte get_offset_byte_4_high_bits(byte offset) {

        byte result = (byte)(offset << 4);
        return result;
    }

    public static byte codify_offset_length_one_byte(byte offset, byte lenght) {
        byte o = get_offset_byte_4_high_bits(offset);
        byte l = get_length_byte_4_low_bits(lenght);

        byte result = (byte)0xFF;

        return result;
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



        WindowBuffer w = new WindowBuffer((short)12,(short)12,input);
        w.fillLookAheadBuffer();

        while (!w.lookAheadIsEmpty()) {
            EncodedString es = w.findMatch();
            //es.print();
            if (es.getLength() > 0) {
                //es.print();
                byte offset = (byte)es.getOffset();
                byte length = (byte)es.getLength();
                byte symbol = (byte)es.getC();
                //off_len = codify_offset_length_one_byte(offset, length);
                bos.write(offset);
                bos.write(length);
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
        DecodeWindow dw = new DecodeWindow(12);
        BufferedInputStream bis2 = null;
        try {
            bis2 = new BufferedInputStream(new FileInputStream("comprimido.txt"));
            int b;
            while ( (b = bis2.read() ) != -1) {
                byte byte_read = (byte)b;
                if (byte_read > 12) { //flag literal
                    //b = bis2.read(); //literal
                    //dw.addChar((char)b);

                    //no flag byte, directly char
                    dw.addChar((char)byte_read);
                }
                else {
                    //b was offset
                    int len = bis2.read(); //length
                    int symbol = bis2.read(); //last symbol
                    dw.copyCharsSince(len,(int)b,(char)symbol);




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

