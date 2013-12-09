package protka.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HmmTopReader {

  private BufferedReader bufferReader;
  private String lastACNum = "";

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

  public String getLastACNum() {
    return lastACNum;
  }
  
  private void findNextHeaderRow() throws IOException {
    String line = bufferReader.readLine();
    while (!line.startsWith(">")) {
      line = bufferReader.readLine();
    }
    lastACNum = line.split("\\|")[1];
  }

  public boolean getNextProteinBeginsInside() {
    try {
      findNextHeaderRow();
      String line = bufferReader.readLine(); // "The best model" row
      line = bufferReader.readLine(); // an empty row
      line = bufferReader.readLine(); // first seq row
      line = bufferReader.readLine(); // first pred row
      return lineBeginsInside(line);
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
