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



public class XmlDocument extends XmlEvent {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=entities KEEP=NO
  public XmlEntityList entities = null;
// ***** VDMTOOLS END Name=entities


// ***** VDMTOOLS START Name=vdm_init_XmlDocument KEEP=NO
  private void vdm_init_XmlDocument () throws CGException {
    try {
      entities = (XmlEntityList) new XmlEntityList();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_XmlDocument


// ***** VDMTOOLS START Name=XmlDocument KEEP=NO
  public XmlDocument () throws CGException {
    vdm_init_XmlDocument();
  }
// ***** VDMTOOLS END Name=XmlDocument


// ***** VDMTOOLS START Name=addEntity#1|XmlEntity KEEP=NO
  public void addEntity (final XmlEntity pent) throws CGException {
    entities.addTail((XmlEntity) pent);
  }
// ***** VDMTOOLS END Name=addEntity#1|XmlEntity


// ***** VDMTOOLS START Name=accept#1|XmlVisitor KEEP=NO
  public void accept (final XmlVisitor pxv) throws CGException {
    pxv.VisitXmlDocument((XmlDocument) this);
  }
// ***** VDMTOOLS END Name=accept#1|XmlVisitor


// ***** VDMTOOLS START Name=StartDocument KEEP=NO
  protected Boolean StartDocument () throws CGException {
    return new Boolean(true);
  }
// ***** VDMTOOLS END Name=StartDocument


// ***** VDMTOOLS START Name=StopDocument KEEP=NO
  protected Boolean StopDocument () throws CGException {
    return new Boolean(true);
  }
// ***** VDMTOOLS END Name=StopDocument


// ***** VDMTOOLS START Name=StartEntity#1|String KEEP=NO
  protected Boolean StartEntity (final String pname) throws CGException {

    XmlEntity nxe = new XmlEntity(pname);
    if (new Boolean(stackDepth().intValue() == new Long(0).intValue()).booleanValue()) {

      addEntity((XmlEntity) nxe);
      pushEntity((XmlEntity) nxe);
    }
    else {

      XmlEntity obj_7 = null;
      obj_7 = (XmlEntity) peekEntity();
      obj_7.addEntity((XmlEntity) nxe);
      pushEntity((XmlEntity) nxe);
    }
    return new Boolean(true);
  }
// ***** VDMTOOLS END Name=StartEntity#1|String


// ***** VDMTOOLS START Name=StopEntity#1|String KEEP=NO
  protected Boolean StopEntity (final String pname) throws CGException {
    if (new Boolean((stackDepth().intValue()) > (new Long(0).intValue())).booleanValue()) {

      Boolean cond_6 = null;
      String var1_7 = null;
      var1_7 = peekEntity().name;
      cond_6 = new Boolean(UTIL.equals(var1_7, pname));
      if (cond_6.booleanValue()) {

        popEntity();
        return new Boolean(true);
      }
      else 
        return new Boolean(false);
    }
    else 
      return new Boolean(false);
  }
// ***** VDMTOOLS END Name=StopEntity#1|String


// ***** VDMTOOLS START Name=StartAttribute#2|String|String KEEP=NO
  protected Boolean StartAttribute (final String pattr, final String pval) throws CGException {
    if (new Boolean((stackDepth().intValue()) > (new Long(0).intValue())).booleanValue()) {

      XmlEntity obj_8 = null;
      obj_8 = (XmlEntity) peekEntity();
      obj_8.addAttribute((XmlAttribute) new XmlAttribute(pattr, pval));
      return new Boolean(true);
    }
    else 
      return new Boolean(false);
  }
// ***** VDMTOOLS END Name=StartAttribute#2|String|String


// ***** VDMTOOLS START Name=StartData#1|String KEEP=NO
  protected Boolean StartData (final String pdata) throws CGException {
    if (new Boolean((stackDepth().intValue()) > (new Long(0).intValue())).booleanValue()) {

      XmlEntity obj_7 = null;
      obj_7 = (XmlEntity) peekEntity();
      obj_7.addData((XmlData) new XmlData(pdata));
      return new Boolean(true);
    }
    else 
      return new Boolean(false);
  }
// ***** VDMTOOLS END Name=StartData#1|String

}
;
