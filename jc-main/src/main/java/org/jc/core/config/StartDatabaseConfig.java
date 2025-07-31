package org.jc.core.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartDatabaseConfig {
    private boolean remote;
    private String path;
    private String url;
    private String username;
    private String password;
}
