package com.baibei.shiyi.gateway.dto;

public class PushMessage<T> {

	//quote=行情，open=建仓结果，close=平仓结果
	private String type;
	
	private T data;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
