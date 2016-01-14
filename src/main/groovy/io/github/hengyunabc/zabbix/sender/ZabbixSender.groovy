package io.github.hengyunabc.zabbix.sender

import groovy.json.JsonSlurper

public class ZabbixSender {
	def String host;
	def int port;
	def int connectionTimeoutMillis = 3000;
	def int socketTimeoutMillis = 3000;

	def ZabbixSender(String host, int port) {
		this.host = host;
		this.port = port;
	}

	def ZabbixSender(String host, int port, int connectionTimeoutMillis,
											int socketTimeoutMillis) {
		this(host, port);
		this.connectionTimeoutMillis = connectionTimeoutMillis;
		this.socketTimeoutMillis = socketTimeoutMillis;
	}

	def SenderResult send(DataObject dataObject) throws IOException {
		return send(dataObject, System.currentTimeMillis());
	}

	def SenderResult send(DataObject dataObject, long clock)
			throws IOException {
		List<DataObject> dataObjectList = new LinkedList<DataObject>();
		dataObjectList.add(dataObject);
		return send(dataObjectList, clock);
	}

	def SenderResult send(List<DataObject> dataObjectList)
			throws IOException {
		return send(dataObjectList, System.currentTimeMillis());
	}

	def SenderResult send(List<DataObject> dataObjectList, long clock)
			throws IOException {
		SenderResult senderResult = new SenderResult();

		Socket socket = null;
		try {
			socket = new Socket();

			socket.setSoTimeout(socketTimeoutMillis);
			socket.connect(new InetSocketAddress(host, port), connectionTimeoutMillis);

			SenderRequest senderRequest = new SenderRequest();
			senderRequest.setData(dataObjectList);
			senderRequest.setClock(clock);

			socket.withStreams { inputStream, outputStream ->

				outputStream.write(senderRequest.toBytes());
				outputStream.flush();
				senderResult = readData(inputStream)
			}

		} finally {
			if (socket != null) {
				socket.close();
			}
		}

		return senderResult;
	}

	static def SenderResult readData(InputStream inputStream) {
		def senderResult = new SenderResult();
		// normal responseData.length < 100
		byte[] responseData = new byte[512];

		int readCount = 0;

		while (true) {
			int read = inputStream.read(responseData, readCount,
					responseData.length - readCount);
			if (read <= 0) {
				break;
			}
			readCount += read;
		}

		if (readCount < 13) {
			// seems zabbix server return "[]"?
			senderResult.setbReturnEmptyArray(true);
		}

		// header('ZBXD\1') + len + 0
		// 5  + 4 + 4
		String jsonString = new String(responseData, 13, readCount - 13);
		def json = JsonSlurper.parseText(jsonString);
		String info = json.info;
		//example info: processed: 1; failed: 0; total: 1; seconds spent: 0.000053
		//after split: [, 1, 0, 1, 0.000053]
		String[] split = info.split("[^0-9\\.]+");

		senderResult.setProcessed(Integer.parseInt(split[1]));
		senderResult.setFailed(Integer.parseInt(split[2]));
		senderResult.setTotal(Integer.parseInt(split[3]));
		senderResult.setSpentSeconds(Float.parseFloat(split[4]));

		return senderResult
	}
}
