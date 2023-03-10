package batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchJobScheduler {

	@Autowired
	private Job job;

	@Autowired
	private JobLauncher jobLauncher;

	// Este método se ejecuta periódicamente gracias a la anotación "@Scheduled".
	@Scheduled(fixedDelay = 8000)
	public void runBatchJob() {
		// Se crean los parámetros del trabajo, que incluyen un ID único generado a partir del tiempo actual.
		JobParameters params = new JobParametersBuilder()
				.addLong("jobId", System.currentTimeMillis())
				.toJobParameters();

		// Se lanza el trabajo con el objeto "jobLauncher" y los parámetros creados anteriormente.
		try {
			jobLauncher.run(job, params);
			// Se manejan las excepciones que pueden ocurrir durante la ejecución del trabajo.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}