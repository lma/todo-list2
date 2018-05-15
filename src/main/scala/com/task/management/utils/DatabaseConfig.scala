package com.task.management.utils

trait DatabaseConfig extends Config{
  val driver = slick.jdbc.H2Profile

  import driver.api._

  def db = Database.forConfig("h2mem1")

  implicit val session: Session = db.createSession()
}
