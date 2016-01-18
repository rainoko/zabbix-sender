package io.github.hengyunabc.zabbix.sender

import groovy.json.JsonOutput
import groovy.json.internal.ArrayUtils

import java.nio.charset.StandardCharsets;

public class SenderRequest {

  def String request;
  def long clock;
  def List<DataObject> data;

  def byte[] toBytes() {
    // https://www.zabbix.org/wiki/Docs/protocols/zabbix_sender/2.0
    // https://www.zabbix.org/wiki/Docs/protocols/zabbix_sender/1.8/java_example

    byte[] content = JsonOutput.toJson(this).getBytes(StandardCharsets.UTF_8)

    def length = content.length
    println "sending content: ${content}"
    byte[] header = [
      'Z', 'B', 'X', 'D',
      '\1',
      (byte) (length & 0xFF),
      (byte) ((length >> 8) & 0x00FF),
      (byte) ((length >> 16) & 0x0000FF),
      (byte) ((length >> 24) & 0x000000FF),
      '\0', '\0', '\0', '\0']

    byte[] combined = new byte[header.length + content.length];

    System.arraycopy(header,0,combined,0         ,header.length);
    System.arraycopy(content,0,combined,header.length,content.length);

    return combined;
  }
}
