package chunk.context;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Persona {
	private String codigo;
	private String nombre;

	public Persona(String codigo, String nombre) {
		setCodigo(codigo);
		setNombre(nombre);
	}
}