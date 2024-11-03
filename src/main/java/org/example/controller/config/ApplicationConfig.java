package org.example.controller.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")//Can be overwritten in web.xml using servlet configuration.
public class ApplicationConfig extends Application {

}
