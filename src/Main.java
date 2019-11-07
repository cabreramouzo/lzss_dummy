import java.io.*;
import java.lang.Integer;
public class Main {
    public static String shiftByOne(String s) {
        String res = s.substring(1);
        return res;
    }

    public static void main(String[] args) throws IOException {

        System.out.println("-------");
        Integer numero = 3;
        byte numero_byte = numero.byteValue();
        byte[] b_array = new byte[1];
        b_array[0] = numero_byte;
        System.out.println(b_array.toString());
        System.out.println("-------");
        File f;
        f = new File("fichero.txt");
        FileOutputStream fos;
        fos = new FileOutputStream(f);
        OutputStream bos;
        bos = new BufferedOutputStream(fos);



        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream("fichero.txt"));
            int c;
            while ( (c = bis.read() ) != -1) {
                System.out.println(c);
            }
        }
        finally {
            bis.close();
        }


        



        WindowBuffer w = new WindowBuffer((short)50,(short)9,"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus elementum dictum mauris, nec pharetra arcu eleifend eu. Aenean semper molestie vehicula. Sed semper sem ligula, eu placerat magna eleifend in. Quisque at dapibus sem, vitae convallis quam. Vivamus sollicitudin lectus in libero consectetur malesuada. Nullam vel nibh et metus faucibus auctor nec nec elit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nulla tempor mi sit amet felis dapibus suscipit. Sed ullamcorper laoreet placerat. Proin et commodo ante, a egestas felis. Donec blandit tincidunt orci, in hendrerit magna ultricies eu.");
        //WindowBuffer w = new WindowBuffer(9,9,"abracadabrarray");
        w.fillLookAheadBuffer();
        String result = "";

        while (!w.lookAheadIsEmpty()) {
            EncodedString es = w.findMatch();
            if (es.getLength() > 0) {
                es.print();
                char offset_char = (char)(es.getOffset()+'0');
                byte offset = (byte)offset_char;
                char length_char = (char)(es.getLength()+'0');
                byte length = (byte)length_char;
                byte symbol = (byte)es.getC();
                bos.write(offset);
                bos.write(length);
                bos.write(symbol);
                bos.flush();
                w.shiftLeft(es.getLength()+1);
            } else {
                String out = "0" + w.getFirstCharLookAheadBuffer();
                System.out.print(out);
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



        //decode
        //String encoded = "00U00n00a00 21l00t00r62i00d00e52d00’00o00p00t00i00m21t00z00a00c51ó00 00q00u00e41v00a00m41i00n00t91n31a00r91v41 00s00e72l72t00è00c00n00i31a81d00e31p00r00o31a00g21c00i00ó00 00d00e31r31s00t41i00c11i00o00n91 51 81h00e31k00F00o00r00w00a31d00 00q00u00e41c00o00n00s00i21t91i00x00 00a21c00o00m00p00r41v91r00 00d00e00s00p61é41 82 00c00a51a51a00s11i00g00n61c51ó00 92 00e00n91a00r21 71x00i00s00t51i61 00u00n00a41s00o00l71c00i00ó81p81s11i00b00l00e91a00m61 72s41s42s00i00o00n92q00u00e41e86d82 00p41r41a00s11i00g00n61r91i21l00e00s41p00o41s00i00b00l00e51 00a00u63,71d00i62 41 00h00o00r83q00u51 43d21n71l11i91r81s00.91L00a31i00m00p00l00e41e00n00t00a00c00i00ó00 00d00’71q00u00e00s00t61 31è00c00n00i31a81h32s81g00u00t61p00o81a00r61d00i00n71 51e31c00a51a51s81s11i00ó71u00n00a41i41s00t00à41c71a00 00d00’00u81a61c00l41s11e71a00u00x00i00l21a00r91q91e41h31m41a00n00o51e41a00t92u00x41q41e41c00o00n00t00é61p91r41a21c31d52s00e21s00i00ó71e00l31n00o00m00b00r81 00d32p00o00s11i00b00l91s00 00a93g00n61c51o51s00 00q00u00e41e72p00o00d00r00i81n81f41r41e72l41s41a00u64d00i53i21h00o00r83q00u51 43d21n71l11i91r81s81a21m62u00r72q51e41e00s31v00a00n41a71s00i00g71a21t00 81e21s00i00o91s00,00 00é41 00a21d00i00r41p00e42c00a00d21 21s11i00g00n61c51ó00 00r00e61o41r51m91t71t41e00l31v41c81o00r71d71 00s31s11i00o00n41 00p91r41m91d00i00f21c00a00r00 00e00l31n00o00m00b91e71d00'00a00s11i00g00n61c51o51s00 00p51s11i00b00l00e51 00q00u51 43d21n71d41 00l31s41s42s00i00o00n92q00u00e41s00’00h00a00n61v00i81t51a00f00e00c61a00d51s00 00p41r41a00q00u61s00t61 00ú00l51i00m72a00s11i00g00n61c51ó00.00 00A00q00u00e00s00t00a81ú00l51i00m72t00è00c00n81c82n00o31é00s31a21l32v00e00r00s00i00ó71f41n00a00l61d00e31l00’82g00o00r00i00s00m00e00 00p31r00q00u00è71m00i00l11o00r00a00v21 00m71l00t51p51c41e81 91e00m00p00s61i21e71b00r00u00t00a00v21 91a00s11a61e00l31c00o00d00i51i21l00’00a31g00o00r91t00m00e00 00(00d71f21c00u00l00t00a00n31-31e00 91a31c00o00m00p00r00e00n00s00i00ó00)00.";
        /*
        BufferedInputStream bis2 = null;
        bis2 = new BufferedInputStream(new FileInputStream("fichero.txt"));
        byte[] bytes_array = new byte[1314];
        while (bis2.read(bytes_array, 0, 1314) != -1) {
           //String encoded = bytes_array.toString();
            //if (new String(bytes_array[0]) == "0")
            String encoded = new String(bytes_array);


            DecodeWindow resultd = new DecodeWindow(10);
            while(!encoded.isEmpty()) {
                char offset = encoded.charAt(0);
                encoded = shiftByOne(encoded);
                char length = encoded.charAt(0);
                encoded = shiftByOne(encoded);
                char c = encoded.charAt(0);
                encoded = shiftByOne(encoded);
                if (offset == '0' && length == '0') {
                    resultd.AddChar(c);
                }
                else {
                    resultd.copyCharsSince(length, offset, c);
                }
            }
            resultd.print();

        }
        bis.close();
        */




    }
}
