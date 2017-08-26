package com.friday;

import java.util.Properties;

import org.junit.Test;
import com.friday.esearch.ElasticSearchAPI;
import com.friday.esearch.ElasticSearchConnector;
import com.friday.esearch.impl.ElasticSearchAPIImpl;
import com.friday.schedule.MyScheduler;


/**
 * Unit test for simple App.
 */
public class AppTest {

    /**
     * Rigourous Test :-)
     */
    @Test
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

    @Test
    public void testScheduler(){
        try{
            Properties props = new Properties();
            props.setProperty("schedule.job.dataCleanJobCron", "0 0 * * * ? *");
            props.setProperty("schedule.job.accAvgJobCron", "0 0 * * * ? *");
            MyScheduler.getInstance(props).registerDataCleanJob().start();
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("failed.");
        }
        
    }
}
