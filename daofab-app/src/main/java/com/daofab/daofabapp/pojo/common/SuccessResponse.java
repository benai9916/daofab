package com.daofab.daofabapp.pojo.common;

public class SuccessResponse<T> extends BaseResponse<T> {
	public SuccessResponse(T data) {
		super.setData(data);
		super.setMessage("done successfully");
		super.setStatus(1l);
	}
}
