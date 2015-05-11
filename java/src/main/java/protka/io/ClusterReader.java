package protka.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClusterReader {

  private final InputStream input;
  private final BufferedReader reader;
  
  public ClusterReader(InputStream in){
    input = in;
    reader = new BufferedReader(new InputStreamReader(input));
  }
  
  public int getNextClusterGroup() throws IOException {
    String line;
    if ((line = reader.readLine()) != null) {
      return Integer.parseInt(line.split("\\|")[1]);
    }
    return -1;
  }

}
