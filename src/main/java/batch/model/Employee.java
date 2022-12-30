package batch.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Employee {
	private long empCode;
	private String empName;
	private int expInYears;
}