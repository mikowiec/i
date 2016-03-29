//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.03.27 at 04:30:25 PM EEST 
//


package org.igov.util.swind.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DGKodDocROVPD3_2.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DGKodDocROVPD3_2">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ПН"/>
 *     &lt;enumeration value="РК"/>
 *     &lt;enumeration value="ВМД"/>
 *     &lt;enumeration value="ПП"/>
 *     &lt;enumeration value="ЗЦ"/>
 *     &lt;enumeration value="ПНУ"/>
 *     &lt;enumeration value="РКУ"/>
 *     &lt;enumeration value="ВМДУ"/>
 *     &lt;enumeration value="ППУ"/>
 *     &lt;enumeration value="ЗЦУ"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DGKodDocROVPD3_2")
@XmlEnum
public enum DGKodDocROVPD32 {


    /**
     * 
     * 				ПН - податкова накладна
     * 			
     * 
     */
    ПН,

    /**
     * 
     * 				РК - розрахунок коригування до податкової накладної (розрахунок коригування кількісних і вартісних показників (додаток 2 до податкової накладної))
     * 			
     * 
     */
    РК,

    /**
     * 
     * 				ВМД - вантажна митна деклараці
     * 			
     * 
     */
    ВМД,

    /**
     * 
     * 				ПП - податкова накладна за щоденними підсумками операцій
     * 			
     * 
     */
    ПП,

    /**
     * 
     * 				ЗЦ - податкова накладна, виписана на суму перевищення звичайної ціни над фактичною
     * 			
     * 
     */
    ЗЦ,

    /**
     * 
     * 				ПНУ - податкова накладна [уточнення]
     * 			
     * 
     */
    ПНУ,

    /**
     * 
     * 				РКУ - розрахунок коригування до податкової накладної (розрахунок коригування кількісних і вартісних показників (додаток 2 до податкової накладної)) [уточнення]
     * 			
     * 
     */
    РКУ,

    /**
     * 
     * 				ВМДУ - вантажна митна декларація [уточнення]
     * 			
     * 
     */
    ВМДУ,

    /**
     * 
     * 				ППУ - податкова накладна за щоденними підсумками операцій [уточнення]
     * 			
     * 
     */
    ППУ,

    /**
     * 
     * 				ЗЦУ - податкова накладна, виписана на суму перевищення звичайної ціни над фактичною [уточнення]
     * 			
     * 
     */
    ЗЦУ;

    public String value() {
        return name();
    }

    public static DGKodDocROVPD32 fromValue(String v) {
        return valueOf(v);
    }

}