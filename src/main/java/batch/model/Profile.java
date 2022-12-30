package batch.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Profile {
	private long empCode;	
	private String empName;
	private String profileName;

	public Profile(long empCode, String empName, String profileName) {
		this.empCode = empCode;
		this.empName = empName;
		this.profileName = profileName;
	}

}