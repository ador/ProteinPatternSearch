package protka.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SimpleWriter {

  private OutputStream os;

  public SimpleWriter(OutputStream outputStream) {
    os = outputStream;
  }

  public void closeOS() throws IOException {
    os.flush();
    os.close();
  }

  public void writeData(String data) throws IOException {
    os.write(data.getBytes(Charset.forName("UTF-8")));
    os.write("\n".getBytes(Charset.forName("UTF-8")));
  }

}
