package id.kotlin.logger.service

import id.kotlin.logger.model.Log
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.text.ParseException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class WebLogService {

    @Throws(IOException::class, ParseException::class)
    fun readLog(threshold: String, folder: List<File>): List<Log> {
        val logs = mutableListOf<Log>()

        folder.map {
            val reader = FileReader(it)

            CSVFormat.RFC4180.withDelimiter(' ').parse(reader).map {
                val formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z")
                val datetime = "${it.get(3)} ${it.get(4)}".replace("[", "").replace("]", "")
                val start = LocalDateTime.parse(datetime, formatter)
                val end = LocalTime.of(0, threshold.toInt())
                val timestamp = start.takeIf { start.minute <= end.minute }

                if (null != timestamp) {
                    val log = Log(
                            ipAddress = it.get(0),
                            userIdentifier = it.get(2),
                            date = timestamp,
                            method = it.get(5),
                            response = it.get(6),
                            sequence = it.get(7)
                    )
                    logs.add(log)
                }
            }
        }

        return logs
    }
}