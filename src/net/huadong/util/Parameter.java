package net.huadong.util;

public class Parameter {
	
	public static final int IN = 1;
	public static final int OUT = 2;
	
	public Object param ; //存储的参数值ֵ
	public int direction = 1 ; //当我不为此值设置任何值得时候，默认该参数为输入类型的参数
	
	public Parameter(Object param, int direction) {
		super();
		this.param = param;
		this.direction = direction;
	}
	public Parameter() {
		super();
	}
	@Override
	public String toString() {
		return "Parameter [param=" + param + ", direction=" + direction + "]";
	}
	

}
