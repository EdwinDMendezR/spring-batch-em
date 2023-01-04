package chunk;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringChunkApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringChunkApplication.class, args);
    }

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Override
    public void run(String... args) throws Exception {

        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
//
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("getJob", System.currentTimeMillis())
//                .toJobParameters();
//        jobLauncher.run(job, jobParameters);


    }


}
