package potel.myorders;

public enum PrdOrderStatusEnum{
	Created('1', "已建立"), Paid('2',"已付款"), Canceled('3', "已取消");
	
	private char status;
	private String desc;
	
	private PrdOrderStatusEnum(char status, String desc) {
		this.setStatus(status);
		this.setDesc(desc);
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
