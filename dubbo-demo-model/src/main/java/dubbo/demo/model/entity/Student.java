package dubbo.demo.model.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Student implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String num;
	private String name;
	private int age;
	private String sex;
	private String remark;
}
