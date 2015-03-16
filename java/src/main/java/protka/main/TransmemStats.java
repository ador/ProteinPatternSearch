package protka.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import protka.Protein;
import protka.io.SwissprotReader;

public class TransmemStats {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: TransmemStats inputfile.dat");
      return;
    }
    
    try {
      FileInputStream is = new FileInputStream(args[0]);
      int tmProteinsWithTIE = 0;
      int tmProteinsWithT = 0;
      int allProteins = 0;
   
      SwissprotReader reader = new SwissprotReader(is);
      Protein p = reader.getNextProtein();
      while (null != p) {

        allProteins++;
        if (p.hasTmOrientationInfo()) {
          tmProteinsWithTIE++;
        }
        if (null != p.getTmNumbers() && ! p.getTmNumbers().isEmpty()) {
          tmProteinsWithT++;
        }
        if (allProteins % 1000 == 0) {
          System.out.println("At protein: " + allProteins);
        }
        p = reader.getNextProtein();
      }
    
      System.out.println("All proteins in dat file: " + allProteins);
      System.out.println("Proteins with annotated TM domain: " + tmProteinsWithT);
      System.out.println("Proteins with annotated TM domain and orientation: " + tmProteinsWithTIE);
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
