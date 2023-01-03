package batch.processor;

import batch.model.Persona;
import batch.model.PersonaEntity;
import org.springframework.batch.item.ItemProcessor;

public class PersonaItemProcessor implements ItemProcessor<Persona, PersonaEntity> {

	@Override
	public PersonaEntity process(final Persona persona) throws Exception {
		// Se devuelve un nuevo objeto "PersonaEntity".
		return new PersonaEntity(persona.getCodigo(), persona.getNombre());
	}

}
