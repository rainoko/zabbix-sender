package io.github.hengyunabc.zabbix.sender;

import groovy.json.JsonOutput;

public class DataObject {
	long clock;
	String host;
	String key;
	String value;

	public DataObject() {

	}

	public DataObject(long clock, String host, String key, String value) {
		this.clock = clock;
		this.host = host;
		this.key = key;
		this.value = value;
	}

	static public Builder builder() {
		return new Builder();
	}

	public static class Builder {
		Long clock;
		String host;
		String key;
		String value;

		Builder() {

		}

		public Builder clock(long clock) {
			this.clock = clock;
			return this;
		}

		public Builder host(String host) {
			this.host = host;
			return this;
		}

		public Builder key(String key) {
			this.key = key;
			return this;
		}

		public Builder value(String value) {
			this.value = value;
			return this;
		}

		public DataObject build() {
			if (clock == null) {
				clock = System.currentTimeMillis();
			}
			return new DataObject(clock, host, key, value);
		}
	}

	@Override
	public String toString(){
		return JsonOutput.toJson(this);
	}

}
