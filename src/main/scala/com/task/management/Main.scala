package com.task.management

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.task.management.actor.TaskActor
import com.task.management.utils.Config
import scala.concurrent.duration._

object Main extends App with Routes with Config{
  implicit val system = ActorSystem("task-actor-system")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val taskActor = system.actorOf(TaskActor.props())

  //system.scheduler.schedule(0 seconds, 30 seconds, taskActor, TaskActor.CheckDueDate)

  Http().bindAndHandle(handler = routes, interface = host, port = port) map { binding =>
    println(s"REST interface bound to ${binding.localAddress}") } recover { case ex =>
    println(s"REST interface could not bind to $host:$port", ex.getMessage)
  }
}
