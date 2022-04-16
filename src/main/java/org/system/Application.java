
package org.system;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.flywaydb.core.Flyway;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class Application {
  public static void main(String[] args) throws Exception {
    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setUser("user");
    dataSource.setPassword("pass");
    dataSource.setServerName("localhost");
    dataSource.setDatabaseName("airlines");
    Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .locations("db/migrations")
            .load();

    flyway.clean();
    flyway.migrate();

    AllocationStrategy[] strategies = {
//            new NoLockStrategy(),
//            new SelectThenUpdate(),
            new SelectForUpdateStrategy(),
//            new SkipLockStrategy()
    };
    for(AllocationStrategy strategy: strategies) {
//      long start = System.nanoTime();
      Instant start = Instant.now();
      Allocator allocator = new Allocator(dataSource, strategy);
      List<Thread> tasks = new LinkedList<>();
      for(int user = 1; user <= 120; user++) {
        int finalUser = user;
        Thread task = new Thread(() -> allocator.allocateFor(String.format("%03d", finalUser)));
        task.setDaemon(true);
        tasks.add(task);
      }

      for(Thread task: tasks) {
        task.start();
      }

      for(Thread task: tasks) {
        task.join();
      }
      Instant finish = Instant.now();
      long timeElapsed = Duration.between(start, finish).toMillis();
      allocator.printSeatMap();
      System.out.println("=".repeat(80));
      System.out.printf("Took %d millis\n", timeElapsed);
    }
  }
}
