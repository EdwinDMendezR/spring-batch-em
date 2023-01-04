package multi.tasklet.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class MultiTaskletConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ATasklet aTasklet;

    @Autowired
    private BTasklet bTasklet;

    @Autowired
    private CTasklet cTasklet;


    // Este método crea un trabajo de Spring Batch que consiste en un único paso.
    @Bean
    public Job createJob() {
        // Se crea una instancia de "Job" a partir de un "JobBuilderFactory".
        return jobBuilderFactory
                .get("createPersonaJob")
                .incrementer(new RunIdIncrementer())
                .start(stepA())
                .next(stepB())
                .next(stepC())
                .build();
    }

    @Bean
    public Step stepA() {
        return stepBuilderFactory
                .get("stepA")
                .tasklet(aTasklet)
                .build();
    }

    @Bean
    public Step stepB() {
        return stepBuilderFactory
                .get("stepB")
                .tasklet(bTasklet)
                .build();
    }

    @Bean
    public Step stepC() {
        return stepBuilderFactory
                .get("stepC")
                .tasklet(cTasklet)
                .build();
    }


}
