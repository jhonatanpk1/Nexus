package com.nexus.nexus.controlador;

import java.util.Arrays;
import java.util.List;

/*import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.util.AntPathMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    //private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

    private final List<String> publicRoutes = Arrays.asList(
            "/",
            "/login",
            "/logout",
            "/viaje/**",
            "/vuelo/**",
            "/vista/**",
            "/reserva/**",
            "/pago/**"
    );

    private final List<String> staticResources = Arrays.asList(
            "/css/**",
            "/js/**",
            "/images/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        String path = request.getRequestURI();

        boolean isPublic = publicRoutes.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
        boolean isStatic = staticResources.stream().anyMatch(pattern -> pathMatcher.matchStart(path, pattern));

        /*logger.info("Interceptando petición a la ruta: {}", path);
        logger.info("Ruta solicitada: {}", path);
        logger.info("Lista de rutas públicas: {}", publicRoutes);
        logger.info("¿Es ruta pública? {}", isPublic);
        logger.info("Lista de recursos estáticos: {}", staticResources);
        logger.info("¿Comienza con recurso estático? {}", isStatic);
        logger.info("Usuario en sesión (usuarioId): {}", usuarioId);*/

        if (isPublic || isStatic) {
            //logger.info("Acceso permitido a ruta pública o recurso estático: {}", path);
            return true;
        }

        if (usuarioId == null) {
            //logger.warn("Usuario no autenticado, redirigiendo a /login desde: {}", path);
            response.sendRedirect("/login");
            return false;
        }

        //logger.info("Usuario autenticado, permitiendo acceso a: {}", path);
        return true;
    }
}