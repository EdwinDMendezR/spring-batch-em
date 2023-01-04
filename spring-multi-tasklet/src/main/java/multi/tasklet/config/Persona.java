package multi.tasklet.config;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Persona {
	private String codigo;
	private String nombre;

	public Persona(String codigoA, String personaA) {
		setCodigo(codigoA);
		setNombre(personaA);
	}
}