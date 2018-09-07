package es.gincol.blog;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import es.gincol.blog.utils.Constants;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		addDefaultProfile(app, source);
		Environment env = app.run(args).getEnvironment();
		try {
			log.info(
					"Access URLs:\n----------------------------------------------------------\n\t"
							+ "Local: \t\thttp://127.0.0.1:{}\n\t"
							+ "External: \thttp://{}:{}\n----------------------------------------------------------",
					env.getProperty("server.port"), InetAddress.getLocalHost().getHostAddress(),
					env.getProperty("server.port"));
		} catch (UnknownHostException e) {
			log.error("Host desconocido", e);
		}
	}

	private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
		// Si no encontramos ningun profile activo ponemos des por defecto
		if (!source.containsProperty("spring.profiles.active")
				&& !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
			log.info("No profile definido. Asignamos profile " + Constants.SPRING_PROFILE_LOCAL);
			app.setAdditionalProfiles(Constants.SPRING_PROFILE_LOCAL);
		}
	}
	

}
