package io.github.hengyunabc.zabbix.sender.test

import io.github.hengyunabc.zabbix.sender.DataObject;
import io.github.hengyunabc.zabbix.sender.SenderResult;
import io.github.hengyunabc.zabbix.sender.ZabbixSender;

import org.junit.Test;

public class ZabbixSenderTest {
	

	@Test
	public void test_LLD_rule() throws IOException {
		String host = "127.0.0.1";
		int port = 49156;
		ZabbixSender zabbixSender = new ZabbixSender(host, port);

		DataObject dataObject = new DataObject();
		dataObject.setHost("172.17.42.1");
		dataObject.setKey("test_discovery_rule");

		def data = '''\
		{
			"data" : { "hello" : "hello"}

	  }'''
		
		dataObject.setValue(data);
		dataObject.setClock(System.currentTimeMillis());
		SenderResult result = zabbixSender.send(dataObject);

		System.out.println("result:" + result);
		if (result.success()) {
			System.out.println("send success.");
		} else {
			System.err.println("sned fail!");
		}
		

	}
	
	@Test
	public void test() throws IOException {
		String host = "127.0.0.1";
		int port = 49156;
		ZabbixSender zabbixSender = new ZabbixSender(host, port);

		DataObject dataObject = new DataObject();
		dataObject.setHost("172.17.42.1");
		dataObject.setKey("a[test, jvm.mem.non-heap.used]");
		dataObject.setValue("10");
		dataObject.setClock(System.currentTimeMillis());
		SenderResult result = zabbixSender.send(dataObject);

		System.out.println("result:" + result);
		if (result.success()) {
			System.out.println("send success.");
		} else {
			System.err.println("sned fail!");
		}
		

	}

}
