
public class DecodeWindow {

    String buffer;

    public DecodeWindow(int searchBufferLength) {

        buffer = "";
    }

    public void AddChar(char c) {
        buffer += c;
    }

    public void print() {
        System.out.println(buffer);
    }

    public void copyCharsSince(char len, char offset, char c) {
        int positions = len-48;
        int off = offset-48;

        int index = buffer.length()-off;
        buffer += buffer.substring(index, index+positions);
        buffer += c;
    }


}
