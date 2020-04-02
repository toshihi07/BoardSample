package boardSample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//Spring SecurityのWebセキュリティサポートを有効にし、Spring MVC統合を提供するための注釈
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	//SpringSecurityの制限を無視してほしい部分を記載
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/favicon.ico", "/css/**",
				"/js/**", "/img/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		//.antMatchers("/").permitAll()で全ユーザーがアクセスできる場所を記載
		.antMatchers("/","/boards/{id}","/board/boardShow","/signup","/login","/boards/index","/boards/search","/boards//{id}/comments/searchTitle","/boards//{id}/comments/searchText","/boards/index/{pageNumber}","/{pageNumber}").permitAll().anyRequest().authenticated().and().formLogin()
				//.loginProcessingUrl("/authenticate")で記載したURLがログイン処理を行う。
				.loginProcessingUrl("/authenticate")
				//ログインフォームのパスを記載します。
				.loginPage("/loginForm")
				//ログイン失敗時の遷移先URL
				.failureUrl("/?error")
				//ログイン成功時の遷移先URL
				.defaultSuccessUrl("/", true)
				//ユーザ名のパラメータを指定
				.usernameParameter("username")
				//パスワードのパラメータを指定
				.passwordParameter("password")
				.and().logout()
				//ログアウト成功時の遷移先URL
				.logoutSuccessUrl("/").and();
	}

	@Bean
  PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
  }

}
