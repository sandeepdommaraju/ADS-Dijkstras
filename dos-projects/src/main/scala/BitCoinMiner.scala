import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import scala.concurrent.duration._
import java.security.MessageDigest
import scala.annotation.tailrec

case class setParams(zeroes: Int, chunk: Int, limit: Int)
case class isValid(str: String)
case class Mail(message: String)
case object StartWorkers
case object Done
case object Stop
case object StopAck
case class BitCoin(inputStr: String, outputStr: String)


/**
 * Server:  1. create workers
 *          2. if no workers are present, act as worker !! --> verify
 *          3. if we have found desired number of bitcoins or if TLE terminate
 */
class ServerActor(zeroes: Int, workers: Int, chunk: Int, limit: Int) extends Actor {
    
    var workerPool = new Array[ActorRef](workers)
    var bitcoins = 0
    var reqcoins = 5
    var strcounter = 0
    var results: Map[String, String] = Map()
    var stopcount = 0
    var actors = workers
    
    def receive = {
        
        case "getparams" => println("zeroes: " + zeroes + " workers: "+ workers + " chunk: "+ chunk + " limit: "+limit)
        case StartWorkers => startWorkers()
        case "Found" => bitcoins += 1
                        println("#bitcoins: "+bitcoins)
                      if (bitcoins == reqcoins) sender ! stopSystem()
        case Done => if (strcounter < limit){
                                sender ! "mine"
                                strcounter += chunk
                            }else{
                                sender ! "stop"
                            }
        //case "stopWorkers" => stopWorkers()
        case BitCoin(inputstr, hashstr) => results += (inputstr -> hashstr)
                                            println(inputstr + "\t" + hashstr)
        case StopAck => stopcount += 1
                        if (stopcount == workers){
                            context.system.shutdown
                        }
        case "remoteworker" => println("TODO remoteworker")
        case _ => println("default case!!")
    }
    
    def startWorkers() = {
        val i = 1
        for (i <- 1 to workers){
            workerPool(i-1) = context.actorOf(Props[WorkerActor], name="Worker"+i)
        }
        for (i <- 1 to workers){
            workerPool(i-1) ! setParams(zeroes, chunk, limit)
            workerPool(i-1) ! "mine"
        }
    }
    
    def stopAllWorkers() = {
        val i = 1
        for (i <- 1 to workers){
            workerPool(i-1) ! "stop"
        }
    }
    
    def stopSystem() = {
        stopAllWorkers()
        context.system.shutdown
    }
    
}

/**
 * Worker:  1. generates a random string
 *          2. hash the string using sha-256
 *          3. verify if the hashed string has 'k' leading zeroes -> we found bitcoin
 */
class WorkerActor extends Actor {
    var zeroes = 0
    var chunk = 0
    var limit = 0
    
    def receive = {
        
        case setParams(z, c, l) =>  zeroes = z
                                    chunk = c
                                    limit = l
        case "mine" =>  mine(sender)
                        sender ! Done
        case "stop" =>  sender ! StopAck
                        context.stop(self)
        case _ => println("default case!!")
    }
    
    object Hex {

      def valueOf(buf: Array[Byte]): String = buf.map("%02X" format _).mkString
    
    }
    
    def mine(sender: ActorRef) = {
        var randstr = randomStringGenerator()
        for (i <- 1 to chunk) {
            var hash = sha256("sandom;"+randstr)
            println(hash)
            if(isValidHash(hash)){
                println("found bitcoin")
                sender ! BitCoin("sandom;"+randstr, hash.getOrElse(""))
            }
        }
    }
    
    
    def sha256 (s: String): Option[String] = {
        //println("hash this: "+s)
        val msg = MessageDigest.getInstance("SHA-256").digest(s.getBytes)
        val hash: Option[String] = Option(msg).map(Hex.valueOf)
        return hash
    }
    
    def isValidHash(hash: Option[String]): Boolean = {
        val str: String = hash.getOrElse("") //to convert Option[String] to String
        //println("isvalid check: "+str)
        for (i <- 1 to zeroes){
            if (str.charAt(i-1)!='0') return false
        }
        println("bitcoin: " + str)
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
        //println("randomStr: " + sb.toString())
        return sb.toString()
    }
}

object Miner extends App {
    
        val chunk = 10000 //worker chunk size
        val limit = 1000000 //threshold
        val zeroes = 4;  //leading zeroes
        val workers = 10; //workers
    
        // main system
        val system = ActorSystem("MinerSystem");
        
        // create sever actor
        val serverActor = system.actorOf(Props(new ServerActor(zeroes, workers, chunk, limit)), name="server")
        
        // TODO: remove: workers have to be created inside server
        val workerActor = system.actorOf(Props[WorkerActor], name="worker")
        
        serverActor ! "getparams"
        
        serverActor ! StartWorkers
        
        //TODO: clean
        /*val inbox = Inbox.create(system)
        inbox.send(workerActor, setZeroes(zs))
        
        inbox.send(workerActor, "mine")*/
}