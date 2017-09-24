package com.spring.batch.api.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.HibernateItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

import com.spring.batch.api.model.Employee;

@SpringBootApplication
@EnableBatchProcessing
@Import({ HibernateConfiguration.class })
public class SpringBatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	@Autowired
	private HibernateConfiguration conf;

	@Bean
	public FlatFileItemReader<Employee> reader() {
		String[] CSV_HEADERS = new String[] { "name", "salary", "age" };
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setResource(new ClassPathResource("user.csv"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<Employee>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer(",") {
					{
						setNames(CSV_HEADERS);
					}
				});

				setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
					{
						setTargetType(Employee.class);
					}
				});

				/* setFieldSetMapper(new EmployeeMapper()); */
			}
		});
		return reader;
	}

	@Bean
	public EmployeeItemProcessor processor() {
		return new EmployeeItemProcessor();
	}

	@Bean
	public HibernateItemWriter<Employee> writter() {
		HibernateItemWriter<Employee> writter = new HibernateItemWriter<>();
		writter.setSessionFactory(conf.sessionFactory().getObject());
		return writter;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Employee, Employee> chunk(3)
				.reader(reader()).processor(processor()).writer(writter())
				.build();
	}

	@Bean
	public Job proceedJob() {
		return jobBuilderFactory.get("proceedJob")
				.incrementer(new RunIdIncrementer()).flow(step1()).end()
				.build();
	}
}
