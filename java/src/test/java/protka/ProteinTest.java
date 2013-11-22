package protka;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ProteinTest {
  private Protein protein1 = new Protein();
  private Protein protein2 = new Protein();
  private Protein protein3 = new Protein();
  private Protein protein4 = new Protein();

  @Before
  public void setUp() {
    List<String> lines1 = new ArrayList<String>();
    lines1.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines1.add("AC   Q6GZX4;");
    lines1.add("FT   TRANSMEM    13    31       Helical; (Potential).");
    lines1.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    lines1.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    lines1.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    lines1.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    lines1.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    lines1.add("     SFRKIYTDLG WKFTPL");
    lines1.add("//");

    List<String> lines2 = new ArrayList<String>();
    lines2.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines2.add("AC   Q6GZX4;");
    lines2.add("FT   TRANSMEM    12    21       Helical; (Potential).");
    lines2.add("FT   TRANSMEM    23    25       Helical; (Potential).");
    lines2.add("FT   TRANSMEM   101   111       Helical; (Potential).");
    lines2.add("FT   TRANSMEM   123   123       Helical; (Potential).");
    lines2.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    lines2.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS "
        + "PPRVQVECPK APVEWNNPPS");
    lines2.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM "
        + "RRYQGIQTCK IPGKVLSDLD");
    lines2.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY "
        + "ENVNLIHYIL TDKRVDIQHL");
    lines2.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG "
        + "PDGPDILTVK TGSKGVLYDD");
    lines2.add("     SFRKIYTDLG WKFTPL");
    lines2.add("//");

    List<String> lines3 = new ArrayList<String>();
    lines3.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines3.add("AC   Q6GZX4;");
    lines3.add("FT   TRANSMEM   240   250       Helical; (Potential).");
    lines3.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    lines3.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    lines3.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    lines3.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    lines3.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    lines3.add("     SFRKIYTDLG WKFTPL");
    lines3.add("//");

    List<String> lines4 = new ArrayList<String>();
    lines4.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines4.add("AC   Q6GZX4;");
    lines4.add("FT   CHAIN         1    261       RPII140-upstream gene "
        + "protein.");
    lines4.add("FT                                /FTId=PRO_0000064352.");
    lines4.add("FT   TRANSMEM     67     87       Helical; (Potential).");
    lines4.add("FT   TRANSMEM    131    151       Helical; (Potential).");
    lines4.add("FT   TRANSMEM    183    203       Helical; (Potential).");
    lines4.add("FT   CONFLICT     64     64       S -> F (in Ref. 1; "
        + "AAD40352).");
    lines4.add("SQ   SEQUENCE   261 AA;  29182 MW;  5DB78CF6CFC4435A CRC64;");
    lines4.add("     MNFLWKGRRF LIAGILPTFE GAADEIVDKE NKTYKAFLAS KPPEETGLER "
        + "LKQMFTIDEF");
    lines4.add("     GSISSELNSV YQAGFLGFLI GAIYGGVTQS RVAYMNFMEN NQATAFKSHF "
        + "DAKKKLQDQF");
    lines4.add("     TVNFAKGGFK WGWRVGLFTT SYFGIITCMS VYRGKSSIYE YLAAGSITGS "
        + "LYKVSLGLRG");
    lines4.add("     MAAGGIIGGF LGGVAGVTSL LLMKASGTSM EEVRYWQYKW RLDRDENIQQ "
        + "AFKKLTEDEN");
    lines4.add("     PELFKAHDEK TSEHVSLDTI K");
    lines4.add("//");

    protein1.setLines(lines1);
    protein2.setLines(lines2);
    protein3.setLines(lines3);
    protein4.setLines(lines4);
    Protein.setMaxLength(70);
    Protein.setMinLength(40);
  }

  @Test
  public void testAcNum() {
    assertTrue(protein1.acNum.equals("Q6GZX4"));
  }

  @Test
  public void testReadTmNumbers() {
    List<SequencePart> tmNumbers = protein1.getTmNumbers();
    assertNotNull(tmNumbers);
    assertEquals(1, tmNumbers.size());
    assertEquals(13, tmNumbers.get(0).getFrom());
    assertEquals(31, tmNumbers.get(0).getTo());

    tmNumbers = protein2.getTmNumbers();
    assertNotNull(tmNumbers);
    assertEquals(4, tmNumbers.size());
  }

  @Test
  public void testReadMoreTmNumbers() {
    List<SequencePart> tmNumbers = protein2.getTmNumbers();
    assertNotNull(tmNumbers);
    assertEquals(4, tmNumbers.size());
    assertEquals(12, tmNumbers.get(0).getFrom());
    assertEquals(21, tmNumbers.get(0).getTo());
    assertEquals(123, tmNumbers.get(3).getFrom());
    assertEquals(123, tmNumbers.get(3).getTo());
  }

  @Test
  public void testGetSequencePart() {
    assertEquals("YKGEKA", protein1.getSequencePart(74, 79));
    assertEquals("L", protein1.getSequencePart(256, 256));
    assertNull(protein1.getSequencePart(256, 257));
  }

  @Test
  public void testGetSeqForTmPart() {
    protein1.setBeginsInside(false);
    SequencePart seqPart = protein1.getSeqForTmPart(0);
    assertNull(seqPart);
    protein1.setBeginsInside(true);
    seqPart = protein1.getSeqForTmPart(0);
    assertEquals(13, seqPart.getFrom());
    assertEquals(101, seqPart.getTo());

    protein3.setBeginsInside(true);
    seqPart = protein3.getSeqForTmPart(0);
    assertNull(seqPart);
    protein3.setBeginsInside(false);
    seqPart = protein3.getSeqForTmPart(0);
    assertEquals(170, seqPart.getFrom());
    assertEquals(250, seqPart.getTo());

    protein2.setBeginsInside(false);
    seqPart = protein2.getSeqForTmPart(0);
    assertNull(seqPart);
    seqPart = protein2.getSeqForTmPart(1);
    assertEquals(23, seqPart.getFrom());
    assertEquals(95, seqPart.getTo());

    protein2.setBeginsInside(true);
    seqPart = protein2.getSeqForTmPart(0);
    assertNull(seqPart);
    seqPart = protein2.getSeqForTmPart(1);
    assertNull(seqPart);
  }

  @Test
  public void testProt4() {
    protein4.setBeginsInside(false);
    List<SequencePart> tmNumbers = protein4.getTmNumbers();
    assertEquals(3, tmNumbers.size());
    assertEquals(67, protein4.getTmNumbers().get(0).getFrom());
    assertEquals(87, protein4.getTmNumbers().get(0).getTo());
    assertEquals(203, protein4.getTmNumbers().get(2).getTo());
    SequencePart seqPart = protein4.getSeqForTmPart(0);
    assertEquals(1, seqPart.getFrom());
    assertEquals(87, seqPart.getTo());
  }
}
