package chunk.context;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WriterPersona implements ItemWriter<Object> {

    @Override
    public void write(List<?> items) throws Exception {
        System.out.println("***********************");
    }
}
