package dubbo.demo.model.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 学生实体
 * @author chenlong12
 *
 */
@Data
public class Student implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *主键Id
	 */
	private int id;
	/**
	 * 学号
	 */
	private String num;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 年龄
	 */
	private int age;
	/**
	 * 性别
	 */
	private String sex;
	/**
	 * 备注
	 */
	private String remark;
}
