package protka.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import protka.FastaItem;
import protka.io.ClusterReader;
import protka.io.FastaReader;
import protka.io.FastaWriter;

public class CreateClusterFiles {
  private static final String[] requiredProps = { "wekaOutClusterPath",
      "outputFastaFilesPrefix", "numberOfClusters", "inputFastaFile" };

  private static void createOutDir(String outFastaPrefix) throws IOException {
    String outDir = outFastaPrefix.substring(0, outFastaPrefix.lastIndexOf(File.separator));
    File targetFile = new File(outDir); 
    targetFile.mkdirs();
  }
      
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: CreateArff prop.properties");
      return;
    }

    PropertyHandler propHandler = new PropertyHandler(requiredProps);
    Properties properties = propHandler.readPropertiesFile(args[0]);

    if (propHandler.checkProps(properties)) {
      try {
        int numOfClusters = Integer.parseInt(properties
            .getProperty("numberOfClusters"));
        createOutDir(properties.getProperty("outputFastaFilesPrefix"));
        FileInputStream icluster = new FileInputStream(
            properties.getProperty("wekaOutClusterPath"));
        FileInputStream ifasta = new FileInputStream(
            properties.getProperty("inputFastaFile"));
        List<FastaWriter> fastaWriters = new ArrayList<FastaWriter>();

        FastaReader fastaReader = new FastaReader(ifasta);

        FileOutputStream os;
        for (int i = 0; i < numOfClusters; ++i) {
          os = new FileOutputStream(properties.getProperty("outputFastaFilesPrefix")
              + i + ".fasta");
          fastaWriters.add(new FastaWriter(os));
        }

        ClusterReader clusterReader = new ClusterReader(icluster);
        int clusterGroup;
        FastaItem fastaItem;
        while ((clusterGroup = clusterReader.getNextClusterGroup()) != -1) {
          if ((fastaItem = fastaReader.getNextFastaItem()) != null) {
            fastaWriters.get(clusterGroup).writeFastaItem(fastaItem);
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
