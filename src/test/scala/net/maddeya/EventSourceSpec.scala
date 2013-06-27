package net.maddeya

import akka.actor.{ Props, Actor, ActorSystem }
import akka.testkit.{ TestKit, TestActorRef, ImplicitSender }
import org.scalatest.{ WordSpec, BeforeAndAfterAll }
import org.scalatest.matchers.ShouldMatchers

class TestEventSource extends Actor
  with ProductionEventSource {
  def receive = eventSourceReceive
}

class EventSourceSpec extends TestKit(ActorSystem("EventSourceSpec"))
  with WordSpec
  with ShouldMatchers
  with BeforeAndAfterAll {

  import EventSource._
  override def afterAll() { system.shutdown() }

  "EventSource" should {
    "allow us to register a listener" in {
      val real = TestActorRef[TestEventSource].underlyingActor
      real.receive(RegisterListener(testActor))
      val ls = real.listeners.asInstanceOf[Iterable[akka.actor.ActorRef]]
      real.listeners should contain(testActor)
    }
    
    "allow us to unregister a listener" in {
      val real = TestActorRef[TestEventSource].underlyingActor
      real.receive(RegisterListener(testActor))
      real.receive(UnregisterListener(testActor))
      real.listeners.size should be(0)
    }
    
    "send the event to our test actor" in {
      val testA = TestActorRef[TestEventSource]
      testA ! RegisterListener(testActor)
      testA.underlyingActor.sendEvent("Fibonacci")
      expectMsg("Fibonacci")
    }
  }
}