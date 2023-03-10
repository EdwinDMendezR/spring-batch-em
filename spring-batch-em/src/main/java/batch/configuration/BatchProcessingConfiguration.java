package batch.configuration;

import batch.listener.JobExecutionListener;
import batch.model.Persona;
import batch.model.PersonaEntity;
import batch.processor.PersonaItemProcessor;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchProcessingConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Bean
	public FlatFileItemReader<Persona> reader() {
		return new FlatFileItemReaderBuilder<Persona>()
		  .name("flatFileItemReader") // establece el nombre del objeto FlatFileItemReader
		  .resource(new ClassPathResource("person.csv")) // establece la ubicaci??n del archivo que se va a leer
		  .delimited() //indica que el archivo est?? delimitado por un caracter especial (como una coma) y no por un
				// n??mero fijo de caracteres
		  .names(new String[]{ "codigo", "nombre" }) // establece los nombres de los campos para cada
				// elemento en el archivo
		  .fieldSetMapper(new BeanWrapperFieldSetMapper<Persona>() {{ // establece c??mo se deben mapear los campos
			  // le??dos al objeto de destino
			   setTargetType(Persona.class);
		  }})
		  .linesToSkip(1) //indica cu??ntas l??neas del archivo se deben saltar al comienzo de la lectura. En este caso,
				// se salta la primera l??nea, lo que suele ser ??til si el archivo tiene un encabezado con los nombres
				// de los campos
		  .build();
	}


	@Bean
	// Este m??todo crea un escritor de lotes de JDBC para objetos de tipo "Profile".
	public JdbcBatchItemWriter<PersonaEntity> writer(DataSource dataSource) {
		// Se crea una instancia de "JdbcBatchItemWriter" a partir de un "JdbcBatchItemWriterBuilder".
		return new JdbcBatchItemWriterBuilder<PersonaEntity>()
				// Se establece un "itemSqlParameterSourceProvider" que utiliza las propiedades de un
				// objeto "Profile" para proporcionar los par??metros de SQL.
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<PersonaEntity>())
				// Se especifica la instrucci??n SQL que se usar?? para insertar datos en la tabla "persona".
				.sql("INSERT INTO persona (codigo, nombre) VALUES (:codigo, :nombre)")
				// Se establece la fuente de datos que se usar?? para conectarse a la base de datos.
				.dataSource(dataSource)
				// Se construye la instancia de "JdbcBatchItemWriter" y se devuelve.
				.build();
	}


	@Bean
	public ItemProcessor<Persona, PersonaEntity> processor() {
		return new PersonaItemProcessor();
	}


	// Este m??todo crea un trabajo de Spring Batch que consiste en un ??nico paso.
	@Bean
	public Job createPersonaJob(JobExecutionListener listener, Step step1) {
		// Se crea una instancia de "Job" a partir de un "JobBuilderFactory".
		return jobBuilderFactory
				// Se le da un nombre al trabajo.
				.get("createPersonaJob")
				// Se establece un "incrementer" para generar un identificador ??nico para cada ejecuci??n del trabajo.
				.incrementer(new RunIdIncrementer())
				// Se establece un "listener" para monitorear el progreso y los resultados del trabajo.
				.listener(listener)
				// Se agrega el ??nico paso del trabajo.
				.flow(step1)
				// Se finaliza el flujo de trabajo.
				.end()
				// Se construye y devuelve el trabajo.
				.build();
	}

	// Este m??todo crea un paso de Spring Batch que procesa y escribe datos en lotes.
	@Bean
	public Step step1(ItemReader<Persona> reader, ItemWriter<PersonaEntity> writer,
					  ItemProcessor<Persona, PersonaEntity> processor) {
		// Se crea una instancia de "Step" a partir de un "StepBuilderFactory".
		return stepBuilderFactory
				// Se le da un nombre al paso.
				.get("step1")
				// Se establece el tama??o del lote en 5 elementos.
				.<Persona, PersonaEntity>chunk(5)
				// Se establece el lector de datos que se utilizar?? para obtener los elementos a procesar.
				.reader(reader)
				// Se establece el procesador que se utilizar?? para transformar los elementos.
				.processor(processor)
				// Se establece el escritor que se utilizar?? para escribir los elementos procesados en la salida.
				.writer(writer)
				// Se construye y devuelve el paso.
				.build();
	}


	@Bean
	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setJdbcUrl("jdbc:h2:mem:testdb");
		dataSource.setUsername("");
		dataSource.setPassword("");
		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}