package protka.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import protka.io.ArffWriter;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;

public class WekaClustering {

  private static final String[] requiredProps = { "inputArffFile",
      "numberOfClusters", "randomSeed", "outputClusterFile" };

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: Clustering prop.clusterprop");
      return;
    }

    PropertyHandler propHandler = new PropertyHandler(requiredProps);
    Properties properties = propHandler.readPropertiesFile(args[0]);

    if (propHandler.checkProps(properties)) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(
            properties.getProperty("inputArffFile")));
        Instances instances = new Instances(reader);
        reader.close();

        SimpleKMeans kmeans = new SimpleKMeans();

        kmeans.setSeed(Integer.parseInt(properties.getProperty("randomSeed")));

        int numberOfClusters = Integer.parseInt(properties
            .getProperty("numberOfClusters"));
        kmeans.setPreserveInstancesOrder(true);
        kmeans.setNumClusters(numberOfClusters);
        kmeans.buildClusterer(instances);

        // This array returns the cluster number (starting with 0) for each
        // instance
        // The array has as many elements as the number of instances
        int[] assignments = kmeans.getAssignments();

        int i = 0;
        if (properties.containsKey("outputClusterFile")) {
          FileOutputStream os = new FileOutputStream(
              properties.getProperty("outputClusterFile"));
          ArffWriter arffWriter = new ArffWriter(os);
          String line;
          for (int clusterNum : assignments) {
            line = i + "|" + clusterNum;
            arffWriter.writeData(line);
            i++;
          }
        } else {
          for (int clusterNum : assignments) {
            System.out.printf("%d|%d", i, clusterNum);
            i++;
          }
        }

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
