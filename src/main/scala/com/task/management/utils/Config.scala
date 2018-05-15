package com.task.management.utils

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")
  private val databaseConfig = config.getConfig("h2mem1")

  val host = httpConfig.getString("host")
  val port = httpConfig.getInt("port")

  val databaseUrl = databaseConfig.getString("url")

}
