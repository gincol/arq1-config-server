//package es.gincol.blog.config;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import org.springframework.cloud.config.monitor.PropertyPathNotification;
//import org.springframework.cloud.config.monitor.PropertyPathNotificationExtractor;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.util.MultiValueMap;
//
//@Order(Ordered.LOWEST_PRECEDENCE - 300)
//public class GithubPropertyPathNotificationExtractor
//		implements PropertyPathNotificationExtractor {
//
//	@Override
//	public PropertyPathNotification extract(MultiValueMap<String, String> headers,
//			Map<String, Object> request) {
//		if ("push".equals(headers.getFirst("X-Github-Event"))) {
//			if (request.get("commits") instanceof Collection) {
//				Set<String> paths = new HashSet<>();
//				@SuppressWarnings("unchecked")
//				Collection<Map<String, Object>> commits = (Collection<Map<String, Object>>) request
//						.get("commits");
//				for (Map<String, Object> commit : commits) {
//					addAllPaths(paths, commit, "added");
//					addAllPaths(paths, commit, "removed");
//					addAllPaths(paths, commit, "modified");
//				}
//				if (!paths.isEmpty()) {
//					return new PropertyPathNotification(paths.toArray(new String[0]));
//				}
//			}
//		}
//		return null;
//	}
//
//	private void addAllPaths(Set<String> paths, Map<String, Object> commit, String name) {
//		@SuppressWarnings("unchecked")
//		Collection<String> files = (Collection<String>) commit.get(name);
//		if (files != null) {
//			paths.addAll(files);
//		}
//	}
//
//}
