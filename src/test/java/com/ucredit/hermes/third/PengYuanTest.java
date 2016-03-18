/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.ucredit.hermes.model.Conditions;
import com.ucredit.hermes.model.Conditions.Condition;
import com.ucredit.hermes.model.Conditions.Item;

/**
 * @author caoming
 */
public class PengYuanTest {

    public static void main(String[] args) {
        Conditions conditions = new Conditions();
        Condition condition = new Condition();
        List<Item> items = new ArrayList<>(0);
        Item item1 = new Item();
        item1.setName("name");
        item1.setValue("张三");
        Item item2 = new Item();
        item2.setName("documentNo");
        item2.setValue("430521199202176866");
        Item item3 = new Item();
        item3.setName("subreportIDs");
        item3.setValue("10602");
        Item item4 = new Item();
        item4.setName("refID");
        item4.setValue("1");
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        condition.setItem(items);
        condition.setQueryType("25121");
        conditions.setCondition(condition);

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Conditions.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty("jaxb.encoding", "GB2312");
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller
                .setProperty("com.sun.xml.bind.xmlDeclaration", false);

            @SuppressWarnings("resource")
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(conditions, baos);
            String version = "<?xml version=\"1.0\" encoding=\"GB2312\" ?>";
            System.out.println(version
                + new String(baos.toByteArray(), "GB2312"));
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
