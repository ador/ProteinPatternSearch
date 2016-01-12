package protka.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import protka.FastaItem;
import protka.io.ProteinStatsArffWriter;
import protka.io.FastaReader;
import protka.stat.ProteinAminoAcidStats;

public class CreateArffFromFragmentStats {

  private static final String[] requiredProps = { "inputFastaFile",
      "proteinStatsArffFile" };

  private static FileOutputStream createOutDirsAndFile(String outFilePath) throws IOException {
    String outFileDir = outFilePath.substring(0, outFilePath.lastIndexOf(File.separator));
    File targetFile = new File(outFileDir); 
    targetFile.mkdirs();
    return new FileOutputStream(outFilePath);
  }
      
  public static void main(String args[]) {
    if (args.length != 1) {
      System.out.println("Usage: CreateArffFromFragmentStats prop.properties");
      return;
    }

    PropertyHandler propHandler = new PropertyHandler(requiredProps);
    Properties properties;
    FileInputStream is;
    FileOutputStream os;
    
    try {
      properties = propHandler.readPropertiesFile(args[0]);
      if (propHandler.checkProps(properties)) {
        is = new FileInputStream(
            properties.getProperty("inputFastaFile"));
        os = createOutDirsAndFile(properties.getProperty("proteinStatsArffFile"));

        FastaReader fastaReader = new FastaReader(is);
        ProteinAminoAcidStats fastaStats = new ProteinAminoAcidStats();
        ProteinStatsArffWriter arffWriter = new ProteinStatsArffWriter(os);
        arffWriter.writeHeader();
        
        int counter = 0;
        FastaItem fastaItem = fastaReader.getNextFastaItem();
        while(fastaItem != null){
          arffWriter.writeData(fastaStats.computeCsvStatsString(fastaItem));
          fastaItem = fastaReader.getNextFastaItem();
          ++counter;
          if (counter % 1000 == 0) {
            System.out.println("Read and wrote " + counter + " fasta items.");
          }
        }
        arffWriter.closeOS();
        
      } else {
        System.out.println("Missing property! Aborting.");
        System.exit(2);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
