package com.ucredit.hermes.dto;

public class CommonResult<T> {
	private T result;

	private Operation operation;

	public T getResult() {
		return this.result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Operation getOperation() {
		return this.operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

}
