package com.daofab.daofabapp.pojo.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse<T> {
	private String message;
	private long status;
	private T data;
}
