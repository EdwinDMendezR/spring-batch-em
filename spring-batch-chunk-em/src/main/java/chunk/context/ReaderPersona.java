package chunk.context;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class ReaderPersona implements ItemReader<Persona> {

    @Override
    public Persona read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        System.out.println("__________________________________________");
        return new Persona("codigo", "nombre");
    }

}
