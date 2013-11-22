package protka.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SwissprotRowFilter {
  private InputStream inputDat;
  private OutputStream outputDat;
  private BufferedReader reader;
  private List<String> excludePatterns = new ArrayList<String>();
  private int countIn = 0;
  private int countOut = 0;

  // TODO ? : filtering of "SQ" rows is not supported yet, but we might not need
  // it

  public OutputStream getOutputDat() {
    return outputDat;
  }

  public void setOutputDat(OutputStream output) {
    this.outputDat = output;
  }

  public InputStream getInputDat() {
    return inputDat;
  }

  public void setInputDat(InputStream is) {
    inputDat = is;
    reader = new BufferedReader(new InputStreamReader(inputDat));
  }

  public void filter() throws IOException {
    String line = reader.readLine();

    while (null != line) {
      boolean skip = false;
      for (String pattern : excludePatterns) {
        if (line.startsWith(pattern)) {
          skip = true;
          break;
        }
      }
      if (!skip) {
        line = line + "\n";
        outputDat.write(line.getBytes(Charset.forName("UTF-8")));
        ++countOut;
      }
      countIn++;
      if (countIn % 100000 == 0) {
        System.out.println("Proteins processed: " + countIn + "   out : "
            + countOut);
      }
      line = reader.readLine();
      
    }
  }

  public void addFilterPattern(String p) {
    excludePatterns.add(p);
  }

}
