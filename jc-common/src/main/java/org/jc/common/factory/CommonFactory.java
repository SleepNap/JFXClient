package org.jc.common.factory;

import lombok.Data;
import org.jc.common.api.ConfigInterface;
import org.jc.common.api.DatabaseInterface;

@Data
public class CommonFactory {
    private ConfigInterface configService;
    private DatabaseInterface databaseService;


}
