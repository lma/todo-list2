package com.task.management

import akka.NotUsed
import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.scaladsl.Source
import akka.util.Timeout
import com.task.management.actor.TaskActor
import com.task.management.mappings.JsonMappings
import com.task.management.model.Task
import spray.json._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait Routes extends JsonMappings {
  val taskActor: ActorRef
  implicit def requestTimeout = Timeout(5 seconds)

  val routes =
    pathPrefix("api") {
      (path("tasks") & get) {
        complete((taskActor ? TaskActor.GetAll).mapTo[Seq[Task]])
      } ~
        (path("tasks" / IntNumber) & get) { id =>
          complete((taskActor ? TaskActor.GetById(id)).mapTo[Task])
        } ~
        (path("tasks") & post) {
          entity(as[Task]) { task =>
            complete((taskActor ? TaskActor.Create(task.name, task.description, task.category, task.dueDate, task.createDate)).map(r => r.asInstanceOf[Long].toJson))
          }
        } ~
        (path("tasks" / IntNumber) & put) { id =>
          entity(as[Task]) { task =>
            complete((taskActor ? TaskActor.Update(id, task.name, task.description, task.category, task.dueDate, task.createDate)).map(r => r.asInstanceOf[Int].toJson))
          }
        } ~
        (path("tasks" / IntNumber) & delete) { id =>
          complete((taskActor ? TaskActor.Delete(id)).map(r => r.asInstanceOf[Int].toJson))
        } ~
        (path("events") & get) {
            complete {
              Source
                .tick(0.seconds, 10.seconds, NotUsed)
                .map(_ => ServerSentEvent(taskToMessage))
            }
          }
    }

  private def taskToMessage = {
    Await.result((taskActor ? TaskActor.CheckDueDate).map(r => r.asInstanceOf[Seq[Task]].map(t => t.id.get).mkString("\n")), Duration.Inf)
  }
}
