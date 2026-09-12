#!/bin/bash
# Production launcher. NOTE: do NOT add -Xmx/-Xms here — each box is
# cgroup-capped at 512MB RAM and the JVM sizes its heap from that
# automatically (25% => ~128MB). A fixed -Xmx above the cap gets the
# process OOM-killed at boot. Run from the repo root. Extra args
# (e.g. --server.port=XXXX) are forwarded to Spring Boot.
exec java -jar target/chitchat-0.0.1-SNAPSHOT.jar "$@"
