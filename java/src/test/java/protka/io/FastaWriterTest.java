package protka.io;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import protka.FastaItem;
import protka.Protein;

public class FastaWriterTest {
  private Protein protein = new Protein();
  private OutputStream os;

  @Before
  public void setUp() {
    os = new ByteArrayOutputStream();

    List<String> lines = new ArrayList<String>();
    lines.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    lines.add("AC   Q6GZX4;");
    lines.add("FT   TRANSMEM    13    32       Helical; (Potential).");
    lines.add("FT   TRANSMEM   103   105       Helical; (Potential).");
    lines.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    lines.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    lines.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    lines.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    lines.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    lines.add("     SFRKIYTDLG WKFTPL");
    lines.add("//");

    protein.setLines(lines);
    Protein.setMaxLength(70);
    Protein.setMinLength(40);
  }

  @Test
  public void testWriteFastaItem() {
    FastaWriter fastaWriter = new FastaWriter(os);
    FastaItem fastaItem = new FastaItem(">sp|Q23456|something", "Q23456");
    fastaItem.addSeqRow("AAAA");
    fastaItem.addSeqRow("BBBB");
    fastaItem.addSeqRow("CCCC");
    try {
      fastaWriter.writeFastaItem(fastaItem);
      fastaWriter.closeOS();
      String expected = ">sp|Q23456|something\nAAAA\nBBBB\nCCCC\n";
      assertEquals(expected, os.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWriteFastaItemList() {
    FastaWriter fastaWriter = new FastaWriter(os);
    FastaItem fastaItem1 = new FastaItem(">sp|Q23456|something1", "Q23456");
    fastaItem1.addSeqRow("AAAA");
    fastaItem1.addSeqRow("BBBB");
    fastaItem1.addSeqRow("CCCC");
    FastaItem fastaItem2 = new FastaItem(">sp|QBCDEF|something2", "QBCDEF");
    fastaItem2.addSeqRow("DDDD");
    fastaItem2.addSeqRow("EEEE");
    fastaItem2.addSeqRow("FFFF");
    List<FastaItem> fastaList = new ArrayList<FastaItem>();
    fastaList.add(fastaItem1);
    fastaList.add(fastaItem2);
    try {
      fastaWriter.writeFastaList(fastaList);
      fastaWriter.closeOS();
      String expected = ">sp|Q23456|something1\nAAAA\nBBBB\nCCCC\n"
          + ">sp|QBCDEF|something2\nDDDD\nEEEE\nFFFF\n";
      assertEquals(expected, os.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWriteProtSeqInPieces() {
    try {
      FastaWriter fastaWriter = new FastaWriter(os);
      protein.setBeginsInside(false);
      fastaWriter.writeProtSeqInPieces(protein);
      String expected = ">sp|Q6GZX4|1|103|175\n";
      expected += "YQGIQTCKIPGKVLSDLDAKIKAYNLTVEGVEGFVRYSRVTKQHVAAFLKELRHSKQYEN"
          + "VNLIHYILTDKRV\n";
      assertEquals(expected, os.toString());
      
      os = new ByteArrayOutputStream();
      fastaWriter = new FastaWriter(os);
      protein.setBeginsInside(true);
      fastaWriter.writeProtSeqInPieces(protein);
      expected = ">sp|Q6GZX4|0|13|102\n"
          + "DRRRRMEALLLSLYYPNDRKLLDYKEWSPPRVQVECPKAPVEWNNPPSEKGLIVGHFSGIKYKGEK"
          + "AQASEVDVNKMCCWVSKFKDAMRR\n"
          + ">sp|Q6GZX4|1|33|105\n"
          + "LLDYKEWSPPRVQVECPKAPVEWNNPPSEKGLIVGHFSGIKYKGEKAQASEVDVNKMCCWVSKFKD"
          + "AMRRYQG\n";
      assertEquals(expected, os.toString());
      fastaWriter.closeOS();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
