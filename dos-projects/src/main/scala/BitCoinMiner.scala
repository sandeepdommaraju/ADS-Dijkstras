import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import scala.concurrent.duration._
import java.security.MessageDigest
import scala.annotation.tailrec

case class setZeroesAndWorkers(zs: Int, ws: Int)
case class setZeroes(z: Int)
case class isValid(str: String)
case class Mail(message: String)

/**
 * Server:  1. create workers
 *          2. if no workers are present, act as worker !! --> verify
 *          3. if we have found desired number of bitcoins or if TLE terminate
 */
class ServerActor extends Actor {
    var k = 0 //number of leading zeroes required in the hash
    var w = 0 //number of workers
    
    def receive = {
        
        case setZeroesAndWorkers(zs, ws) => k = zs
                                            w = ws
        case "getparams" => println("zeroes: " + k + " workers: "+ w)
        case _ => println("default case!!")
    }
}

/**
 * Worker:  1. generates a random string
 *          2. hash the string using sha-256
 *          3. verify if the hashed string has 'k' leading zeroes -> we found bitcoin
 */
class WorkerActor extends Actor {
    var k = 0
    
    def receive = {
        
        case setZeroes(z) => k = z
        case "mine" =>  while (!isValidHash(sha256("sandom;"+randomStringGenerator()))){ println("hash miss")}
                        println("found bitcoin")
        case _ => println("default case!!")
    }
    
    object Hex {

      def valueOf(buf: Array[Byte]): String = buf.map("%02X" format _).mkString
    
    }
    
    
    def sha256 (s: String): Option[String] = {
        println("hash this: "+s)
        val msg = MessageDigest.getInstance("SHA-256").digest(s.getBytes)
        val hash: Option[String] = Option(msg).map(Hex.valueOf)
        return hash
    }
    
    def isValidHash(hash: Option[String]): Boolean = {
        val str: String = hash.getOrElse("") //to convert Option[String] to String
        println("isvalid check: "+str)
        for (i <- 1 to k){
            if (str.charAt(i-1)!='0') return false
        }
        return true
    }
    
    def randomStringGenerator(): String = {
        val length = util.Random.nextInt(10) + 1
        val chars = ('a' to 'z') ++ ('A' to 'Z')
        val sb = new StringBuilder
        for (i <- 1 to length) {
          val randomNum = util.Random.nextInt(chars.length)
          sb.append(chars(randomNum))
        }
        println("randomStr: " + sb.toString())
        return sb.toString()
    }
}

object Miner extends App {
    
    // main system
    val system = ActorSystem("MinerSystem");
    
    // create sever actor
    val serverActor = system.actorOf(Props[ServerActor], name="server")
    
    // TODO: remove: workers have to be created inside server
    val workerActor = system.actorOf(Props[WorkerActor], name="worker")
    
    val zs = 2;  //leading zeroes
    
    val ws = 10; //workers
    
    serverActor ! "gg"
    
    serverActor ! setZeroesAndWorkers(zs, ws)
    
    serverActor ! "getparams"
    
    //TODO: clean
    val inbox = Inbox.create(system)
    inbox.send(workerActor, setZeroes(zs))
    
    inbox.send(workerActor, "mine")
    val v1 = inbox.receive(2.seconds)
    println(v1)
    
    //TODO: clean
    /*serverActor ! "manage"
    serverActor ! "numofzeroes"
    serverActor ! setZeroes(4)
    serverActor ! "numofzeroes"
    serverActor ! "numofworkers"
    serverActor ! setWorkers(10)
    serverActor ! "numofworkers"*/
    
    //val greetPrinter = system.actorOf(Props[GreetPrinter])
    // after zero seconds, send a Greet message every second to the greeter with a sender of the greetPrinter
    //system.scheduler.schedule(0.seconds, 1.second, workerActor, "mine")(system.dispatcher, greetPrinter)
  
}

    // prints a greeting
    class GreetPrinter extends Actor {
      def receive = {
        case Mail(message) => println(message)
      }
    }
    