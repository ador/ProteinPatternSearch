package protka.io;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import protka.Protein;
import protka.io.SwissprotReader;

public class SwissprotReaderTest {
  private final String oneShortItem1 = "ID   001R_FRG3G              Reviewed;         256 AA.\n"
      + "AC   Q6GZX4;\n"
      + "SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;\n"
      + "     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK APVEWNNPPS\n"
      + "     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK IPGKVLSDLD\n"
      + "     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL TDKRVDIQHL\n"
      + "     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK TGSKGVLYDD\n"
      + "     SFRKIYTDLG WKFTPL\n" + "//\n";

  private final String oneShortItem2 = "ID   013L_IIV3               Reviewed;          90 AA.\n"
      + "AC   Q197E7;\n"
      + "KW   Complete proteome; Host membrane; Membrane; Reference proteome;\n"
      + "KW   Transmembrane; Transmembrane helix.\n"
      + "FT   CHAIN         1     90       Uncharacterized protein IIV3-013L.\n"
      + "FT                                /FTId=PRO_0000377942.\n"
      + "FT   TRANSMEM     52     72       Helical; (Potential).\n"
      + "SQ   SEQUENCE   90 AA;  10851 MW;  077C22D16315DB07 CRC64;\n"
      + "     MYYRDQYGNV KYAPEGMGPH HAASSSHHSA QHHHMTKENF SMDDVHSWFE KYKMWFLYAL\n"
      + "     ILALIFGVFM WWSKYNHDKK RSLNTASIFYHD\n" + "//\n";

  private final String oneShortItem3 = "ID   004R_FRG3G              Reviewed;          60 AA.\n"
      + "AC   Q6GZX1;\n"
      + "KW   Complete proteome; Host membrane; Membrane; Reference proteome;\n"
      + "KW   Transmembrane; Transmembrane helix.\n"
      + "FT   CHAIN         1     60       Uncharacterized protein 004R.\n"
      + "FT                                /FTId=PRO_0000410528.\n"
      + "FT   TRANSMEM     14     34       Helical; (Potential).\n"
      + "SQ   SEQUENCE   60 AA;  6514 MW;  12F072778EE6DFE4 CRC64;\n"
      + "     HDAKYDTDQG VGRMLFLGTI GLAVVVGGLM AYGYYYDGKT PSSGTSFHTA SPSFSSRYRY\n"
      + "//\n";

  private InputStream isAll;

  @Before
  public void setUp() {
    isAll = new ByteArrayInputStream(
        (oneShortItem1 + oneShortItem2 + oneShortItem3).getBytes());
  }

  @Test
  public void testNumOfProteins() {
    SwissprotReader reader = new SwissprotReader(isAll);
    List<Protein> proteinList = new ArrayList<Protein>();
    try {
      Protein p = reader.getNextProtein();
      while (p != null) {
        proteinList.add(p);
        p = reader.getNextProtein();
      }
      assertEquals(3, proteinList.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testProteinSimple1() {
    SwissprotReader reader = new SwissprotReader(isAll);
    List<Protein> proteinList = new ArrayList<Protein>();
    try {
      Protein p = reader.getNextProtein();
      while (p != null) {
        proteinList.add(p);
        p = reader.getNextProtein();
      }
      assertEquals(3, proteinList.size());
      Protein p1 = proteinList.get(0);
      Protein p2 = proteinList.get(1);

      assertEquals("Q6GZX4", p1.acNum);
      assertEquals("Q197E7", p2.acNum);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testProteinID() {
    SwissprotReader reader = new SwissprotReader(isAll);
    List<Protein> proteinList = new ArrayList<Protein>();
    try {
      Protein p = reader.getNextProtein();
      while (p != null) {
        proteinList.add(p);
        p = reader.getNextProtein();
      }

      Protein p1 = proteinList.get(0);
      Protein p2 = proteinList.get(1);

      assertNotNull(p1.getLine("ID"));
      assertNotNull(p2.getLine("ID"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void testSequenceLine() {
    SwissprotReader reader = new SwissprotReader(isAll);
    List<Protein> proteinList = new ArrayList<Protein>();
    try {
      Protein p = reader.getNextProtein();
      while (p != null) {
        proteinList.add(p);
        p = reader.getNextProtein();
      }
      assertEquals(3, proteinList.size());
      Protein p2 = proteinList.get(1);
      Protein p3 = proteinList.get(2);

      assertEquals("SQ   SEQUENCE   90 AA;  10851 MW;  077C22D16315DB07 CRC64;",
          p2.getLine("SQ"));
      assertEquals("SQ   SEQUENCE   60 AA;  6514 MW;  12F072778EE6DFE4 CRC64;",
          p3.getLine("SQ"));

      assertEquals("HDAKYDTDQGVGRMLFLGTIGLAVVVGGLMAYGYYYDGKTPSSGTSFHTASPSFSSRYRY",
          p3.getSequence());
      assertEquals(
          "MYYRDQYGNVKYAPEGMGPHHAASSSHHSAQHHHMTKENFSMDDVHSWFEKYKMWFLYALILALIFGVFMWWSKYNHDKKRSLNTASIFYHD",
          p2.getSequence());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testMultipleLines() {
    SwissprotReader reader = new SwissprotReader(isAll);
    List<Protein> proteinList = new ArrayList<Protein>();
    try {
      Protein p = reader.getNextProtein();
      while (p != null) {
        proteinList.add(p);
        p = reader.getNextProtein();
      }
      assertEquals(3, proteinList.size());
      Protein p2 = proteinList.get(1);

      assertEquals(
          "FT   CHAIN         1     90       Uncharacterized protein IIV3-013L.", p2
              .getLines("FT", "CHAIN").get(0));
      assertEquals("FT   TRANSMEM     52     72       Helical; (Potential).", p2
          .getLines("FT", "TRANSMEM").get(0));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
