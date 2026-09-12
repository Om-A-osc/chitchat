#!/bin/bash
# Production start script for allotted low-CPU boxes.
# -Xms/-Xmx 2g: caps G1-era heap bloat; small heap = short GC pauses.
# UseParallelGC: throughput-oriented collector; on ~1 vCPU it beats G1's
# concurrent phases which steal app CPU continuously.
# Run from the repo root (uses relative target/ path). Extra args (e.g.
# --server.port=XXXX) are forwarded to Spring Boot.
exec java -Xms2g -Xmx2g -XX:+UseParallelGC -jar target/chitchat-0.0.1-SNAPSHOT.jar "$@"
