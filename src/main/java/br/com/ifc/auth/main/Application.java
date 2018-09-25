package br.com.ifc.auth.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableEurekaClient
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages={"br.com.ifc.auth.repository"})
@SpringBootApplication(scanBasePackages= {"br.com.ifc.auth.config", "br.com.ifc.auth.controller", "br.com.ifc.auth.filter", "br.com.ifc.auth.service"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
