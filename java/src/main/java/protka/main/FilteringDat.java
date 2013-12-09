package protka.main;

import java.io.*;
import java.util.Properties;

import protka.Protein;
import protka.filter.ProteinFilter;
import protka.filter.SwissprotRowFilter;
import protka.io.SwissprotReader;
import protka.io.SwissprotWriter;

public class FilteringDat {
  private static final String[] requiredProps = { "inputDatFile",
      "outputDatFile" };

  public static void main(String args[]) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: FilteringDat prop.properties");
      return;
    }

    PropertyHandler propHandler = new PropertyHandler(requiredProps);
    SwissprotRowFilter swissprotRowFilter = new SwissprotRowFilter();
    ProteinFilter proteinFilter = new ProteinFilter();

    FileInputStream is;
    FileOutputStream os;
    File tempFile = File.createTempFile("tempFile", ".tmp");
    OutputStream out = new FileOutputStream(tempFile);

    try {
      Properties properties = propHandler.readPropertiesFile(args[0]);

      is = new FileInputStream(
          properties.getProperty("inputDatFile"));
      os = new FileOutputStream(
          properties.getProperty("outputDatFile"));

      swissprotRowFilter.setInputDat(is);
      swissprotRowFilter.setOutputDat(out);

      if (propHandler.checkProps(properties)) {

        if (properties.containsKey("throwOutRows")) {
          String[] throwOutRows = properties.getProperty(
              "throwOutRows").split(",");
          for (String pattern : throwOutRows)
            swissprotRowFilter.addFilterPattern(pattern);
        }

        swissprotRowFilter.filter();
        out.close();

        InputStream in = new FileInputStream(tempFile);

        SwissprotReader swissprotReader = new SwissprotReader(in);
        SwissprotWriter swissprotWriter = new SwissprotWriter(os);

        if (properties.containsKey("matchProteinPatterns")) {
          String[] patterns = properties.getProperty(
              "matchProteinPatterns").split("|");
          for (String pattern : patterns) {
            String[] kvPair = pattern.split(":");
            String key = kvPair[0];
            String value = kvPair[1];
            proteinFilter.addPattern(key, value);
          }
        }

        Protein protein;
        int counter = 1;
        while ((protein = swissprotReader.getNextProtein()) != null) {
          if (proteinFilter.match(protein)) {
            swissprotWriter.writeProtein(protein);
          }

          if (counter % 10000 == 0) {
            System.out.println("Read: " + counter + " proteins.");
          }
          ++counter;
        }
        swissprotWriter.closeOS();
        in.close();
        tempFile.delete();

      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
