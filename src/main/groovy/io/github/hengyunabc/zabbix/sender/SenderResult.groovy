package io.github.hengyunabc.zabbix.sender

import groovy.json.JsonOutput;

/**
 * 
 * @author hengyunabc
 *
 */
public class SenderResult {
	def int processed;
	def int failed;
	def int total;
	def float spentSeconds;

	/**
	 * sometimes zabbix server will return "[]".
	 */
	def boolean bReturnEmptyArray = false;

	/**
	 * if all sended data are processed, will return true, else return false.
	 * 
	 * @return
	 */
	def boolean success() {
		return !bReturnEmptyArray && processed == total;
	}

	@Override
	public String toString() {
		return JsonOutput.toJson(this);
	}
}
