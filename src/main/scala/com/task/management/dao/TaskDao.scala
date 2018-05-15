package com.task.management.dao

import java.sql.Timestamp
import java.util.Date

import com.task.management.model.Task
import com.task.management.model.definition.TaskTable
import com.task.management.utils.DatabaseConfig
import slick.dbio.NoStream
import slick.jdbc.H2Profile.api._
import slick.sql.{FixedSqlStreamingAction, SqlAction}

import scala.concurrent.Future

object TaskDao extends DatabaseConfig {
  val tasks = TableQuery[TaskTable]

  protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }

  protected implicit def executeReadStreamFromDb[A](action: FixedSqlStreamingAction[Seq[A], A, _ <: slick.dbio.Effect]): Future[Seq[A]] = {
    db.run(action)
  }

  implicit val timestamp2dateTime: BaseColumnType[Date] = MappedColumnType.base[Date, Timestamp](
    dateTime => new Timestamp(dateTime.getTime),
    timestamp => new Date(timestamp.getTime)
  )

  val setup = DBIO.seq(
    (tasks.schema).create
  )

  db.run(setup)

  def findAll: Future[Seq[Task]] = tasks.result

  def findAllWithDueDateExceeded: Future[Seq[Task]] = tasks.filter(_.dueDate <= new Date()).result

  def delete(taskId: Long): Future[Int] = tasks.filter(_.id === taskId).delete

  def findById(taskId: Long): Future[Task] = tasks.filter(_.id === taskId).result.head

  def create(task: Task): Future[Long] = (tasks returning tasks.map(_.id)) += task

  def update(newTask: Task, taskId: Long): Future[Int] = tasks.filter(_.id === taskId)
    .map(task => (task.name, task.description, task.category, task.dueDate, task.createDate))
    .update((newTask.name, newTask.description, newTask.category, newTask.dueDate, newTask.createDate))
}
