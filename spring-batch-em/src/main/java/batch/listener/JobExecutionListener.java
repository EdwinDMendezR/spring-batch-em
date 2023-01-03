package batch.listener;

import batch.model.PersonaEntity;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class JobExecutionListener implements org.springframework.batch.core.JobExecutionListener {

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
		// en la tabla "persona".
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			List<PersonaEntity> result = jdbcTemplate
					.query("SELECT codigo, nombre FROM persona",
					new RowMapper<PersonaEntity>() {
						@Override
						public PersonaEntity mapRow(ResultSet rs, int row) throws SQLException {
							return new PersonaEntity(
									rs.getString(1),
									rs.getString(2)
							);
						}
					});
			System.out.println("Records:"+result.size());
		}
	}

}