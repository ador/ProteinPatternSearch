package protka.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import protka.FastaItem;
import protka.io.KClustResultReader;

public class KClustResultReaderTest {

  private final String clusters =
    "# 11\n"
    + "1 1\n"
    + "2 1\n"
    + "3 1\n"
    + "4 1\n"
    + "5 1\n"
    + "6 1\n"
    + "7 1\n"
    + "10 1\n"
    + "8 8\n"
    + "9 8\n"
    + "11 11\n";
  
  private final String headers = 
    "1 > Q09160|239-332|b\n"
    + "2 > P30459|239-332|b\n"
    + "3 > P30457|239-332|b\n"
    + "4 > P16210|239-332|b\n"
    + "5 > P30376|239-332|b\n"
    + "6 > P30515|239-332|b\n"
    + "7 > P30375|239-332|b\n"
    + "10 > P10316|238-328|b\n"
    + "8 > Q99LR1|75-165|a\n"
    + "9 > Q4R766|75-165|a\n"
    + "11 > Q24093|5-92|a\n";

  private final String fastaInput = 
    "> P30375|239-332|b\n"
    + "LTWQRDGEDQTQDTELVETRPAGDGTFQKWAAVVVPSGQEQRYTCHVQHEGLPEPLTLRWEPSSQPTIPIVGIIAGLVLFGAVIAGAVVAAVRW\n"
    + "> P30515|239-332|b\n"
    + "VTWQRDGEDQTQDMELVETRPAGDGTFQKWAAVVVLSGEEQRYTCHVQHEGLPEPLTLRWEPPSQPTIPIMGIVAILAILGVVVTGAVVAAVMW\n"
    + "> P30376|239-332|b\n"
    + "LTWQRDGEDQTQDTELVETRPAGDGTFQKWAAVVVPSGQEQRYTCHVQHESLPKPLTLRWEPSSQPTIPIVGIIAGLVLFGAVIAGAVIAAVRW\n"
    + "> P16210|239-332|b\n"
    + "LTWQRDGEDQTQDTELVETRPAGDRTFQKWAAVVVPSGEEQRYTCHVQHEGLPKPLTLRWEPSSQSTIPIVGIVAGLAVLAVVVIGAVVAAVMC\n"
    + "> P30457|239-332|b\n"
    + "LTWQRDGEDQTQDTELVETRPAGDGTFQKWASVVVPSGQEQRYTCHVQHEGLPKPLTLRWEPSSQPTIPIVGIIAGLVLFGAVIAGAVVAAVMW\n"
    + "> P10316|238-328|b\n"
    + "TLTWQRDGEDQTQDTELVETRPAGDGTFQKWAAVVVPSGQEQRYTCHVQHEGLPKPLTLRWEPSSQPTIPIVGIIAGLVLFGAVITGAVVA\n"
    + "> P30459|239-332|b\n"
    + "LTWQRDGEDQTQDTELVETRPAGDGTFQKWASVVVPSGQEQRYTCHVQHEGLPKPLTLRWEPSSQPTIPIVGIIAGLVLFGAMFAGAVVAAVRW\n"
    + "> Q09160|239-332|b\n"
    + "LTWQRDGEDQTQDTELVETRPAGDGTFQKWAAVVVPSGKEKRYTCHVQHEGLPEPLTLRWEPSSQPTIPIVGIIAGLVLLGAVIAGAVVAAVMW\n"
    + "> Q4R766|75-165|a\n"
    + "ILFCVLGLYIAIPFLIKLCPGIQAKLIFLNFVRVPYFIDLKKPQDQGLNHTCNYYLQPEEDVTIGVWHTVPAVWWKNAQGKDQMWYEDALA\n"
    + "> Q99LR1|75-165|a\n"
    + "ILLCVLGFYIAIPFLVKLCPGIQAKLIFLNFVRVPYFIDLKKPQDQGLNHTCNYYLQPEDDVTIGVWHTIPSVWWKNAQGKDQMWYEDALA\n"
    + "> Q24093|5-92|a\n"
    + "FLTLIAVIVCILFRILNVHSQPLKPSVWCLDAHFLDCLYKIAPVLREPYIPPRLWGFSGHVQTVLHSIVGRVRCPWPLGERVYMSLKD\n";
  
  private InputStream isClusters;
  private InputStream isHeaders;
  private InputStream isFastaAll;

  private KClustResultReader reader;
  
  @Before
  public void setUp() {
    isClusters = new ByteArrayInputStream(clusters.getBytes());
    isHeaders = new ByteArrayInputStream(headers.getBytes());
    isFastaAll = new ByteArrayInputStream(fastaInput.getBytes());
    reader = new KClustResultReader();
    reader.setClustersInput(isClusters);
    reader.setHeadersInput(isHeaders);
    reader.setFastaInput(isFastaAll);
  }

  @Test
  public void testReaderNotNull() {
    assertNotNull(reader);
  }

  @Test
  public void testSizes() {
    try {
      reader.readClusters();
      // in this example we have 11 fasta items, so 11 header rows must be read
      assertEquals(11, reader.getSizeOfHeadersMap());
      assertEquals(11, reader.getSizeOfHeadersMap());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testNumOfClusters() {
    try {
      // in this example we expect 3 clusters
      reader.readClusters();
      int numOfClusters = reader.getNumOfClusters();
      assertEquals(3, numOfClusters);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testClusters() {
    try {
      reader.readClusters();
      List<List<FastaItem> > result = reader.getClusters();
      assertEquals(3, result.size());
      List<FastaItem> clusterA = result.get(0);
      List<FastaItem> clusterB = result.get(1);
      List<FastaItem> clusterC = result.get(2);
      assertNotNull(clusterA);
      assertNotNull(clusterB);
      assertNotNull(clusterC);
      assertEquals(8, clusterA.size());
      assertEquals(2, clusterB.size());
      assertEquals(1, clusterC.size());
      assertNotNull(clusterA.get(0));
      assertEquals("> Q09160|239-332|b", clusterA.get(0).getHeader());
      assertEquals("> P16210|239-332|b", clusterA.get(3).getHeader());
      assertEquals("> Q24093|5-92|a", clusterC.get(0).getHeader());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
