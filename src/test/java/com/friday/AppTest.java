package com.friday;

import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.ElasticSearchConnector;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testESearch() {
        try {
            ElasticSearchAPI api = new ElasticSearchAPIImpl();
            api.countKeyword("detail.keyword",
                    "011a4002:1: SNMP_TRAP: Pool /Common/lua.midea.com.cn member lua-huaian_midea_com_cn (ip:port=10.159.1.251:7070) state change red --> green");
            api.countKeyword("targetHost.keyword", "GDSD-PDC-Intranet-GTM2");
            api.countKeyword("level.keyword", "alert");
            ElasticSearchConnector.getSingletonInstance().disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("failed.");
        }
    }
}
