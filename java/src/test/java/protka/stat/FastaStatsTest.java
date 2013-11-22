package protka.stat;

import static org.junit.Assert.*;

import org.junit.Test;

import protka.FastaItem;

public class FastaStatsTest {

  // amino acids:
  // ACDEFGHIKLMNPQRSTVWY
  // class of this acids:
  // aliphatic: AGILV
  // hydroxyl or sulfur-containing: CMST
  // cyclic: P
  // aromatic: FWY
  // basic: HKR
  // acidic and their amide: DENQ
  
  @Test
  public void test() {
    FastaStats fastaStats = new FastaStats();
    String header = ">Q6GZX3;0;231;318";
    String ac = "Q6GZX3";
    FastaItem fastaItem = new FastaItem(header, ac);
    fastaItem.addSeqRow("AAAGIIIIIIIILLLVVVVVVCCCCCMSSTTTTTTTPFWWWWWWYYKKEEEQ");
    String expected = "3,5,0,3,1,1,0,8,2,3,1,0,1,1,0,2,7,6,6,2,21,15,1,9,2,4";
    assertTrue(fastaStats.computeStats(fastaItem).equals(expected));
  }
}
