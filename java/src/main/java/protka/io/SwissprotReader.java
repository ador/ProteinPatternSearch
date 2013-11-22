package protka.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import protka.Protein;

public class SwissprotReader {

  private InputStream input;
  private BufferedReader reader;

  private List<String> lastRows = new ArrayList<String>();

  public InputStream getInput() {
    return input;
  }

  public SwissprotReader(InputStream is) {
    input = is;
    reader = new BufferedReader(new InputStreamReader(input));
  }

  public Protein getNextProtein() throws IOException {
    String line = reader.readLine();
    while (null != line) {
      if (line.startsWith("//")) {
        Protein p = new Protein();
        p.setLines(lastRows);
        lastRows.clear();
        return p;
      }
      lastRows.add(line);
      line = reader.readLine();
    }
    return null;
  }

  public void closeReader() throws IOException{
    reader.close();
  }
}
