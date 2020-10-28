package client.algebra

import akka.actor.ActorSystem
import zio.Task

sealed trait Service {
  def getActorSystem: Task[ActorSystem]
}


sealed trait ActorEnv {
  def dependencies: Service
}

trait ActorEnvLive extends ActorEnv {

  private val system = ActorSystem("ZIO" + System.currentTimeMillis())

  val dependencies = new Service {
    override def getActorSystem: Task[ActorSystem] = Task(system)
  }

}
