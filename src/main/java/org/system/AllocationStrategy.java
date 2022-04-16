package org.system;

import javax.sql.DataSource;

public interface AllocationStrategy {
    void allocate(DataSource dataSource, String userId);
}
