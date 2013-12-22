package hit.cs.iread.model;

import java.io.Serializable;
import java.util.Date;

public class Bookmark implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mid;
	private Date time;
	private int refer;

	public Bookmark() {
	}

	public Bookmark(int mid) {
		this.mid = mid;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getRefer() {
		return refer;
	}

	public void setRefer(int refer) {
		this.refer = refer;
	}

}
