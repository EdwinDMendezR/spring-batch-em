package batch.configuration;

import batch.listener.JobExecutionListener;
import batch.model.Persona;
import batch.model.PersonaEntity;
import batch.processor.EmployeeItemProcessor;
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
		  .resource(new ClassPathResource("person.csv")) // establece la ubicación del archivo que se va a leer
		  .delimited() //indica que el archivo está delimitado por un caracter especial (como una coma) y no por un
				// número fijo de caracteres
		  .names(new String[]{ "codigo", "nombre" }) // establece los nombres de los campos para cada
				// elemento en el archivo
		  .fieldSetMapper(new BeanWrapperFieldSetMapper<Persona>() {{ // establece cómo se deben mapear los campos
			  // leídos al objeto de destino
			   setTargetType(Persona.class);
		  }})
		  .linesToSkip(1) //indica cuántas líneas del archivo se deben saltar al comienzo de la lectura. En este caso,
				// se salta la primera línea, lo que suele ser útil si el archivo tiene un encabezado con los nombres
				// de los campos
		  .build();
	}


	@Bean
	// Este método crea un escritor de lotes de JDBC para objetos de tipo "Profile".
	public JdbcBatchItemWriter<PersonaEntity> writer(DataSource dataSource) {
		// Se crea una instancia de "JdbcBatchItemWriter" a partir de un "JdbcBatchItemWriterBuilder".
		return new JdbcBatchItemWriterBuilder<PersonaEntity>()
				// Se establece un "itemSqlParameterSourceProvider" que utiliza las propiedades de un
				// objeto "Profile" para proporcionar los parámetros de SQL.
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<PersonaEntity>())
				// Se especifica la instrucción SQL que se usará para insertar datos en la tabla "persona".
				.sql("INSERT INTO persona (codigo, nombre) VALUES (:codigo, :nombre)")
				// Se establece la fuente de datos que se usará para conectarse a la base de datos.
				.dataSource(dataSource)
				// Se construye la instancia de "JdbcBatchItemWriter" y se devuelve.
				.build();
	}


	@Bean
	public ItemProcessor<Persona, PersonaEntity> processor() {
		return new EmployeeItemProcessor();
	}


	// Este método crea un trabajo de Spring Batch que consiste en un único paso.
	@Bean
	public Job createEmployeeJob(JobExecutionListener listener, Step step1) {
		// Se crea una instancia de "Job" a partir de un "JobBuilderFactory".
		return jobBuilderFactory
				// Se le da un nombre al trabajo.
				.get("createPersonaJob")
				// Se establece un "incrementer" para generar un identificador único para cada ejecución del trabajo.
				.incrementer(new RunIdIncrementer())
				// Se establece un "listener" para monitorear el progreso y los resultados del trabajo.
				.listener(listener)
				// Se agrega el único paso del trabajo.
				.flow(step1)
				// Se finaliza el flujo de trabajo.
				.end()
				// Se construye y devuelve el trabajo.
				.build();
	}

	// Este método crea un paso de Spring Batch que procesa y escribe datos en lotes.
	@Bean
	public Step step1(ItemReader<Persona> reader, ItemWriter<PersonaEntity> writer,
					  ItemProcessor<Persona, PersonaEntity> processor) {
		// Se crea una instancia de "Step" a partir de un "StepBuilderFactory".
		return stepBuilderFactory
				// Se le da un nombre al paso.
				.get("step1")
				// Se establece el tamaño del lote en 5 elementos.
				.<Persona, PersonaEntity>chunk(5)
				// Se establece el lector de datos que se utilizará para obtener los elementos a procesar.
				.reader(reader)
				// Se establece el procesador que se utilizará para transformar los elementos.
				.processor(processor)
				// Se establece el escritor que se utilizará para escribir los elementos procesados en la salida.
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