package org.jc.common.factory;

import org.jc.common.api.DatabaseInterface;

public record DatabaseFactory(DatabaseInterface databaseService) {
}