package org.pingle.pingleserver.interceptor.pre;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.annotation.GUserId;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.BusinessException;
import org.pingle.pingleserver.service.UserMeetingService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class GUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserMeetingService userMeetingService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class)
                && parameter.hasParameterAnnotation(GUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        final Principal principal = webRequest.getUserPrincipal();
        if (principal == null) {
            throw new BusinessException(ErrorMessage.NO_SUCH_USER);
        }

        if (webRequest.getHeader("Group-Id") == null)
            throw new BusinessException(ErrorMessage.INVALID_HEADER_ERROR);
        Long groupId = Long.valueOf(webRequest.getHeader("Group-Id"));

        userMeetingService.verifyUser(getIdFromPrincipal(principal), groupId);

        return Long.valueOf(principal.getName());
    }

    private Long getIdFromPrincipal (Principal principal) {
        return Long.valueOf(principal.getName());
    }

}


