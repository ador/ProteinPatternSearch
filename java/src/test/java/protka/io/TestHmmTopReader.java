package protka.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class TestHmmTopReader {
  private InputStream is1;
  private InputStream is2;
  private InputStream is3;
  private InputStream is4;
  private InputStream isAll;

  private final String outputFile1 = ">HP: 320 sp|Q6GZX3|002L_FRG3G "
      + "Uncharacterized protein 002L OS=Frog virus 3 (isolate Goorha) "
      + "GN=FV3-002L PE=4 OUT   1  301  318 \n" + "The best model:\n\n"
      + "     seq  MSIIGATRLQ NDKSDTYSAG PCYAGGCSAF TPRGTCGKDW DLGEQTCASG    "
      + "50\n"
      + "     pred OOOOOOOOOO OOOOOOOOOO OOOOOOOOOO OOOOOOOOOO OOOOOOOOOO\n";
  private final String outputFile2 = ">HP: 320 sp|Q6GZX4|002L_FRG3G "
      + "Uncharacterized protein 002L OS=Frog virus 3 (isolate Goorha) "
      + "GN=FV3-002L PE=4 OUT   1  301  318 \n" + "The best model:\n\n"
      + "     seq  LKQMFTIDEF GSISSELNSV YQAGFLGFLI GAIYGGVTQS RVAYMNFMEN   100\n" 
      + "     pred iIIIIIiiii OOOOOOOOOO OOOOOOOOOO OOOOOOOOOO OOOOOOOOOO\n"
      + "\n"
      + "     seq  MSIIGATRLQ NDKSDTYSAG PCYAGGCSAF TPRGTCGKDW DLGEQTCASG    "
      + "50\n"
      + "     pred OOOOoooooo oooooooooH HHHHHHHHHH HHHHHHHiii iiiiiiiiii \n";
  private final String outputFile3 = ">HP: 320 sp|Q6GZX5|002L_FRG3G "
      + "Uncharacterized protein 002L OS=Frog virus 3 (isolate Goorha) "
      + "GN=FV3-002L PE=4 OUT   1  301  318 \n"
      + "The best model:\n\n"
      + "     seq  MSIIGATRLQ NDKSDTYSAG PCYAGGCSAF TPRGTCGKDW DLGEQTCASG    50"
      + "50\n"
      + "     pred iOoooOoooo OOOOOOOOOO OOOOOOOOOO OOOOOOOOOO OOOOOOOOOO\n"
      + "\n"
      + "     seq  LKQMFTIDEF GSISSELNSV YQAGFLGFLI GAIYGGVTQS RVAYMNFMEN   100\n" 
      + "     pred OOOOoooooo oooooooooH HHHHHHHHHH HHHHHHHiii iiiiiiiiii \n" 
      + "\n"
      + "     seq  LKQMFTIDEF GSISSELNSV YQAGFLGFLI GAIYGGVTQS RVAYMNFMEN   100\n" 
      + "     pred OOOOoooooo oooooooooH HHHHHHHHHH HHHHHHHiii iiiiiiiiii \n" 
      + "\n";
  private final String outputFile4 = ">HP: 320 sp|Q6GZX6|002L_FRG3G "
      + "Uncharacterized protein 002L OS=Frog virus 3 (isolate Goorha) "
      + "GN=FV3-002L PE=4 OUT   1  301  318 \n" + "The best model:\n\n"
      + "     seq  MSIIGATRLQ NDKSDTYSAG PCYAGGCSAF TPRGTCGKDW DLGEQTCASG    "
      + "50\n"
      + "     pred HhqxahhhHh iOOOOOOOOO OOOOOOOOOO OOOOOOOOOO OOOOOOOOOO\n";
  private final String outputFileAll = outputFile1 + outputFile2 + outputFile3
      + outputFile4;

  @Before
  public void setUp() {
    is1 = new ByteArrayInputStream(outputFile1.getBytes());
    is2 = new ByteArrayInputStream(outputFile2.getBytes());
    is3 = new ByteArrayInputStream(outputFile3.getBytes());
    is4 = new ByteArrayInputStream(outputFile4.getBytes());
    isAll = new ByteArrayInputStream(outputFileAll.getBytes());
  }

  @Test
  public void testGetNextProteinBeginsInside() {
    HmmTopReader hmmTopReader1 = new HmmTopReader(is1);
    HmmTopReader hmmTopReader2 = new HmmTopReader(is2);
    HmmTopReader hmmTopReader3 = new HmmTopReader(is3);
    HmmTopReader hmmTopReader4 = new HmmTopReader(is4);
    HmmTopReader hmmTopReaderAll = new HmmTopReader(isAll);

    assertFalse(hmmTopReader1.getNextProteinBeginsInside());
    hmmTopReader1.closeReader();
    assertTrue(hmmTopReader2.getNextProteinBeginsInside());
    hmmTopReader2.closeReader();
    assertTrue(hmmTopReader3.getNextProteinBeginsInside());
    hmmTopReader3.closeReader();
    assertFalse(hmmTopReader4.getNextProteinBeginsInside());
    hmmTopReader4.closeReader();

    assertFalse(hmmTopReaderAll.getNextProteinBeginsInside());
    assertEquals("Q6GZX3", hmmTopReaderAll.getLastACNum());
    assertTrue(hmmTopReaderAll.getNextProteinBeginsInside());
    assertEquals("Q6GZX4", hmmTopReaderAll.getLastACNum());
    assertTrue(hmmTopReaderAll.getNextProteinBeginsInside());
    assertEquals("Q6GZX5", hmmTopReaderAll.getLastACNum());
    assertFalse(hmmTopReaderAll.getNextProteinBeginsInside());
    assertEquals("Q6GZX6", hmmTopReaderAll.getLastACNum());
    hmmTopReaderAll.closeReader();
  }

}
