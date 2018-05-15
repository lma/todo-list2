package com.task.management.mappings

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.task.management.model.Task
import spray.json.DefaultJsonProtocol
import DateMarshalling._

trait JsonMappings extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userFormat = jsonFormat6(Task)
}
