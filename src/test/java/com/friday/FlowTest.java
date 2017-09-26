package com.friday;

import org.apache.http.message.BasicHeader;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friday.utils.HttpUtil;

public class FlowTest {
	private static final  Logger LOG = LoggerFactory.getLogger(FlowTest.class);
	private String url = "http://183.62.244.214:8089/bigtext.html";

	@Test
	@Ignore
	public void testFakeVisiting() {
		try {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							HttpUtil.get(url, new BasicHeader(HttpUtil.HTTP_HEADER_USER_AGENT,
									"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Safari/604.1.38"));
							LOG.info("Http get method completed.");
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();

			Thread.sleep(100000);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}
}
