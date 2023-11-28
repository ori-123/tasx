package com.codecool.tasx.service.auth.oauth2;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final HttpCookieOAuth2AuthorizationRequestRepository
    httpCookieOAuth2AuthorizationRequestRepository;

  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    throws IOException {
    Optional<String> redirectUri = CookieUtils.getCookie(
        request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
      .map(Cookie::getValue);

    //TODO: impl storage of providers w authorized redirect URIs
    if (redirectUri.isPresent()/* && !isAuthorizedRedirectUri(redirectUri.get())*/) {
      throw new UnauthorizedException();
    }

    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

    if (response.isCommitted()) {
      throw new UnauthorizedException(
        "Response has already been committed. Unable to redirect to " + targetUrl);
    }

    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(
      request,
      response);

    //TODO: sign access, refresh token, get userinfo? (/auth/login)

    super.getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
