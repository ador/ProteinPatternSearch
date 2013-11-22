package protka.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import protka.Protein;
import protka.io.SwissprotReader;
import protka.stat.ProteinMatcher;

public class ComputeHDStats {

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: ComputeHDStats inputfile.dat");
      return;
    }
    FileInputStream is;

    int hAllSum = 0;
    int dAllSum = 0;
    int hdAllSum = 0;
    int allCount = 0;
    
    long sumLenAllSeqs = 0L;
    
    try {
      is = new FileInputStream(args[0]);
      
      SwissprotReader reader = new SwissprotReader(is);
      Protein p = reader.getNextProtein();
      while (null != p) {
        int hNum = ProteinMatcher.matchSequence("H", p);
        int dNum = ProteinMatcher.matchSequence("D", p);
        int hdNum = ProteinMatcher.matchSequence("HD", p);
        int seqLen = p.getSequence().length();
        allCount++;
        hAllSum += hNum;
        dAllSum += dNum;
        hdAllSum += hdNum;
        sumLenAllSeqs += seqLen;
        if (allCount % 1000 == 0) {
          System.out.println("At protein: " + allCount);
        }
        p = reader.getNextProtein();
      }

      double hRatio = 1.0 * hAllSum / sumLenAllSeqs;
      double dRatio = 1.0 * dAllSum / sumLenAllSeqs;
      double hdRatio = 1.0 * hdAllSum / sumLenAllSeqs;
      
      // end: print results:
      System.out.println("\n STATS -----: " + allCount);
      System.out.println("All proteins:   " + allCount);
      System.out.println("H count:    " + hAllSum + "   H ratio: " + hRatio);
      System.out.println("D count:    " + dAllSum + "   D ratio: " + dRatio);
      System.out.println("HD count:   " + hdAllSum + "  HD ratio: " + hdRatio);
     
      System.out.println("\n  Expected HD ratio:   " + (hRatio * dRatio) );
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
      
  }

}
