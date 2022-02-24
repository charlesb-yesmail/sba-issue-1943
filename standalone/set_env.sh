#!/bin/sh
# Use this file to set environment specific configuration.
# This is a template which lists the variables that can be set, but should not set them to a value.
# Edit the file locally to configure your own environment and use git stash to save your local copy to reapply
# !!!! Very important note !!!!
# !!!! DO NOT COMMIT local changes to GitHub unless adding a new environment variable!!!!
# !!!! Failure to observe this rule should result in PR rejection !!!!
#
# Environment variables
#
# This variable defines the Spring profile to use when running the Microservices application.
# This should correspond to a variant of the application.yml file.
# For example to use the config defined in application-integrationtest.yml set this value to "integrationtest"
# Comment it out to use the default profile
export SPRING_PROFILES_ACTIVE=""
#
# Add any additional environment variables to override Spring config here.
# Basic guidelines:
# All uppercase
# Replace period (.) with underscore (_)
# Remove hyphens
# Example spring.main.log-startup-info becomes SPRING_MAIN_LOGSTARTUPINFO
# See here for documentation: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-relaxed-binding-from-environment-variables

# Spring Boot Admin settings - set these values to use SB Admin
# The URL of the Spring Boot Admin server
export SPRING_BOOT_ADMIN_CLIENT_URL=""
# The username for the SpringBoot Admin server
export SPRING_SECURITY_USER_NAME="username"
# The password for the SpringBoot Admin server
export SPRING_SECURITY_USER_PASSWORD='{noop}password'
# Username and password used by Spring Boot Admin to authenticate with Actuator endpoints
export SB_ADMIN_USER=${SPRING_SECURITY_USER_NAME}
export SB_ADMIN_PASSWORD=${SPRING_SECURITY_USER_PASSWORD}
