package name.charlesb.webapp1.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.StringUtils;

@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration {

	@Configuration
	@Order(1)
	public static class PagesConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http
				.authorizeRequests()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.permitAll()
				.and()
				.antMatcher("/p/**")
				.csrf().disable()
				.authorizeRequests()
				.antMatchers("/p/**")
				.permitAll();

		}
	}

	@Configuration
	@Slf4j
	public static class ActuatorAuthentication extends WebSecurityConfigurerAdapter {

		@Value("${spring.boot.admin.username}")
		private String sbAdminUser;

		@Value("${spring.boot.admin.password}")
		private String sbAdminPwd;

		@Bean
		public UserDetailsService userDetailsService() {
			if (!StringUtils.hasText(sbAdminUser)) {
				LOG.warn("Spring Boot Admin User (spring.boot.admin.user) property not set!");
			}
			if (!StringUtils.hasText(sbAdminPwd)) {
				LOG.warn("Spring Boot Admin Password (spring.boot.admin.password) property not set!");
			}

			UserDetails sbAdminUser = User.withUsername(this.sbAdminUser).password("{noop}"+sbAdminPwd).roles("ACTUATOR").build();

			InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
			manager.createUser(sbAdminUser);
			return manager;
		}


		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth
				.userDetailsService(userDetailsService())
				.passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				// do not require authentication for health endpoint
				.antMatcher("/actuator/health")
				.authorizeRequests()
				.antMatchers("/actuator/health")
				.permitAll()
				.and()
				// all other actuator endpoints should require authentication
				.antMatcher("/actuator/**")
				.csrf().ignoringAntMatchers("/actuator/**")
				.and()
				.authorizeRequests()
				.antMatchers("/actuator/**")
				.hasRole("ACTUATOR").and().httpBasic();
		}
	}
}
