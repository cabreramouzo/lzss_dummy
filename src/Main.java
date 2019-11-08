import java.io.*;
import java.lang.Integer;
import java.sql.SQLOutput;

public class Main {
    public static String shiftByOne(String s) {
        String res = s.substring(1);
        return res;
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

        System.out.println(input);



        File f;
        f = new File("comprimido.txt");
        FileOutputStream fos;
        fos = new FileOutputStream(f);
        OutputStream bos;
        bos = new BufferedOutputStream(fos);



        WindowBuffer w = new WindowBuffer((short)9,(short)9,input);
        w.fillLookAheadBuffer();

        while (!w.lookAheadIsEmpty()) {
            EncodedString es = w.findMatch();
            if (es.getLength() > 0) {
                es.print();
                byte offset = (byte)es.getOffset();
                byte length = (byte)es.getLength();
                byte symbol = (byte)es.getC();
                bos.write(offset);
                bos.write(length);
                bos.write(symbol);
                bos.flush();
                w.shiftLeft(es.getLength()+1);
            } else {
                //String out = "0" + w.getFirstCharLookAheadBuffer();
                //System.out.print(out);
                byte flag_literal = (byte)0;
                //only ASCII
                byte symbol = (byte)w.getFirstCharLookAheadBuffer();
                bos.write(flag_literal);
                bos.write(symbol);
                bos.flush();

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
        DecodeWindow dw = new DecodeWindow(9);
        BufferedInputStream bis2 = null;
        try {
            bis2 = new BufferedInputStream(new FileInputStream("comprimido.txt"));
            int b;
            while ( (b = bis2.read() ) != -1) {
                byte byte_read = (byte)b;
                if (byte_read == 0) { //flag literal
                    b = bis2.read(); //literal
                    dw.addChar((char)b);
                    //pw.print((char)b);
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
            pw.print(dw.getBuffer());
            bis2.close();
            pw.close();
        }




            /*
            DecodeWindow resultd = new DecodeWindow(10);
            while(!encoded.isEmpty()) {
                char offset = encoded.charAt(0);
                encoded = shiftByOne(encoded);
                char length = encoded.charAt(0);
                encoded = shiftByOne(encoded);
                char c = encoded.charAt(0);
                encoded = shiftByOne(encoded);
                if (offset == '0' && length == '0') {
                    resultd.addChar(c);
                }
                else {
                    resultd.copyCharsSince(length, offset, c);
                }
            }
            resultd.print();
             */


        }





    }

