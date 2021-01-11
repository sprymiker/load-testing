#!/bin/bash

GATLING_DOWNLOAD_MAVEN_URL='https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/3.3.0/gatling-charts-highcharts-bundle-3.3.0-bundle.zip'
GATLING_ARCHIVE_NAME='gatling-charts-highcharts-bundle-3.3.0-bundle.zip'
GATLING_DIRECTORY_NAME='gatling-charts-highcharts-bundle-3.3.0'

function installNpm() {
  npm i
}

function cleanUpGatling() {
  rm -r ./.dist/${GATLING_DIRECTORY_NAME}/ 2>/dev/null
  rm -r ./gatling 2>/dev/null
  rm ./.dist/*.zip 2>/dev/null
}

function downloadGatling() {
  curl ${GATLING_DOWNLOAD_MAVEN_URL} --output ./.dist/${GATLING_ARCHIVE_NAME}
}

function unpackGatling() {
  unzip -q ./.dist/${GATLING_ARCHIVE_NAME} -d ./.dist
  rm ./.dist/${GATLING_ARCHIVE_NAME} 2>/dev/null
  rm -r ./gatling 2>/dev/null
  cp -R -n ./.dist/${GATLING_DIRECTORY_NAME} ./gatling/
}

function saveGatlingConfig() {
  if [ -d './gatling/conf' ]; then
    rm -rf ./.dist/gatling-conf 2>/dev/null
    mkdir ./.dist/gatling-conf
    cp -R -n ./gatling/conf/* ./.dist/gatling-conf
  fi
}

function loadGatlingConfig() {
  if [ -d './.dist/gatling-conf' ]; then
    rm -rf ./gatling/conf 2>/dev/null
    mv ./.dist/gatling-conf ./gatling/conf
  fi
}

function installGatling() {
  saveGatlingConfig
  cleanUpGatling
  downloadGatling
  unpackGatling
  loadGatlingConfig
}

case "${1}" in
  gatling)
    installGatling
    ;;
  npm)
    installNpm
    ;;
  *)
    installGatling
    installNpm
    ;;
esac
