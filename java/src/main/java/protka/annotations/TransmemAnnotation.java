package protka.annotations;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import protka.annotations.Annotation.Type;

public class TransmemAnnotation {
  private String header;
  private String annotString;
  private List<Annotation> annotations;
  private boolean valid = false;
  private int length;
  private final static Logger LOGGER = 
      Logger.getLogger(TransmemAnnotation.class.getName());

  public TransmemAnnotation(String string) {
    annotString = string.trim().toLowerCase();
    boolean ok = parseAnnotations();
    if (!ok && !annotString.equals("")) {
      LOGGER.warning("Not valid annot string: " + annotString);
    }
    if (annotString.equals("")) {
      LOGGER.warning("Empty annot string!");
    }
    if (annotations.isEmpty()) {
      length = 0;
    } else {
      length = annotations.get(annotations.size() - 1).to;
    }
  }

  public List<Annotation> getAnnotations() {
    return annotations;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    if (header.equals(">(no header)")) {
      this.header = null;
    } else {
      this.header = header;
    }
  }

  public int getSeqLen() {
    return length;
  }
  
  public List<Annotation> getAnnotations(Type t) {
    List<Annotation> ret = new ArrayList<Annotation>();
    for (Annotation a : annotations) {
      if (a.type == t) {
        ret.add(new Annotation(t, a.from, a.to));
      }
    }
    return ret;
  }

  public int getNumOfAnnotations() {
    return annotations.size();
  }

  public int getNumOfAnnotations(Type t) {
    int cnt = 0;
    for (Annotation a : annotations) {
      if (a.type == t) {
        cnt++;
      }
    }
    return cnt;
  }

  public Annotation getAnnotation(int i) {
    if (i < annotations.size()) {
      return annotations.get(i);
    }
    LOGGER.warning("No annotation with index: " + i);
    return null;
  }

  private boolean parseAnnotations() {
    annotations = new ArrayList<Annotation>();
    List<String> annotStrs = getAnnotParts();
    boolean allOk = true;
    for (int i = 0; i < annotStrs.size(); i++) {
      String as = annotStrs.get(i);
      Annotation a = getAnnotation(as);
      if (a != null) {
        annotations.add(a);
      } else {
        if (allOk) {
          allOk = false;
        }
      }
    }
    if (allOk == false) {
      LOGGER.warning("There  was a null annot");
      return false;
    }
    mergeAllConsecutiveSameTypeAnnots();
    return checkOrderAndIntersections();
  }

  private List<String> getAnnotParts() {
    ArrayList<String> ret = new ArrayList<String>();
    if (annotString.contains(";")) {
      String[] split = annotString.split(";");
      for (String s : split) {
        ret.add(s.trim());
      }
    } else {
      ret.add(annotString.trim());
    }
    return ret;
  }

  private boolean checkOrderAndIntersections() {
    int lastEnd = 0;
    Annotation.Type lastType = null;
    for (Annotation annot : annotations) {
      if (annot.from <= lastEnd) { // intersect
        LOGGER.warning("Intersection in annots");
        valid = false;
        return false;
      }
      if (annot.from == lastEnd + 1 && lastType != null
          && lastType == annot.type) { 
        // should have been merged (but swissprot HAS not merged consecutive 
        // TM parts in some proteins)
        LOGGER.warning("Annots should have been merged");
        valid = false;
        return false;
      }
      // i and o are not allowed without a TM in-between them
      if (lastType != null && lastType != Annotation.Type.TRANSMEM
          && annot.type != Annotation.Type.TRANSMEM) {
        LOGGER.warning("Change in annot type without TM part");
        valid = false;
        return false;
      }
      lastType = annot.type;
      lastEnd = annot.to;
    }
    valid = true;
    return true;
  }

  private Annotation getAnnotation(String s) {
    try {
      String[] fields = s.split(":");
      if (fields.length != 2) {
        return null;
      }
      Annotation.Type type;
      String typeStr = fields[0];
      if (typeStr.isEmpty()) {
        return null;
      } else {
        type = Annotation.charToType(typeStr.charAt(0));
      }      
      String numStr = fields[1];
      if (numStr.contains("-")) {
        String[] nums = numStr.split("-");
        if (nums.length != 2) {
          return null;
        }
        boolean fuzzyStart = false;
        boolean fuzzyEnd = false;
        String numStartStr = nums[0];
        String numEndStr = nums[1];
        if (numStartStr.charAt(0) == '~') {
          fuzzyStart = true;
          numStartStr = numStartStr.substring(1);
        }
        if (numEndStr.charAt(0) == '~') {
          fuzzyEnd = true;
          numEndStr = numEndStr.substring(1);
        }
        int from = Integer.parseInt(numStartStr.trim());
        int to = Integer.parseInt(numEndStr.trim());
        if (from > 0 && to >= from) {
          return new Annotation(type, from, to, fuzzyStart, fuzzyEnd);
        }
        return null;
      } else {
        return null;
      }
    } catch (Exception e) {
      // e.printStackTrace();
      return null;
    }
  }

  public boolean isEmpty() {
    return annotString.isEmpty();
  }

  public boolean isValid() {
    return valid;
  }

  public boolean isComplete(int lenOfSeq) {
    int lastEnd = 0;
    for (Annotation a : annotations) {
      if (a.from != lastEnd + 1) {
        return false;
      }
      if (a.type == Annotation.Type.UNKNOWN) {
        return false;
      }
      lastEnd = a.to;
    }
    if (lastEnd == lenOfSeq) {
      return true;
    }
    return false;
  }
  
  private String createLongAnnotString() {
    StringBuilder sb = new StringBuilder();
    for (Annotation ann : annotations) {
      for (int i = 0; i < ann.to - ann.from + 1; i++) {
        if (ann.type == Annotation.Type.INNER)
          sb.append("i");
        if (ann.type == Annotation.Type.OUTER)
          sb.append("o");
        if (ann.type == Annotation.Type.TRANSMEM)
          sb.append("t");
      }
    }
    return sb.toString();
  }

  protected String createCompleteAnnotString(int len) {
    this.length = len;
    StringBuilder sb = new StringBuilder();
    int lastSeenIdx = 0;
    for (Annotation ann : annotations) {
      int diff = ann.from - lastSeenIdx;
      while (diff > 1) {
        sb.append('?');
        diff--;
      }
      for (int i = 0; i < ann.to - ann.from + 1; i++) {
        if (ann.type == Annotation.Type.INNER)
          sb.append('i');
        if (ann.type == Annotation.Type.OUTER)
          sb.append('o');
        if (ann.type == Annotation.Type.TRANSMEM)
          sb.append('t');
      }
      lastSeenIdx = ann.to;
    }
    while (sb.length() < this.length) {
      sb.append('?');
    }
    return sb.toString();
  }

  public double matchAnnotation(TransmemAnnotation annot2, int len,
      boolean strict) {
    String s1;
    String s2;

    if (annot2.isComplete(len) && this.isComplete(len)) {
      s1 = this.createLongAnnotString();
      s2 = annot2.createLongAnnotString();
    } else {
      s1 = this.createCompleteAnnotString(len);
      s2 = annot2.createCompleteAnnotString(len);
    }
    if (s1.length() != s2.length()) {
      LOGGER.warning("ERROR: length mismatch in match computation: "
          + s1.length() + " vs " + s2.length());
      return Double.NaN;
    }

    int matchNum = 0;
    int missingNum = 0;
    int misMatchNum = 0;
    for (int i = 0; i < s1.length(); i++) {
      if (s1.charAt(i) == s2.charAt(i) && s1.charAt(i) != '?') {
        matchNum++;
      } else if (s1.charAt(i) == '?' || s2.charAt(i) == '?') {
        missingNum++;
      } else {
        misMatchNum++;
      }
    }

    if (strict) {
      return (1.0 * matchNum / (misMatchNum + matchNum + missingNum));
    }
    return (1.0 * matchNum / (misMatchNum + matchNum));
  }

  public boolean isOriented() {
    for (Annotation a : annotations) {
      if (a.type == Type.INNER || a.type == Type.OUTER) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return annotString;
  }

  /**
   * Two TM parts are considered "matched" if their intersection is at least 10
   * AA long. Returns the two times the number of matched TM parts divided by
   * the total number of separate TM annotations in the two TransmemAnnotations,
   * so returns a value between 0.0 and 1.0.
   */
  public double matchTmAnnotation(TransmemAnnotation predAnnot) {
    int num = 0;
    int matchNum = 0;
    List<Annotation> tmAnnots = getAnnotations(Type.TRANSMEM);
    for (Annotation ann : tmAnnots) {
      num++;
      if (containsMatchingTmPart(predAnnot.annotations, ann)) {
        matchNum++;
      }
    }
    for (Annotation annOther : predAnnot.getAnnotations(Type.TRANSMEM)) {
      num++;
      if (containsMatchingTmPart(this.annotations, annOther)) {
        matchNum++;
      }
    }
    return (1.0 * matchNum / num);
  }

  private boolean containsMatchingTmPart(List<Annotation> list, 
      Annotation ann) {
    if (ann.type != Type.TRANSMEM) {
      LOGGER.warning("can not TM-match non TM annotation!");
      return false;
    }
    for (Annotation a : list) {
      if (a.type == ann.type) {
        int intersectFrom = Math.max(a.from, ann.from);
        int intersectTo = Math.min(a.to, ann.to);
        if (intersectTo - intersectFrom >= 10) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean containsMatchingPart(List<Annotation> list, 
      Annotation ann, int diffAllowed) {
    int maxAllowedDiff = diffAllowed;
    for (Annotation a : list) {
      if (a.type == ann.type) {
        int diffFrom = Math.abs(a.from - ann.from);
        int diffTo = Math.abs(a.to - ann.to);
        if (diffFrom <= maxAllowedDiff && diffTo <= maxAllowedDiff) {
          return true;
        }
      }
    }
    return false;
  }
  private boolean fixBeginning() {
    Annotation firstAnn = annotations.get(0);
    if (firstAnn.from > 1
        && (firstAnn.type == Type.INNER || firstAnn.type == Type.OUTER)) {
      Annotation newFirstAnn = new Annotation(firstAnn.type, 1, firstAnn.to);
      annotations.set(0, newFirstAnn);
      // update annotString
      annotString = createLongAnnotString();
      return true;
    }
    return false;
  }

  private boolean fixEnding(int len) {
    Annotation lastAnn = annotations.get(getNumOfAnnotations() - 1);
    this.length = len;
    if (lastAnn.to < len
        && (lastAnn.type == Type.INNER || lastAnn.type == Type.OUTER)) {
      Annotation newLastAnn = new Annotation(lastAnn.type, lastAnn.from, len);
      annotations.set(getNumOfAnnotations() - 1, newLastAnn);
      // update annotString
      annotString = createLongAnnotString();
      return true;
    }
    return false;
  }

  private boolean isIo(Annotation annot) {
    if (annot == null) {
      return false;
    }
    return (annot.type == Type.INNER || annot.type == Type.OUTER);
  }

  public int getMatchingTmNum(TransmemAnnotation other, int diffAllowed) {
    List<Annotation> myAnnots = getAnnotations(Type.TRANSMEM);
    int matchNum = 0;
    for (Annotation ann : myAnnots) {
      if (containsMatchingPart(other.annotations, ann, diffAllowed)) {
        matchNum++;
      }
    }
    return matchNum;
  }

  private List<Annotation> createAlternatingIoAnnots(int tmIndex) {
    List<Annotation> newAnnotations = new ArrayList<Annotation>();
    Type prevState = annotations.get(tmIndex - 1).type;
    Annotation nextTmAnnot;
    int actualTmIdx = tmIndex;
    while (actualTmIdx < annotations.size() - 1 && 
        !isIo(nextTmAnnot = annotations.get(actualTmIdx + 1))) {
      Annotation tmAnnot = annotations.get(actualTmIdx);
      Annotation newAnnot = new Annotation(
          Annotation.revIo(prevState), tmAnnot.to + 1, nextTmAnnot.from - 1);
      newAnnotations.add(newAnnot);
      prevState = Annotation.revIo(prevState);
      ++actualTmIdx;
    }
    // check I/O consistence
    if (actualTmIdx < annotations.size() - 1) {
      if (prevState == annotations.get(actualTmIdx +1).type) {
        // conflict
        return null;
      }
    }
    return newAnnotations;
  }

  public static List<Annotation> mergeAnnots(List<Annotation> listA, 
      List<Annotation> listB) {
    List<Annotation> ret = new ArrayList<Annotation>();
    int sizeA = listA.size();
    int sizeB = listB.size();
    int idxA = 0;
    int idxB = 0;
    while (idxA < sizeA || idxB < sizeB) {
      if (idxA == sizeA) {
        ret.addAll(listB.subList(idxB, sizeB));
        return ret;
      }
      if (idxB == sizeB) {
        ret.addAll(listA.subList(idxA, sizeA));
        return ret;
      }
      if (listA.get(idxA).from < listB.get(idxB).from) {
        ret.add(listA.get(idxA));
        ++idxA;
      } else {
        ret.add(listB.get(idxB));
        ++idxB;
      }
    }
    return ret;
  }
  
  private Annotation getLastAnnotation(Type t) {
    List<Annotation> annots = getAnnotations(t);
    if (!annots.isEmpty()) {
      return annots.get(annots.size() - 1); 
    }
    return null;
  }
  
  // @returns true if any change happened
  // inserts UNKNOWN between TMs if not consistent
  private boolean fixMissingIo(int len) {
    if (annotations == null || annotations.isEmpty() || !isIo(annotations.get(0))) {
      return false;
    }
    // should not violate alternating ...i/t/o/t/i/t/o... pattern;
    List<Annotation> newAllAnnotations = new ArrayList<Annotation>();
    int tmIndex = 0;
    while ((tmIndex = getNextMissingAnnotation(tmIndex)) >= 0) {
      List<Annotation> newAnnotations = createAlternatingIoAnnots(tmIndex);
      if (null == newAnnotations) {
        return false;
      }
      int newAnnotsSize = newAnnotations.size();
      if (newAnnotsSize != 0) {
        newAllAnnotations.addAll(newAnnotations);
      }
      tmIndex += newAnnotsSize;
    }
    Annotation lastOrigAnnot = annotations.get(annotations.size() - 1);  
    if (lastOrigAnnot.to < len && lastOrigAnnot.type == Type.TRANSMEM) {
      // add the last one
      Type newLastType;
      if (!newAllAnnotations.isEmpty()) {
        Annotation lastPredAnnot = newAllAnnotations.get(newAllAnnotations.size() - 1);
        newLastType = Annotation.revIo(lastPredAnnot.type);
      } else {
        Annotation lastIn = getLastAnnotation(Type.INNER);
        Annotation lastOut = getLastAnnotation(Type.OUTER);
        if (lastIn == null && lastOut == null) {
          LOGGER.warning("Should not get here");
          newLastType = Type.INNER;
        } else if (lastIn == null) {
          newLastType = Type.INNER;
        } else if (lastOut == null){
          newLastType = Type.OUTER;
        } else if (lastIn.from < lastOut.from) {
          newLastType = Type.INNER;
        } else {
          newLastType = Type.OUTER;
        }
      }
      Annotation newLast = new Annotation(newLastType, lastOrigAnnot.to + 1, len);
      newAllAnnotations.add(newLast);
    }
    List<Annotation> resultAnnotations = 
        mergeAnnots(newAllAnnotations, annotations);
    
    annotations = resultAnnotations;
    String newAnnot = createLongAnnotString();
    annotString = newAnnot;
    return true;
  }

  /**
   * If there is at least 2 i/o annotations parts at the ends, and they are
   * consistent with the model that i and o parts are alternating then
   * explicitely generate i and o parts between t parts (if missing). Also
   * extends first and last i/o parts until the beginning or end.
   * 
   * @returns true if any change happened
   * */
  public boolean extrapolateIoAnnotations(int len) {
    if (annotations.isEmpty()) {
      return false;
    }
    boolean changed = false;
    boolean beginChanged = fixBeginning();
    if (beginChanged) {
      changed = true;
    }
    boolean endChanged = fixEnding(len);
    if (endChanged) {
      changed = true;
    }
    boolean middleChanged = fixMissingIo(len);
    if (middleChanged) {
      changed = true;
    }
    annotString = createAnnotString(annotations);
    return changed;
  }

  public double matchIoAnnotationPerChar(TransmemAnnotation annot2) {
    if (!annot2.isValid()) {
      LOGGER.warning("Match IO with not valid annotation: " 
          + annot2.annotString
      		+ "\n matchIO returns NaN !");
      return Double.NaN;
    }
    int len = Math.max(this.getSeqLen(), annot2.getSeqLen());
    extrapolateIoAnnotations(len);
    annot2.extrapolateIoAnnotations(len);
    String s1 = createCompleteAnnotString(len);
    String s2 = annot2.createCompleteAnnotString(len);
    int matchNum = 0;
    int totalIoNum = 0;
    for (int i = 0; i< len; i++) {
      Type t1 = Annotation.charToType(s1.charAt(i));
      Type t2 = Annotation.charToType(s2.charAt(i));
      if (t1 == Type.INNER && t2 == Type.INNER || 
          t1 == Type.OUTER && t2 == Type.OUTER) {
        matchNum++;
        totalIoNum++;
      } else if ((t1 == Type.INNER || t1 == Type.OUTER) && 
          (t2 == Type.INNER || t2 == Type.OUTER)) {
        totalIoNum++;
      }
    }
    return (1.0 * matchNum / totalIoNum);
  }
  
  /**
   * Two I/O parts are considered "matched" if their start and end differs at 
   * most by @param diffAllowed AAs. Returns the two times the number of 
   * matched parts divided by the total number of separate I/O annotations 
   * in the two TransmemAnnotations,
   * so returns a value between 0.0 and 1.0.
   */
  public double matchIoAnnotationPerAnnot(TransmemAnnotation predAnnot,
      int diffAllowed) {
    int matchNum = 0;
    List<Annotation> inAnnots = getAnnotations(Type.INNER);
    List<Annotation> outAnnots = getAnnotations(Type.OUTER);

    if (inAnnots.isEmpty() && outAnnots.isEmpty()) {
      return Double.NaN;
    }
    int num = inAnnots.size() + outAnnots.size() + predAnnot.getNumOfAnnotations(Type.INNER) +
        predAnnot.getNumOfAnnotations(Type.OUTER);

    matchNum += getNumOfMatchingParts(predAnnot, Type.INNER, diffAllowed);
    matchNum += getNumOfMatchingParts(predAnnot, Type.OUTER, diffAllowed);

    matchNum += predAnnot.getNumOfMatchingParts(this, Type.INNER, diffAllowed);
    matchNum += predAnnot.getNumOfMatchingParts(this, Type.OUTER, diffAllowed);

    return (1.0 * matchNum / num);
  }

  private String createAnnotString(List<Annotation> annotList) {
    StringBuilder sb = new StringBuilder();
    for (Annotation a : annotList) {
      char c = Annotation.typeToChar(a.type);
      sb.append(c).append(':');
      sb.append(a.from).append('-').append(a.to).append(';');
    }
    return sb.toString();
  }

  public int getNextMissingAnnotation(int fromIndex) {
    int from = Math.max(1, fromIndex);
    for (int i = from; i < annotations.size() - 1; ++i) {
      if (annotations.get(i).type == Type.TRANSMEM
          && annotations.get(i + 1).type == Type.TRANSMEM
          && isIo(annotations.get(i - 1))) {
        return i;
      }
    }
    return -1;
  }

  public int getNumOfIoParts() {
    int numInner = getAnnotations(Type.INNER).size();
    int numOuter = getAnnotations(Type.OUTER).size();
    return numInner + numOuter;
  }
  
  public int getNumOfMatchingParts(TransmemAnnotation predAnnot, Type type, int diffAllowed) {
    List<Annotation> myAnnots = getAnnotations(type);
    int matchNum = 0;
    for (Annotation ann : myAnnots) {
      if (containsMatchingPart(predAnnot.annotations, ann, diffAllowed)) {
        matchNum++;
      }
    }
    for (Annotation annOther : predAnnot.getAnnotations(Type.INNER)) {
      if (containsMatchingPart(this.annotations, annOther, diffAllowed)) {
        matchNum++;
      }
    }
    return matchNum;
  }
  
  public int matchingIoAnnotNum(TransmemAnnotation predAnnot, int diffAllowed) {
    int maxLen = Math.max(this.getSeqLen(), predAnnot.getSeqLen());
    fixMissingIo(maxLen);
    int all = getNumOfIoParts();
    if (all == 0) {
      return 0;
    }
    int inn = getNumOfMatchingParts(predAnnot, Type.INNER, diffAllowed);
    int out = getNumOfMatchingParts(predAnnot, Type.OUTER, diffAllowed);
    return (inn + out) / 2; // because counting twice; from this annots point of view ant the other
  }

  /**
   * If an I/O part follows an O/I part, and there is 15-30 length part missing
   * inbetween then assume that a TM part is there but was not annotated.
   * So add the new Annotation to this. 
   * */
  public void fillMissingTM() {
    Annotation lastAnnot = null;
    List<Annotation> newTmAnnots = new ArrayList<Annotation>();
    boolean fixed = false;
    for (Annotation a : annotations) {
      if (lastAnnot != null && isIo(a) && isIo(lastAnnot)
          && Annotation.revIo(a.type) == lastAnnot.type) {
        int diffLen = a.from - lastAnnot.to;
        if (diffLen >= 15 && diffLen < 30) {
          Annotation newAnn = new Annotation(Type.TRANSMEM, lastAnnot.to + 1,
              a.from - 1);
          newTmAnnots.add(newAnn);
          fixed = true;
        } else {
          LOGGER.warning("inconsistency in annotation, can not be fixed!");
        }
      }
      lastAnnot = a;
    }
    if (fixed) {
      List<Annotation> newAnnots = mergeAnnots(annotations, newTmAnnots);
      annotations = newAnnots;
      annotString = createAnnotString(annotations);
    }
  }
  
  /**
   * All consecutive same-type Annotations will be merged to one.
   * */
  public void mergeAllConsecutiveSameTypeAnnots() {
    while (mergeAnyTwoConsecutiveSameTypeAnnots()) {
      LOGGER.log(Level.INFO, "Some annotations were merged.");
    }
    annotString = createAnnotString(this.annotations);
  }
  
  /**
   * Returns true if at least one merge was done, so this TransmemAnnotation
   * was changed. Repeat calling this function until false is returned
   * to merge all consecutive same-type Annotations and get a "nice" one.
   * */
  private boolean mergeAnyTwoConsecutiveSameTypeAnnots() {
    List<Annotation> annotsToDel = new ArrayList<Annotation>();
    List<Annotation> annotsToAdd = new ArrayList<Annotation>();
    Annotation lastAnnot = null;
    int lastEnd = 0;
    boolean changed = false;
    boolean lastDeleted = false;
    for (Annotation annot : annotations) {
      if (annot.from == lastEnd + 1 && lastAnnot != null
          && lastAnnot.type == annot.type && lastDeleted == false) {
        changed = true;
        lastDeleted = true;
        annotsToDel.add(lastAnnot);
        annotsToDel.add(annot);
        Annotation newAnn = 
            new Annotation(annot.type, lastAnnot.from, annot.to);
        annotsToAdd.add(newAnn);
      }
      else {
        lastDeleted = false;
      }
      lastEnd = annot.to;
      lastAnnot = annot;
    }
    
    this.annotations.removeAll(annotsToDel);
    List<Annotation> newAnnots = mergeAnnots(annotations, annotsToAdd);
    this.annotations = newAnnots;
    return changed;
  }

  private Annotation getFirstIoAnn() {
    for (Annotation a:  getAnnotations()) {
      if (isIo(a)){
        return a;
      }
    }
    return null;
  }
  
  public boolean hasReturningSegment() {
    if (annotations.isEmpty() || getFirstIoAnn() == null) {
      return false;
    }
    boolean reversed = false;
    Annotation prevIoAnn = getFirstIoAnn();
    for (Annotation a:  getAnnotations()) {
      if (isIo(a) && a.from > prevIoAnn.to && a.type == prevIoAnn.type) {
        return true;
      } else if (isIo(a) && a.from > prevIoAnn.to) {
        prevIoAnn = a;
      }
    }
    return reversed;
  }
}
