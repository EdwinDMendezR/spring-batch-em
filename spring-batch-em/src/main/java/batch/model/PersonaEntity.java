package batch.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonaEntity {
	private String codigo;
	private String nombre;

	public PersonaEntity(String codigo, String nombre) {
		setCodigo(codigo);
		setNombre(nombre);
	}

}