package batch.listener;

import batch.model.Profile;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class EmpJobExecutionListener implements JobExecutionListener {

	@Autowired
	public JdbcTemplate jdbcTemplate;

	// Este método se ejecuta justo antes de que se inicie el trabajo.
	@Override
	public void beforeJob(JobExecution jobExecution) {
		// Se imprime el ID del trabajo que se está ejecutando.
		System.out.println("Executing job id " + jobExecution.getId());
	}


	// Este método se ejecuta justo después de que se complete el trabajo.
	@Override
	public void afterJob(JobExecution jobExecution) {
		// Si el trabajo se completó con éxito, se imprime el número de registros que se insertaron
		// en la tabla "profile".
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			List<Profile> result = jdbcTemplate.query("SELECT empCode, empName, profileName FROM profile",
					new RowMapper<Profile>() {
						@Override
						public Profile mapRow(ResultSet rs, int row) throws SQLException {
							return new Profile(rs.getLong(1), rs.getString(2), rs.getString(3));
						}
					});
			System.out.println("Number of Records:"+result.size());
		}
	}

}