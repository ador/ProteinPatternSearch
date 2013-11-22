package protka.io;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import protka.Protein;

public class SwissprotWriterTest {

  private OutputStream os;

  @Before
  public void setUp() {
    os = new ByteArrayOutputStream();
  }

  @Test
  public void testWriteProtein() {
    SwissprotWriter swissprotWriter = new SwissprotWriter(os);
    List<String> data = new ArrayList<String>();

    data.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data.add("AC   Q6GZX4;");
    data.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    data.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    data.add("     SFRKIYTDLG WKFTPL");

    Protein protein = new Protein();
    protein.setLines(data);

    String expected = "ID   001R_FRG3G              Reviewed;         256 AA.\n"
        + "AC   Q6GZX4;\n"
        + "SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;\n"
        + "     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS\n"
        + "     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD\n"
        + "     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL\n"
        + "     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD\n" + "     SFRKIYTDLG WKFTPL\n" + "//\n";

    try {
      swissprotWriter.writeProtein(protein);
      swissprotWriter.closeOS();
      assertTrue(os.toString().equals(expected));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWriteProteinList() {
    SwissprotWriter swissprotWriter = new SwissprotWriter(os);
    List<String> data1 = new ArrayList<String>();

    data1.add("ID   001R_FRG3G              Reviewed;         256 AA.");
    data1.add("AC   Q6GZX4;");
    data1.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data1.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data1.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data1.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    data1.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    data1.add("     SFRKIYTDLG WKFTPL");

    Protein protein1 = new Protein();
    protein1.setLines(data1);

    List<String> data2 = new ArrayList<String>();

    data2.add("ID   001R_FRG3G              Reviewed;         256 AB.");
    data2.add("AC   Q6GZX3;");
    data2.add("SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;");
    data2.add("     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS");
    data2.add("     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD");
    data2.add("     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL");
    data2.add("     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD");
    data2.add("     SFRKIYTDLG WKFTPLBM");

    Protein protein2 = new Protein();
    protein2.setLines(data2);

    List<Protein> proteinList = new ArrayList<Protein>();
    proteinList.add(protein1);
    proteinList.add(protein2);

    String expected = "ID   001R_FRG3G              Reviewed;         256 AA.\n"
        + "AC   Q6GZX4;\n"
        + "SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;\n"
        + "     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS\n"
        + "     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD\n"
        + "     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL\n"
        + "     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD\n"
        + "     SFRKIYTDLG WKFTPL\n"
        + "//\n"
        + "ID   001R_FRG3G              Reviewed;         256 AB.\n"
        + "AC   Q6GZX3;\n"
        + "SQ   SEQUENCE   256 AA;  29735 MW;  B4840739BF7D4121 CRC64;\n"
        + "     MAFSAEDVLK EYDRRRRMEA LLLSLYYPND RKLLDYKEWS PPRVQVECPK "
        + "APVEWNNPPS\n"
        + "     EKGLIVGHFS GIKYKGEKAQ ASEVDVNKMC CWVSKFKDAM RRYQGIQTCK "
        + "IPGKVLSDLD\n"
        + "     AKIKAYNLTV EGVEGFVRYS RVTKQHVAAF LKELRHSKQY ENVNLIHYIL "
        + "TDKRVDIQHL\n"
        + "     EKDLVKDFKA LVESAHRMRQ GHMINVKYIL YQLLKKHGHG PDGPDILTVK "
        + "TGSKGVLYDD\n" + "     SFRKIYTDLG WKFTPLBM\n" + "//\n";

    try {
      swissprotWriter.writeProteinList(proteinList);
      swissprotWriter.closeOS();
      assertTrue(os.toString().equals(expected));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
