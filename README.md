```
mvn quarkus:dev
curl localhost:8080  # to generate some tracing data
curl localhost:8080/prometheus | grep span    # to view the metrics
```
