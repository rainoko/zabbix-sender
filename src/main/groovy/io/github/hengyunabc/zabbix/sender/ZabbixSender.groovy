package io.github.hengyunabc.zabbix.sender

interface ZabbixSender {

  def SenderResult send(DataObject dataObject)

  def SenderResult send(DataObject dataObject, long clock)

  def SenderResult send(List<DataObject> dataObjectList)

  def SenderResult send(List<DataObject> dataObjectList, long clock)
}