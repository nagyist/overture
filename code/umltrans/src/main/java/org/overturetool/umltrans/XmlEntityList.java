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



public class XmlEntityList {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=entities KEEP=NO
  public Vector entities = null;
// ***** VDMTOOLS END Name=entities

// ***** VDMTOOLS START Name=index KEEP=NO
  private Long index = null;
// ***** VDMTOOLS END Name=index


// ***** VDMTOOLS START Name=vdm_init_XmlEntityList KEEP=NO
  private void vdm_init_XmlEntityList () throws CGException {
    try {

      entities = new Vector();
      index = new Long(0);
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_XmlEntityList


// ***** VDMTOOLS START Name=XmlEntityList KEEP=NO
  public XmlEntityList () throws CGException {

    vdm_init_XmlEntityList();
    entities = (Vector) UTIL.ConvertToList(UTIL.clone(new Vector()));
  }
// ***** VDMTOOLS END Name=XmlEntityList


// ***** VDMTOOLS START Name=addHead#1|XmlEntity KEEP=NO
  public void addHead (final XmlEntity pxe) throws CGException {

    Vector rhs_2 = null;
    Vector var1_3 = null;
    var1_3 = new Vector();
    var1_3.add(pxe);
    rhs_2 = (Vector) var1_3.clone();
    rhs_2.addAll(entities);
    entities = (Vector) UTIL.ConvertToList(UTIL.clone(rhs_2));
  }
// ***** VDMTOOLS END Name=addHead#1|XmlEntity


// ***** VDMTOOLS START Name=addTail#1|XmlEntity KEEP=NO
  public void addTail (final XmlEntity pxe) throws CGException {
    entities.add(pxe);
  }
// ***** VDMTOOLS END Name=addTail#1|XmlEntity


// ***** VDMTOOLS START Name=Length KEEP=NO
  public Long Length () throws CGException {
    return new Long(entities.size());
  }
// ***** VDMTOOLS END Name=Length


// ***** VDMTOOLS START Name=First KEEP=NO
  public Boolean First () throws CGException {
    if (new Boolean((new Long(entities.size()).intValue()) > (new Long(0).intValue())).booleanValue()) {

      index = UTIL.NumberToLong(UTIL.clone(new Long(1)));
      return new Boolean(true);
    }
    else {

      index = UTIL.NumberToLong(UTIL.clone(new Long(0)));
      return new Boolean(false);
    }
  }
// ***** VDMTOOLS END Name=First


// ***** VDMTOOLS START Name=Next KEEP=NO
  public Boolean Next () throws CGException {

    Boolean cond_1 = null;
    {
      if ((cond_1 = new Boolean((new Long(entities.size()).intValue()) > (new Long(0).intValue()))).booleanValue()) 
        cond_1 = new Boolean((index.intValue()) < (new Long(entities.size()).intValue()));
    }
    if (cond_1.booleanValue()) {

      index = UTIL.NumberToLong(UTIL.clone(new Long(index.intValue() + new Long(1).intValue())));
      return new Boolean(true);
    }
    else {

      index = UTIL.NumberToLong(UTIL.clone(new Long(0)));
      return new Boolean(false);
    }
  }
// ***** VDMTOOLS END Name=Next


// ***** VDMTOOLS START Name=Last KEEP=NO
  public Boolean Last () throws CGException {
    if (new Boolean((new Long(entities.size()).intValue()) > (new Long(0).intValue())).booleanValue()) {

      index = UTIL.NumberToLong(UTIL.clone(new Long(entities.size())));
      return new Boolean(true);
    }
    else {

      index = UTIL.NumberToLong(UTIL.clone(new Long(0)));
      return new Boolean(false);
    }
  }
// ***** VDMTOOLS END Name=Last


// ***** VDMTOOLS START Name=Previous KEEP=NO
  public Boolean Previous () throws CGException {

    Boolean cond_1 = null;
    {
      if ((cond_1 = new Boolean((new Long(entities.size()).intValue()) > (new Long(0).intValue()))).booleanValue()) 
        cond_1 = new Boolean((index.intValue()) > (new Long(1).intValue()));
    }
    if (cond_1.booleanValue()) {

      index = UTIL.NumberToLong(UTIL.clone(new Long(index.intValue() - new Long(1).intValue())));
      return new Boolean(true);
    }
    else {

      index = UTIL.NumberToLong(UTIL.clone(new Long(0)));
      return new Boolean(false);
    }
  }
// ***** VDMTOOLS END Name=Previous


// ***** VDMTOOLS START Name=getEntity KEEP=NO
  public XmlEntity getEntity () throws CGException {

    XmlEntity rexpr_1 = null;
    if ((1 <= index.intValue()) && (index.intValue() <= entities.size())) 
      rexpr_1 = (XmlEntity) entities.get(index.intValue() - 1);
    else 
      UTIL.RunTime("Run-Time Error:Illegal index");
    return (XmlEntity) rexpr_1;
  }
// ***** VDMTOOLS END Name=getEntity

}
;
