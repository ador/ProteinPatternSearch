package protka.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ProteinStatsArffWriter {

  private OutputStream os;
  private final List<String> header = new ArrayList<String>();

  // TODO : more general... ?
  
  public ProteinStatsArffWriter(OutputStream outputStream) {
    os = outputStream;
    header.add("@relation 'protka'");
    header.add("@attribute A real");
    header.add("@attribute C real");
    header.add("@attribute D real");
    header.add("@attribute E real");
    header.add("@attribute F real");
    header.add("@attribute G real");
    header.add("@attribute H real");
    header.add("@attribute I real");
    header.add("@attribute K real");
    header.add("@attribute L real");
    header.add("@attribute M real");
    header.add("@attribute N real");
    header.add("@attribute P real");
    header.add("@attribute Q real");
    header.add("@attribute R real");
    header.add("@attribute S real");
    header.add("@attribute T real");
    header.add("@attribute V real");
    header.add("@attribute W real");
    header.add("@attribute Y real");
    header.add("@attribute Apiphatic real");
    header.add("@attribute HydroxylOrSulfurContaining real");
    header.add("@attribute Cyclic real");
    header.add("@attribute Aromatic real");
    header.add("@attribute Basic real");
    header.add("@attribute AcidicAndTheirAmide real");
    header.add("@data");
  }

  public void closeOS() throws IOException {
    os.flush();
    os.close();
  }

  public void writeHeader() throws IOException {
    for (int i = 0; i < header.size(); ++i) {
      os.write(header.get(i).getBytes(Charset.forName("UTF-8")));
      os.write("\n".getBytes(Charset.forName("UTF-8")));
    }
  }

  public void writeData(String data) throws IOException {
    os.write(data.getBytes(Charset.forName("UTF-8")));
    os.write("\n".getBytes(Charset.forName("UTF-8")));
  }

}
