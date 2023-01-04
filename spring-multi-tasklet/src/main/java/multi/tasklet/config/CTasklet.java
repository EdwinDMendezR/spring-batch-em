package multi.tasklet.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class CTasklet implements Tasklet {

    private String name;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Execute C.....");

        Persona persona = (Persona) chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext().get("persona");

        System.out.println(persona.getCodigo());
        System.out.println(persona.getNombre());

        return RepeatStatus.FINISHED;
    }

}
