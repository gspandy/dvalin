/**
 *
 */
package de.taimos.dvalin.mongo;

/*
 * #%L
 * MongoDB support for dvalin
 * %%
 * Copyright (C) 2015 Taimos GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.mongobee.Mongobee;

import de.taimos.dvalin.mongo.links.DLinkDAO;

/**
 * Copyright 2015 Taimos GmbH<br>
 * <br>
 *
 * @author thoeger
 */
public class LinkTester {

    private static final LinkDAO dao = new LinkDAO();
    private static final LinkedDAO ldao = new LinkedDAO();

    private static final DLinkDAO dlinkDAO = new DLinkDAO();


    @BeforeClass
    public static void init() {
        try {
            System.setProperty("mongodb.name", ABaseTest.dbName);
            Field mongoField = AbstractMongoDAO.class.getDeclaredField("mongo");
            mongoField.setAccessible(true);
            mongoField.set(LinkTester.dao, ABaseTest.mongo);
            mongoField.set(LinkTester.ldao, ABaseTest.mongo);

            Field mongoField2 = DLinkDAO.class.getDeclaredField("mongo");
            mongoField2.setAccessible(true);
            mongoField2.set(LinkTester.dlinkDAO, ABaseTest.mongo);

            Mongobee bee = new Mongobee(ABaseTest.mongo);
            bee.setChangeLogsScanPackage("de.taimos.dvalin.mongo.changelog");
            bee.setDbName(ABaseTest.dbName);
            bee.setEnabled(true);
            bee.execute();
            LinkTester.dao.init();
            LinkTester.ldao.init();
            LinkTester.dlinkDAO.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLinks() {
        LinkedObject lo1 = new LinkedObject();
        lo1.setName("LinkedObject1");
        lo1 = LinkTester.ldao.save(lo1);

        LinkedObject lo2 = new LinkedObject();
        lo2.setName("LinkedObject2");
        lo2 = LinkTester.ldao.save(lo2);

        LinkObject lo = new LinkObject();
        lo.setName("LinkObject");
        lo.getLinks().add(lo1.asLink());
        lo.getLinks().add(lo2.asLink());
        lo = LinkTester.dao.save(lo);

        Assert.assertEquals(2, lo.getLinks().size());

        List<LinkedObject> list = LinkTester.dlinkDAO.resolve(lo.getLinks(), LinkedObject.class);
        Assert.assertEquals(2, list.size());
    }

}
