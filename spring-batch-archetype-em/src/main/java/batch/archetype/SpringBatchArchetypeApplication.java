package batch.archetype;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class SpringBatchArchetypeApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchArchetypeApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String[] springConfig = { "jobs/job-config.xml" };

        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        Job job = (Job) context.getBean("taskletJob");

        try {
            JobParameters jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis()).toJobParameters();
            JobExecution execution = jobLauncher.run(job, jobParameters);
            System.out.println("Exit Status : " + execution.getStatus());
            System.out.println("Exit Status : " + execution.getAllFailureExceptions());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }

}
