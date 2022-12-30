package batch.processor;

import batch.model.Employee;
import batch.model.Profile;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Profile> {

	// Este método procesa un objeto "Employee" y devuelve un objeto "Profile".
	@Override
	public Profile process(final Employee emp) throws Exception {
		// Se determina el perfil del empleado basándose en sus años de experiencia.
		String profileName = "";
		if (emp.getExpInYears() < 5) {
			profileName = "Developer";
		} else if (emp.getExpInYears() >= 5 && emp.getExpInYears() <= 8) {
			profileName = "Team Lead";
		} else if (emp.getExpInYears() > 8) {
			profileName = "Manager";
		}

		// Se imprimen los detalles del empleado y su perfil.
		System.out.println("Emp Code: " + emp.getEmpCode() +
				", Emp Name: " + emp.getEmpName() + ", Profile Name:" + profileName);

		// Se devuelve un nuevo objeto "Profile" con los detalles del empleado y su perfil.
		return new Profile(emp.getEmpCode(), emp.getEmpName(), profileName);
	}

}
