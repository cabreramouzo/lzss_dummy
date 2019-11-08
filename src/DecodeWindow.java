
public class DecodeWindow {

    String buffer;

    public DecodeWindow(int searchBufferLength) {

        buffer = "";
    }

    public void addChar(char c) {
        buffer += c;
    }

    public void print() {
        System.out.println(buffer);
    }

    public String getBuffer() {
        return buffer;
    }

    public void copyCharsSince(int len, int offset, char c) {
        int positions = len;
        int off = offset;
        //System.out.println(buffer);

        int index = buffer.length()-off;
        buffer += buffer.substring(index, index+positions);
        buffer += c;
    }


}
