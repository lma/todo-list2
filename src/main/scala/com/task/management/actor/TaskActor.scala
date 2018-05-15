package com.task.management.actor

import java.util.Date

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe
import com.task.management.dao.TaskDao
import com.task.management.model.Task

object TaskActor {

  def props(): Props = Props(new TaskActor())

  case class Create(name: String, description: String, category: String, dueDate: Date, createDate: Date)

  case class Update(id: Long, name: String, description: String, category: String, dueDate: Date, createDate: Date)

  case class GetAll()

  case class Delete(id: Long)

  case class GetById(id: Long)

  case class CheckDueDate()

}

class TaskActor extends Actor with ActorLogging {

  import TaskActor._
  import TaskDao._

  implicit val ec = context.dispatcher

  override def receive: Receive = {
    case GetAll                                                       => findAll      pipeTo sender()
    case GetById(id)                                                  => findById(id) pipeTo sender()
    case Delete(id)                                                   => delete(id)   pipeTo sender()
    case CheckDueDate                                                 => findAllWithDueDateExceeded pipeTo sender()
    case Create(name, description, category, dueDate, createDate)     => create(Task(None, name, description, category, dueDate, createDate))     pipeTo sender()
    case Update(id, name, description, category, dueDate, createDate) => update(Task(None, name, description, category, dueDate, createDate), id) pipeTo sender()
  }
}
