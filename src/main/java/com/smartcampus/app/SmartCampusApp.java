
package com.smartcampus.app;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api/v1")
public class SmartCampusApp extends Application {
    // Jersey auto-scans packages via web.xml
}
