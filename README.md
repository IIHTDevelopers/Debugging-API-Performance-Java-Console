### Scenario
The `generateReport()` method times out under load testing (500 RPS). Profiling shows most time is spent waiting for a synchronous external API call.

### Objective
Refactor to make the external call asynchronous using `CompletableFuture` and thread pool, and ensure 95th percentile latency < 300 ms.


#### Steps to Reproduce
1. Compile with `mvn clean package`.
2. Run unit tests to observe failure using 'mvn test'
3. Final objective is to make test case pass.
4. You can run test cases many times and refactor your code.

