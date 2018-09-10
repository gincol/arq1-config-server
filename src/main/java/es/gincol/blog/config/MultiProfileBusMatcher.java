package es.gincol.blog.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.bus.BusAutoConfiguration;
import org.springframework.cloud.bus.BusPathMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.Map;

@Configuration
public class MultiProfileBusMatcher implements PathMatcher {

	Logger LOGGER = LoggerFactory.getLogger(MultiProfileBusMatcher.class);
	AntPathMatcher matcher = new AntPathMatcher(":");

	@PostConstruct
	public void debugMatcherInit() {
		LOGGER.info("I am born");
	}

	@BusPathMatcher
	@Bean(BusAutoConfiguration.BUS_PATH_MATCHER_NAME)
	public MultiProfileBusMatcher getMultiProfileBusMatcher() {
		return this;
	}

	public boolean matchMultiProfile(String event, String applicationContextID) {

		event = "*";
		
		LOGGER.info("custom matchMultiProfile : {} , {}", event, applicationContextID);

		boolean matched = false;

		// parse the application-context-id
		String[] appContextIDTokens = applicationContextID.split(":");
		String selfApplicationName = appContextIDTokens[0];
		String selfProfiles = appContextIDTokens[1];

		// parse the RemoteRefreshApplicationEvent
		String[] eventTokens = event.split(":");
		String eventApplicationName = eventTokens[0];
		String eventProfile = eventTokens[1];

		// Match the application
		// selfApplicationName.equalsIgnoreCase(eventApplicationName)
		if (matcher.match(selfApplicationName, eventApplicationName)) {

			// Get the if event profile matches any of our profile
			String[] profiles = selfProfiles.split(",");
			for (int i = 0; i < profiles.length; i++) {
				String selfProfile = profiles[i];
				if (matcher.match(selfProfile, eventProfile)) {
					matched = true;
					break;
				}
			}
		}

		LOGGER.info("custom matched {}", matched);
		return matched;
	}

	@Override
	public boolean isPattern(String path) {
		return matcher.isPattern(path);
	}

	@Override
	public boolean match(String pattern, String path) {
		LOGGER.info("custom In match: {} , {}", pattern, path);
		if (!matcher.match(pattern, path)) {
			return matchMultiProfile(pattern, path);
		}
		return true;
	}

	@Override
	public boolean matchStart(String pattern, String path) {
		return matcher.matchStart(pattern, path);
	}

	@Override
	public String extractPathWithinPattern(String pattern, String path) {
		return matcher.extractPathWithinPattern(pattern, path);
	}

	@Override
	public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
		return matcher.extractUriTemplateVariables(pattern, path);
	}

	@Override
	public Comparator<String> getPatternComparator(String path) {
		return matcher.getPatternComparator(path);
	}

	@Override
	public String combine(String pattern1, String pattern2) {
		return matcher.combine(pattern1, pattern2);
	}
}
