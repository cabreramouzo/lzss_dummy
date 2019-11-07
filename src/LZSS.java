
public class LZSS {

  public static void enc() {
    String input = "abracadabrarray";
    WindowBuffer windowBuffer = new WindowBuffer((short)8, (short)6, input);

    while (!windowBuffer.lookAheadIsEmpty()) {
      EncodedString es = windowBuffer.findMatch();
      if (es.getLength() > 1) {
        es.print();
        windowBuffer.shiftLeft(1);
      } else {
        System.out.printf("< 0, 0, %c >", windowBuffer.getFirstCharLookAheadBuffer());
      }

    }
  }

}
