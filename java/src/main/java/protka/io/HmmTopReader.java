package protka.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HmmTopReader {

  private BufferedReader bufferReader;

  public HmmTopReader(InputStream is) {
    this.bufferReader = new BufferedReader(new InputStreamReader(is));
  }

  public void closeReader() {
    try {
      bufferReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean getNextProteinBeginsInside() {
    try {
      String line = bufferReader.readLine();
      line = bufferReader.readLine();
      while (line != null && !line.startsWith(">")) {
        if (line.startsWith("     pred")) {
          return lineBeginsInside(line);
        }
        line = bufferReader.readLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private boolean lineBeginsInside(String line) {
    if(line.length() < 12)
      return false;
    char pred = line.charAt(10);
    if(pred == 'i' || pred == 'I'){
      return true;
    }
    if(pred == 'o' || pred == 'O'){
      return false;
    }
    for (int i = 11; i < line.length(); ++i) {
      pred = line.charAt(i);
      if(pred == 'i' || pred == 'I'){
        return false;
      }
      if(pred == 'o' || pred == 'O'){
        return true;
      }
    }
    return false;
  }

}
