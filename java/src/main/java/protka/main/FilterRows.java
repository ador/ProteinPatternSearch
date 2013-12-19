package protka.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import protka.filter.SwissprotRowFilter;

public class FilterRows {
  private static final String[] requiredProps = { "inputDatFile",
      "outputDatFile" };

  public static void main(String args[]) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: FilterRows settings.properties");
      return;
    }

    PropertyHandler propHandler = new PropertyHandler(requiredProps);
    SwissprotRowFilter swissprotRowFilter = new SwissprotRowFilter();

    FileInputStream is;
    FileOutputStream os;

    try {
      Properties properties = propHandler.readPropertiesFile(args[0]);

      is = new FileInputStream(
          properties.getProperty("inputDatFile"));
      os = new FileOutputStream(
          properties.getProperty("outputDatFile"));

      swissprotRowFilter.setInputDat(is);
      swissprotRowFilter.setOutputDat(os);

      if (propHandler.checkProps(properties)) {

        if (properties.containsKey("throwOutRows")) {
          String[] throwOutRows = properties.getProperty(
              "throwOutRows").split(",");
          for (String pattern : throwOutRows)
            swissprotRowFilter.addFilterPattern(pattern);
        }

        swissprotRowFilter.filter();
        os.flush();
        os.close();

      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
