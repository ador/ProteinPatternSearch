package protka.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import protka.filter.SwissprotRowFilter;

public class FilterRows {
  private static final String[] requiredProps = { "inputDatFile",
      "outputDatFile" };
  private PropertyHandler propHandler = new PropertyHandler(requiredProps);
  private FileInputStream is;
  private FileOutputStream os;
  private Properties properties; 
  
  private void createOutDirsAndFile(String outDatPath) throws IOException {
    String outDatDir = outDatPath.substring(0, outDatPath.lastIndexOf(File.separator));
    File targetFile = new File(outDatDir); 
    targetFile.mkdirs();
    os = new FileOutputStream(outDatPath);
  }
  
  private void filter() throws FileNotFoundException, IOException {
    if (propHandler.checkProps(properties)) {
      SwissprotRowFilter swissprotRowFilter = new SwissprotRowFilter();
      is = new FileInputStream(properties.getProperty("inputDatFile"));
      createOutDirsAndFile(properties.getProperty("outputDatFile"));
      if (properties.containsKey("throwOutRows")) {
        String[] throwOutRows = properties.getProperty(
            "throwOutRows").split(",");
        for (String pattern : throwOutRows)
          swissprotRowFilter.addFilterPattern(pattern);
      }
      swissprotRowFilter.setInputDat(is);
      swissprotRowFilter.setOutputDat(os);
      swissprotRowFilter.filter();
      os.flush();
      os.close();
    }
  }
  
  public static void main(String args[]) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: FilterRows settings.properties");
      return;
    }
    FilterRows fr = new FilterRows();
    try {
      fr.properties = fr.propHandler.readPropertiesFile(args[0]);
      fr.filter();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
