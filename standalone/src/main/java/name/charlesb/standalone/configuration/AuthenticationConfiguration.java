package name.charlesb.standalone.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.util.StringUtils;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Slf4j
public class AuthenticationConfiguration {

	@Configuration
	@Order(1)
	@Slf4j
	public static class ServicesAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.antMatcher("/v1/services/**")
				.cors().and().csrf().disable()
				.authorizeRequests()
				.antMatchers("/v1/services/**")
				.permitAll();
		}
	}

	@Configuration
	@Order(2)
	@Slf4j
	public static class ActuatorAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

		@Value("${spring.boot.admin.username}")
		private String sbAdminUserName;

		@Value("${spring.boot.admin.password}")
		private String sbAdminPwd;

		@Bean
		public PasswordEncoder passwordEncoder() { return PasswordEncoderFactories.createDelegatingPasswordEncoder(); }

		@Bean
		public UserDetailsService userDetailsService() {
			if (!StringUtils.hasText(sbAdminUserName)) {
				LOG.warn("Spring Boot Admin User (spring.boot.admin.user) property not set!");
			}
			if (!StringUtils.hasText(sbAdminPwd)) {
				LOG.warn("Spring Boot Admin Password (spring.boot.admin.password) property not set!");
			}

			UserDetails sbAdminUser = User.withUsername(sbAdminUserName).password("{noop}"+sbAdminPwd).roles("ACTUATOR").build();

			InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
			manager.createUser(sbAdminUser);
			return manager;

		}

		@Bean(name = "actuatorAuthMgr")
		@Override
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
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

		/*@Autowired
		private PasswordEncoder passwordEncoder;

		@Autowired
		private UserDetailsService userDetailsService;*/

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth
				.userDetailsService(userDetailsService())
				.passwordEncoder(passwordEncoder());
		}
	}
}
