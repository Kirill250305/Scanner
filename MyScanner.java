import java.io.*;
import java.util.Arrays;

public class MyScanner {

    public interface Separation {
        boolean check(char symbol);
    }

    public static boolean token(char symbol) {
        return Character.isWhitespace(symbol) && symbol != '\n' && symbol != '\r';
    }
    public Separation function;
    int update = 0;
    char[] buffer = new char[2048];
    int pointer = 0;

    char lastSymbol;
    Reader reader;

    int read;

    public MyScanner(String string,Separation token) {
        this.reader = new StringReader(string);
        this.recordingInput();
        this.function = token;
    }

    public MyScanner(InputStream inputStream,Separation token) {
        this.reader = new InputStreamReader(inputStream);
        this.recordingInput();
        this.function = token;
    }

    public MyScanner(String string) {
        this.reader = new StringReader(string);
        this.recordingInput();
        this.function = MyScanner::token;
    }

    public MyScanner(InputStream inputStream) {
        this.reader = new InputStreamReader(inputStream);
        this.recordingInput();
        this.function = MyScanner::token;
    }
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Failed to complete reading: " + e.getMessage());
        }
    }
    public void recordingInput() {
        try {
            this.read = this.reader.read(this.buffer);
            if (this.read > 0) {
                this.buffer = Arrays.copyOfRange(this.buffer, 0, this.read);
            }
            this.pointer = 0;
            this.update++;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O exception: " + e.getMessage());
        }
    }

    public boolean hasNextLine() {
        boolean nextLine = this.update == 1 && this.pointer == 0;
        if (endOfLine(this.lastSymbol, this.buffer[this.pointer])) {
            if (this.pointer + 1 != this.buffer.length) {
                nextLine = true;
            } else {
                recordingInput();
                if (this.read > 0) {
                    nextLine = true;
                }
            }
        }
        return nextLine;
    }
    public boolean hasNextInLine() {
        if (this.pointer == this.buffer.length) {
            recordingInput();
        }
        while (endOfLine(this.lastSymbol, this.buffer[this.pointer])) {
            this.lastSymbol = this.buffer[this.pointer];
            this.pointer++;
            if (this.pointer == this.buffer.length) {
                recordingInput();
            }
        }
        while (this.read > 0
                && this.function.check(buffer[pointer])
                && !endOfLine(this.lastSymbol, this.buffer[this.pointer])) {
            this.lastSymbol = this.buffer[this.pointer];
            pointer++;
            if (this.pointer == this.buffer.length) {
                recordingInput();
            }
        }
        return read > 0 && !endOfLine(this.lastSymbol, this.buffer[this.pointer]);
    }
    public String nextLine() {
        StringBuilder line = new StringBuilder();
        if (endOfLine(this.lastSymbol, this.buffer[this.pointer])) {
            this.lastSymbol = this.buffer[this.pointer];
            this.pointer++;
        }
        if (this.pointer >= this.buffer.length) {
            recordingInput();
        }
        while (this.read > 0 && !endOfLine(this.lastSymbol, this.buffer[this.pointer])) {
            line.append(this.buffer[this.pointer]);
            this.lastSymbol = this.buffer[this.pointer];
            this.pointer++;
            if (this.pointer == this.buffer.length) {
                recordingInput();
            }
        }
        return line.toString();
    }

    public boolean hasNext() {
        boolean next = false;
        if (this.pointer == this.buffer.length && this.read > 0) {
            recordingInput();
        }
        while (this.read > 0 && this.function.check(buffer[pointer])) {
            this.lastSymbol = this.buffer[this.pointer];
            this.pointer++;
            if (this.pointer == this.buffer.length) {
                recordingInput();
            }
        }
        if (this.pointer < this.buffer.length && this.read > 0 && !this.function.check(this.buffer[this.pointer])) {
            next = true;
        }
        return next;
    }
    public String next() {
        StringBuilder nextString = new StringBuilder();
        while (this.read > 0 && !this.function.check(this.buffer[this.pointer])) {
            nextString.append(this.buffer[this.pointer]);
            this.lastSymbol = this.buffer[this.pointer];
            this.pointer++;
            if (pointer == this.buffer.length) {
                recordingInput();
            }
        }
        this.lastSymbol = this.buffer[this.pointer];
        this.pointer++;
        if (pointer == this.buffer.length) {
            recordingInput();
        }
        return nextString.toString();
    }
    public int nextInt() {
        return Integer.parseInt(next());
    }
    public boolean endOfLine(char ch1, char ch2) {
        String chars = String.valueOf(ch1) +
                ch2;
        return chars.startsWith(System.lineSeparator());
    }
}