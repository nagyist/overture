//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2009-02-27 by the VDM++ to JAVA Code Generator
// (v8.2b - Wed 18-Feb-2009 16:15:35)
//
// Supported compilers: jdk 1.4/1.5/1.6
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.umltrans;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=NO

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
// ***** VDMTOOLS END Name=imports



public class UmlMessageKindQuotes {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=IQUNKNOWN KEEP=NO
  public static Long IQUNKNOWN = new Long(0);
// ***** VDMTOOLS END Name=IQUNKNOWN

// ***** VDMTOOLS START Name=IQCOMPLETE KEEP=NO
  public static Long IQCOMPLETE = new Long(1);
// ***** VDMTOOLS END Name=IQCOMPLETE

// ***** VDMTOOLS START Name=qmap KEEP=NO
  private static HashMap qmap = new HashMap();
// ***** VDMTOOLS END Name=qmap


// ***** VDMTOOLS START Name=static KEEP=NO
  static {
    try {

      UmlMessageKindQuotes.qmap = new HashMap();
      UmlMessageKindQuotes.qmap.put(IQUNKNOWN, new String("<UNKNOWN>"));
      UmlMessageKindQuotes.qmap.put(IQCOMPLETE, new String("<COMPLETE>"));
    }
    catch (Throwable e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=static


// ***** VDMTOOLS START Name=vdm_init_UmlMessageKindQuotes KEEP=NO
  private void vdm_init_UmlMessageKindQuotes () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_UmlMessageKindQuotes


// ***** VDMTOOLS START Name=UmlMessageKindQuotes KEEP=NO
  public UmlMessageKindQuotes () throws CGException {
    vdm_init_UmlMessageKindQuotes();
  }
// ***** VDMTOOLS END Name=UmlMessageKindQuotes


// ***** VDMTOOLS START Name=getQuoteName#1|Long KEEP=NO
  static public String getQuoteName (final Long pid) throws CGException {
    return UTIL.ConvertToString(qmap.get(pid));
  }
// ***** VDMTOOLS END Name=getQuoteName#1|Long


// ***** VDMTOOLS START Name=validQuote#1|Long KEEP=NO
  static public Boolean validQuote (final Long pid) throws CGException {

    Boolean rexpr_2 = null;
    rexpr_2 = new Boolean(qmap.containsKey(pid));
    return rexpr_2;
  }
// ***** VDMTOOLS END Name=validQuote#1|Long

}
;
