package id.kotlin.logger

import id.kotlin.logger.service.WebLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File
import java.time.format.DateTimeFormatter

@SpringBootApplication
class WebLogApplication(@Autowired private val service: WebLogService) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        args?.let {
            val time = it.getOptionValues("t")[0]
            val directory = it.getOptionValues("d")[0]
            println("Get log from last $time")

            try {
                val folder = File(directory).listFiles().toList()
                val threshold = time.replace("m", "")

                service.readLog(threshold, folder).map {
                    val ipAddress = it.ipAddress
                    val userIdentifier = "user-identifier ${it.userIdentifier}"
                    val date = "[${it.date?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}]"
                    val method = it.method
                    val response = it.response
                    val sequence = it.sequence
                    println("$ipAddress $userIdentifier $date $method $response $sequence")
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<WebLogApplication>(*args)
}