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



public class UmlStringType extends IUmlStringType {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp


// ***** VDMTOOLS START Name=vdm_init_UmlStringType KEEP=NO
  private void vdm_init_UmlStringType () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_UmlStringType


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("StringType");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitStringType((IUmlStringType) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlStringType KEEP=NO
  public UmlStringType () throws CGException {
    vdm_init_UmlStringType();
  }
// ***** VDMTOOLS END Name=UmlStringType


// ***** VDMTOOLS START Name=UmlStringType#2|Long|Long KEEP=NO
  public UmlStringType (final Long line, final Long column) throws CGException {

    vdm_init_UmlStringType();
    setPosition(line, column);
  }
// ***** VDMTOOLS END Name=UmlStringType#2|Long|Long


// ***** VDMTOOLS START Name=init#1|HashMap KEEP=NO
  public void init (final HashMap var_1_1) throws CGException {}
// ***** VDMTOOLS END Name=init#1|HashMap

}
;
