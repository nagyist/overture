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



public class UmlOwnedOperations extends IUmlOwnedOperations {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivOperationList KEEP=NO
  private HashSet ivOperationList = new HashSet();
// ***** VDMTOOLS END Name=ivOperationList


// ***** VDMTOOLS START Name=vdm_init_UmlOwnedOperations KEEP=NO
  private void vdm_init_UmlOwnedOperations () throws CGException {
    try {
      ivOperationList = new HashSet();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_UmlOwnedOperations


// ***** VDMTOOLS START Name=UmlOwnedOperations KEEP=NO
  public UmlOwnedOperations () throws CGException {
    vdm_init_UmlOwnedOperations();
  }
// ***** VDMTOOLS END Name=UmlOwnedOperations


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("OwnedOperations");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitOwnedOperations((IUmlOwnedOperations) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlOwnedOperations#1|HashSet KEEP=NO
  public UmlOwnedOperations (final HashSet p1) throws CGException {

    vdm_init_UmlOwnedOperations();
    setOperationList(p1);
  }
// ***** VDMTOOLS END Name=UmlOwnedOperations#1|HashSet


// ***** VDMTOOLS START Name=UmlOwnedOperations#3|HashSet|Long|Long KEEP=NO
  public UmlOwnedOperations (final HashSet p1, final Long line, final Long column) throws CGException {

    vdm_init_UmlOwnedOperations();
    {

      setOperationList(p1);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=UmlOwnedOperations#3|HashSet|Long|Long


// ***** VDMTOOLS START Name=init#1|HashMap KEEP=NO
  public void init (final HashMap data) throws CGException {

    String tmpVal_3 = null;
    tmpVal_3 = new String("operationList");
    String fname = null;
    fname = tmpVal_3;
    Boolean cond_4 = null;
    cond_4 = new Boolean(data.containsKey(fname));
    if (cond_4.booleanValue()) 
      setOperationList((HashSet) data.get(fname));
  }
// ***** VDMTOOLS END Name=init#1|HashMap


// ***** VDMTOOLS START Name=getOperationList KEEP=NO
  public HashSet getOperationList () throws CGException {
    return ivOperationList;
  }
// ***** VDMTOOLS END Name=getOperationList


// ***** VDMTOOLS START Name=setOperationList#1|HashSet KEEP=NO
  public void setOperationList (final HashSet parg) throws CGException {
    ivOperationList = (HashSet) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setOperationList#1|HashSet


// ***** VDMTOOLS START Name=addOperationList#1|IUmlNode KEEP=NO
  public void addOperationList (final IUmlNode parg) throws CGException {
    ivOperationList.add(parg);
  }
// ***** VDMTOOLS END Name=addOperationList#1|IUmlNode

}
;
